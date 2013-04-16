#include "wrappers_AVWrapper.h"

/*android specific headers*/
#include <jni.h>
#include <android/log.h>
#include <android/bitmap.h>

/*standard library*/
#include <time.h>
#include <math.h>
#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <unistd.h>
#include <assert.h>

/*ffmpeg headers*/
#include <libavutil/avstring.h>
#include <libavutil/pixdesc.h>
#include <libavutil/imgutils.h>
#include <libavutil/samplefmt.h>

#include <libavformat/avformat.h>

#include <libswscale/swscale.h>

#include <libavcodec/avcodec.h>
#include <libavcodec/opt.h>
/*#include <libavcodec/options_table.h>*/
#include <libavcodec/avfft.h>

/*for android logs*/
#define LOG_TAG "FFMpegJNI"
#define LOG_LEVEL 10
#define LOGI(level, ...) if (level <= LOG_LEVEL) {__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__);}
#define LOGE(level, ...) if (level <= LOG_LEVEL) {__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__);}

char *gFileName; //the file name of the video

AVFormatContext *gFormatCtx;
int gVideoStreamIndex; //video stream index

AVCodecContext *gVideoCodecCtx;

static void get_video_info(char *prFilename);

/*parsing the video file, done by parse thread*/
static void get_video_info(char *prFilename) {
    AVCodec *lVideoCodec;
    int lError;
    /*some global variables initialization*/
    LOGI(10, "get video info starts!");
    /*register the codec*/
    /*extern AVCodec ff_h263_decoder;
    avcodec_register(&ff_h263_decoder);
    extern AVCodec ff_h264_decoder;
    avcodec_register(&ff_h264_decoder);
    extern AVCodec ff_mpeg4_decoder;
    avcodec_register(&ff_mpeg4_decoder);
    extern AVCodec ff_mjpeg_decoder;
    avcodec_register(&ff_mjpeg_decoder);
    /*register parsers*/
    //extern AVCodecParser ff_h264_parser;
    //av_register_codec_parser(&ff_h264_parser);
    //extern AVCodecParser ff_mpeg4video_parser;
    //av_register_codec_parser(&ff_mpeg4video_parser);
    /*register demux*/
    extern AVInputFormat ff_mov_demuxer;
    av_register_input_format(&ff_mov_demuxer);
    //extern AVInputFormat ff_h264_demuxer;
    //av_register_input_format(&ff_h264_demuxer);
    /*register the protocol*/
    extern URLProtocol ff_file_protocol;
    av_register_protocol2(&ff_file_protocol, sizeof(ff_file_protocol));
    /*open the video file*/
    if ((lError = av_open_input_file(&gFormatCtx, gFileName, NULL, 0, NULL)) !=0 ) {
        LOGE(1, "Error open video file: %d", lError);
        return;	//open file failed
    }
    /*retrieve stream information*/
    if ((lError = av_find_stream_info(gFormatCtx)) < 0) {
        LOGE(1, "Error find stream information: %d", lError);
        return;
    } 
    /*find the video stream and its decoder*/
    gVideoStreamIndex = av_find_best_stream(gFormatCtx, AVMEDIA_TYPE_VIDEO, -1, -1, &lVideoCodec, 0);
    if (gVideoStreamIndex == AVERROR_STREAM_NOT_FOUND) {
        LOGE(1, "Error: cannot find a video stream");
        return;
    } else {
	LOGI(10, "video codec: %s", lVideoCodec->name);
    }
    if (gVideoStreamIndex == AVERROR_DECODER_NOT_FOUND) {
        LOGE(1, "Error: video stream found, but no decoder is found!");
        return;
    }   
    /*open the codec*/
    gVideoCodecCtx = gFormatCtx->streams[gVideoStreamIndex]->codec;
    LOGI(10, "open codec: (%d, %d)", gVideoCodecCtx->height, gVideoCodecCtx->width);
#ifdef SELECTIVE_DECODING
    gVideoCodecCtx->allow_selective_decoding = 1;
#endif
    if (avcodec_open(gVideoCodecCtx, lVideoCodec) < 0) {
	LOGE(1, "Error: cannot open the video codec!");
        return;
    }
    LOGI(10, "get video info ends");
}

JNIEXPORT void JNICALL Java_wrappers_AVWrapper_openFile
  (JNIEnv * je, jclass jc, jstring fileName)
  {
	int l_mbH, l_mbW;
    /*get the video file name*/
    gFileName = (char *)(*je)->GetStringUTFChars(je, fileName, NULL);
    if (gFileName == NULL) {
        LOGE(1, "Error: cannot get the video file name!");
        return;
    } 
    LOGI(10, "video file name is %s", gFileName);
	av_register_all();
    get_video_info(gFileName);
    LOGI(10, "initialization done")
}

JNIEXPORT jintArray JNICALL Java_wrappers_AVWrapper_naGetVideoResolution
  (JNIEnv * je, jclass jc)
  {
	jintArray lRes;
    lRes = (*je)->NewIntArray(je, 2);
    if (lRes == NULL) {
        LOGI(1, "cannot allocate memory for video size");
        return NULL;
    }
    jint lVideoRes[2];
    lVideoRes[0] = gVideoCodecCtx->width;
    lVideoRes[1] = gVideoCodecCtx->height;
    (*je)->SetIntArrayRegion(je, lRes, 0, 2, lVideoRes);
    return lRes;
  }
  
  JNIEXPORT jstring JNICALL Java_wrappers_AVWrapper_naGetVideoCodecName
  (JNIEnv * je, jclass jc)
  {
	char* lCodecName = gVideoCodecCtx->codec->name;
    return (*je)->NewStringUTF(je, lCodecName);
  }
  
  JNIEXPORT jstring JNICALL Java_wrappers_AVWrapper_naGetVideoFormatName
  (JNIEnv * je, jclass jc)
  {
	char* lFormatName = gFormatCtx->iformat->name;
    return (*je)->NewStringUTF(je, lFormatName);
  }
  
  JNIEXPORT void JNICALL Java_wrappers_AVWrapper_naClose
  (JNIEnv * je, jclass jc)
  {
	int l_mbH = (gVideoCodecCtx->height + 15) / 16;
    /*close the video codec*/
    avcodec_close(gVideoCodecCtx);
    /*close the video file*/
    av_close_input_file(gFormatCtx);
  }

  JNIEXPORT void JNICALL Java_wrappers_AVWrapper_decode
    (JNIEnv * je, jclass jc, jbyteArray videoBuffer, jbyteArray audioBuffer)
  {



  }
  }
