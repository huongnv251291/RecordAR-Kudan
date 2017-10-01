//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.content.res.AssetFileDescriptor;
import android.opengl.GLES20;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

public class ARTexture3D extends ARTexture {
    private List<String> mFaceAssets = new ArrayList();

    public ARTexture3D(String posX, String negX, String posY, String negY, String posZ, String negZ) {
        this.mFaceAssets.add(posX);
        this.mFaceAssets.add(negX);
        this.mFaceAssets.add(posY);
        this.mFaceAssets.add(negY);
        this.mFaceAssets.add(posZ);
        this.mFaceAssets.add(negZ);
    }

    public void bindTexture(int unit) {
        int texUnit = '蓀';
        if(unit == 1) {
            texUnit = '蓁';
        }

        GLES20.glActiveTexture(texUnit);
        GLES20.glBindTexture('蔓', this.mTextureID);
    }

    private native void loadDataN(FileDescriptor var1, int var2, int var3, int var4);

    public void loadData() {
        this.createTexture();
        this.bindTexture(0);

        for(int i = 0; i < 6; ++i) {
            String assetName = (String)this.mFaceAssets.get(i);
            int[] faces = new int[]{'蔕', '蔖', '蔗', '蔘', '蔙', '蔚'};
            int face = faces[i];

            try {
                ARRenderer renderer = ARRenderer.getInstance();
                AssetFileDescriptor fd = renderer.getAssetManager().openFd(assetName);
                this.loadDataN(fd.getFileDescriptor(), (int)fd.getStartOffset(), (int)fd.getLength(), face);
                fd.close();
            } catch (Exception var7) {
                var7.printStackTrace();
                return;
            }
        }

    }

    public void prepareRenderer(int unit) {
        super.prepareRenderer(unit);
        this.bindTexture(unit);
        GLES20.glTexParameterf('蔓', 10241, 9729.0F);
        GLES20.glTexParameterf('蔓', 10240, 9729.0F);
    }
}
