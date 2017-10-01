//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class ARBlendAnimationChannel {
    private int mNativeMem;
    private ARBlendShapeChannel mShapeChannel;

    public ARBlendAnimationChannel(ARBlendShapeChannel channel, int nativeMem) {
        this.mNativeMem = nativeMem;
        this.mShapeChannel = channel;
    }

    private native float getInfluenceN(int var1);

    public void update(int frameNo) {
        float influence = this.getInfluenceN(frameNo);
        this.mShapeChannel.setInfluence(influence);
    }
}
