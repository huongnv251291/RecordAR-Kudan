//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class ARImageNode extends ARMeshNode {
    private ARTexture2D mTexture;

    public ARImageNode() {
    }

    public ARImageNode(String assetName) {
        this();
        this.mTexture = new ARTexture2D();
        this.mTexture.loadFromAsset(assetName);
        this.initialise();
    }

    public ARImageNode initWithPath(String filePath) {
        this.mTexture = new ARTexture2D();
        this.mTexture.loadFromPath(filePath);
        this.initialise();
        return this;
    }

    public ARImageNode(ARTexture2D texture) {
        this();
        this.mTexture = texture;
        this.initialise();
    }

    public void setTexture(ARTexture2D texture) {
        this.mTexture = texture;
        this.initialise();
    }

    private void initialise() {
        this.initialiseMaterial();
        this.initialiseMesh();
    }

    private void initialiseMaterial() {
        ARTextureMaterial textureMaterial = new ARTextureMaterial(this.mTexture);
        textureMaterial.setTransparent(true);
        this.setMaterial(textureMaterial);
    }

    void initialiseMesh() {
        ARMesh mesh = new ARMesh();
        mesh.createTestMesh2((float)this.mTexture.getWidth(), (float)this.mTexture.getHeight());
        this.setMesh(mesh);
    }

    public ARTexture2D getTexture() {
        return this.mTexture;
    }
}
