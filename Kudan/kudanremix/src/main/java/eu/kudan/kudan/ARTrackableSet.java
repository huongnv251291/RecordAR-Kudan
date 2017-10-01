//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.content.res.AssetFileDescriptor;
import android.util.Log;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

public class ARTrackableSet {
    private List<ARImageTrackable> mTrackables = new ArrayList();

    public ARTrackableSet() {
    }

    public List<ARImageTrackable> getTrackables() {
        return this.mTrackables;
    }

    private native void loadFromPathN(String var1);

    public void loadFromPath(String path) {
        this.loadFromPathN(path);
    }

    private native void loadFromAssetN(FileDescriptor var1, int var2, int var3);

    public void loadFromAsset(String asset) {
        try {
            ARRenderer renderer = ARRenderer.getInstance();
            AssetFileDescriptor fd = renderer.getAssetManager().openFd(asset);
            this.loadFromAssetN(fd.getFileDescriptor(), (int)fd.getStartOffset(), (int)fd.getLength());
            fd.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    private void createTrackableWithNativeMem(int nativeMem) {
        ARImageTrackable trackable = new ARImageTrackable(nativeMem);
        this.mTrackables.add(trackable);
        Log.i("KudanAR", "adding: " + trackable.getName());
    }
}
