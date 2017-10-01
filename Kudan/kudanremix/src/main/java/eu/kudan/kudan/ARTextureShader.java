//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;

public class ARTextureShader extends ARShaderProgram {
    static final String vertexString = "attribute vec4 vertexPosition;\nattribute vec2 vertexUV;\n\nuniform mat4 modelViewProjectionMatrix;\n\nvarying vec2 uvCoord;\nvarying vec2 uvCoordMask;\n\nvoid main()\n{\n    uvCoord = vertexUV;\n    uvCoordMask.x = uvCoord.x + 0.5;\n    uvCoordMask.y = uvCoord.y;\n    gl_Position = modelViewProjectionMatrix * vertexPosition;\n}";
    static final String fragmentString = "precision mediump float;\nuniform vec3 colour;\nvarying vec2 uvCoord;\nvarying vec2 uvCoordMask;\nuniform sampler2D uvSampler;\nuniform float alpha;\n\nvoid main()\n{\n    vec4 col = texture2D(uvSampler, uvCoord);\n    //float alpha = texture2D(uvSampler, uvCoordMask).x;\n    col *= alpha;\n    gl_FragColor = col;\n}";

    public ARTextureShader() {
        this.setShaderStrings("attribute vec4 vertexPosition;\nattribute vec2 vertexUV;\n\nuniform mat4 modelViewProjectionMatrix;\n\nvarying vec2 uvCoord;\nvarying vec2 uvCoordMask;\n\nvoid main()\n{\n    uvCoord = vertexUV;\n    uvCoordMask.x = uvCoord.x + 0.5;\n    uvCoordMask.y = uvCoord.y;\n    gl_Position = modelViewProjectionMatrix * vertexPosition;\n}", "precision mediump float;\nuniform vec3 colour;\nvarying vec2 uvCoord;\nvarying vec2 uvCoordMask;\nuniform sampler2D uvSampler;\nuniform float alpha;\n\nvoid main()\n{\n    vec4 col = texture2D(uvSampler, uvCoord);\n    //float alpha = texture2D(uvSampler, uvCoordMask).x;\n    col *= alpha;\n    gl_FragColor = col;\n}");
    }

    public static ARTextureShader getShader() {
        ARShaderManager shaderManager = ARShaderManager.getInstance();
        boolean[] properties = new boolean[]{true};
        ARTextureShader shader = (ARTextureShader)shaderManager.findShader(ARTextureShader.class, properties);
        if(shader != null) {
            return shader;
        } else {
            shader = new ARTextureShader();
            shaderManager.addShader(shader, properties);
            ARRenderer renderer = ARRenderer.getInstance();
            renderer.addShader(shader);
            return shader;
        }
    }

    public void setAlpha(float alpha) {
        int alphaUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "alpha");
        if(alphaUniformHandle >= 0) {
            GLES20.glUniform1f(alphaUniformHandle, alpha);
        }

    }
}
