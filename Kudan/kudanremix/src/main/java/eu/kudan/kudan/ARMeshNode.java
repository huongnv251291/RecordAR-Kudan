//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class ARMeshNode extends ARNode {
    private ARMesh mMesh;
    private ARMaterial mMaterial;
    private Matrix3f mWorldOrientation = new Matrix3f();
    private Matrix4f mWorldInverse = new Matrix4f();
    private Vector3f mZeroVector = new Vector3f(0.0F, 0.0F, 0.0F);
    private Vector3f mYVector = new Vector3f(0.0F, 1.0F, 0.0F);
    private Vector3f mLightVector = new Vector3f();
    private Vector3f mWorldCameraPosition = new Vector3f();
    private Vector3f mCaptureColour;
    private ARMaterial mCaptureMaterial;

    public ARMeshNode() {
        ARRenderer renderer = ARRenderer.getInstance();
        this.mCaptureColour = renderer.getNextCaptureColour();
        Vector3f col = this.mCaptureColour.mult(0.00390625F);
        this.mCaptureMaterial = new ARColourMaterial(col);
    }

    public Vector3f getCaptureColour() {
        return this.mCaptureColour;
    }

    public ARMesh getMesh() {
        return this.mMesh;
    }

    public void setMesh(ARMesh mesh) {
        this.mMesh = mesh;
    }

    public ARMaterial getMaterial() {
        return this.mMaterial;
    }

    public void setMaterial(ARMaterial material) {
        this.mMaterial = material;
    }

    public void render() {
        super.render();
        ARRenderer renderer = ARRenderer.getInstance();
        renderer.setModelViewMatrix(this.getFullTransform());
        renderer.setModelMatrix(this.getWorldTransform());
        Quaternion worldOrientation = this.getWorld().getWorldOrientation();
        this.mWorldOrientation.set(this.getWorldOrientation());
        this.mWorldOrientation.scale(this.getWorldScale());
        renderer.setNormalMatrix(this.mWorldOrientation);
        this.mWorldInverse.set(this.getWorld().getWorldTransform());
        this.mWorldInverse.invertLocal();
        this.mWorldInverse.mult(this.mZeroVector, this.mWorldCameraPosition);
        renderer.setWorldCameraPosition(this.mWorldCameraPosition);
        renderer.setLightPosition(worldOrientation.mult(this.mLightVector));
        if(this.mMesh.prepareRenderer(this)) {
            if(!renderer.getRenderForCapture()) {
                this.mMaterial.prepareRendererWithNode(this);
            } else {
                this.mCaptureMaterial.prepareRendererWithNode(this);
            }

            GLES20.glEnable(2929);
            GLES20.glDisable(2884);
            GLES20.glBlendFunc(770, 771);
            if(!this.mMaterial.getTransparent()) {
                GLES20.glDisable(3042);
            } else {
                GLES20.glEnable(3042);
            }

            this.glDraw();
        }
    }

    public Vector3f getLightDirection() {
        return this.mLightVector;
    }

    public void setLightDirection(Vector3f mLightVector) {
        this.mLightVector = mLightVector;
    }

    protected void glDraw() {
        GLES20.glDrawElements(4, this.mMesh.getNumberOfIndices(), 5123, 0);
    }
}
