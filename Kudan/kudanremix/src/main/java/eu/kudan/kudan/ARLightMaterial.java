//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ARLightMaterial extends ARMaterial {
    private ARLightShader mShader;
    private Vector3f mColour = new Vector3f();
    private Vector3f mDiffuse = new Vector3f();
    private Vector3f mAmbient = new Vector3f();
    private Vector3f mSpecular = new Vector3f();
    private float mShininess = 1.0F;
    private float mAlpha = 1.0F;
    private ARTexture2D mTexture;
    private ARTexture2D mOcclusionTexture;
    private ARTexture3D mCubeTexture;
    private float mReflectivity;
    private boolean mHasReflection;
    private boolean mHasTexture;
    private boolean mHasAlpha;
    private boolean mHasBones;
    private Matrix4f mCachedBoneFullTransform = new Matrix4f();
    private Matrix4f mCachedNodeFullTransform = new Matrix4f();
    private List<Matrix4f> mCachedBones = new ArrayList(20);
    private List<Matrix3f> mCachedBonesRotation = new ArrayList(20);

    private void chooseShader(ARMeshNode meshNode) {
        ARMesh mesh = meshNode.getMesh();
        boolean hasBones = false;
        boolean hasMorph = false;
        boolean hasReflection = false;
        boolean hasTexture = false;
        boolean hasAlpha = false;
        hasBones = mesh.getBones().size() > 0;
        if(this.mCubeTexture != null) {
            hasReflection = true;
        }

        if(this.mTexture != null) {
            hasTexture = true;
        }

        if(this.mAlpha < 1.0F) {
            hasAlpha = true;
        }

        this.mShader = ARLightShader.getShader(hasReflection, hasTexture, hasAlpha, hasBones, hasMorph);
        this.mShader.compileShaders();
    }

    public ARLightMaterial() {
    }

    public void setColour(float r, float g, float b) {
        this.mColour.set(r, g, b);
    }

    public Vector3f getColour() {
        return this.mColour;
    }

    public void setDiffuse(float r, float g, float b) {
        this.mDiffuse.set(r, g, b);
    }

    public Vector3f getDiffuse() {
        return this.mDiffuse;
    }

    public void setAmbient(float r, float g, float b) {
        this.mAmbient.set(r, g, b);
    }

    public Vector3f getAmbient() {
        return this.mAmbient;
    }

    public void setSpecular(float r, float g, float b) {
        this.mSpecular.set(r, g, b);
    }

    public Vector3f getSpecular() {
        return this.mSpecular;
    }

    public void setShininess(float shininess) {
        if(shininess > 0.0F) {
            this.mShininess = shininess;
        }

    }

    public float getShininess() {
        return this.mShininess;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public void setAlpha(float alpha) {
        this.mAlpha = alpha;
    }

    public void setReflectivity(float reflectivity) {
        this.mReflectivity = reflectivity;
    }

    public float getReflectivity() {
        return this.mReflectivity;
    }

    public void setTexture(ARTexture2D texture) {
        this.mTexture = texture;
    }

    public void setCubeTexture(ARTexture3D cubeTexture) {
        this.mCubeTexture = cubeTexture;
    }

    public boolean prepareRendererWithNode(ARNode node) {
        if(!super.prepareRendererWithNode(node)) {
            return false;
        } else {
            this.chooseShader((ARMeshNode)node);
            this.mShader.prepareRenderer();
            this.mShader.setDiffuse(this.mDiffuse);
            this.mShader.setAmbient(this.mAmbient);
            this.mShader.setSpecular(this.mSpecular);
            this.mShader.setShininess(this.mShininess);
            if(this.mHasAlpha) {
                this.mShader.setAlpha(this.mAlpha);
            }

            this.mShader.setReflectivity(this.mReflectivity);
            this.mShader.setColour(this.mColour);
            this.mShader.setUniforms();
            if(this.mTexture != null) {
                this.mTexture.prepareRenderer(1);
            }

            if(this.mCubeTexture != null) {
                this.mCubeTexture.prepareRenderer(0);
            }

            ARMeshNode meshNode = (ARMeshNode)node;
            ARMesh mesh = meshNode.getMesh();
            if(mesh.getBones().size() > 0) {
                int i = 0;

                for(Iterator var5 = mesh.getBones().iterator(); var5.hasNext(); ++i) {
                    ARBoneNode bone = (ARBoneNode)var5.next();
                }

                this.mShader.setBones(ARRenderer.getInstance().getBones(), mesh.getBones().size());
            }

            return true;
        }
    }
}
