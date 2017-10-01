//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class ARIndexBuffer {
    private int mBufferID;
    private ShortBuffer mIndexData;
    private NativeMesh mNativeMesh;
    private int mNativeMem;

    ARIndexBuffer() {
        ARRenderer renderer = ARRenderer.getInstance();
        renderer.addIndexBuffer(this);
    }

    ARIndexBuffer(NativeMesh nativeMesh) {
        this();
        this.mNativeMesh = nativeMesh;
    }

    ARIndexBuffer(int nativeBuffer) {
        this();
        this.mNativeMem = nativeBuffer;
    }

    public void setIndexData(short[] indexData) {
        this.mIndexData = ShortBuffer.allocate(indexData.length);
        this.mIndexData.put(indexData);
        this.mIndexData.flip();
    }

    void createBuffer() {
        IntBuffer buffer = IntBuffer.allocate(1);
        GLES20.glGenBuffers(1, buffer);
        this.mBufferID = buffer.get(0);
    }

    public void bindBuffer() {
        GLES20.glBindBuffer('袓', this.mBufferID);
    }

    private native void loadDataN(int var1);

    public void loadData() {
        this.createBuffer();
        this.bindBuffer();
        if(this.mNativeMem == 0) {
            int size = this.mIndexData.capacity() * 2;
            GLES20.glBufferData('袓', size, this.mIndexData, '裤');
        } else {
            this.loadDataN(this.mNativeMem);
        }

    }

    public boolean prepareRenderer() {
        if(this.mBufferID == 0) {
            return false;
        } else {
            this.bindBuffer();
            return true;
        }
    }
}
