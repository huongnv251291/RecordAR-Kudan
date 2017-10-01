//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.google.vrtoolkit.cardboard.sensors.HeadTracker;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ARGyroManager implements ARRendererListener {
    private static ARGyroManager gyroManager;
    private HeadTracker mHeadTracker;
    private ARWorld mGyroWorld = new ARWorld();
    private List<ARGyroManagerListener> mListeners;
    private boolean notifyListeners;
    private boolean isInitialised;

    public ARGyroManager() {
    }

    public static ARGyroManager getInstance() {
        if(gyroManager == null || !gyroManager.isInitialised) {
            gyroManager = new ARGyroManager();
            gyroManager.initialise();
            gyroManager.isInitialised = true;
        }

        return gyroManager;
    }

    public void initialise() {
        this.mGyroWorld.setName("GyroWorld");
        this.mListeners = new ArrayList();
        this.notifyListeners = false;
        this.mHeadTracker = new HeadTracker(ARRenderer.getInstance().getActivity());
    }

    public void deinitialise() {
        this.mGyroWorld.remove();
        this.mGyroWorld.removeAllChildren();
        this.mHeadTracker = null;
        this.mGyroWorld = new ARWorld();
        this.mGyroWorld.setVisible(false);
        this.isInitialised = false;
    }

    public void start() {
        if(this.mHeadTracker != null) {
            this.mHeadTracker.startTracking();
            this.mGyroWorld.setVisible(true);
            ARRenderer.getInstance().addListener(this);
            this.notifyListeners = true;
        }

    }

    public void stop() {
        if(this.mHeadTracker != null) {
            this.mHeadTracker.stopTracking();
            this.mGyroWorld.setVisible(false);
            ARRenderer.getInstance().removeListener(this);
        }

    }

    public synchronized void updateNode() {
        float[] fmat = new float[16];
        this.mHeadTracker.getLastHeadView(fmat, 0);
        Matrix4f matrix = new Matrix4f(fmat);
        Quaternion orientation = matrix.toRotationQuat();
        this.mGyroWorld.setOrientation(orientation);
        if(this.notifyListeners) {
            this.notifyListenersGyroStarted();
            this.notifyListeners = false;
        }

    }

    public ARWorld getWorld() {
        return this.mGyroWorld;
    }

    public void preRender() {
        this.updateNode();
    }

    public void postRender() {
    }

    public void rendererDidPause() {
    }

    public void rendererDidResume() {
    }

    public void notifyListenersGyroStarted() {
        Iterator var1 = this.mListeners.iterator();

        while(var1.hasNext()) {
            ARGyroManagerListener listener = (ARGyroManagerListener)var1.next();
            listener.gyroStarted();
        }

    }

    public void addListener(ARGyroManagerListener listener) {
        this.mListeners.add(listener);
    }

    public void removeListener(ARGyroManagerListener listener) {
        this.mListeners.remove(listener);
    }
}
