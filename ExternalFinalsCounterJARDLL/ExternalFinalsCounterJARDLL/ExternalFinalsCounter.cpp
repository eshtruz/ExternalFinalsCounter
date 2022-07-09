#include "pch.h"
#include "com_shtruz_externalfinalscounter_ExternalFinalsCounter.h"

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM* vm, void* reserved)
{
	return JNI_VERSION_1_8;
}

jboolean JNICALL
Java_com_shtruz_externalfinalscounter_ExternalFinalsCounter_initialize(JNIEnv* env, jobject object, jobject classLoader)
{

}

JNIEXPORT void JNICALL
JNI_OnUnload(JavaVM* vm, void* reserved)
{
    
}