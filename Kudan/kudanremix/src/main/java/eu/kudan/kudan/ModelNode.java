//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import java.util.ArrayList;
import java.util.List;

public class ModelNode {
    public int nodeID;
    public String name;
    public int numberOfChildren;
    public int numberOfMeshes;
    public float[] transform = new float[16];
    public List<Integer> childrenList = new ArrayList();
    public List<Integer> meshList = new ArrayList();

    public ModelNode() {
    }

    public void addChild(int childNo) {
        this.childrenList.add(Integer.valueOf(childNo));
    }

    public void addMesh(int meshNo) {
        this.meshList.add(Integer.valueOf(meshNo));
    }
}
