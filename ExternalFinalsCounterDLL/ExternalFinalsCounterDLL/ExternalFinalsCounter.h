#pragma once

#include "pch.h"

class ExternalFinalsCounter
{
public:
	ExternalFinalsCounter(HMODULE hModule);
private:
	typedef jint(JNICALL *GetCreatedJavaVMs)(JavaVM**, jsize, jsize*);

	JavaVM* vm = nullptr;
	JNIEnv* jni = nullptr;
	jvmtiEnv* jvmti = nullptr;

	bool attach();
	bool load(HMODULE hModule);
};