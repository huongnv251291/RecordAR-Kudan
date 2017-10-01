//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.content.res.AssetFileDescriptor;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ARModelImporter {
    private int mNativeMem;
    private List<ARMesh> mMeshes;
    private List<ARNode> mNodes;
    private List<ARMeshNode> mMeshNodes;
    private List<ARAnimationChannel> mNodeAnimationChannels;
    private List<ARBlendAnimationChannel> mBlendAnimationChannels;
    private ARModelNode mModelNode;

    private native void initN();

    public ARModelImporter() {
        this.initN();
        this.mMeshes = new ArrayList();
        this.mNodes = new ArrayList();
        this.mMeshNodes = new ArrayList();
        this.mNodeAnimationChannels = new ArrayList();
        this.mBlendAnimationChannels = new ArrayList();
        this.mModelNode = new ARModelNode();
    }

    private native void destroyN(int var1);

    public void finalize() {
        this.destroyN(this.mNativeMem);
    }

    private native void loadFromPathN(String var1);

    public void loadFromPath(String path) {
        this.loadFromPathN(path);
        this.process();
    }

    private native void loadFromAssetN(FileDescriptor var1, int var2, int var3);

    public void loadFromAsset(String asset) {
        try {
            ARRenderer renderer = ARRenderer.getInstance();
            AssetFileDescriptor fd = renderer.getAssetManager().openFd(asset);
            this.loadFromAssetN(fd.getFileDescriptor(), (int)fd.getStartOffset(), (int)fd.getLength());
            fd.close();
            this.process();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    private void process() {
        this.mModelNode.setMeshNodes(this.mMeshNodes);
        this.mModelNode.setNodeAnimationChannels(this.mNodeAnimationChannels);
        this.mModelNode.setBlendAnimationChannels(this.mBlendAnimationChannels);
        Iterator var1 = this.mNodes.iterator();

        while(var1.hasNext()) {
            ARNode node = (ARNode)var1.next();
            if(node.getParent() == null) {
                this.mModelNode.addChild(node);
                break;
            }
        }

    }

    private void addMesh(ARMesh mesh) {
        this.mMeshes.add(mesh);
    }

    private void addMeshNode(ARMeshNode meshNode) {
        this.mMeshNodes.add(meshNode);
    }

    private void addNode(ARNode node) {
        this.mNodes.add(node);
    }

    private void addAnimationChannel(ARNode node, int nativeMem) {
        ARAnimationChannel channel = new ARAnimationChannel(node, nativeMem);
        this.mNodeAnimationChannels.add(channel);
    }

    public ARModelNode getNode() {
        return this.mModelNode;
    }

    public List<ARMeshNode> getMeshNodes() {
        return this.mMeshNodes;
    }

    public void addBlendAnimationChannel(ARBlendShapeChannel channel, int nativeMem) {
        ARBlendAnimationChannel blendChannel = new ARBlendAnimationChannel(channel, nativeMem);
        this.mBlendAnimationChannels.add(blendChannel);
    }
}
