//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;
import android.util.Log;
import java.nio.Buffer;

public class ARTextureRenderTarget extends ARRenderTarget {
    private int mWidth;
    private int mHeight;
    private ARTexture2D mTexture;

    public ARTextureRenderTarget(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public void bind() {
        GLES20.glBindFramebuffer('赀', this.mFramebufferID);
    }

    public void create() {
        int[] n = new int[1];
        GLES20.glGenFramebuffers(1, n, 0);
        int fboID = n[0];
        this.mFramebufferID = fboID;
        GLES20.glBindFramebuffer('赀', fboID);
        GLES20.glGenTextures(1, n, 0);
        int textureID = n[0];
        GLES20.glBindTexture(3553, textureID);
        Log.i("ARTextureRenderTarget", "create texture framebuffer with ID: " + textureID + " " + this.mWidth + "x" + this.mHeight);
        GLES20.glTexParameterf(3553, 10241, 9729.0F);
        GLES20.glTexParameterf(3553, 10240, 9729.0F);
        GLES20.glTexParameterf(3553, 10243, 33071.0F);
        GLES20.glTexParameterf(3553, 10242, 33071.0F);
        GLES20.glTexImage2D(3553, 0, 6408, this.mWidth, this.mHeight, 0, 6408, 5121, (Buffer)null);
        GLES20.glFramebufferTexture2D('赀', '賠', 3553, textureID, 0);
    }
}
