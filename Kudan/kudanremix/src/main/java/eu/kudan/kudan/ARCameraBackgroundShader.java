//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;

public class ARCameraBackgroundShader extends ARShaderProgram {
    static final String vertexString = "attribute vec4 vertexPosition;\n\nuniform mat4 modelViewProjectionMatrix;\n\nvoid main()\n{\n    gl_Position = vertexPosition;\n}";
    static final String fragmentString = "#extension GL_OES_EGL_image_external :require\nprecision mediump float;\n\nuniform samplerExternalOES uvSampler;\nuniform mat4 markerModelViewProjection;\nuniform mat4 uvTransform;\n\nvoid main()\n{\n\tvec4 clipCoord = markerModelViewProjection * vec4(gl_FragCoord.xy, 0.0, 1.0);\n\tvec2 ndc = vec2(clipCoord.x, clipCoord.y);\n    ndc /= clipCoord.w;\n    float x = (ndc.x * 0.5 + 0.5);\n    float y = (ndc.y * 0.5 + 0.5);\n\tvec2 lookup = vec2(x, y);\n    vec4 uv = vec4(lookup, 0, 1.0);\n    uv = uvTransform * uv;\n    lookup = vec2(uv.x, uv.y);\n    vec4 rgb = texture2D(uvSampler, lookup);\n    gl_FragColor = rgb;\n}";

    public ARCameraBackgroundShader() {
        this.setShaderStrings("attribute vec4 vertexPosition;\n\nuniform mat4 modelViewProjectionMatrix;\n\nvoid main()\n{\n    gl_Position = vertexPosition;\n}", "#extension GL_OES_EGL_image_external :require\nprecision mediump float;\n\nuniform samplerExternalOES uvSampler;\nuniform mat4 markerModelViewProjection;\nuniform mat4 uvTransform;\n\nvoid main()\n{\n\tvec4 clipCoord = markerModelViewProjection * vec4(gl_FragCoord.xy, 0.0, 1.0);\n\tvec2 ndc = vec2(clipCoord.x, clipCoord.y);\n    ndc /= clipCoord.w;\n    float x = (ndc.x * 0.5 + 0.5);\n    float y = (ndc.y * 0.5 + 0.5);\n\tvec2 lookup = vec2(x, y);\n    vec4 uv = vec4(lookup, 0, 1.0);\n    uv = uvTransform * uv;\n    lookup = vec2(uv.x, uv.y);\n    vec4 rgb = texture2D(uvSampler, lookup);\n    gl_FragColor = rgb;\n}");
    }

    public static ARCameraBackgroundShader getShader() {
        ARShaderManager shaderManager = ARShaderManager.getInstance();
        boolean[] properties = new boolean[]{true};
        ARCameraBackgroundShader shader = (ARCameraBackgroundShader)shaderManager.findShader(ARCameraBackgroundShader.class, properties);
        if(shader != null) {
            return shader;
        } else {
            shader = new ARCameraBackgroundShader();
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

    public void setMarkerModelViewProjection(float[] mvp) {
        int mvpHandle = GLES20.glGetUniformLocation(this.mShaderID, "markerModelViewProjection");
        if(mvpHandle >= 0) {
            GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvp, 0);
        }

    }
}
