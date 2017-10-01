//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import com.jme3.math.Vector3f;

public class ARColourMaterial extends ARMaterial {
    private ARColourShader mShader;
    private Vector3f mColour;

    public ARColourMaterial() {
        this.mShader = ARColourShader.getShader();
    }

    public ARColourMaterial(Vector3f colour) {
        this();
        this.mColour = colour;
    }

    public void setColour(Vector3f colour) {
        this.mColour = colour;
    }

    public boolean prepareRendererWithNode(ARNode node) {
        boolean b = super.prepareRendererWithNode(node);
        if(!b) {
            return false;
        } else {
            this.mShader.prepareRenderer();
            this.mShader.setColour(this.mColour);
            return true;
        }
    }
}
