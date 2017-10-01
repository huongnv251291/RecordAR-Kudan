//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.jme3.math.Matrix4f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ARArbiTrack implements ARGyroManagerListener {
    private static ARArbiTrack arbiTrack;
    private ARWorld mWorld = new ARWorld();
    private ARNode mTargetNode = new ARNode();
    private boolean mIsTracking;
    private List<ARArbiTrackListener> mListeners;
    private boolean shouldNotify;
    private boolean mIsInitialised;

    public ARArbiTrack() {
    }

    public static ARArbiTrack getInstance() {
        if(arbiTrack == null || !arbiTrack.mIsInitialised) {
            arbiTrack = new ARArbiTrack();
            arbiTrack.initialise();
            arbiTrack.mIsInitialised = true;
        }

        return arbiTrack;
    }

    private native void processFrameN(byte[] var1, int var2, int var3);

    void processFrame(byte[] cameraImage, int width, int height) {
        if(this.mIsInitialised) {
            this.mWorld.setOrientation(ARGyroManager.getInstance().getWorld().getOrientation());
            this.processFrameN(cameraImage, width, height);
            if(this.shouldNotify) {
                this.notifyListenersArbiTrackStarted();
                this.shouldNotify = false;
            }

        }
    }

    private native void initN();

    public void initialise() {
        this.initN();
        ARGyroManager.getInstance().addListener(this);
        this.mWorld.setVisible(false);
        this.mListeners = new ArrayList();
        this.shouldNotify = false;
    }

    private native void destroyN();

    private native void deinitialiseN();

    public static void deinitialise() {
        if(arbiTrack != null) {
            arbiTrack.stop();
            arbiTrack.deinitialiseN();
            arbiTrack.destroyN();
            arbiTrack.mTargetNode = new ARNode();
            arbiTrack.mWorld = new ARWorld();
            arbiTrack.mIsTracking = false;
            arbiTrack.mIsInitialised = false;
        }

    }

    public void setTargetNode(ARNode node) {
        this.mTargetNode = node;
    }

    public ARNode getTargetNode() {
        return this.mTargetNode;
    }

    public ARWorld getWorld() {
        return this.mWorld;
    }

    public boolean getIsTracking() {
        return this.mIsTracking;
    }

    private native void startN(float[] var1);

    public void start() {
        Matrix4f fullTransform = this.mTargetNode.getFullTransform();
        float[] transform = new float[16];
        fullTransform.get(transform, false);
        this.startN(transform);
        ARGyroManager.getInstance().start();
        this.mWorld.setVisible(true);
        this.mIsTracking = true;
    }

    private native void stopN();

    public void stop() {
        this.stopN();
        this.mWorld.setVisible(false);
        this.mIsTracking = false;
    }

    private void setWorldPosition(float x, float y, float z) {
        this.mWorld.setPosition(-x, -y, z);
    }

    public void notifyListenersArbiTrackStarted() {
        Iterator var1 = this.mListeners.iterator();

        while(var1.hasNext()) {
            ARArbiTrackListener listener = (ARArbiTrackListener)var1.next();
            listener.arbiTrackStarted();
        }

    }

    public void addListener(ARArbiTrackListener listener) {
        this.mListeners.add(listener);
    }

    public void removeListener(ARArbiTrackListener listener) {
        this.mListeners.remove(listener);
    }

    public void gyroStarted() {
        this.shouldNotify = true;
    }

    public boolean getIsInitialised() {
        return this.mIsInitialised;
    }
}
