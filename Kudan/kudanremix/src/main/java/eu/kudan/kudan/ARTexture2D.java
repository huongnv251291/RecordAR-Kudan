//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.content.res.AssetFileDescriptor;
import android.opengl.GLES20;
import java.io.FileDescriptor;

public class ARTexture2D extends ARTexture {
    String mAssetName;
    private int mNativeMem;

    private native void loadFromAssetN(FileDescriptor var1, int var2, int var3);

    private native void loadFromPathN(String var1);

    private native void loadFromBytesN(byte[] var1);

    void loadFromBytes(byte[] imageData, String name) {
        this.loadFromBytesN(imageData);
        this.mAssetName = name;
    }

    public void loadFromAsset(String assetName) {
        try {
            ARRenderer renderer = ARRenderer.getInstance();
            AssetFileDescriptor fd = renderer.getAssetManager().openFd(assetName);
            this.loadFromAssetN(fd.getFileDescriptor(), (int)fd.getStartOffset(), (int)fd.getLength());
            fd.close();
        } catch (Exception var4) {
            var4.printStackTrace();
            return;
        }

        this.mAssetName = assetName;
    }

    public void loadFromPath(String path) {
        this.mAssetName = path;
        this.loadFromPathN(path);
    }

    public ARTexture2D(int textureID) {
        this.mTextureID = textureID;
    }

    public ARTexture2D() {
    }

    public void bindTexture(int unit) {
        int texUnit = '蓀';
        if(unit == 1) {
            texUnit = '蓁';
        }

        if(unit == 2) {
            texUnit = '蓂';
        }

        GLES20.glActiveTexture(texUnit);
        GLES20.glBindTexture(3553, this.mTextureID);
    }

    private native void loadDataN();

    public void loadData() {
        if(this.mAssetName != null) {
            this.createTexture();
        }

        this.bindTexture(0);
        this.loadDataN();
    }

    public void prepareRenderer(int unit) {
        super.prepareRenderer(unit);
        this.bindTexture(unit);
        GLES20.glTexParameterf(3553, 10241, 9729.0F);
        GLES20.glTexParameterf(3553, 10240, 9729.0F);
    }
}
