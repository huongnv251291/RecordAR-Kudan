//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class ARCameraTextureMaterial extends ARMaterial {
    private ARTexture mTexture;
    private ARCameraTextureShader mShader;
    private float[] mUVTransform;

    public ARCameraTextureMaterial() {
        this.mShader = ARCameraTextureShader.getShader();
    }

    public ARCameraTextureMaterial(ARTexture texture) {
        this();
        this.mTexture = texture;
    }

    public boolean prepareRendererWithNode(ARNode node) {
        boolean b = super.prepareRendererWithNode(node);
        if(!b) {
            return false;
        } else {
            this.mShader.prepareRenderer();
            this.mShader.setUVTransform(this.mUVTransform);
            this.mTexture.prepareRenderer(0);
            return true;
        }
    }

    public void setUVTransform(float[] uvTransform) {
        this.mUVTransform = uvTransform;
    }
}
