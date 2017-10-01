//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.jme3.math.Matrix4f;

public class ARBoneNode extends ARNode {
    private Matrix4f mOffsetMatrix;

    public ARBoneNode() {
    }

    public Matrix4f getOffsetMatrix() {
        return this.mOffsetMatrix;
    }

    public void setOffsetMatrix(Matrix4f offsetMatrix) {
        this.mOffsetMatrix = offsetMatrix;
    }

    public void setOffsetMatrix(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
        this.mOffsetMatrix = new Matrix4f(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
        this.mOffsetMatrix.transposeLocal();
    }
}
