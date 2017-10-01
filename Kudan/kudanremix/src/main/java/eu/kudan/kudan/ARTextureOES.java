//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;

public class ARTextureOES extends ARTexture {
    protected SurfaceTexture mSurfaceTexture;

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        this.mSurfaceTexture = surfaceTexture;
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.mSurfaceTexture;
    }

    public ARTextureOES(int textureID) {
        this.mTextureID = textureID;
    }

    public ARTextureOES() {
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
        GLES20.glBindTexture('赥', this.mTextureID);
    }

    public void prepareRenderer(int unit) {
        super.prepareRenderer(unit);
        this.bindTexture(unit);
        GLES20.glTexParameterf(3553, 10241, 9729.0F);
        GLES20.glTexParameterf(3553, 10240, 9729.0F);
    }
}
