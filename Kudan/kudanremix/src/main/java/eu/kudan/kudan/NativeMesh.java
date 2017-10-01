//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class NativeMesh {
    private int mNativeMem;

    private native void initN();

    public NativeMesh() {
        this.initN();
    }

    native boolean getHasNormalsN();

    public boolean getHasNormals() {
        return this.getHasNormalsN();
    }

    native boolean getHasUVsN();

    public boolean getHasUVs() {
        return this.getHasUVsN();
    }

    native boolean getHasTangentsN();

    public boolean getHasTangents() {
        return this.getHasTangentsN();
    }

    native boolean getHasBonesN();

    public boolean getHasBones() {
        return this.getHasBones();
    }

    native int getMaxBonesN();

    public int getMaxBones() {
        return this.getMaxBonesN();
    }

    native int getNumberOfBonesN();

    public int getNumberOfBones() {
        return this.getNumberOfBonesN();
    }

    native int getNumberOfIndicesN();

    public int getNumberOfIndices() {
        return this.getNumberOfIndicesN();
    }

    private native void releaseN();

    public void finalize() {
        this.releaseN();
    }
}
