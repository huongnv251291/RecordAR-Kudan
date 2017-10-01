//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;

public class ARPointNode extends ARMeshNode {
    private int mNumberOfPoints;

    public ARPointNode() {
        ARMesh mesh = new ARMesh();
        mesh.createTestMeshNoUV(10.0F, 10.0F);
        this.setMesh(mesh);
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.mNumberOfPoints = numberOfPoints;
    }

    protected void glDraw() {
        GLES20.glClear(256);
        GLES20.glDisable(2929);
        GLES20.glDrawArrays(0, 0, this.mNumberOfPoints);
    }
}
