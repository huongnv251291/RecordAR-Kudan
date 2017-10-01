//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;
import android.util.Log;
import com.jme3.math.Vector3f;

public class ARShaderProgram {
    private String mVertexShaderString;
    private String mFragmentShaderString;
    protected int mShaderID;
    private float[] mTmpMatrix = new float[16];
    private float[] mTmpMatrix9 = new float[9];
    private float[] mTmpVector3 = new float[3];
    private boolean mIsCompiled = false;

    static int initShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if(shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] glStatusVar = new int[]{0};
            GLES20.glGetShaderiv(shader, '讁', glStatusVar, 0);
            if(glStatusVar[0] == 0) {
                Log.e("KudanAR", "couldn't compile shader" + shaderType + " : " + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }

        return shader;
    }

    public static int createShader(String vertexShaderSrc, String fragmentShaderSrc) {
        int vertShader = initShader('謱', vertexShaderSrc);
        int fragShader = initShader('謰', fragmentShaderSrc);
        if(vertShader != 0 && fragShader != 0) {
            int program = GLES20.glCreateProgram();
            if(program != 0) {
                GLES20.glAttachShader(program, vertShader);
                checkError();
                GLES20.glAttachShader(program, fragShader);
                checkError();
                GLES20.glBindAttribLocation(program, 0, "vertexPosition");
                GLES20.glBindAttribLocation(program, 1, "vertexNormal");
                GLES20.glBindAttribLocation(program, 2, "vertexUV");
                GLES20.glBindAttribLocation(program, 3, "boneIndex");
                GLES20.glBindAttribLocation(program, 4, "boneWeight");
                GLES20.glLinkProgram(program);
                int[] glStatusVar = new int[]{0};
                GLES20.glGetProgramiv(program, '讂', glStatusVar, 0);
                if(glStatusVar[0] == 0) {
                    Log.e("KudanAR", "Couldn't link shader: " + GLES20.glGetProgramInfoLog(program));
                    GLES20.glDeleteProgram(program);
                    program = 0;
                }
            }

            return program;
        } else {
            return 0;
        }
    }

    public static void checkError() {
        for(int error = GLES20.glGetError(); error != 0; error = GLES20.glGetError()) {
            Log.e("KudanAR", "gl error: " + Integer.toHexString(error));
        }

    }

    ARShaderProgram() {
    }

    public void setShaderStrings(String vertexShaderString, String fragmentShaderString) {
        this.mVertexShaderString = vertexShaderString;
        this.mFragmentShaderString = fragmentShaderString;
    }

    public void compileShaders() {
        if(!this.mIsCompiled) {
            this.mIsCompiled = true;
            this.mShaderID = createShader(this.mVertexShaderString, this.mFragmentShaderString);
        }
    }

    public void useProgram() {
        GLES20.glUseProgram(this.mShaderID);
    }

    public void prepareRenderer() {
        this.useProgram();
        this.setGlobalUniforms();
    }

    private void setGlobalUniforms() {
        ARRenderer renderer = ARRenderer.getInstance();
        int modelViewProjectionUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "modelViewProjectionMatrix");
        if(modelViewProjectionUniformHandle >= 0) {
            renderer.getModelViewProjectionMatrix().get(this.mTmpMatrix, false);
            GLES20.glUniformMatrix4fv(modelViewProjectionUniformHandle, 1, false, this.mTmpMatrix, 0);
        }

        int modelViewUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "modelViewMatrix");
        if(modelViewUniformHandle >= 0) {
            renderer.getModelViewMatrix().get(this.mTmpMatrix, false);
            GLES20.glUniformMatrix4fv(modelViewUniformHandle, 1, false, this.mTmpMatrix, 0);
        }

        int modelUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "modelMatrix");
        if(modelUniformHandle >= 0) {
            renderer.getModelMatrix().get(this.mTmpMatrix, false);
            GLES20.glUniformMatrix4fv(modelUniformHandle, 1, false, this.mTmpMatrix, 0);
        }

        int normalUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "normalMatrix");
        if(normalUniformHandle >= 0) {
            renderer.getNormalMatrix().get(this.mTmpMatrix9, false);
            GLES20.glUniformMatrix3fv(normalUniformHandle, 1, false, this.mTmpMatrix9, 0);
        }

        int worldCameraHandle = GLES20.glGetUniformLocation(this.mShaderID, "worldCameraPosition");
        if(worldCameraHandle >= 0) {
            Vector3f worldCamPosition = renderer.getWorldCameraPosition();
            this.mTmpVector3[0] = worldCamPosition.getX();
            this.mTmpVector3[1] = worldCamPosition.getY();
            this.mTmpVector3[2] = worldCamPosition.getZ();
            GLES20.glUniform3fv(worldCameraHandle, 1, this.mTmpVector3, 0);
        }

    }
}
