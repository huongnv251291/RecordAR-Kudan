//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.jme3.math.Matrix4f;

public class ARCameraBackgroundMaterial extends ARMaterial {
    private ARTexture mTexture;
    private ARCameraBackgroundShader mShader;
    private float[] mUVTransform;
    private Matrix4f mMarkerModelViewProjection;

    public void setMarkerModelViewProjection(Matrix4f matrix) {
        this.mMarkerModelViewProjection = matrix;
    }

    public Matrix4f getMarkerModelViewProjection() {
        return this.mMarkerModelViewProjection;
    }

    public void setTexture(ARTexture texture) {
        this.mTexture = texture;
    }

    public ARTexture getTexture() {
        return this.mTexture;
    }

    public ARCameraBackgroundMaterial() {
        this.mShader = ARCameraBackgroundShader.getShader();
    }

    public ARCameraBackgroundMaterial(ARTexture texture) {
        this();
        this.mTexture = texture;
    }

    public boolean prepareRendererWithNode(ARNode node) {
        boolean b = super.prepareRendererWithNode(node);
        if(!b) {
            return false;
        } else {
            this.mShader.prepareRenderer();
            this.mUVTransform = ARCameraStream.getInstance().getCameraTransform();
            this.mShader.setUVTransform(this.mUVTransform);
            float[] f = new float[16];
            this.mMarkerModelViewProjection.get(f, false);
            this.mShader.setMarkerModelViewProjection(f);
            this.mTexture.prepareRenderer(0);
            return true;
        }
    }

    public void setUVTransform(float[] uvTransform) {
        this.mUVTransform = uvTransform;
    }
}
