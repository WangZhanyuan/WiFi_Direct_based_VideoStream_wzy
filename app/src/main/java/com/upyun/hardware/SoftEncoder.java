package com.upyun.hardware;

import android.media.MediaCodec;
import android.os.Build;
import android.util.Log;

import com.example.dell.wi_fi_direct_based_videostream_ltf.Cache.ServerCache;

import java.nio.ByteBuffer;

import static com.example.dell.wi_fi_direct_based_videostream_ltf.Camera.CameraActivity.TAG;


public class SoftEncoder {
    static {
        System.loadLibrary("yuv");
        System.loadLibrary("enc");
    }

    private ServerCache serverCache;
    private boolean isInit = false;

    public SoftEncoder(ServerCache serverCache) { this.serverCache = serverCache; }
    public void setInit(boolean init) {
        isInit = init;
    }
    public boolean isInit() {
        return isInit;
    }

    public native void setEncoderResolution(int outWidth, int outHeight);
    public native void setEncoderFps(int fps);
    public native void setEncoderGop(int gop);
    public native void setEncoderBitrate(int bitrate);
    public native void setBitrateDynamic(int bitrate);
    public native void setEncoderPreset(String preset);
    public native byte[] NV21ToI420(byte[] yuvFrame, int width, int height, boolean flip, int rotate);
    public native byte[] NV21ToI420(byte[] yuvFrame, int width, int height, boolean flip, int rotate, int cropX, int cropY, int cropWidth, int cropHeight);
    public native byte[] NV21ToNV12(byte[] yuvFrame, int width, int height, boolean flip, int rotate);
    public native byte[] NV21ToNV12(byte[] yuvFrame, int width, int height, boolean flip, int rotate, int cropX, int cropY, int cropWidth, int cropHeight);
    public native int NV21SoftEncode(byte[] yuvFrame, int width, int height, boolean flip, int rotate, long pts);
    public native int NV21SoftEncode(byte[] yuvFrame, int width, int height, boolean flip, int rotate, long pts, int cropX, int cropY, int cropWidth, int cropHeight);
    public native int I420SoftEncode(byte[] yuvFrame, int width, int height, boolean flip, int rotate, long pts);
    public native boolean openSoftEncoder();
    public native void closeSoftEncoder();

    private void onSoftEncodedData(byte[] es, long pts, boolean isKeyFrame) {
//        MediaCodec.BufferInfo vebi = new MediaCodec.BufferInfo();
//        ByteBuffer bb = ByteBuffer.wrap(es);
//        vebi.offset = 0;
//        vebi.size = es.length;
//        vebi.presentationTimeUs = pts;
//        if (Build.VERSION.SDK_INT >= 21) {
//            vebi.flags = isKeyFrame ? MediaCodec.BUFFER_FLAG_KEY_FRAME : 0;
//        } else {
//            vebi.flags = isKeyFrame ? MediaCodec.BUFFER_FLAG_SYNC_FRAME : 0;
//        }

        if (serverCache != null) {
            Log.d(TAG, "onSoftEncodedData: who am i ? "+serverCache.getmBitrate());
            Log.d(TAG, "onSoftEncodedData: my thread : "+android.os.Process.myTid());
            Log.d(TAG, "onSoftEncodedData: my address : "+System.identityHashCode(this));
            serverCache.put(es);
        }
    }
}
