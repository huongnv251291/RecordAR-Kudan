//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.opengl.GLES20;
import android.util.Log;
import android.view.Surface;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ARVideoTexture extends ARTextureOES implements ARRendererListener, OnCompletionListener {
    private int mWidth;
    private int mHeight;
    private MediaPlayer mMediaPlayer;
    private long mLastRenderTime;
    private long mPauseTolerance;
    private long mResetThreshold = 1000L;
    private String mVideoAssetPath;
    private boolean mLoaded;
    private boolean isVideoAnAsset;
    private ARVideoTexture.State mState;
    private List<ARVideoTextureListener> mListeners = new ArrayList();

    private native void initN(String var1);

    public void spill() {
        this.mLoaded = false;
        if(this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.stop();
        }

        this.mSurfaceTexture = null;
        this.mMediaPlayer.release();
        this.mMediaPlayer = null;
    }

    public void open() {
        this.mMediaPlayer = new MediaPlayer();
        this.mState = ARVideoTexture.State.Playing;

        try {
            if(this.isVideoAnAsset) {
                ARRenderer renderer = ARRenderer.getInstance();
                AssetFileDescriptor afd = renderer.getAssetManager().openFd(this.mVideoAssetPath);
                this.mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            } else {
                this.mMediaPlayer.setDataSource(this.mVideoAssetPath);
            }

            this.mMediaPlayer.prepare();
            this.mMediaPlayer.setOnCompletionListener(this);
            this.mWidth = this.mMediaPlayer.getVideoWidth();
            this.mHeight = this.mMediaPlayer.getVideoHeight();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public ARVideoTexture() {
    }

    public void loadFromAsset(String assetPath) {
        try {
            if(!Arrays.asList(ARRenderer.getInstance().getAssetManager().list("")).contains(assetPath)) {
                throw new FileNotFoundException("Video " + assetPath + " was treated as an asset but was not found in the assets folder.");
            }

            this.isVideoAnAsset = true;
            this.mVideoAssetPath = assetPath;
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        ARRenderer renderer = ARRenderer.getInstance();
        renderer.makeActiveVideoTexture(this);
        renderer.addListener(this);
    }

    public void loadFromPath(String filePath) {
        try {
            File videoFile = new File(filePath);
            if(!videoFile.exists()) {
                throw new FileNotFoundException("Video " + filePath + " was not found.");
            }

            this.isVideoAnAsset = false;
            this.mVideoAssetPath = filePath;
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        }

        ARRenderer renderer = ARRenderer.getInstance();
        renderer.makeActiveVideoTexture(this);
        renderer.addListener(this);
    }

    public void loadData() {
        this.createTexture();
    }

    public void loadSurfaceData() {
        this.mSurfaceTexture = new SurfaceTexture(this.getTextureID());
        Surface surface = new Surface(this.mSurfaceTexture);
        this.mMediaPlayer.setSurface(surface);
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    private native boolean nextFrameN(long var1);

    public void prepareRenderer(int unit) {
        super.prepareRenderer(unit);
        ARRenderer renderer;
        if(!this.mLoaded) {
            renderer = ARRenderer.getInstance();
            renderer.makeActiveVideoTexture(this);
            this.loadSurfaceData();
            this.mLoaded = true;
        }

        renderer = ARRenderer.getInstance();
        if(this.mResetThreshold > 0L && renderer.getRenderTime() - this.mLastRenderTime > this.mResetThreshold) {
            Log.i("KudanAR", "Video inactivity threshold reached. Resetting the video.");
            this.reset();
        }

        if(this.mState == ARVideoTexture.State.Playing) {
            this.mMediaPlayer.start();
        }

        if(this.mState == ARVideoTexture.State.Paused) {
            this.mMediaPlayer.pause();
        }

        this.mSurfaceTexture.updateTexImage();
        GLES20.glTexParameterf(3553, 10242, 33071.0F);
        GLES20.glTexParameterf(3553, 10243, 33071.0F);
        this.mLastRenderTime = ARRenderer.getInstance().getRenderTime();
    }

    public void start() {
        this.mState = ARVideoTexture.State.Playing;
    }

    public void pause() {
        this.mState = ARVideoTexture.State.Paused;
    }

    public void reset() {
        if(this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
        }

        this.mMediaPlayer.seekTo(0);
        this.mState = ARVideoTexture.State.Playing;
    }

    public void preRender() {
    }

    public void postRender() {
        ARRenderer renderer = ARRenderer.getInstance();
        if(this.mMediaPlayer != null) {
            if(this.mMediaPlayer.isPlaying()) {
                if(renderer.getRenderTime() - this.mLastRenderTime > this.mPauseTolerance) {
                    this.mMediaPlayer.pause();
                }

            }
        }
    }

    public void rendererDidPause() {
        if(this.mMediaPlayer != null) {
            if(this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.pause();
            }

        }
    }

    public void rendererDidResume() {
    }

    public void onCompletion(MediaPlayer mp) {
        this.mState = ARVideoTexture.State.Stopped;
        Iterator var2 = this.mListeners.iterator();

        while(var2.hasNext()) {
            ARVideoTextureListener listener = (ARVideoTextureListener)var2.next();
            listener.videoDidFinish(this);
        }

    }

    public List<ARVideoTextureListener> getListeners() {
        return this.mListeners;
    }

    public void addListener(ARVideoTextureListener listener) {
        this.mListeners.add(listener);
    }

    public static enum State {
        Playing,
        Stopped,
        Paused;

        private State() {
        }
    }
}
