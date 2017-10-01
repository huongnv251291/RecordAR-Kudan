//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.OrientationEventListener;

import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ARView extends GLSurfaceView {
    private final OrientationEventListener mOrientationEventListener;
    private final ARSurfaceRenderer mRenderer = new ARSurfaceRenderer(this);
    boolean onLoad = true;
    boolean resetCameraMesh = false;
    private ARViewPort mCameraViewPort;
    private ARViewPort mContentViewPort;
    private ARRenderTarget mRenderTarget;
    private ARCameraTextureMaterial mCamTextureMaterial;
    private ARMeshNode mCameraMeshNode;
    private ARMesh mCameraMesh;
    private List<ARDeviceUpdatesListener> mListeners = new ArrayList();
    private SurfaceTexture mCameraSurfaceTexture;
    private ARTextureOES mCameraTexture;
    private MediaPlayer mMediaPlayer;
    private ARNode mTrackedNode;
    private ARActivity mActivity;

    public ARView(Context context) {
        super(context);
        this.mActivity = (ARActivity) context;
        this.mOrientationEventListener = new OrientationEventListener(this.mActivity, 3) {
            int mLastRotation;

            public void onOrientationChanged(int orientation) {
                int rotation = ARView.this.mActivity.getRotation();
                if (rotation != this.mLastRotation) {
                    Log.i("KudanAR", "Orientation changed >>> " + rotation);
                    ARView.this.resetCameraMesh = true;
                    this.mLastRotation = rotation;
                }

            }
        };
        this.onConstuction(context);
    }

    public ARView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mActivity = (ARActivity) context;
        this.mOrientationEventListener = new OrientationEventListener(this.mActivity, 3) {
            int mLastRotation;

            public void onOrientationChanged(int orientation) {
                int rotation = ARView.this.mActivity.getRotation();
                if (rotation != this.mLastRotation) {
                    Log.i("KudanAR", "Orientation changed >>> " + rotation);
                    ARView.this.resetCameraMesh = true;
                    this.mLastRotation = rotation;
                }

            }
        };
        this.onConstuction(context);
    }

    public void setupNewCamera() {
        ARCameraStream cameraStream = ARCameraStream.getInstance();
        cameraStream.mArView = this;
        this.mCameraMeshNode = new ARMeshNode();
        this.mCameraMesh = new ARMesh();
        this.mCameraMeshNode.setMesh(this.mCameraMesh);
        this.mCamTextureMaterial = new ARCameraTextureMaterial(this.mCameraTexture);
        this.mCamTextureMaterial.setDepthWrite(false);
        this.mCameraMeshNode.setMaterial(this.mCamTextureMaterial);
        cameraStream.setMaterial(this.mCamTextureMaterial);
        cameraStream.setTexture(this.mCameraTexture);
        this.mRenderTarget = new ARRenderTarget();
        ARRenderer renderer = ARRenderer.getInstance();
        this.mCameraViewPort = new ARViewPort();
        this.mRenderTarget.addViewPort(this.mCameraViewPort);
        this.mCameraViewPort.getCamera().addChild(this.mCameraMeshNode);
        this.mCameraViewPort.setZOrder(-100);
        this.mContentViewPort = new ARViewPort();
        this.mRenderTarget.addViewPort(this.mContentViewPort);
        ARImageTracker tracker = ARImageTracker.getInstance();
        this.mContentViewPort.getCamera().addChild(tracker.getBaseNode());
    }

    void setup() {
        this.mActivity.setup();
    }

    void setupViewports(boolean rebuildMesh) {
        int screenOrientation = this.mActivity.getRotation();
        ARCameraStream cameraStream = ARCameraStream.getInstance();
        float halfWidth = (float) (this.getWidth() / 2);
        float halfHeight = (float) (this.getHeight() / 2);
        Matrix4f projection = new Matrix4f(-halfWidth, halfWidth, -halfHeight, halfHeight, 1.0F, -1.0F);
        this.mCameraViewPort.getCamera().setProjectionMatrix(projection);
        float screenAspect = (float) this.getWidth() / (float) this.getHeight();
        float cameraAspect = (float) cameraStream.getWidth() / (float) cameraStream.getHeight();
        Log.i("KudanAR", "screen: " + this.getWidth() + "x" + this.getHeight());
        this.mCameraViewPort.setViewportParms(0, 0, this.getWidth(), this.getHeight());
        float uv;
        float width;
        if (screenAspect > cameraAspect) {
            cameraStream.rotateCameraPreview(0);
            width = (float) cameraStream.getWidth() / screenAspect;
            uv = width / (float) cameraStream.getHeight();
        } else {
            cameraStream.rotateCameraPreview(90);
            width = (float) cameraStream.getWidth() * screenAspect;
            uv = width / (float) cameraStream.getHeight();
        }

        uv = 1.0F - uv;
        uv /= 2.0F;
        if (screenOrientation == 3) {
            this.mCameraMesh.createTestMeshWithUvs(2.0F, 2.0F, 1.0F, 0.0F, 1.0F - uv, uv, false, rebuildMesh);
        } else if (screenOrientation == 1) {
            this.mCameraMesh.createTestMeshWithUvs(2.0F, 2.0F, 0.0F, 1.0F, uv, 1.0F - uv, false, rebuildMesh);
        } else if (screenOrientation == 2) {
            this.mCameraMesh.createTestMeshWithUvs(2.0F, 2.0F, 1.0F - uv, uv, 1.0F, 0.0F, false, rebuildMesh);
        } else {
            this.mCameraMesh.createTestMeshWithUvs(2.0F, 2.0F, uv, 1.0F - uv, 0.0F, 1.0F, false, rebuildMesh);
        }

        width = (float) this.getWidth();
        float height = (float) this.getHeight();
        float cameraHeight = width / cameraAspect;
        float offsetY = (cameraHeight - (float) this.getHeight()) / 2.0F;
        float offsetX = (height / cameraAspect - (float) this.getWidth()) / 2.0F;
        Matrix4f projectionMatrix;
        if (screenOrientation == 1) {
            this.mContentViewPort.setViewportParms(0, (int) (-offsetY), (int) width, (int) cameraHeight);
            projectionMatrix = new Matrix4f(546.0F, 546.0F, 320.0F, 240.0F);
        } else if (screenOrientation == 3) {
            this.mContentViewPort.setViewportParms(0, (int) (-offsetY), (int) width, (int) cameraHeight);
            projectionMatrix = new Matrix4f(546.0F, 546.0F, 320.0F, 240.0F);
            projectionMatrix.multLocal(new Quaternion(new float[]{0.0F, 0.0F, 3.1415F}));
        } else if (screenOrientation == 2) {
            this.mContentViewPort.setViewportParms((int) (-offsetX), 0, (int) (height / cameraAspect), (int) height);
            projectionMatrix = new Matrix4f(546.0F, 546.0F, 320.0F, 240.0F, true);
            projectionMatrix.multLocal(new Quaternion(new float[]{0.0F, 0.0F, 1.5708F}));
        } else {
            this.mContentViewPort.setViewportParms((int) (-offsetX), 0, (int) (height / cameraAspect), (int) height);
            projectionMatrix = new Matrix4f(546.0F, 546.0F, 320.0F, 240.0F, true);
            projectionMatrix.multLocal(new Quaternion(new float[]{0.0F, 0.0F, -1.5708F}));
        }

        this.mContentViewPort.getCamera().setProjectionMatrix(projectionMatrix);
    }

    void preRender() {
        if (this.resetCameraMesh) {
            this.setupViewports(true);
            this.resetCameraMesh = false;
        }

        this.mCameraSurfaceTexture.updateTexImage();
        float[] m = new float[16];
        this.mCameraSurfaceTexture.getTransformMatrix(m);
        this.mCamTextureMaterial.setUVTransform(m);
        ARCameraStream stream = ARCameraStream.getInstance();
        stream.setCameraTransform(m);
    }

    private void onConstuction(Context context) {
        this.setEGLContextClientVersion(2);
        this.setPreserveEGLContextOnPause(true);
        this.setRenderer(this.mRenderer);
        ARCameraStream cameraStream = ARCameraStream.getInstance();
        this.mCameraTexture = new ARTextureOES();
        this.mCameraSurfaceTexture = new SurfaceTexture(this.mCameraTexture.getTextureID());
        cameraStream.setSurfaceTexture(this.mCameraSurfaceTexture);
        this.setupNewCamera();
        if (this.mOrientationEventListener.canDetectOrientation()) {
            this.mOrientationEventListener.enable();
        }

    }

    public void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        this.setRenderMode(0);
        if (this.onLoad) {
            this.setupViewports(false);
            this.setup();
            this.onLoad = false;
        } else {
            this.resetCameraMesh = true;
        }

    }

    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        this.notifyListenersDeviceRotated();
    }

    public void onResume() {
        super.onResume();
        ARCameraStream cameraStream = ARCameraStream.getInstance();
        cameraStream.start();
        ARRenderer renderer = ARRenderer.getInstance();
        renderer.resume();
        if (this.mOrientationEventListener.canDetectOrientation()) {
            this.mOrientationEventListener.enable();
        }

        this.resetCameraMesh = true;
    }

    public void onPause() {
        super.onPause();
        ARCameraStream cameraStream = ARCameraStream.getInstance();
        cameraStream.stop();
        ARRenderer renderer = ARRenderer.getInstance();
        renderer.pause();
        this.mOrientationEventListener.disable();
    }

    public ARViewPort getCameraViewPort() {
        return this.mCameraViewPort;
    }

    public ARViewPort getContentViewPort() {
        return this.mContentViewPort;
    }

    public void render() {
        this.requestRender();
    }

    public Bitmap screenshot() {
        final Bitmap[] b = new Bitmap[1];
        final CountDownLatch latch = new CountDownLatch(1);
        this.queueEvent(new Runnable() {
            public void run() {
                b[0] = ARView.this.takeScreenshot();
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (Exception var4) {
            System.out.println("Exception " + var4);
        }

        return b[0];
    }

    private Bitmap takeScreenshot() {
        return this.mRenderTarget.screenshot(this.getCameraViewPort().getWidth(), this.getCameraViewPort().getHeight());
    }

    public void setCameraTexture(ARTextureOES cameraTexture) {
        this.mCameraTexture = cameraTexture;
    }

    public ARTextureOES getmCameraTexture() {
        return this.mCameraTexture;
    }

    public ARActivity getCameraActivity() {
        return this.mActivity;
    }

    public void setCameraActivity(ARActivity cameraActivity) {
        this.mActivity = cameraActivity;
    }

    synchronized void addDeviceListener(ARDeviceUpdatesListener listener) {
        this.mListeners.add(listener);
    }

    synchronized void removeDeviceListener(ARDeviceUpdatesListener listener) {
        this.mListeners.remove(listener);
    }

    synchronized void notifyListenersDeviceRotated() {
        Iterator var1 = this.mListeners.iterator();

        while (var1.hasNext()) {
            ARDeviceUpdatesListener listener = (ARDeviceUpdatesListener) var1.next();
            listener.didChangeOrientation();
        }

    }

    public synchronized void startRecordVideo() {

        mRenderer.startRecord();
    }

    public void stopRecordVideo() {
        mRenderer.stopRecord();
    }
}
