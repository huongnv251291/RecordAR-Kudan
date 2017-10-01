//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ARVertexBuffer {
    private int mBufferID;
    private int mStride;
    private int mPositionOffset;
    private int mNormalOffset;
    private int mUVOffset;
    private int mTangentOffset;
    private int mBoneIndexOffset;
    private int mBoneWeightOffset;
    private int mNormalTargetOffset;
    private int mUVTargetOffset;
    private boolean mHasNormals;
    private boolean mHasUVs;
    private boolean mHasTangents;
    private boolean mHasBones;
    private int mMaxBones;
    private NativeMesh mNativeMesh;
    private int mNativeMem;
    private FloatBuffer mVertexData;

    public ARVertexBuffer(boolean hasNormals, boolean hasUVs, boolean hasTangents, int maxBones) {
        this.mBufferID = -1;
        this.mStride = 0;
        this.mPositionOffset = 0;
        this.mNormalOffset = -1;
        this.mUVOffset = -1;
        this.mTangentOffset = -1;
        this.mBoneIndexOffset = -1;
        this.mBoneWeightOffset = -1;
        this.mNormalTargetOffset = -1;
        this.mUVTargetOffset = -1;
        this.mHasNormals = hasNormals;
        this.mHasUVs = hasUVs;
        this.mHasTangents = hasTangents;
        this.mMaxBones = maxBones;
        this.mHasBones = maxBones > 0;
        int offset = 12;
        if (this.mHasNormals) {
            this.mNormalOffset = offset;
            offset += 12;
        }

        if (this.mHasTangents) {
            this.mTangentOffset = offset;
            offset += 16;
        }

        if (this.mHasUVs) {
            this.mUVOffset = offset;
            offset += 8;
        }

        if (this.mMaxBones > 0) {
            this.mBoneIndexOffset = offset;
            offset += 4 * this.mMaxBones;
            this.mBoneWeightOffset = offset;
            offset += 4 * this.mMaxBones;
        }

        this.mStride = offset;
        ARRenderer renderer = ARRenderer.getInstance();
        renderer.addVertexBuffer(this);
    }

    public ARVertexBuffer(NativeMesh nativeMesh) {
        this(nativeMesh.getHasNormals(), nativeMesh.getHasUVs(), nativeMesh.getHasTangents(), nativeMesh.getMaxBones());
        this.mNativeMesh = nativeMesh;
    }

    public ARVertexBuffer(int nativeBuffer, boolean hasNormals, boolean hasUVs, boolean hasTangents, int maxBonesPerVertex) {
        this(hasNormals, hasUVs, hasTangents, maxBonesPerVertex);
        this.mNativeMem = nativeBuffer;
    }

    public int getStride() {
        return this.mStride;
    }

    public void setVertexData(float[] vertexData) {
        this.mVertexData = FloatBuffer.allocate(vertexData.length);
        this.mVertexData.put(vertexData);
        this.mVertexData.flip();
    }

    public void createBuffer() {
        IntBuffer buffer = IntBuffer.allocate(1);
        GLES20.glGenBuffers(1, buffer);
        this.mBufferID = buffer.get(0);
    }

    public void bindBuffer() {
        GLES20.glBindBuffer('袒', this.mBufferID);
    }

    private native void loadDataN(int var1);

    public void loadData() {
        this.createBuffer();
        this.bindBuffer();
        if (this.mNativeMem == 0) {
            int size = this.mVertexData.capacity() * 4;
            GLES20.glBufferData('袒', size, this.mVertexData, '裤');
        } else {
            this.loadDataN(this.mNativeMem);
        }

    }

    public void updateData() {
        this.bindBuffer();
        if (this.mNativeMem == 0) {
            int size = this.mVertexData.capacity() * 4;
            GLES20.glBufferData('袒', size, this.mVertexData, '裤');
        } else {
            this.loadDataN(this.mNativeMem);
        }

    }

    public void updateBlendShape(boolean hasNormals, boolean hasUVs, boolean hasTangents) {
        this.mHasNormals = hasNormals;
        this.mHasUVs = hasUVs;
        this.mHasTangents = hasTangents;
        int offset = 12;
        if (this.mHasNormals) {
            this.mNormalOffset = offset;
            offset += 12;
        }

        if (this.mHasTangents) {
            this.mTangentOffset = offset;
            offset += 16;
        }

        if (this.mHasUVs) {
            this.mUVOffset = offset;
            offset += 8;
        }

        if (this.mHasNormals) {
            this.mNormalTargetOffset = offset;
            offset += 12;
        }

        if (this.mHasUVs) {
            this.mUVTargetOffset = offset;
            offset += 8;
        }

        this.mStride = offset;
    }

    public boolean prepareRenderer() {
        if (this.mBufferID == 0) {
            return false;
        } else {
            this.bindBuffer();
            ARRenderer renderer = ARRenderer.getInstance();
            GLES20.glVertexAttribPointer(0, 3, 5126, false, this.getStride(), this.mPositionOffset);
            GLES20.glEnableVertexAttribArray(0);
            if (this.mHasNormals) {
                GLES20.glVertexAttribPointer(1, 3, 5126, false, this.getStride(), this.mNormalOffset);
                GLES20.glEnableVertexAttribArray(1);
            } else {
                GLES20.glDisableVertexAttribArray(1);
            }

            if (this.mHasUVs) {
                GLES20.glVertexAttribPointer(2, 2, 5126, false, this.getStride(), this.mUVOffset);
                GLES20.glEnableVertexAttribArray(2);
            } else {
                GLES20.glDisableVertexAttribArray(2);
            }

            if (this.mHasBones) {
                GLES20.glVertexAttribPointer(3, 4, 5126, false, this.getStride(), this.mBoneIndexOffset);
                GLES20.glVertexAttribPointer(4, 4, 5126, false, this.getStride(), this.mBoneWeightOffset);
                GLES20.glEnableVertexAttribArray(3);
                GLES20.glEnableVertexAttribArray(4);
            } else {
                GLES20.glVertexAttribPointer(3, 3, 5126, false, this.getStride(), 0);
                GLES20.glVertexAttribPointer(4, 3, 5126, false, this.getStride(), 0);
                GLES20.glEnableVertexAttribArray(3);
                GLES20.glEnableVertexAttribArray(4);
            }

            return true;
        }
    }
}
