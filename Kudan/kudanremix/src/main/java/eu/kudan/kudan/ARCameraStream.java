//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ARCameraStream implements PreviewCallback {
    private static ARCameraStream cameraStream;
    private Lock mLock = new ReentrantLock();
    private boolean mProcessing = false;
    private float[] mCameraTransform;
    private List<ARCameraStreamListener> mListeners;
    private ARCameraTextureMaterial mMaterial;
    private ARTexture mTexture;
    private Camera mCamera = null;
    private int mWidth = 640;
    private int mHeight = 480;
    private SurfaceTexture mSurfaceTexture;
    public ARView mArView;

    public static ARCameraStream getInstance() {
        if(cameraStream == null) {
            cameraStream = new ARCameraStream();
            cameraStream.initialise();
        }

        return cameraStream;
    }

    public void setCameraTransform(float[] transform) {
        this.mCameraTransform = transform;
    }

    public float[] getCameraTransform() {
        return this.mCameraTransform;
    }

    public ARTexture getTexture() {
        return this.mTexture;
    }

    public void setTexture(ARTexture texture) {
        this.mTexture = texture;
    }

    public ARCameraTextureMaterial getMaterial() {
        return this.mMaterial;
    }

    public void setMaterial(ARCameraTextureMaterial material) {
        this.mMaterial = material;
    }

    public ARCameraStream() {
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        this.mSurfaceTexture = surfaceTexture;
    }

    public void onPreviewFrame(byte[] data, Camera arg1) {
        synchronized(ARRenderer.getInstance()) {
            this.process(data);
            this.notifyListenersUpdateFrame(data);
        }

        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();
        arbiTrack.processFrame(data, 640, 480);
        if(this.mArView != null) {
            this.mArView.render();
        }

    }

    private void process(byte[] data) {
        ARImageTracker tracker = ARImageTracker.getInstance();
        tracker.processFrame(data, 640, 480);
    }

    private void processBG(byte[] data) {
        this.process(data);
        synchronized(this) {
            this.mProcessing = false;
        }
    }

    public void initialise() {
    }

    public void start() {
        this.mCamera = Camera.open();

        try {
            this.mCamera.setPreviewTexture(this.mSurfaceTexture);
            this.mCamera.setPreviewCallback(this);
        } catch (IOException var12) {
            this.mCamera.release();
            this.mCamera = null;
            return;
        }

        Parameters parameters = this.mCamera.getParameters();
        parameters.setPreviewSize(this.mWidth, this.mHeight);
        List<String> focusModes = parameters.getSupportedFocusModes();
        if(focusModes.contains("continuous-video")) {
            parameters.setFocusMode("continuous-video");
        }

        double horizFOV = Math.toRadians((double)parameters.getHorizontalViewAngle());
        double sensorWidth = Math.tan(horizFOV / 2.0D) * (double)parameters.getFocalLength();
        sensorWidth *= 2.0D;
        List<int[]> ranges = parameters.getSupportedPreviewFpsRange();
        Iterator var8 = ranges.iterator();

        while(var8.hasNext()) {
            int[] range = (int[])var8.next();
            int min = range[0];
            int max = range[1];
            if(min == 30000 && max == 30000) {
                parameters.setPreviewFpsRange(min, max);
                break;
            }

            if(min == 30000) {
                parameters.setPreviewFpsRange(min, max);
            }
        }

        this.mCamera.setParameters(parameters);
        this.mCamera.startPreview();
    }

    public void stop() {
        if(this.mCamera != null) {
            this.mCamera.setPreviewCallback((PreviewCallback)null);
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void rotateCameraPreview(int degrees) {
        if(this.mCamera != null) {
            this.mCamera.setDisplayOrientation(degrees);
        }

    }

    public void addListener(ARCameraStreamListener listener) {
        if(this.mListeners == null) {
            this.mListeners = new ArrayList();
        }

        this.mListeners.add(listener);
    }

    public void removeListener(ARCameraStreamListener listener) {
        this.mListeners.remove(listener);
    }

    public void notifyListenersUpdateFrame(byte[] data) {
        if(this.mListeners != null) {
            Iterator var2 = this.mListeners.iterator();

            while(var2.hasNext()) {
                ARCameraStreamListener listener = (ARCameraStreamListener)var2.next();
                listener.didRecieveCameraFrame(data);
            }
        }

    }

    private class AsyncTaskRunner extends AsyncTask<byte[], String, String> {
        private AsyncTaskRunner() {
        }

        protected String doInBackground(byte[]... arg0) {
            ARCameraStream.this.processBG(arg0[0]);
            return null;
        }
    }
}
