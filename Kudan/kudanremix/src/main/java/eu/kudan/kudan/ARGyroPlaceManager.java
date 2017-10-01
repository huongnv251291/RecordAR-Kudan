//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;

public class ARGyroPlaceManager implements ARRendererListener {
    private static ARGyroPlaceManager gyroPlaceManager;
    private float mFloorDepth = -150.0F;
    private ARNode mWorld = new ARNode();

    public ARGyroPlaceManager() {
    }

    public static ARGyroPlaceManager getInstance() {
        if(gyroPlaceManager == null) {
            gyroPlaceManager = new ARGyroPlaceManager();
            gyroPlaceManager.initialise();
        }

        return gyroPlaceManager;
    }

    public void initialise() {
        ARGyroManager gyroManager = ARGyroManager.getInstance();
        gyroManager.start();
        ARRenderer.getInstance().addListener(this);
    }

    public void deinitialise() {
        this.mWorld = new ARNode();
        ARRenderer.getInstance().removeListener(this);
    }

    private native void getFloorPositionN(float[] var1, float var2, float var3, float var4, float var5, float var6);

    public void preRender() {
        ARGyroManager gyroManager = ARGyroManager.getInstance();
        gyroManager.updateNode();
        Quaternion gyroRot = gyroManager.getWorld().getOrientation();
        Matrix4f projMatrix = ARRenderer.getInstance().getActivity().getARView().getContentViewPort().getCamera().getProjectionMatrix();
        float[] gLProjMatrix = new float[16];
        projMatrix.get(gLProjMatrix, false);
        this.getFloorPositionN(gLProjMatrix, gyroRot.getX(), gyroRot.getY(), gyroRot.getZ(), gyroRot.getW(), this.mFloorDepth);
    }

    private void setWorldPosition(float x, float y, float z) {
        this.mWorld.setPosition(x, y, z);
    }

    private void setWorldOrientation(float x, float y, float z, float w) {
        this.mWorld.setOrientation(x, y, z, w);
    }

    public float getFloorDepth() {
        return this.mFloorDepth;
    }

    public void setFloorDepth(float floorDepth) {
        this.mFloorDepth = floorDepth;
    }

    public ARNode getWorld() {
        return this.mWorld;
    }

    public void postRender() {
    }

    public void rendererDidPause() {
    }

    public void rendererDidResume() {
    }
}
