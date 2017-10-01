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

public class ARImageTrackable {
    private ARWorld mWorld;
    private String mName;
    private float mWidth;
    private float mHeight;
    private boolean mDetected;
    private List<ARImageTrackableListener> mListeners;
    private int mNativeMem;
    private boolean mLastDetectionState;

    private native void initN();

    private native void destroyN(int var1);

    public ARImageTrackable() {
        this.mWorld = new ARWorld();
        this.mListeners = new ArrayList();
        this.initN();
    }

    public ARImageTrackable(int nativeMem) {
        this.mListeners = new ArrayList();
        this.mWorld = new ARWorld();
        this.mNativeMem = nativeMem;
    }

    public ARImageTrackable(String name) {
        this();
        this.mName = name;
    }

    private native void loadFromPathN(String var1, boolean var2);

    public void loadFromPath(String path) {
        this.loadFromPathN(path, false);
        if(this.mName != null) {
            this.setName(this.mName);
        }

    }

    public void loadFromPath(String path, boolean autoCrop) {
        this.loadFromPathN(path, autoCrop);
        if(this.mName != null) {
            this.setName(this.mName);
        }

    }

    private native void loadFromAssetN(FileDescriptor var1, int var2, int var3, boolean var4);

    public void loadFromAsset(String asset) {
        this.loadFromAsset(asset, false);
    }

    public void loadFromAsset(String asset, boolean autoCrop) {
        try {
            ARRenderer renderer = ARRenderer.getInstance();
            AssetFileDescriptor fd = renderer.getAssetManager().openFd(asset);
            this.loadFromAssetN(fd.getFileDescriptor(), (int)fd.getStartOffset(), (int)fd.getLength(), autoCrop);
            fd.close();
            if(this.mName != null) {
                this.setName(this.mName);
            }

        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    public void finalize() {
        this.destroyN(this.mNativeMem);
        this.mNativeMem = 0;
    }

    public ARWorld getWorld() {
        return this.mWorld;
    }

    private native void setNameN(String var1);

    public void setName(String name) {
        this.setNameN(name);
    }

    private native String getNameN();

    public String getName() {
        return this.getNameN();
    }

    public boolean getDetected() {
        return this.mDetected;
    }

    public void trackerStartFrame() {
        this.mLastDetectionState = this.mDetected;
    }

    private native float getWidthN();

    public float getWidth() {
        return this.getWidthN();
    }

    private native float getHeightN();

    public float getHeight() {
        return this.getHeightN();
    }

    public void trackerEndFrame() {
        Iterator var1;
        ARImageTrackableListener listener;
        if(this.mDetected) {
            if(!this.mLastDetectionState) {
                var1 = this.mListeners.iterator();

                while(var1.hasNext()) {
                    listener = (ARImageTrackableListener)var1.next();
                    listener.didDetect(this);
                }
            }

            var1 = this.mListeners.iterator();

            while(var1.hasNext()) {
                listener = (ARImageTrackableListener)var1.next();
                listener.didTrack(this);
            }
        } else if(this.mLastDetectionState) {
            var1 = this.mListeners.iterator();

            while(var1.hasNext()) {
                listener = (ARImageTrackableListener)var1.next();
                listener.didLose(this);
            }
        }

    }

    public void trackerSetDetected(boolean detected) {
        this.mDetected = detected;
    }

    public List<ARImageTrackableListener> getListeners() {
        return this.mListeners;
    }

    public void addListener(ARImageTrackableListener listener) {
        this.mListeners.add(listener);
    }

    private native boolean isFlowRecoverableN(boolean var1);

    public boolean isFlowRecoverable(boolean globalSetting) {
        return this.isFlowRecoverableN(globalSetting);
    }

    private native void setExtensibleN(boolean var1);

    public void setExtensible(boolean extensible) {
        if(this.mName != null) {
            this.setExtensibleN(extensible);
        }

    }

    private native boolean isExtensibleN();

    public boolean isExtensible() {
        return this.isExtensibleN();
    }

    private native boolean isExtendedN();

    public boolean isExtended() {
        return this.isExtendedN();
    }

    private native void clearExtensionsN();

    public void clearExtensions() {
        this.clearExtensionsN();
    }
}
