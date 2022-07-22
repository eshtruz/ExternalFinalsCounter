#include "pch.h"
#include "globals.h"
#include "com_shtruz_externalfinalscounter_ExternalFinalsCounter.h"

JavaVM* vm = nullptr;
JNIEnv* jni = nullptr;
jvmtiEnv* jvmti = nullptr;

jobject transformers = nullptr;

jclass listClass = nullptr;
jmethodID sizeMethodID = nullptr;
jmethodID getMethodID = nullptr;

jclass transformerClass = nullptr;
jmethodID transformMethodID = nullptr;

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM* jvm, void* reserved)
{
	vm = jvm;

	return JNI_VERSION_1_8;
}

jboolean JNICALL
Java_com_shtruz_externalfinalscounter_ExternalFinalsCounter_initialize(JNIEnv*, jobject object, jobject classLoader)
{
    if (vm->GetEnv(reinterpret_cast<void**>(&jni), JNI_VERSION_1_8) != JNI_OK)
    {
        return JNI_FALSE;
    }

    if (vm->GetEnv(reinterpret_cast<void**>(&jvmti), JVMTI_VERSION) != JNI_OK)
    {
        return JNI_FALSE;
    }

    jvmtiCapabilities capabilities;
    memset(&capabilities, 0, sizeof(capabilities));
    capabilities.can_retransform_classes = 1;
    
    if (jvmti->AddCapabilities(&capabilities) != JVMTI_ERROR_NONE)
    {
        jvmti->DisposeEnvironment();
        return JNI_FALSE;
    }

    jvmtiEventCallbacks callbacks;
    memset(&callbacks, 0, sizeof(callbacks));
    callbacks.ClassFileLoadHook = ClassFileLoadHook;
 
    if (jvmti->SetEventCallbacks(&callbacks, sizeof(callbacks)) != JVMTI_ERROR_NONE)
    {
        jvmti->DisposeEnvironment();
        return JNI_FALSE;
    }

    if (jvmti->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, nullptr) != JVMTI_ERROR_NONE)
    {
        jvmti->DisposeEnvironment();
        return JNI_FALSE;
    }

    jclass classLoaderClass = jni->FindClass("java/lang/ClassLoader");
    jmethodID loadClassMethodID = jni->GetMethodID(classLoaderClass, "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");

    listClass = reinterpret_cast<jclass>(jni->NewGlobalRef(jni->FindClass("java/util/List")));
    sizeMethodID = jni->GetMethodID(listClass, "size", "()I");
    getMethodID = jni->GetMethodID(listClass, "get", "(I)Ljava/lang/Object;");

    transformerClass = reinterpret_cast<jclass>(jni->NewGlobalRef(jni->CallObjectMethod(classLoader, loadClassMethodID, jni->NewStringUTF("com.shtruz.externalfinalscounter.instrument.transformer.Transformer"))));
    transformMethodID = jni->GetMethodID(transformerClass, "transform", "(Ljava/lang/ClassLoader;Ljava/lang/String;Ljava/lang/Class;Ljava/security/ProtectionDomain;[B)[B");

    jclass instrumentationClass = reinterpret_cast<jclass>(jni->CallObjectMethod(classLoader, loadClassMethodID, jni->NewStringUTF("com.shtruz.externalfinalscounter.instrument.Instrumentation")));
    jfieldID instrumentationInstanceFieldID = jni->GetStaticFieldID(instrumentationClass, "instance", "Lcom/shtruz/externalfinalscounter/instrument/Instrumentation;");
    jfieldID transformersFieldID = jni->GetFieldID(instrumentationClass, "transformers", "Ljava/util/List;");

    jobject instrumentationInstance = jni->GetStaticObjectField(instrumentationClass, instrumentationInstanceFieldID);

    transformers = jni->NewGlobalRef(jni->GetObjectField(instrumentationInstance, transformersFieldID));

    return JNI_TRUE;
}

JNIEXPORT void JNICALL
JNI_OnUnload(JavaVM* jvm, void* reserved)
{
    jni->DeleteGlobalRef(transformers);
    jni->DeleteGlobalRef(listClass);
    jni->DeleteGlobalRef(transformerClass);

    jvmti->DisposeEnvironment();
}