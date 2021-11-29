#include "pch.h"
#include "ExternalFinalsCounter.h"

ExternalFinalsCounter::ExternalFinalsCounter()
{
	if (!attach())
	{
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

	GetCreatedJavaVMs jni_GetCreatedJavaVMs = (GetCreatedJavaVMs) jni_GetCreatedJavaVMsProcAddress;

	jsize count;
	if (jni_GetCreatedJavaVMs(&vm, 1, &count) != JNI_OK)
	{
		Utils::messageBox("Failed to get the JVM");
		return false;
	}

	if (count == 0)
	{
		Utils::messageBox("No JVM found");
		return false;
	}

	jint result = vm->GetEnv(reinterpret_cast<void**>(&jni), JNI_VERSION_1_8);

	if (result == JNI_EDETACHED)
	{
		result = vm->AttachCurrentThread(reinterpret_cast<void**>(&jni), nullptr);
	}

	if (result != JNI_OK)
	{
		Utils::messageBox("Failed to attach current thread to the JVM");
		return false;
	}

	result = vm->GetEnv(reinterpret_cast<void**>(&jvmti), JVMTI_VERSION);
	if (result != JNI_OK)
	{
		Utils::messageBox("Failed to get JVMTI");
		return false;
	}

	return true;
}