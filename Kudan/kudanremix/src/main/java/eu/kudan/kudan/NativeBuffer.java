//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class NativeBuffer {
    private int mNativeMem;

    private native void initN();

    public NativeBuffer() {
        this.initN();
    }

    public void finalize() {
    }
}
