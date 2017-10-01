//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class ARAnimationChannel {
    private ARNode mNode;
    private int mNativeMem;

    public ARAnimationChannel(ARNode node, int nativeMem) {
        this.mNode = node;
        this.mNativeMem = nativeMem;
    }

    private native boolean updateTransformN(ARNode var1, int var2);

    public boolean updateTransform(int frame) {
        return this.updateTransformN(this.mNode, frame);
    }
}
