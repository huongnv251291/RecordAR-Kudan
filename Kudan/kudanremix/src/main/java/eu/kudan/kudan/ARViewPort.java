//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class ARViewPort {
    private ARCamera mCamera;
    private int mOffsetX;
    private int mOffsetY;
    private int mWidth;
    private int mHeight;
    private int mZOrder;

    public ARViewPort() {
        this.mCamera = new ARCamera();
    }

    public ARViewPort(int offsetX, int offsetY, int width, int height) {
        this();
        this.setViewportParms(offsetX, offsetY, width, height);
    }

    public void setCamera(ARCamera camera) {
        this.mCamera = camera;
    }

    public int getOffsetX() {
        return this.mOffsetX;
    }

    public int getOffsetY() {
        return this.mOffsetY;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setViewportParms(int offsetX, int offsetY, int width, int height) {
        this.mOffsetX = offsetX;
        this.mOffsetY = offsetY;
        this.mWidth = width;
        this.mHeight = height;
    }

    public ARCamera getCamera() {
        return this.mCamera;
    }

    public void setZOrder(int zOrder) {
        this.mZOrder = zOrder;
    }

    public int getZOrder() {
        return this.mZOrder;
    }
}
