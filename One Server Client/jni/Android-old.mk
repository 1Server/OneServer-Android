#LOCAL_SRC_FILES := ffmpeg/android/armv7-a/libffmpeg.so
 

#LOCAL_EXPORT_C_INCLUDES := ffmpeg/android/armv7-a/include
 

#LOCAL_EXPORT_LDLIBS := ffmpeg/android/armv7-a/libffmpeg.so
 

#LOCAL_PRELINK_MODULE := true
 

#include $(PREBUILT_SHARED_LIBRARY)
 

 

#the ffmpeg-test-jni library
 

#include $(CLEAR_VARS)
 

#LOCAL_ALLOW_UNDEFINED_SYMBOLS=false
 

#LOCAL_MODULE := ffmpeg-test-jni
 

#LOCAL_SRC_FILES := wrappers_AVWrapper.c
 

#LOCAL_C_INCLUDES := $(LOCAL_PATH)/ffmpeg/android/armv7-a/include
 

#LOCAL_SHARED_LIBRARY := ffmpeg-prebuilt
 

#LOCAL_LDLIBS    := -llog -ljnigraphics -lz -lm $(LOCAL_PATH)/ffmpeg/android/armv7-a/libffmpeg.so
 

#include $(BUILD_SHARED_LIBRARY)