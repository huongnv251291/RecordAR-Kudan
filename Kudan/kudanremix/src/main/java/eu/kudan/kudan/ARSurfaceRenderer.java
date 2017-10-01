//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

import com.jme3.math.Matrix4f;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static eu.kudan.kudan.GameRecorder.ARENA_HEIGHT;
import static eu.kudan.kudan.GameRecorder.ARENA_WIDTH;

public class ARSurfaceRenderer implements Renderer {
    static final float mProjectionMatrixx[] = new float[16];
    private final float mSavedMatrix[] = new float[16];
    boolean mIsActive = false;
    ARCameraStream camStream;
    ARTexture2D camTexture;
    ARView mArView;
    boolean loaded = false;
    private Matrix4f mProjectionMatrix = null;
    private long mLastTime = 0L;
    private int mFramesRendered = 0;
    private String TAG = "ARSurfaceRenderer";
    private EGLDisplay mSavedEglDisplay;
    private EGLSurface mSavedEglDrawSurface;
    private EGLSurface mSavedEglReadSurface;
    private EGLContext mSavedEglContext;
    private int mFrameCount;
    private boolean isRecord = false;

    public ARSurfaceRenderer(ARView view) {
        this.mArView = view;
    }

    public void onDrawFrame(GL10 gl) {
        synchronized (ARRenderer.getInstance()) {
            drawFrame();
            GameRecorder recorder = GameRecorder.getInstance();
            if (isRecord && recorder.isRecording()) {
                if (recorder.isRecording() && recordThisFrame()) {
                    saveRenderState();

                    // switch to recorder state
                    recorder.makeCurrent();
                    recorder.getProjectionMatrix(mProjectionMatrixx);
                    recorder.setViewport();

                    // render everything again
                    renderFrame();
                    recorder.swapBuffers();

                    restoreRenderState();
                }
            }
        }

    }

    private void drawFrame() {
        this.mArView.preRender();
        this.renderFrame();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.initRendering();
        GameRecorder recorder = GameRecorder.getInstance();
        if (isRecord && recorder.isRecording()) {
            startFirstFrame();
        }

    }

    private void startFirstFrame() {
        GameRecorder recorder = GameRecorder.getInstance();
        if (recorder.isRecording()) {
            Log.d(TAG, "configuring GL for recorder");
            saveRenderState();
            recorder.firstTimeSetup();
            recorder.makeCurrent();
            this.initRendering();
            restoreRenderState();

            mFrameCount = 0;
        }
    }

    /**
     * Saves the current projection matrix and EGL state.
     */
    private void restoreRenderState() {
        // switch back to previous state
        if (!EGL14.eglMakeCurrent(mSavedEglDisplay, mSavedEglDrawSurface, mSavedEglReadSurface,
                mSavedEglContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
        System.arraycopy(mSavedMatrix, 0, mProjectionMatrixx, 0, mProjectionMatrixx.length);
    }

    private void saveRenderState() {
        System.arraycopy(mProjectionMatrixx, 0, mSavedMatrix, 0, mProjectionMatrixx.length);
        mSavedEglDisplay = EGL14.eglGetCurrentDisplay();
        mSavedEglDrawSurface = EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW);
        mSavedEglReadSurface = EGL14.eglGetCurrentSurface(EGL14.EGL_READ);
        mSavedEglContext = EGL14.eglGetCurrentContext();
    }

    /**
     * Decides whether we want to record the current frame, based on the target frame rate
     * and an assumed 60fps refresh rate.
     * <p>
     * We could be smarter here, and not drop a frame if the system dropped one inadvertently
     * (i.e. we missed a vsync by being too slow).
     */
    private boolean recordThisFrame() {
        final int TARGET_FPS = 30;

        mFrameCount++;
        switch (TARGET_FPS) {
            case 60:
                return true;
            case 30:
                return (mFrameCount & 0x01) == 0;
            case 24:
                // want 2 out of every 5 frames
                int mod = mFrameCount % 5;
                return mod == 0 || mod == 2;
            default:
                return true;
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Matrix.orthoM(mProjectionMatrixx, 0, 0, ARENA_WIDTH,
                0, ARENA_HEIGHT, -1, 1);
    }

    private void initRendering() {
        Log.i("KudanAR", "initRendering");
        GLES20.glClearColor(1.0F, 0.0F, 0.0F, 1.0F);
        ARRenderer renderer = ARRenderer.getInstance();
        renderer.loadContext();
    }

    private synchronized void renderFrame() {
        ARRenderer renderer = ARRenderer.getInstance();
        GLES20.glColorMask(true, true, true, true);
        GLES20.glDisable(3042);
        renderer.render();
        long curTime = System.currentTimeMillis();
        if (this.mLastTime == 0L) {
            this.mLastTime = curTime;
        }

        ++this.mFramesRendered;
        if (curTime - this.mLastTime > 5000L) {
            float fps = (float) this.mFramesRendered / ((float) (curTime - this.mLastTime) / 1000.0F);
            Log.i("AR", "fps: " + fps);
            this.mFramesRendered = 0;
            this.mLastTime = curTime;
        }

    }

    public void startRecord() {
        GameRecorder gameRecorder = GameRecorder.getInstance();
        gameRecorder.prepareEncoder(mArView.getContext());
        gameRecorder.firstTimeSetup();
        isRecord = true;
    }

    public void stopRecord() {
        isRecord = true;
        GameRecorder gameRecorder = GameRecorder.getInstance();
        gameRecorder.gamePaused();
    }
}
