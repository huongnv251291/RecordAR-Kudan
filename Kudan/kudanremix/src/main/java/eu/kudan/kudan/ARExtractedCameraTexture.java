//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;

public class ARExtractedCameraTexture extends ARRenderTargetTexture implements ARRendererListener {
    private ARNode mOffsetNode;
    private ARNode mSrcNode;
    private float mSrcWidth;
    private float mSrcHeight;
    private ARCameraBackgroundMaterial mCameraBackgroundMaterial;

    public float getmSrcHeight() {
        return this.mSrcHeight;
    }

    public float getmSrcWidth() {
        return this.mSrcWidth;
    }

    public void setSrcWidth(float srcWidth) {
        this.mSrcWidth = srcWidth;
        this.updateOffsetNode();
    }

    public void setSrcHeight(float srcHeight) {
        this.mSrcHeight = srcHeight;
        this.updateOffsetNode();
    }

    public void setSrcNode(ARNode node) {
        this.mSrcNode = node;
    }

    public ARNode getSrcNode() {
        return this.mSrcNode;
    }

    private void updateOffsetNode() {
        Vector3f position = this.mOffsetNode.getPosition();
        position.setX(-this.mSrcWidth / 2.0F);
        position.setY(-this.mSrcHeight / 2.0F);
        this.mOffsetNode.setPosition(position);
        Vector3f scale = this.mOffsetNode.getScale();
        float scaleFactorX = this.mSrcWidth / (float)this.getWidth();
        float scaleFactorY = this.mSrcHeight / (float)this.getHeight();
        scale.setX(scaleFactorX);
        scale.setY(scaleFactorY);
        this.mOffsetNode.setScale(scale);
    }

    private void setupOffscreen() {
        this.mOffsetNode = new ARNode();
        this.updateOffsetNode();
        this.setPriority(-100);
        ARViewPort viewPort = new ARViewPort(0, 0, this.getWidth(), this.getHeight());
        this.addViewPort(viewPort);
        viewPort.getCamera().setProjectionMatrix(new Matrix4f());
        ARMesh mesh = new ARMesh();
        mesh.createTestMeshWithUvs(2.0F, 2.0F, 0.0F, 1.0F, 0.0F, 1.0F);
        ARMeshNode cameraMeshNode = new ARMeshNode();
        cameraMeshNode.setMesh(mesh);
        ARCameraStream stream = ARCameraStream.getInstance();
        ARCameraBackgroundMaterial camMaterial = new ARCameraBackgroundMaterial();
        camMaterial.setTexture(stream.getTexture());
        cameraMeshNode.setMaterial(camMaterial);
        this.mCameraBackgroundMaterial = camMaterial;
        viewPort.getCamera().addChild(cameraMeshNode);
        ARRenderer renderer = ARRenderer.getInstance();
        renderer.addListener(this);
    }

    public ARExtractedCameraTexture(int width, int height) {
        super(width, height);
        this.mSrcHeight = (float)height;
        this.mSrcWidth = (float)width;
        this.setupOffscreen();
    }

    public void preRender() {
        Matrix4f projectionMatrix = new Matrix4f(546.0F, 546.0F, 320.0F, 240.0F);
        this.mSrcNode.addChild(this.mOffsetNode);
        Matrix4f modelView = this.mOffsetNode.getFullTransform();
        this.mOffsetNode.remove();
        projectionMatrix = projectionMatrix.mult(modelView);
        this.mCameraBackgroundMaterial.setMarkerModelViewProjection(projectionMatrix);
        Vector3f v1 = new Vector3f(0.0F, 0.0F, 1.0F);
        v1 = projectionMatrix.mult(v1);
        v1.x /= v1.z;
        v1.y /= v1.z;
        Vector3f v2 = new Vector3f(100.0F, 100.0F, 1.0F);
        v2 = projectionMatrix.mult(v2);
        v2.x /= v2.z;
        v2.y /= v2.z;
    }

    public void postRender() {
    }

    public void rendererDidPause() {
    }

    public void rendererDidResume() {
    }
}
