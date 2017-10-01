//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class ARVideoTextureMaterial extends ARMaterial {
    private ARTextureOES mTexture;
    private ARVideoTextureShader mShader;
    private float[] mUVTransform;

    public ARVideoTextureMaterial() {
        this.mUVTransform = new float[16];
        this.mShader = ARVideoTextureShader.getShader();
    }

    public ARVideoTextureMaterial(ARTextureOES texture) {
        this();
        this.mTexture = texture;
    }

    public boolean prepareRendererWithNode(ARNode node) {
        boolean b = super.prepareRendererWithNode(node);
        if(!b) {
            return false;
        } else {
            if(this.mTexture.getSurfaceTexture() != null) {
                this.mTexture.getSurfaceTexture().getTransformMatrix(this.mUVTransform);
            }

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
