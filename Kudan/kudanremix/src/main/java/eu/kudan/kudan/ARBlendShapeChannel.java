//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class ARBlendShapeChannel {
    private int mNativeMem;
    private List<ARBlendShape> mBlendShapes = new ArrayList();
    private float mInfluence;

    public ARBlendShapeChannel(int nativeMem) {
        this.mNativeMem = nativeMem;
    }

    public void addShape(ARBlendShape shape) {
        this.mBlendShapes.add(shape);
    }

    public void setInfluence(float influence) {
        this.mInfluence = influence;
        Log.i("AR", "influence set to " + influence);
    }

    public float getInfluence() {
        return this.mInfluence;
    }

    public List<ARBlendShape> getShapes() {
        return this.mBlendShapes;
    }
}
