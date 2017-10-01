//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class ARVideoNode extends ARMeshNode {
    private ARVideoTexture mVideoTexture;

    public ARVideoTexture getVideoTexture() {
        return this.mVideoTexture;
    }

    public void setVideoTexture(ARVideoTexture videoTexture) {
        this.mVideoTexture = videoTexture;
    }

    public ARVideoNode(ARVideoTexture videoTexture) {
        this.initWithVideoTexture(videoTexture);
    }

    private void initWithVideoTexture(ARVideoTexture videoTexture) {
        this.mVideoTexture = videoTexture;
        ARMesh mesh = new ARMesh();
        mesh.createTestMeshWithUvs((float)videoTexture.getWidth(), (float)videoTexture.getHeight(), 0.0F, 1.0F, 0.0F, 1.0F);
        this.setMesh(mesh);
        ARVideoTextureMaterial material = new ARVideoTextureMaterial(videoTexture);
        this.setMaterial(material);
    }

    public ARVideoNode() {
    }

    public void initWithPackagedFile(String asset) {
        ARVideoTexture videoTexture = new ARVideoTexture();
        videoTexture.loadFromAsset(asset);
        this.initWithVideoTexture(videoTexture);
    }

    public void initFromPath(String path) {
        ARVideoTexture videoTexture = new ARVideoTexture();
        videoTexture.loadFromPath(path);
        this.initWithVideoTexture(videoTexture);
    }
}
