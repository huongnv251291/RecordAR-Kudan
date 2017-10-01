//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;

public class ARMaterial {
    private boolean mIsTransparent;
    private boolean mDepthOnly;
    private boolean mDepthWrite = true;
    private boolean mDepthTest = true;
    private String mName;

    public ARMaterial() {
    }

    public boolean getTransparent() {
        return this.mIsTransparent;
    }

    public void setTransparent(boolean transparent) {
        this.mIsTransparent = transparent;
    }

    public boolean getDepthOnly() {
        return this.mDepthOnly;
    }

    public void setDepthOnly(boolean depthOnly) {
        this.mDepthOnly = depthOnly;
    }

    public void setDepthWrite(boolean depthWrite) {
        this.mDepthWrite = depthWrite;
    }

    public boolean getDepthWrite() {
        return this.mDepthWrite;
    }

    public void setDepthTest(boolean depthTest) {
        this.mDepthTest = depthTest;
    }

    public boolean getDepthTest() {
        return this.mDepthTest;
    }

    public boolean prepareRendererWithNode(ARNode node) {
        if(this.mIsTransparent) {
            GLES20.glEnable(3042);
            GLES20.glBlendFunc(770, 771);
        } else {
            GLES20.glDisable(3042);
        }

        if(this.mDepthOnly) {
            GLES20.glColorMask(false, false, false, false);
        } else {
            GLES20.glColorMask(true, true, true, true);
        }

        GLES20.glDepthMask(this.mDepthWrite);
        if(this.mDepthTest) {
            GLES20.glEnable(2929);
        } else {
            GLES20.glDisable(2929);
        }

        return true;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getName() {
        return this.mName;
    }
}
