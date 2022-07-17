#include "pch.h"
#include "globals.h"
#include "com_shtruz_externalfinalscounter_instrument_Instrumentation.h"

void JNICALL ClassFileLoadHook(jvmtiEnv* jvmti, JNIEnv* jni,
    jclass class_being_redefined, jobject loader,
    const char* name, jobject protection_domain,
    jint data_len, const unsigned char* data,
    jint* new_data_len, unsigned char** new_data)
{
    if (!class_being_redefined || !loader || !name)
    {
        return;
    }

    jbyteArray dataArray = jni->NewByteArray(data_len);
    jni->SetByteArrayRegion(dataArray, 0, data_len, (jbyte*) data);

    jint size = jni->CallIntMethod(transformers, sizeMethodID);
    for (jint i = 0; i < size; i++)
    {
        jobject transformer = jni->CallObjectMethod(transformers, getMethodID, i);

        dataArray = reinterpret_cast<jbyteArray>(jni->CallObjectMethod(transformer, transformMethodID, loader, jni->NewStringUTF(name), class_being_redefined, protection_domain, dataArray));
    }

    *new_data_len = jni->GetArrayLength(dataArray);
    jvmti->Allocate(*new_data_len, new_data);
    jni->GetByteArrayRegion(dataArray, 0, *new_data_len, (jbyte*) *new_data);
}

jboolean JNICALL
Java_com_shtruz_externalfinalscounter_instrument_Instrumentation_retransformClass(JNIEnv* jni, jobject object, jclass clazz)
{
    return jvmti->RetransformClasses(1, &clazz) == JVMTI_ERROR_NONE;
}