//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.graphics.Point;
import com.jme3.math.Matrix4f;

public class ARCamera extends ARNode {
    private Matrix4f mProjectionMatrix;
    private Point frustrumPlaneDistance;
    private Point principlePoint;
    private Point fov;
    private ARViewPort viewPort;

    public ARCamera() {
        this.addChild(ARArbiTrack.getInstance().getWorld());
        this.addChild(ARGyroPlaceManager.getInstance().getWorld());
        this.addChild(ARImageTracker.getInstance().getBaseNode());
        this.addChild(ARGyroManager.getInstance().getWorld());
    }

    public void setProjectionMatrix(Matrix4f projection) {
        this.mProjectionMatrix = projection;
    }

    public Matrix4f getProjectionMatrix() {
        return this.mProjectionMatrix;
    }
}
