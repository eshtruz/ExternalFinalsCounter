#pragma once

#include "pch.h"

extern JavaVM* vm;
extern JNIEnv* jni;
extern jvmtiEnv* jvmti;

extern jobject transformers;

extern jclass listClass;
extern jmethodID sizeMethodID;
extern jmethodID getMethodID;

extern jclass transformerClass;
extern jmethodID transformMethodID;

void JNICALL ClassFileLoadHook(jvmtiEnv* jvmti, JNIEnv* jni,
    jclass class_being_redefined, jobject loader,
    const char* name, jobject protection_domain,
    jint data_len, const unsigned char* data,
    jint* new_data_len, unsigned char** new_data);