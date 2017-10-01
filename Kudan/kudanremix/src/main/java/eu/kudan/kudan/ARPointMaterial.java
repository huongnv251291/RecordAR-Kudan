//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class ARPointMaterial extends ARMaterial {
    private ARTexture2D mTexture;
    private ARPointShader mShader;

    public ARPointMaterial() {
        this.mShader = ARPointShader.getShader();
    }

    public ARPointMaterial(ARTexture2D texture) {
        this();
        this.mTexture = texture;
    }

    public boolean prepareRendererWithNode(ARNode node) {
        boolean b = super.prepareRendererWithNode(node);
        if(!b) {
            return false;
        } else {
            this.mShader.prepareRenderer();
            this.mTexture.prepareRenderer(0);
            return true;
        }
    }
}
