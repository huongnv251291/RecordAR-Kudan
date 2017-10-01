//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ARImageTracker {
    private static ARImageTracker imageTracker;
    private boolean mIsInitialised;
    private ARNode mBaseNode = new ARNode();
    private List<ARImageTrackable> mTrackables;
    private int mDetectedTrackables = 0;

    public static ARImageTracker getInstance() {
        if(imageTracker == null || !imageTracker.mIsInitialised) {
            imageTracker = new ARImageTracker();
            imageTracker.initialise();
            imageTracker.mIsInitialised = true;
        }

        return imageTracker;
    }

    public ARImageTracker() {
    }

    public ARNode getBaseNode() {
        return this.mBaseNode;
    }

    private native void processFrameN(byte[] var1, int var2, int var3);

    private native int getNumberOfResultsN();

    private native String getTrackedMarkerN(int var1, Vector3f var2, Quaternion var3);

    void processFrame(byte[] cameraImage, int width, int height) {
        if(this.mIsInitialised) {
            this.processFrameN(cameraImage, width, height);
            Iterator var4 = this.mTrackables.iterator();

            ARImageTrackable trackable;
            while(var4.hasNext()) {
                trackable = (ARImageTrackable)var4.next();
                trackable.getWorld().setVisible(false);
                trackable.trackerStartFrame();
                trackable.trackerSetDetected(false);
            }

            this.mDetectedTrackables = 0;

            for(int i = 0; i < this.getNumberOfResultsN(); ++i) {
                Vector3f v = new Vector3f();
                Quaternion q = new Quaternion();
                String name = this.getTrackedMarkerN(i, v, q);
                if(name != null) {
                    Iterator var8 = this.mTrackables.iterator();

                    while(var8.hasNext()) {
                         trackable = (ARImageTrackable)var8.next();
                        if(trackable.getName().equals(name)) {
                            ARWorld world = trackable.getWorld();
                            world.setPosition(v);
                            world.setOrientation(q);
                            world.setVisible(true);
                            trackable.trackerSetDetected(true);
                            break;
                        }
                    }

                    ++this.mDetectedTrackables;
                }
            }

            var4 = this.mTrackables.iterator();

            while(var4.hasNext()) {
                trackable = (ARImageTrackable)var4.next();
                trackable.trackerEndFrame();
            }

        }
    }

    private native void initN();

    public void initialise() {
        this.initN();
        this.mTrackables = new ArrayList();
    }

    private native void destroyN();

    public void deinitialise() {
        this.destroyN();
        this.mTrackables = null;
        this.mIsInitialised = false;
        this.mBaseNode.remove();
        this.mBaseNode = new ARNode();
    }

    private native void addTrackableN(ARImageTrackable var1);

    public void addTrackable(ARImageTrackable trackable) {
        this.mTrackables.add(trackable);
        this.mBaseNode.addChild(trackable.getWorld());
        trackable.getWorld().setVisible(false);
        this.addTrackableN(trackable);
    }

    public List<ARImageTrackable> getTrackables() {
        return this.mTrackables;
    }

    public int getNumberOfDetectedTrackables() {
        return this.mDetectedTrackables;
    }

    public List<ARImageTrackable> getDetectedTrackables() {
        return null;
    }

    public void addTrackableSet(ARTrackableSet set) {
        Iterator var2 = set.getTrackables().iterator();

        while(var2.hasNext()) {
            ARImageTrackable trackable = (ARImageTrackable)var2.next();
            this.addTrackable(trackable);
        }

    }

    private native void setMaximumSimultaneousTrackingN(int var1);

    public void setMaximumSimultaneousTracking(int maxToTrack) {
        this.setMaximumSimultaneousTrackingN(maxToTrack);
    }

    public void start() {
    }

    public void stop() {
    }

    public ARImageTrackable findTrackable(String trackableName) {
        Iterator var2 = this.mTrackables.iterator();

        ARImageTrackable trackable;
        do {
            if(!var2.hasNext()) {
                return null;
            }

            trackable = (ARImageTrackable)var2.next();
        } while(!trackable.getName().equals(trackableName));

        return trackable;
    }

    private native void setRecoveryModeN(boolean var1);

    public void setRecoveryMode(boolean doRecovery) {
        this.setRecoveryModeN(doRecovery);
    }

    private native boolean queryRecoveryModeN();

    public boolean queryRecoveryMode() {
        return this.queryRecoveryModeN();
    }

    private native void prohibitRecoveryModeN();

    public void prohibitRecoveryMode() {
        this.prohibitRecoveryModeN();
    }

    public boolean queryRecoveryMode(ARImageTrackable trackable) {
        boolean globalSetting = this.queryRecoveryModeN();
        return trackable.isFlowRecoverable(globalSetting);
    }

    private native void toggleParallelDetectionN(boolean var1);

    public void toggleParallelDetection(boolean isParallel) {
        this.toggleParallelDetectionN(isParallel);
    }

    private native boolean isDetectorParallelN();

    public boolean isDetectorParallel() {
        return this.isDetectorParallelN();
    }
}
