//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;

public class ARVideoTextureShader extends ARShaderProgram {
    static final String vertexString = "attribute vec4 vertexPosition;\nattribute vec2 vertexUV;\n\nuniform mat4 modelViewProjectionMatrix;\nuniform mat4 uvTransform;\n\nvarying vec2 uvCoord;\n\nvoid main() \n{ \n    vec4 uv = vec4(vertexUV.x, vertexUV.y, 0, 1.0);\n    uv = uvTransform * uv;\n    uvCoord = vec2(uv.x, uv.y);\n    gl_Position = modelViewProjectionMatrix * vertexPosition;\n}";
    static final String fragmentString = "#extension GL_OES_EGL_image_external :require\nprecision mediump float;\n\nvarying vec2 uvCoord;\nuniform samplerExternalOES uvSampler;\n\t\t\t\t\t\nvoid main() \n{ \n    vec4 col = texture2D(uvSampler, uvCoord); \n    gl_FragColor = col;\n}";

    public ARVideoTextureShader() {
        this.setShaderStrings("attribute vec4 vertexPosition;\nattribute vec2 vertexUV;\n\nuniform mat4 modelViewProjectionMatrix;\nuniform mat4 uvTransform;\n\nvarying vec2 uvCoord;\n\nvoid main() \n{ \n    vec4 uv = vec4(vertexUV.x, vertexUV.y, 0, 1.0);\n    uv = uvTransform * uv;\n    uvCoord = vec2(uv.x, uv.y);\n    gl_Position = modelViewProjectionMatrix * vertexPosition;\n}", "#extension GL_OES_EGL_image_external :require\nprecision mediump float;\n\nvarying vec2 uvCoord;\nuniform samplerExternalOES uvSampler;\n\t\t\t\t\t\nvoid main() \n{ \n    vec4 col = texture2D(uvSampler, uvCoord); \n    gl_FragColor = col;\n}");
    }

    public static ARVideoTextureShader getShader() {
        ARShaderManager shaderManager = ARShaderManager.getInstance();
        boolean[] properties = new boolean[]{true};
        ARVideoTextureShader shader = (ARVideoTextureShader)shaderManager.findShader(ARVideoTextureShader.class, properties);
        if(shader != null) {
            return shader;
        } else {
            shader = new ARVideoTextureShader();
            shaderManager.addShader(shader, properties);
            ARRenderer renderer = ARRenderer.getInstance();
            renderer.addShader(shader);
            return shader;
        }
    }

    public void setUVTransform(float[] uvTransform) {
        int uvTransformHandle = GLES20.glGetUniformLocation(this.mShaderID, "uvTransform");
        if(uvTransformHandle >= 0) {
            GLES20.glUniformMatrix4fv(uvTransformHandle, 1, false, uvTransform, 0);
        }

    }
}
