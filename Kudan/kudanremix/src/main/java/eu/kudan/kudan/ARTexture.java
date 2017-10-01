//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;
import java.io.FileDescriptor;
import java.nio.IntBuffer;

public class ARTexture {
    protected int mNativeMem;
    protected int mTextureID;

    private native void initN();

    private native void destroyN(int var1);

    public ARTexture() {
        ARRenderer renderer = ARRenderer.getInstance();
        renderer.addTexture(this);
        this.initN();
    }

    public void finalize() {
        this.destroyN(this.mNativeMem);
    }

    public void createTexture() {
        IntBuffer buffer = IntBuffer.allocate(1);
        GLES20.glGenTextures(1, buffer);
        this.mTextureID = buffer.get(0);
    }

    public void bindTexture(int unit) {
    }

    private native void loadDataN(FileDescriptor var1, int var2, int var3);

    public void loadData() {
    }

    public void prepareRenderer(int unit) {
    }

    public int getTextureID() {
        return this.mTextureID;
    }

    public void setTextureID(int textureID) {
        this.mTextureID = textureID;
    }

    private native int getWidthN();

    private native int getHeightN();

    public int getWidth() {
        return this.getWidthN();
    }

    public int getHeight() {
        return this.getHeightN();
    }
}
