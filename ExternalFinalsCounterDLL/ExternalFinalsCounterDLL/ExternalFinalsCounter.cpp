#include "pch.h"
#include "ExternalFinalsCounter.h"

ExternalFinalsCounter::ExternalFinalsCounter(HMODULE hModule)
{
	if (!attach())
	{
		FreeLibraryAndExitThread(hModule, 0);
		return;
	}

	Utils::messageBox("Injected!");
}

bool ExternalFinalsCounter::attach()
{
	HMODULE jvmDLL = GetModuleHandle(L"jvm.dll");
	if (!jvmDLL)
	{
		Utils::messageBox("Failed to get jvm.dll");
		return false;
	}

	FARPROC jni_GetCreatedJavaVMsProcAddress = GetProcAddress(jvmDLL, "JNI_GetCreatedJavaVMs");
	if (!jni_GetCreatedJavaVMsProcAddress)
	{
		Utils::messageBox("Failed to get JNI_GetCreatedJavaVMs function");
		return false;
	}

	GetCreatedJavaVMs jni_GetCreatedJavaVMs = reinterpret_cast<GetCreatedJavaVMs>(jni_GetCreatedJavaVMsProcAddress);

	jsize count;
	if (jni_GetCreatedJavaVMs(&vm, 1, &count) != JNI_OK)
	{
		Utils::messageBox("Failed to get created JVMs");
		return false;
	}

	if (count == 0)
	{
		Utils::messageBox("No JVM found");
		return false;
	}

	if (vm->AttachCurrentThread(reinterpret_cast<void**>(&jni), nullptr) != JNI_OK)
	{
		Utils::messageBox("Failed to attach current thread to the JVM");
		return false;
	}

	if (vm->GetEnv(reinterpret_cast<void**>(&jvmti), JVMTI_VERSION) != JNI_OK)
	{
		Utils::messageBox("Failed to get JVMTI");
		vm->DetachCurrentThread();
		return false;
	}

	return true;
}