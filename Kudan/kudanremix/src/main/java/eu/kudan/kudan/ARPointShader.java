//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

public class ARPointShader extends ARShaderProgram {
    static final String vertexString = "attribute vec4 vertexPosition;\n\nuniform mat4 modelViewProjectionMatrix;\n\nvoid main() \n{ \n    gl_Position = modelViewProjectionMatrix * vertexPosition;\n    gl_PointSize = 32.0;\n}";
    static final String fragmentString = "precision mediump float;\n\nuniform sampler2D uvSampler;\n\t\t\t\t\t\nvoid main() \n{ \n    vec4 col = texture2D(uvSampler, gl_PointCoord); \n    gl_FragColor = col;\n}";

    public ARPointShader() {
        this.setShaderStrings("attribute vec4 vertexPosition;\n\nuniform mat4 modelViewProjectionMatrix;\n\nvoid main() \n{ \n    gl_Position = modelViewProjectionMatrix * vertexPosition;\n    gl_PointSize = 32.0;\n}", "precision mediump float;\n\nuniform sampler2D uvSampler;\n\t\t\t\t\t\nvoid main() \n{ \n    vec4 col = texture2D(uvSampler, gl_PointCoord); \n    gl_FragColor = col;\n}");
    }

    public static ARPointShader getShader() {
        ARShaderManager shaderManager = ARShaderManager.getInstance();
        boolean[] properties = new boolean[]{true};
        ARPointShader shader = (ARPointShader)shaderManager.findShader(ARPointShader.class, properties);
        if(shader != null) {
            return shader;
        } else {
            shader = new ARPointShader();
            shaderManager.addShader(shader, properties);
            ARRenderer renderer = ARRenderer.getInstance();
            renderer.addShader(shader);
            return shader;
        }
    }
}
