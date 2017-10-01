//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ARRenderTarget {
    protected int mFramebufferID;
    private ARCamera mCamera = new ARCamera();
    private boolean mShouldClear = true;
    private boolean mShouldClearDepth = true;
    private int mPriority;
    private ARWatermark mWatermark;
    private List<ARViewPort> mViewPorts = new ArrayList();

    public ARRenderTarget() {
        ARRenderer renderer = ARRenderer.getInstance();
        renderer.addRenderTarget(this);
    }

    public int getPriority() {
        return this.mPriority;
    }

    public void setPriority(int priority) {
        this.mPriority = priority;
    }

    public boolean getShouldClear() {
        return this.mShouldClear;
    }

    public void setShouldClear(boolean shouldClear) {
        this.mShouldClear = shouldClear;
    }

    public boolean getShouldClearDepth() {
        return this.mShouldClearDepth;
    }

    public void setShouldClearDepth(boolean shouldClearDepth) {
        this.mShouldClearDepth = shouldClearDepth;
    }

    public void clear() {
        int flags = 0;
        if (this.mShouldClear) {
            flags |= 16384;
        }

        if (this.mShouldClearDepth) {
            GLES20.glDepthMask(true);
            flags |= 256;
        }

        if (flags != 0) {
            GLES20.glClear(flags);
        }

    }

    public ARCamera getCamera() {
        return this.mCamera;
    }

    public void setCamera(ARCamera camera) {
        this.mCamera = camera;
    }

    public void create() {
    }

    public void bind() {
        GLES20.glBindFramebuffer('赀', 0);
    }

    public void draw() {
        ARRenderer renderer = ARRenderer.getInstance();
        this.bind();
        this.clear();
        if (this.mCamera != null) {
            Collections.sort(this.mViewPorts, new Comparator<ARViewPort>() {
                public int compare(ARViewPort lhs, ARViewPort rhs) {
                    int a = lhs.getZOrder();
                    int b = rhs.getZOrder();
                    return a - b;
                }
            });
            Iterator var2 = this.mViewPorts.iterator();

            while (var2.hasNext()) {
                ARViewPort viewPort = (ARViewPort) var2.next();
                ARCamera camera = viewPort.getCamera();
                if (camera != null) {
                    GLES20.glViewport(viewPort.getOffsetX(), viewPort.getOffsetY(), viewPort.getWidth(), viewPort.getHeight());
                    renderer.setCamera(camera);
                    renderer.setProjectionMatrix(camera.getProjectionMatrix());
                    renderer.draw();
                }
            }

            if (ARAPIKey.getInstance().isFree()) {
                this.drawWatermark();
            }

        }
    }

    private void drawWatermark() {
        if (this.mWatermark == null) {
            this.mWatermark = new ARWatermark();
        }

        this.mWatermark.renderWatermark();
    }

    public List<ARViewPort> getViewPorts() {
        return this.mViewPorts;
    }

    public void addViewPort(ARViewPort viewPort) {
        this.mViewPorts.add(viewPort);
    }

    public Bitmap screenshot(int width, int height) {
        long starting = System.currentTimeMillis();
        int screenshotSize = width * height;
        ByteBuffer buffer = ByteBuffer.allocateDirect(screenshotSize * 4);
        buffer.order(ByteOrder.nativeOrder());
        GLES20.glReadPixels(0, 0, width, height, 6408, 5121, buffer);
        Log.e("screenshot", System.currentTimeMillis() - starting + "");
        int[] pixelsBuffer = new int[screenshotSize];
        buffer.asIntBuffer().get(pixelsBuffer);
        buffer.clear();

        for (int i = 0; i < screenshotSize; ++i) {
            pixelsBuffer[i] = pixelsBuffer[i] & -16711936 | (pixelsBuffer[i] & 255) << 16 | (pixelsBuffer[i] & 16711680) >> 16;
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        bitmap.setPixels(pixelsBuffer, screenshotSize - width, -width, 0, 0, width, height);
        return bitmap;
    }
}
