//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.jme3.math.Matrix4f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ARMesh {
    ARVertexBuffer mVertexBuffer;
    ARIndexBuffer mIndexBuffer;
    private NativeMesh mNativeMesh;
    int mNumberOfIndices;
    private int mNumberOfVertices;
    private int mVertexDataStride;
    private boolean mHasUVs;
    private boolean mHasNormals;
    private boolean mHasTangents;
    private int mBonesPerVertex;
    private List<ARBoneNode> mBones = new ArrayList();
    private List<ARBlendShapeChannel> mBlendShapeChannels = new ArrayList();
    private int mNativeVertexBuffer;

    public ARMesh() {
    }

    public void setVertextBuffer(ARVertexBuffer vertexBuffer) {
        this.mVertexBuffer = vertexBuffer;
    }

    public int getNumbumberOfVertices() {
        return this.mNumberOfVertices;
    }

    public int getVertexDataStride() {
        return this.mVertexDataStride;
    }

    public void setBones(List<ARBoneNode> bones) {
        this.mBones = bones;
    }

    public void addBone(ARBoneNode bone) {
        this.mBones.add(bone);
    }

    public List<ARBoneNode> getBones() {
        return this.mBones;
    }

    public ARMesh(NativeMesh nativeMesh) {
        this.mNativeMesh = nativeMesh;
        this.mVertexBuffer = new ARVertexBuffer(nativeMesh);
        this.mIndexBuffer = new ARIndexBuffer(nativeMesh);
        this.mNumberOfIndices = nativeMesh.getNumberOfIndices();
    }

    public ARMesh(int nativeVertexBuffer, int nativeIndexBuffer, boolean hasNormals, boolean hasUVs, boolean hasTangents, int maxBonesPerVertex) {
        this.mNativeVertexBuffer = nativeVertexBuffer;
        this.mVertexBuffer = new ARVertexBuffer(nativeVertexBuffer, hasNormals, hasUVs, hasTangents, maxBonesPerVertex);
        this.mIndexBuffer = new ARIndexBuffer(nativeIndexBuffer);
    }

    public void createTestMeshNoUV(float width, float height) {
        this.mVertexBuffer = new ARVertexBuffer(false, false, false, 0);
        this.mIndexBuffer = new ARIndexBuffer();
        float x = width / 2.0F;
        float y = height / 2.0F;
        float u = 1.0F;
        float v = 1.0F;
        float[] vertices = new float[]{x, -y, 0.0F, x, y, 0.0F, -x, y, 0.0F, -x, -y, 0.0F};
        short[] indices = new short[]{0, 1, 2, 2, 3, 0};
        this.mVertexBuffer.setVertexData(vertices);
        this.mIndexBuffer.setIndexData(indices);
        this.mNumberOfIndices = 6;
    }

    public void createTestMesh(float width, float height) {
        this.mVertexBuffer = new ARVertexBuffer(false, true, false, 0);
        this.mIndexBuffer = new ARIndexBuffer();
        float x = width / 2.0F;
        float y = height / 2.0F;
        float u = 1.0F;
        float v = 1.0F;
        float[] vertices = new float[]{x, -y, 0.0F, 0.0F, v, x, y, 0.0F, 0.0F, 0.0F, -x, y, 0.0F, u, 0.0F, -x, -y, 0.0F, u, v};
        short[] indices = new short[]{0, 1, 2, 2, 3, 0};
        this.mVertexBuffer.setVertexData(vertices);
        this.mIndexBuffer.setIndexData(indices);
        this.mNumberOfIndices = 6;
    }

    public ARVertexBuffer getVertexBuffer() {
        return this.mVertexBuffer;
    }

    public void createTestMesh2(float width, float height) {
        this.mVertexBuffer = new ARVertexBuffer(false, true, false, 0);
        this.mIndexBuffer = new ARIndexBuffer();
        float x = width / 2.0F;
        float y = height / 2.0F;
        float u = 1.0F;
        float v = 1.0F;
        float[] vertices = new float[]{x, -y, 0.0F, u, 0.0F, x, y, 0.0F, u, v, -x, y, 0.0F, 0.0F, v, -x, -y, 0.0F, 0.0F, 0.0F};
        short[] indices = new short[]{0, 1, 2, 2, 3, 0};
        this.mVertexBuffer.setVertexData(vertices);
        this.mIndexBuffer.setIndexData(indices);
        this.mNumberOfIndices = 6;
    }

    public void createTestMeshWithUvs(float width, float height, float u, float u2, float v, float v2, boolean rotate, boolean update) {
        if(!update) {
            this.mVertexBuffer = new ARVertexBuffer(false, true, false, 0);
            this.mIndexBuffer = new ARIndexBuffer();
        }

        float x = width / 2.0F;
        float y = height / 2.0F;
        float[] vertices;
        if(!rotate) {
            vertices = new float[]{-x, y, 0.0F, u, v2, x, y, 0.0F, u2, v2, x, -y, 0.0F, u2, v, -x, -y, 0.0F, u, v};
        } else {
            vertices = new float[]{-x, y, 0.0F, u2, v2, x, y, 0.0F, u2, v, x, -y, 0.0F, u, v, -x, -y, 0.0F, u, v2};
        }

        short[] indices = new short[]{0, 1, 2, 2, 3, 0};
        this.mVertexBuffer.setVertexData(vertices);
        this.mIndexBuffer.setIndexData(indices);
        this.mNumberOfIndices = 6;
        if(update) {
            this.mVertexBuffer.updateData();
        }

    }

    public void createTestMeshWithUvs(float width, float height, float u, float u2, float v, float v2) {
        this.createTestMeshWithUvs(width, height, u, u2, v, v2, false, false);
    }

    private void processBones(ARMeshNode meshNode) {
        if(this.getBones().size() != 0) {
            List<Matrix4f> transforms = new ArrayList();
            Iterator var3 = this.getBones().iterator();

            while(var3.hasNext()) {
                ARBoneNode node = (ARBoneNode)var3.next();
                Matrix4f full = node.getFullTransform();
                Matrix4f thisInverse = meshNode.getFullTransform().invert();
                full = thisInverse.mult(full);
                full = full.mult(node.getOffsetMatrix());
                transforms.add(full);
            }

            ARRenderer renderer = ARRenderer.getInstance();
            renderer.setBones(transforms);
        }
    }

    private native void updateVertexBufferWithMeshN(int var1, ARBlendShape var2, int var3, int var4);

    private native void updateVertexBufferN(ARBlendShape var1, ARBlendShape var2);

    private void processBlendShapes() {
        ARRenderer renderer = ARRenderer.getInstance();
    }

    public boolean prepareRenderer(ARMeshNode meshNode) {
        if(this.mVertexBuffer.prepareRenderer() && this.mIndexBuffer.prepareRenderer()) {
            this.processBones(meshNode);
            this.processBlendShapes();
            return true;
        } else {
            return false;
        }
    }

    public int getNumberOfIndices() {
        return this.mNumberOfIndices;
    }

    public void addBlendShapeChannel(ARBlendShapeChannel channel) {
        this.mBlendShapeChannels.add(channel);
    }
}
