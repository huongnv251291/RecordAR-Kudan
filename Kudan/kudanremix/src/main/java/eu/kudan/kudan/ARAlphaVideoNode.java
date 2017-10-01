//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class ARAlphaVideoNode extends ARMeshNode {
    private ARVideoTexture mVideoTexture;

    public ARVideoTexture getVideoTexture() {
        return this.mVideoTexture;
    }

    public void setVideoTexture(ARVideoTexture videoTexture) {
        this.mVideoTexture = videoTexture;
    }

    public ARAlphaVideoNode(ARVideoTexture videoTexture) {
        this.initWithVideoTexture(videoTexture);
    }

    private void initWithVideoTexture(ARVideoTexture videoTexture) {
        this.mVideoTexture = videoTexture;
        ARMesh mesh = new ARMesh();
        mesh.createTestMeshWithUvs((float)(videoTexture.getWidth() / 2), (float)videoTexture.getHeight(), 0.0F, 0.5F, 0.0F, 1.0F);
        this.setMesh(mesh);
        ARAlphaVideoTextureMaterial material = new ARAlphaVideoTextureMaterial(videoTexture);
        this.setMaterial(material);
        material.setTransparent(true);
    }

    public ARAlphaVideoNode() {
    }

    public void initWithPackagedFile(String asset) {
        ARVideoTexture videoTexture = new ARVideoTexture();
        videoTexture.loadFromAsset(asset);
        this.initWithVideoTexture(videoTexture);
    }

    public void initFromPath(String filePath) {
        ARVideoTexture videoTexture = new ARVideoTexture();
        videoTexture.loadFromPath(filePath);
        this.initWithVideoTexture(videoTexture);
    }
}
