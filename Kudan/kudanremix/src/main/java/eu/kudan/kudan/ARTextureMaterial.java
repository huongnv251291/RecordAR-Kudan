//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class ARTextureMaterial extends ARMaterial {
    private ARTexture mTexture;
    private ARTextureShader mShader;
    private float mAlpha;

    public float getAlpha() {
        return this.mAlpha;
    }

    public void setAlpha(float alpha) {
        this.mShader.setAlpha(alpha);
        this.mAlpha = alpha;
    }

    public ARTextureMaterial() {
        this.mAlpha = 1.0F;
        this.mShader = ARTextureShader.getShader();
    }

    public ARTextureMaterial(ARTexture texture) {
        this();
        this.mTexture = texture;
    }

    public void setTexture(ARTexture newTexture) {
        this.mTexture = newTexture;
    }

    public ARTexture getTexture() {
        return this.mTexture;
    }

    public boolean prepareRendererWithNode(ARNode node) {
        boolean b = super.prepareRendererWithNode(node);
        if(!b) {
            return false;
        } else {
            this.mShader.prepareRenderer();
            this.mShader.setAlpha(this.mAlpha);
            this.mTexture.prepareRenderer(0);
            return true;
        }
    }
}
