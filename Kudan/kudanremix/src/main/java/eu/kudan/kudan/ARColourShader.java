//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;
import com.jme3.math.Vector3f;

public class ARColourShader extends ARShaderProgram {
    private float[] mTmpVector = new float[3];
    static final String vertexString = "attribute vec4 vertexPosition;\n\nuniform mat4 modelViewProjectionMatrix;\n\nvoid main() \n{ \n    gl_Position = modelViewProjectionMatrix * vertexPosition;\n//\tgl_Position = vertexPosition;\n}";
    static final String fragmentString = "precision mediump float;\n\nuniform vec3 colour;\n\t\t\t\t\t\nvoid main()\n{\n    gl_FragColor = vec4(colour, 1.0);\n}";

    public ARColourShader() {
        this.setShaderStrings("attribute vec4 vertexPosition;\n\nuniform mat4 modelViewProjectionMatrix;\n\nvoid main() \n{ \n    gl_Position = modelViewProjectionMatrix * vertexPosition;\n//\tgl_Position = vertexPosition;\n}", "precision mediump float;\n\nuniform vec3 colour;\n\t\t\t\t\t\nvoid main()\n{\n    gl_FragColor = vec4(colour, 1.0);\n}");
    }

    public static ARColourShader getShader() {
        ARShaderManager shaderManager = ARShaderManager.getInstance();
        boolean[] properties = new boolean[]{true};
        ARColourShader shader = (ARColourShader)shaderManager.findShader(ARColourShader.class, properties);
        if(shader != null) {
            return shader;
        } else {
            shader = new ARColourShader();
            shaderManager.addShader(shader, properties);
            ARRenderer renderer = ARRenderer.getInstance();
            renderer.addShader(shader);
            return shader;
        }
    }

    public void setColour(Vector3f colour) {
        int colourUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "colour");
        if(colourUniformHandle >= 0) {
            this.mTmpVector[0] = colour.getX();
            this.mTmpVector[1] = colour.getY();
            this.mTmpVector[2] = colour.getZ();
            GLES20.glUniform3fv(colourUniformHandle, 1, this.mTmpVector, 0);
        }

    }
}
