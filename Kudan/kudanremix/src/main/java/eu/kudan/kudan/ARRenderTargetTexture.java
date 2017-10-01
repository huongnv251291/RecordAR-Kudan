//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;
import android.util.Log;
import java.nio.Buffer;

public class ARRenderTargetTexture extends ARRenderTarget {
    private ARTexture2D mTexture;
    private int mFBO;
    private int mWidth;
    private int mHeight;

    public ARRenderTargetTexture(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        this.mTexture = new ARTexture2D();
    }

    public ARTexture2D getTexture() {
        return this.mTexture;
    }

    public void bind() {
        GLES20.glBindFramebuffer('赀', this.mFBO);
    }

    public void create() {
        int[] n = new int[1];
        GLES20.glGenFramebuffers(1, n, 0);
        int fboID = n[0];
        this.mFBO = fboID;
        GLES20.glBindFramebuffer('赀', fboID);
        GLES20.glGenTextures(1, n, 0);
        int textureID = n[0];
        GLES20.glBindTexture(3553, textureID);
        this.mTexture.setTextureID(textureID);
        Log.i("TEXTURE", "texture: " + textureID);
        GLES20.glTexParameterf(3553, 10241, 9729.0F);
        GLES20.glTexParameterf(3553, 10240, 9729.0F);
        GLES20.glTexParameterf(3553, 10243, 33071.0F);
        GLES20.glTexParameterf(3553, 10242, 33071.0F);
        GLES20.glTexImage2D(3553, 0, 6408, this.mWidth, this.mHeight, 0, 6408, 5121, (Buffer)null);
        GLES20.glFramebufferTexture2D('赀', '賠', 3553, textureID, 0);
        GLES20.glGenRenderbuffers(1, n, 0);
        int depthBuffer = n[0];
        GLES20.glBindRenderbuffer('赁', depthBuffer);
        GLES20.glRenderbufferStorage('赁', '膥', this.mWidth, this.mHeight);
        GLES20.glFramebufferRenderbuffer('赀', '贀', '赁', depthBuffer);
    }

    public void draw() {
        super.draw();
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }
}
