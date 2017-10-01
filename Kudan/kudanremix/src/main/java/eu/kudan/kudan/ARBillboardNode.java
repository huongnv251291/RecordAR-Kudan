//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.util.Log;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class ARBillboardNode extends ARNode {
    private Vector3f mForward;
    private boolean mLockX;
    private boolean mLockY;
    private boolean mLockZ;

    public ARBillboardNode() {
    }

    public boolean isLockX() {
        return this.mLockX;
    }

    public boolean isLockY() {
        return this.mLockY;
    }

    public boolean isLockZ() {
        return this.mLockZ;
    }

    public void setLockX(boolean value) {
        this.mLockX = value;
    }

    public void setLockY(boolean value) {
        this.mLockY = value;
    }

    public void setLockZ(boolean value) {
        this.mLockZ = value;
    }

    public Vector3f getForward() {
        return this.mForward;
    }

    public void setForward(Vector3f value) {
        this.mForward = value;
    }

    public void preRender() {
        super.preRender();
        this.setOrientation(Quaternion.IDENTITY);
        if(this.mForward == null) {
            Log.e("KudanAR", "forward' value not set in ARBillboardNode");
        } else {
            Vector3f eyeInNode = this.getFullTransform().invert().mult(Vector3f.ZERO);
            if(this.mLockX) {
                eyeInNode.x = 0.0F;
            }

            if(this.mLockY) {
                eyeInNode.y = 0.0F;
            }

            if(this.mLockZ) {
                eyeInNode.z = 0.0F;
            }

            Quaternion rot = this.mForward.rotationTo(eyeInNode);
            this.setOrientation(rot);
        }
    }
}
