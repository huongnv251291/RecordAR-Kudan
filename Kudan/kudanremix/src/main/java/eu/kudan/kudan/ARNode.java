//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ARNode {
    private ARNode mParent = null;
    private List<ARNode> mChildren = new ArrayList();
    private Vector3f mPosition = new Vector3f(0.0F, 0.0F, 0.0F);
    private Quaternion mOrientation = new Quaternion();
    private Vector3f mScale = new Vector3f(1.0F, 1.0F, 1.0F);
    private Matrix4f mLocalTransform = new Matrix4f();
    private Matrix4f mFullTransform = new Matrix4f();
    private Matrix4f mWorldTransform = new Matrix4f();
    private Quaternion mWorldOrientation = new Quaternion();
    private Vector3f mWorldScale = new Vector3f();
    private Vector3f mWorldPosition = new Vector3f();
    private boolean mWorldTransformIsDirty = true;
    private boolean mLocalTransformIsDirty = true;
    private boolean mIsVisible = true;
    private String mName = "" + this;
    private Quaternion mQuaternionTmp = new Quaternion();

    public ARNode() {
    }

    public ARNode(String name) {
        this.mName = name;
    }

    private void markLocalTransformAsDirty() {
        this.mLocalTransformIsDirty = true;
        this.markWorldTranformAsDirty();
    }

    private void updateLocalTransform() {
        if(this.mLocalTransformIsDirty) {
            Matrix3f rotation = this.mOrientation.toRotationMatrix();
            this.mLocalTransform.setTransform(this.mPosition, this.mScale, rotation);
            this.mLocalTransformIsDirty = false;
        }
    }

    private void updateWorldTransform() {
        if(this.mWorldTransformIsDirty) {
            Matrix4f parentTransform = new Matrix4f();
            Quaternion parentOrientation = new Quaternion();
            Vector3f parentScale = new Vector3f();
            Vector3f parentPosition = new Vector3f();
            if(this.mParent != null && !(this.mParent instanceof ARWorld) && !(this.mParent instanceof ARCamera)) {
                parentTransform.set(this.mParent.getWorldTransform());
                parentOrientation.set(this.mParent.getWorldOrientation());
                parentScale.set(this.mParent.getWorldScale());
                parentPosition.set(this.mParent.getWorldPosition());
            } else {
                parentTransform.set(Matrix4f.IDENTITY);
                parentOrientation.set(Quaternion.IDENTITY);
                parentScale = new Vector3f(1.0F, 1.0F, 1.0F);
                parentPosition = new Vector3f(0.0F, 0.0F, 0.0F);
            }

            this.mWorldOrientation.set(parentOrientation.mult(this.mOrientation));
            this.mWorldScale.set(parentScale.mult(this.mScale));
            this.mWorldPosition.set(parentPosition.add(parentOrientation.mult(this.mPosition.mult(parentScale))));
            this.mWorldTransform.set(parentTransform.mult(this.getLocalTransform()));
            this.mWorldTransformIsDirty = false;
        }
    }

    private void markWorldTranformAsDirty() {
        this.mWorldTransformIsDirty = true;
        Iterator var1 = this.mChildren.iterator();

        while(var1.hasNext()) {
            ARNode child = (ARNode)var1.next();
            child.markWorldTranformAsDirty();
        }

    }

    public void remove() {
        synchronized(ARRenderer.getInstance()) {
            if(this.mParent != null) {
                this.mParent.mChildren.remove(this);
                this.setParent((ARNode)null);
            }
        }
    }

    public void removeAllChildren() {
        synchronized(ARRenderer.getInstance()) {
            ListIterator iterator = this.mChildren.listIterator();

            while(iterator.hasNext()) {
                ((ARNode)iterator.next()).setParent((ARNode)null);
                iterator.remove();
            }

        }
    }

    public void addChild(ARNode child) {
        child.remove();
        this.mChildren.add(child);
        child.setParent(this);
        child.mWorldTransformIsDirty = true;
        child.mLocalTransformIsDirty = true;
    }

    public ARNode getParent() {
        return this.mParent;
    }

    public void setParent(ARNode parent) {
        this.mParent = parent;
        this.markLocalTransformAsDirty();
    }

    public List<ARNode> getChildren() {
        return this.mChildren;
    }

    public ARNode getWorld() {
        ARNode world = this;

        while(world.getParent() != null) {
            world = world.getParent();
            if(world instanceof ARWorld) {
                break;
            }
        }

        return world;
    }

    public Vector3f getPosition() {
        return this.mPosition;
    }

    public void setPosition(Vector3f position) {
        this.mPosition.set(position);
        this.markLocalTransformAsDirty();
    }

    public void setPosition(float x, float y, float z) {
        this.mPosition.setX(x);
        this.mPosition.setY(y);
        this.mPosition.setZ(z);
        this.markLocalTransformAsDirty();
    }

    public void translateBy(float x, float y, float z) {
        Vector3f translation = new Vector3f(x, y, z);
        Vector3f delta = this.mOrientation.mult(translation);
        delta.multLocal(this.getScale());
        this.mPosition.addLocal(delta);
        this.markLocalTransformAsDirty();
    }

    public void scaleBy(float x, float y, float z) {
        Vector3f scale = new Vector3f(x, y, z);
        this.mScale.multLocal(scale);
        this.markLocalTransformAsDirty();
    }

    public Vector3f getScale() {
        return this.mScale;
    }

    public void setScale(Vector3f scale) {
        this.mScale = scale;
        this.markLocalTransformAsDirty();
    }

    public void setScale(float x, float y, float z) {
        this.mScale.set(x, y, z);
        this.markLocalTransformAsDirty();
    }

    public void scaleByUniform(float scale) {
        this.scaleBy(scale, scale, scale);
    }

    public Quaternion getOrientation() {
        return this.mOrientation;
    }

    public void setOrientation(Quaternion orientation) {
        this.mOrientation.set(orientation);
        this.mOrientation.normalizeLocal();
        this.markLocalTransformAsDirty();
    }

    public void setOrientation(float x, float y, float z, float w) {
        this.mOrientation.set(x, y, z, w);
        this.markLocalTransformAsDirty();
    }

    public void rotateByQuaternion(Quaternion quaternion) {
        quaternion.normalizeLocal();
        this.mOrientation.multLocal(quaternion);
        this.markLocalTransformAsDirty();
    }

    public void rotateByDegrees(float angle, float x, float y, float z) {
        this.mQuaternionTmp.fromAngleAxis(angle * 0.017453292F, new Vector3f(x, y, z));
        this.rotateByQuaternion(this.mQuaternionTmp);
        this.markLocalTransformAsDirty();
    }

    public Matrix4f getFullTransform() {
        this.updateWorldTransform();
        this.mFullTransform = this.getWorld().getWorldTransform().mult(this.mWorldTransform);
        return this.mFullTransform;
    }

    public Matrix4f getLocalTransform() {
        this.updateLocalTransform();
        return this.mLocalTransform;
    }

    public Matrix4f getWorldTransform() {
        this.updateWorldTransform();
        return this.mWorldTransform;
    }

    public void render() {
    }

    public boolean getVisible() {
        return this.mIsVisible;
    }

    public void setVisible(boolean visible) {
        this.mIsVisible = visible;
    }

    public Quaternion getWorldOrientation() {
        this.updateWorldTransform();
        return this.mWorldOrientation;
    }

    public Vector3f getWorldScale() {
        this.updateWorldTransform();
        return this.mWorldScale;
    }

    public Vector3f getWorldPosition() {
        this.updateWorldTransform();
        return this.mWorldPosition;
    }

    public Vector3f getFullPosition() {
        Vector3f world = this.getWorld().getWorldPosition();
        Quaternion worldOrientation = this.getWorldOrientation();
        return this.getWorld().getWorldPosition().add(this.getWorld().getWorldOrientation().mult(this.getWorldPosition().mult(this.getWorld().getWorldScale())));
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void preRender() {
    }

    public void postRender() {
    }

    public ARNode findChildByName(String name) {
        Iterator var2 = this.mChildren.iterator();

        ARNode node;
        do {
            if(!var2.hasNext()) {
                return null;
            }

            node = (ARNode)var2.next();
        } while(!node.getName().equals(name));

        return node;
    }

    public void addChildren(List<ARNode> children) {
        Iterator var2 = children.iterator();

        while(var2.hasNext()) {
            ARNode node = (ARNode)var2.next();
            this.addChild(node);
        }

    }

    public void translateByVector(Vector3f translation) {
        Vector3f delta = new Vector3f();
        delta.set(this.mOrientation.mult(translation));
        delta.set(delta.multLocal(this.mScale));
        this.mPosition.addLocal(delta);
    }

    public void scaleByVector(Vector3f scale) {
        this.mScale.multLocal(scale);
        this.mLocalTransformIsDirty = true;
    }

    public void rotateByRadians(float angle, float x, float y, float z) {
        Quaternion q = new Quaternion(x, y, z, angle);
        this.rotateByQuaternion(q);
    }
}
