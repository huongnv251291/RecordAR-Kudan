//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import java.util.Iterator;
import java.util.List;

public class ARModelNode extends ARNode {
    private List<ARMeshNode> mMeshNodes;
    private List<ARAnimationChannel> mNodeAnimationChannels;
    private List<ARBlendAnimationChannel> mBlendAnimationChannels;
    private long mStartTime;
    private long mStartAnimationTime;
    private long mCurrentAnimationTime;
    private boolean isPlaying;

    public ARModelNode() {
    }

    public void setMeshNodes(List<ARMeshNode> meshNodes) {
        this.mMeshNodes = meshNodes;
    }

    public List<ARMeshNode> getMeshNodes() {
        return this.mMeshNodes;
    }

    public void setNodeAnimationChannels(List<ARAnimationChannel> animationChannels) {
        this.mNodeAnimationChannels = animationChannels;
    }

    public void setBlendAnimationChannels(List<ARBlendAnimationChannel> animationChannels) {
        this.mBlendAnimationChannels = animationChannels;
    }

    public void play() {
        this.isPlaying = true;
        this.mStartTime = 0L;
    }

    public void pause() {
        this.isPlaying = false;
    }

    public void reset() {
        this.mCurrentAnimationTime = 0L;
        this.mStartAnimationTime = 0L;
        this.mStartTime = 0L;
        this.isPlaying = false;
    }

    private void updateAnimations() {
        ARRenderer renderer = ARRenderer.getInstance();
        if(this.mStartTime == 0L) {
            this.mStartTime = renderer.getRenderTime();
            this.mStartAnimationTime = this.mCurrentAnimationTime;
        }

        long diff = renderer.getRenderTime() - this.mStartTime;
        this.mCurrentAnimationTime = this.mStartAnimationTime + diff;
        int frame = (int)((double)this.mCurrentAnimationTime / 1000.0D * 30.0D);
        Iterator var5 = this.mNodeAnimationChannels.iterator();

        while(var5.hasNext()) {
            ARAnimationChannel channel = (ARAnimationChannel)var5.next();
            boolean finished = channel.updateTransform(frame);
            if(finished) {
                this.reset();
                break;
            }
        }

        var5 = this.mBlendAnimationChannels.iterator();

        while(var5.hasNext()) {
            ARBlendAnimationChannel channel = (ARBlendAnimationChannel)var5.next();
            channel.update(frame);
        }

    }

    public void preRender() {
        super.preRender();
        if(this.isPlaying) {
            if(this.mMeshNodes != null) {
                this.updateAnimations();
            }

        }
    }

    public long getCurrentAnimationTime() {
        return this.mCurrentAnimationTime;
    }

    public void setCurrentAnimationTime(long mCurrentAnimationTime) {
        this.mCurrentAnimationTime = mCurrentAnimationTime;
        synchronized(ARRenderer.getInstance()) {
            this.updateAnimations();
        }
    }
}
