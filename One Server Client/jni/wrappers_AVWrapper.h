/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class wrappers_AVWrapper */

#ifndef _Included_wrappers_AVWrapper
#define _Included_wrappers_AVWrapper
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     wrappers_AVWrapper
 * Method:    openFile
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_wrappers_AVWrapper_openFile
  (JNIEnv *, jclass, jstring);

/*
 * Class:     wrappers_AVWrapper
 * Method:    naGetVideoResolution
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_wrappers_AVWrapper_naGetVideoResolution
  (JNIEnv *, jclass);

/*
 * Class:     wrappers_AVWrapper
 * Method:    naGetVideoCodecName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_wrappers_AVWrapper_naGetVideoCodecName
  (JNIEnv *, jclass);

/*
 * Class:     wrappers_AVWrapper
 * Method:    naGetVideoFormatName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_wrappers_AVWrapper_naGetVideoFormatName
  (JNIEnv *, jclass);

/*
 * Class:     wrappers_AVWrapper
 * Method:    naClose
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_wrappers_AVWrapper_naClose
  (JNIEnv *, jclass);

/*
 * Class:     wrappers_AVWrapper
 * Method:    decode
 * Signature: ([B[B)V
 */
JNIEXPORT void JNICALL Java_wrappers_AVWrapper_decode
  (JNIEnv *, jclass, jbyteArray, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
