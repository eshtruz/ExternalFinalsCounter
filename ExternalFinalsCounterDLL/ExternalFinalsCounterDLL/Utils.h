#pragma once

#include "pch.h"

class Utils
{
public:
	static void messageBox(const char* message)
	{
		MessageBoxA(nullptr, message, "ExternalFinalsCounter", MB_OK);
	}

	static jobject getThreadGroupClassLoader(JNIEnv* jni)
	{
		jclass threadClass = jni->FindClass("java/lang/Thread");
		jmethodID currentThreadMethodID = jni->GetStaticMethodID(threadClass, "currentThread", "()Ljava/lang/Thread;");
		jmethodID getThreadGroupMethodID = jni->GetMethodID(threadClass, "getThreadGroup", "()Ljava/lang/ThreadGroup;");
		jmethodID getContextClassLoaderMethodID = jni->GetMethodID(threadClass, "getContextClassLoader", "()Ljava/lang/ClassLoader;");

		jclass threadGroupClass = jni->FindClass("java/lang/ThreadGroup");
		jmethodID activeCountMethodID = jni->GetMethodID(threadGroupClass, "activeCount", "()I");
		jmethodID enumerateMethodID = jni->GetMethodID(threadGroupClass, "enumerate", "([Ljava/lang/Thread;)I");

		jobject currentThread = jni->CallStaticObjectMethod(threadClass, currentThreadMethodID);

		jobject threadGroup = jni->CallObjectMethod(currentThread, getThreadGroupMethodID);

		jint activeCount = jni->CallIntMethod(threadGroup, activeCountMethodID);
		jobjectArray threads = jni->NewObjectArray(activeCount, threadClass, nullptr);
		jni->CallIntMethod(threadGroup, enumerateMethodID, threads);

		jobject firstThread = jni->GetObjectArrayElement(threads, 0);

		return jni->CallObjectMethod(firstThread, getContextClassLoaderMethodID);
	}
};