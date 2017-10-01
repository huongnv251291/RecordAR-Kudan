//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.jme3.math.Matrix4f;

public class ARBone {
    private Matrix4f mOffsetMatrix;
    private float[] offsetMatrix = new float[16];
    private int nodeID;
    private ARNode mNode;

    public ARBone() {
    }

    public Matrix4f getOffsetMatrix() {
        if(this.mOffsetMatrix == null) {
            this.mOffsetMatrix = new Matrix4f(this.offsetMatrix);
        }

        return this.mOffsetMatrix;
    }

    public int getNodeID() {
        return this.nodeID;
    }

    public ARNode getNode() {
        return this.mNode;
    }

    public void setNode(ARNode node) {
        this.mNode = node;
    }
}
