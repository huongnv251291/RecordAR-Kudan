//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.opengl.GLES20;
import android.util.Log;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import java.util.List;

public class ARLightShader extends ARShaderProgram {
    private float[] mTmpVector;
    private float[] mTmpMatrices;
    private float[] mTmpMatricesRotations;
    static final String vertexString = "attribute vec4 vertexPosition;\nattribute vec3 vertexNormal;\nattribute vec2 vertexUV;\n\n#ifdef BLEND\nattribute vec4 vertexPositionTarget;\nattribute vec3 vertexNormalTarget;\nattribute vec2 vertexUVTarget;\n#endif\n\n#ifdef BONES\nattribute vec4 boneIndex;\nattribute vec4 boneWeight;\nuniform highp mat4 bones[32];\nuniform highp mat3 bonesRotation[32];\n#endif\n\n#ifdef BLEND\nuniform float influence;\n#endif\n \nvoid vertex(vec4 position, vec4 normal, vec2 uv);\nvoid main()\n{\n    vec4 pos;\n    vec3 normal;\n    \n#ifdef BONES\n    mediump ivec4 bIndex = ivec4(boneIndex);\n    mediump vec4 bWeights = boneWeight;\n    \n    highp mat4 boneMatrix = bones[bIndex.x];\n    pos = boneMatrix * vertexPosition * bWeights.x;\n    \n    highp mat4 boneMatrix2 = bones[bIndex.y];\n    pos += boneMatrix2 * vertexPosition * bWeights.y;\n    \n    highp mat4 boneMatrix3 = bones[bIndex.z];\n    pos += boneMatrix3 * vertexPosition * bWeights.z;\n\n    highp mat4 boneMatrix4 = bones[bIndex.w];\n    pos += boneMatrix4 * vertexPosition * bWeights.w;\n    \n    highp mat3 boneNormalMatrix = bonesRotation[bIndex.x];\n    normal = boneNormalMatrix * vertexNormal * bWeights.x;\n    \n    highp mat3 boneNormalMatrix2 = bonesRotation[bIndex.y];\n    normal += boneNormalMatrix2 * vertexNormal * bWeights.y;\n    \n    highp mat3 boneNormalMatrix3 = bonesRotation[bIndex.z];\n    normal += boneNormalMatrix3 * vertexNormal \t* bWeights.z;\n    \n    highp mat3 boneNormalMatrix4 = bonesRotation[bIndex.w];\n    normal += boneNormalMatrix4 * vertexNormal * bWeights.w;\n#else\n    pos = vertexPosition;\n    normal = vertexNormal.xyz;\n#endif\n    \n#ifdef BLEND\n    pos = vertexPosition + (vertexPositionTarget - vertexPosition) * influence;\n    normal = vertexNormal + (vertexNormalTarget - vertexNormal) * influence;\n    normal = normalize(normal);\n#endif\n    \n    vertex(pos, vec4(normal, 1.0), vertexUV);\n}\n\nprecision highp float;\n\nstruct Light {\n    vec3 direction;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n};\n \nstruct Material {\n    vec3 colour;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n    float shininess;\n};\n \n#ifdef TEXTURE\nvarying vec2 uvCoord;\n#endif\n \nuniform mat4 modelViewMatrix;\nuniform mat4 modelMatrix;\nuniform vec3 worldCameraPosition;\nuniform mat3 normalMatrix;\nuniform mat4 modelViewProjectionMatrix;\nuniform vec3 colourVector;\nvarying vec3 col;\n \nuniform Material material;\nuniform Light light;\n \nvarying vec3 vlight;\nvarying vec3 veyeCoords;\nvarying vec3 vnormalEye;\nvarying vec3 diffuse;\nvarying vec3 r;\n \n#ifdef REFLECTION\nvarying vec3 reflectDir;\nvarying float fresnelFactor;\n#endif\n\n\nvoid vertex(vec4 position, vec4 normal, vec2 uv)\n{\n    vec4 pos = position;\n    vec3 norm = normal.xyz;\n    \n    vec3 normalEye = normalize(normalMatrix * norm);\n    vec3 eyeCoords = vec3(modelViewMatrix * pos);\n    vec3 lightEye = -light.direction;\n    \n#ifdef TEXTURE\n    uvCoord = vertexUV;\n#endif\n    \n    float dp = max(0.0, dot(lightEye, normalEye));\n\n    diffuse = material.diffuse * dp + material.ambient;\n    \n    vlight = -lightEye;\n    veyeCoords = eyeCoords;\n    vnormalEye = normalEye;\n    \n    r = reflect(vlight, vnormalEye);\n    \n#ifdef REFLECTION\n    vec3 worldPos = vec3(modelMatrix * position);\n    vec3 worldNorm = vec3(modelMatrix * vec4(normal.xyz, 0.0));\n    vec3 worldView = worldCameraPosition - worldPos;\n    \n    reflectDir = reflect(-worldView, normalize(worldNorm));\n    \n    float f = 1.0 - dot(normalize(worldView), normalize(worldNorm));\n//    fresnelFactor = (f + 0.2) * 0.833;\n    fresnelFactor = 1.0;\n#endif\n    \n    gl_Position = modelViewProjectionMatrix * position;\n}";
    static final String fragmentString = "precision highp float;\n\nstruct Light {\n    vec3 direction;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n};\n \nstruct Material {\n    vec3 colour;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n    float shininess;\n};\n \nuniform Material material;\nuniform Light light;\n \n#ifdef REFLECTION\nuniform samplerCube cubeSampler;\nvarying vec3 reflectDir;\nvarying float fresnelFactor;\n#endif\n \n#ifdef TEXTURE\nvarying vec2 uvCoord;\nuniform sampler2D texSampler;\n#endif\n \nvarying vec3 vlight;\nvarying vec3 veyeCoords;\nvarying vec3 vnormalEye;\nvarying vec3 diffuse;\nvarying vec3 r;\n \nuniform float reflectivity;\nuniform float shininess;\n \n \nvoid main()\n{\n    vec3 v = normalize(-veyeCoords);\n    vec3 spec = material.specular * pow(max(dot(normalize(r), v), 0.0), shininess);\n    \n#ifdef TEXTURE\n    vec3 col = texture2D(texSampler, uvCoord).rgb;\n#else\n    vec3 col = material.colour;\n#endif\n    col = col * diffuse + spec;\n    \n\n#ifdef REFLECTION\n    vec3 cube = textureCube(cubeSampler, reflectDir).rgb;\n    col = mix(col, cube, reflectivity);\n#endif\n    \n    gl_FragColor = vec4(col, 1.0);\n}";

    ARLightShader() {
        this.mTmpVector = new float[3];
        this.mTmpMatrices = new float[1280];
        this.mTmpMatricesRotations = new float[1280];
        this.setShaderStrings("attribute vec4 vertexPosition;\nattribute vec3 vertexNormal;\nattribute vec2 vertexUV;\n\n#ifdef BLEND\nattribute vec4 vertexPositionTarget;\nattribute vec3 vertexNormalTarget;\nattribute vec2 vertexUVTarget;\n#endif\n\n#ifdef BONES\nattribute vec4 boneIndex;\nattribute vec4 boneWeight;\nuniform highp mat4 bones[32];\nuniform highp mat3 bonesRotation[32];\n#endif\n\n#ifdef BLEND\nuniform float influence;\n#endif\n \nvoid vertex(vec4 position, vec4 normal, vec2 uv);\nvoid main()\n{\n    vec4 pos;\n    vec3 normal;\n    \n#ifdef BONES\n    mediump ivec4 bIndex = ivec4(boneIndex);\n    mediump vec4 bWeights = boneWeight;\n    \n    highp mat4 boneMatrix = bones[bIndex.x];\n    pos = boneMatrix * vertexPosition * bWeights.x;\n    \n    highp mat4 boneMatrix2 = bones[bIndex.y];\n    pos += boneMatrix2 * vertexPosition * bWeights.y;\n    \n    highp mat4 boneMatrix3 = bones[bIndex.z];\n    pos += boneMatrix3 * vertexPosition * bWeights.z;\n\n    highp mat4 boneMatrix4 = bones[bIndex.w];\n    pos += boneMatrix4 * vertexPosition * bWeights.w;\n    \n    highp mat3 boneNormalMatrix = bonesRotation[bIndex.x];\n    normal = boneNormalMatrix * vertexNormal * bWeights.x;\n    \n    highp mat3 boneNormalMatrix2 = bonesRotation[bIndex.y];\n    normal += boneNormalMatrix2 * vertexNormal * bWeights.y;\n    \n    highp mat3 boneNormalMatrix3 = bonesRotation[bIndex.z];\n    normal += boneNormalMatrix3 * vertexNormal \t* bWeights.z;\n    \n    highp mat3 boneNormalMatrix4 = bonesRotation[bIndex.w];\n    normal += boneNormalMatrix4 * vertexNormal * bWeights.w;\n#else\n    pos = vertexPosition;\n    normal = vertexNormal.xyz;\n#endif\n    \n#ifdef BLEND\n    pos = vertexPosition + (vertexPositionTarget - vertexPosition) * influence;\n    normal = vertexNormal + (vertexNormalTarget - vertexNormal) * influence;\n    normal = normalize(normal);\n#endif\n    \n    vertex(pos, vec4(normal, 1.0), vertexUV);\n}\n\nprecision highp float;\n\nstruct Light {\n    vec3 direction;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n};\n \nstruct Material {\n    vec3 colour;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n    float shininess;\n};\n \n#ifdef TEXTURE\nvarying vec2 uvCoord;\n#endif\n \nuniform mat4 modelViewMatrix;\nuniform mat4 modelMatrix;\nuniform vec3 worldCameraPosition;\nuniform mat3 normalMatrix;\nuniform mat4 modelViewProjectionMatrix;\nuniform vec3 colourVector;\nvarying vec3 col;\n \nuniform Material material;\nuniform Light light;\n \nvarying vec3 vlight;\nvarying vec3 veyeCoords;\nvarying vec3 vnormalEye;\nvarying vec3 diffuse;\nvarying vec3 r;\n \n#ifdef REFLECTION\nvarying vec3 reflectDir;\nvarying float fresnelFactor;\n#endif\n\n\nvoid vertex(vec4 position, vec4 normal, vec2 uv)\n{\n    vec4 pos = position;\n    vec3 norm = normal.xyz;\n    \n    vec3 normalEye = normalize(normalMatrix * norm);\n    vec3 eyeCoords = vec3(modelViewMatrix * pos);\n    vec3 lightEye = -light.direction;\n    \n#ifdef TEXTURE\n    uvCoord = vertexUV;\n#endif\n    \n    float dp = max(0.0, dot(lightEye, normalEye));\n\n    diffuse = material.diffuse * dp + material.ambient;\n    \n    vlight = -lightEye;\n    veyeCoords = eyeCoords;\n    vnormalEye = normalEye;\n    \n    r = reflect(vlight, vnormalEye);\n    \n#ifdef REFLECTION\n    vec3 worldPos = vec3(modelMatrix * position);\n    vec3 worldNorm = vec3(modelMatrix * vec4(normal.xyz, 0.0));\n    vec3 worldView = worldCameraPosition - worldPos;\n    \n    reflectDir = reflect(-worldView, normalize(worldNorm));\n    \n    float f = 1.0 - dot(normalize(worldView), normalize(worldNorm));\n//    fresnelFactor = (f + 0.2) * 0.833;\n    fresnelFactor = 1.0;\n#endif\n    \n    gl_Position = modelViewProjectionMatrix * position;\n}", "precision highp float;\n\nstruct Light {\n    vec3 direction;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n};\n \nstruct Material {\n    vec3 colour;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n    float shininess;\n};\n \nuniform Material material;\nuniform Light light;\n \n#ifdef REFLECTION\nuniform samplerCube cubeSampler;\nvarying vec3 reflectDir;\nvarying float fresnelFactor;\n#endif\n \n#ifdef TEXTURE\nvarying vec2 uvCoord;\nuniform sampler2D texSampler;\n#endif\n \nvarying vec3 vlight;\nvarying vec3 veyeCoords;\nvarying vec3 vnormalEye;\nvarying vec3 diffuse;\nvarying vec3 r;\n \nuniform float reflectivity;\nuniform float shininess;\n \n \nvoid main()\n{\n    vec3 v = normalize(-veyeCoords);\n    vec3 spec = material.specular * pow(max(dot(normalize(r), v), 0.0), shininess);\n    \n#ifdef TEXTURE\n    vec3 col = texture2D(texSampler, uvCoord).rgb;\n#else\n    vec3 col = material.colour;\n#endif\n    col = col * diffuse + spec;\n    \n\n#ifdef REFLECTION\n    vec3 cube = textureCube(cubeSampler, reflectDir).rgb;\n    col = mix(col, cube, reflectivity);\n#endif\n    \n    gl_FragColor = vec4(col, 1.0);\n}");
    }

    public ARLightShader(boolean hasReflection, boolean hasTexture, boolean hasAlpha, boolean hasBones, boolean hasMorph) {
        this();
        String vertex = "attribute vec4 vertexPosition;\nattribute vec3 vertexNormal;\nattribute vec2 vertexUV;\n\n#ifdef BLEND\nattribute vec4 vertexPositionTarget;\nattribute vec3 vertexNormalTarget;\nattribute vec2 vertexUVTarget;\n#endif\n\n#ifdef BONES\nattribute vec4 boneIndex;\nattribute vec4 boneWeight;\nuniform highp mat4 bones[32];\nuniform highp mat3 bonesRotation[32];\n#endif\n\n#ifdef BLEND\nuniform float influence;\n#endif\n \nvoid vertex(vec4 position, vec4 normal, vec2 uv);\nvoid main()\n{\n    vec4 pos;\n    vec3 normal;\n    \n#ifdef BONES\n    mediump ivec4 bIndex = ivec4(boneIndex);\n    mediump vec4 bWeights = boneWeight;\n    \n    highp mat4 boneMatrix = bones[bIndex.x];\n    pos = boneMatrix * vertexPosition * bWeights.x;\n    \n    highp mat4 boneMatrix2 = bones[bIndex.y];\n    pos += boneMatrix2 * vertexPosition * bWeights.y;\n    \n    highp mat4 boneMatrix3 = bones[bIndex.z];\n    pos += boneMatrix3 * vertexPosition * bWeights.z;\n\n    highp mat4 boneMatrix4 = bones[bIndex.w];\n    pos += boneMatrix4 * vertexPosition * bWeights.w;\n    \n    highp mat3 boneNormalMatrix = bonesRotation[bIndex.x];\n    normal = boneNormalMatrix * vertexNormal * bWeights.x;\n    \n    highp mat3 boneNormalMatrix2 = bonesRotation[bIndex.y];\n    normal += boneNormalMatrix2 * vertexNormal * bWeights.y;\n    \n    highp mat3 boneNormalMatrix3 = bonesRotation[bIndex.z];\n    normal += boneNormalMatrix3 * vertexNormal \t* bWeights.z;\n    \n    highp mat3 boneNormalMatrix4 = bonesRotation[bIndex.w];\n    normal += boneNormalMatrix4 * vertexNormal * bWeights.w;\n#else\n    pos = vertexPosition;\n    normal = vertexNormal.xyz;\n#endif\n    \n#ifdef BLEND\n    pos = vertexPosition + (vertexPositionTarget - vertexPosition) * influence;\n    normal = vertexNormal + (vertexNormalTarget - vertexNormal) * influence;\n    normal = normalize(normal);\n#endif\n    \n    vertex(pos, vec4(normal, 1.0), vertexUV);\n}\n\nprecision highp float;\n\nstruct Light {\n    vec3 direction;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n};\n \nstruct Material {\n    vec3 colour;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n    float shininess;\n};\n \n#ifdef TEXTURE\nvarying vec2 uvCoord;\n#endif\n \nuniform mat4 modelViewMatrix;\nuniform mat4 modelMatrix;\nuniform vec3 worldCameraPosition;\nuniform mat3 normalMatrix;\nuniform mat4 modelViewProjectionMatrix;\nuniform vec3 colourVector;\nvarying vec3 col;\n \nuniform Material material;\nuniform Light light;\n \nvarying vec3 vlight;\nvarying vec3 veyeCoords;\nvarying vec3 vnormalEye;\nvarying vec3 diffuse;\nvarying vec3 r;\n \n#ifdef REFLECTION\nvarying vec3 reflectDir;\nvarying float fresnelFactor;\n#endif\n\n\nvoid vertex(vec4 position, vec4 normal, vec2 uv)\n{\n    vec4 pos = position;\n    vec3 norm = normal.xyz;\n    \n    vec3 normalEye = normalize(normalMatrix * norm);\n    vec3 eyeCoords = vec3(modelViewMatrix * pos);\n    vec3 lightEye = -light.direction;\n    \n#ifdef TEXTURE\n    uvCoord = vertexUV;\n#endif\n    \n    float dp = max(0.0, dot(lightEye, normalEye));\n\n    diffuse = material.diffuse * dp + material.ambient;\n    \n    vlight = -lightEye;\n    veyeCoords = eyeCoords;\n    vnormalEye = normalEye;\n    \n    r = reflect(vlight, vnormalEye);\n    \n#ifdef REFLECTION\n    vec3 worldPos = vec3(modelMatrix * position);\n    vec3 worldNorm = vec3(modelMatrix * vec4(normal.xyz, 0.0));\n    vec3 worldView = worldCameraPosition - worldPos;\n    \n    reflectDir = reflect(-worldView, normalize(worldNorm));\n    \n    float f = 1.0 - dot(normalize(worldView), normalize(worldNorm));\n//    fresnelFactor = (f + 0.2) * 0.833;\n    fresnelFactor = 1.0;\n#endif\n    \n    gl_Position = modelViewProjectionMatrix * position;\n}";
        String fragment = "precision highp float;\n\nstruct Light {\n    vec3 direction;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n};\n \nstruct Material {\n    vec3 colour;\n    vec3 ambient;\n    vec3 diffuse;\n    vec3 specular;\n    float shininess;\n};\n \nuniform Material material;\nuniform Light light;\n \n#ifdef REFLECTION\nuniform samplerCube cubeSampler;\nvarying vec3 reflectDir;\nvarying float fresnelFactor;\n#endif\n \n#ifdef TEXTURE\nvarying vec2 uvCoord;\nuniform sampler2D texSampler;\n#endif\n \nvarying vec3 vlight;\nvarying vec3 veyeCoords;\nvarying vec3 vnormalEye;\nvarying vec3 diffuse;\nvarying vec3 r;\n \nuniform float reflectivity;\nuniform float shininess;\n \n \nvoid main()\n{\n    vec3 v = normalize(-veyeCoords);\n    vec3 spec = material.specular * pow(max(dot(normalize(r), v), 0.0), shininess);\n    \n#ifdef TEXTURE\n    vec3 col = texture2D(texSampler, uvCoord).rgb;\n#else\n    vec3 col = material.colour;\n#endif\n    col = col * diffuse + spec;\n    \n\n#ifdef REFLECTION\n    vec3 cube = textureCube(cubeSampler, reflectDir).rgb;\n    col = mix(col, cube, reflectivity);\n#endif\n    \n    gl_FragColor = vec4(col, 1.0);\n}";
        if(hasReflection) {
            vertex = "#define REFLECTION\n" + vertex;
            fragment = "#define REFLECTION\n" + fragment;
        }

        if(hasTexture) {
            vertex = "#define TEXTURE\n" + vertex;
            fragment = "#define TEXTURE\n" + fragment;
        }

        if(hasAlpha) {
            vertex = "#define ALPHA\n" + vertex;
            fragment = "#define ALPHA\n" + fragment;
        }

        if(hasBones) {
            vertex = "#define BONES\n" + vertex;
            fragment = "#define BONES\n" + fragment;
        }

        if(hasMorph) {
            vertex = "#define MORPH\n" + vertex;
            fragment = "#define MORPH\n" + fragment;
        }

        this.setShaderStrings(vertex, fragment);
    }

    public static ARLightShader getShader(boolean hasReflection, boolean hasTexture, boolean hasAlpha, boolean hasBones, boolean hasMorph) {
        ARShaderManager shaderManager = ARShaderManager.getInstance();
        boolean[] properties = new boolean[]{hasReflection, hasTexture, hasAlpha, hasBones, hasMorph};
        ARLightShader shader = (ARLightShader)shaderManager.findShader(ARLightShader.class, properties);
        if(shader != null) {
            return shader;
        } else {
            shader = new ARLightShader(hasReflection, hasTexture, hasAlpha, hasBones, hasMorph);
            Log.i("SHADER", "creating new shader with " + hasReflection + " " + hasTexture + " " + hasAlpha + " " + hasBones + " " + hasMorph);
            shaderManager.addShader(shader, properties);
            ARRenderer renderer = ARRenderer.getInstance();
            renderer.addShader(shader);
            return shader;
        }
    }

    public void setDiffuse(Vector3f diffuse) {
        int diffuseUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "material.diffuse");
        if(diffuseUniformHandle >= 0) {
            this.mTmpVector[0] = diffuse.getX();
            this.mTmpVector[1] = diffuse.getY();
            this.mTmpVector[2] = diffuse.getZ();
            GLES20.glUniform3fv(diffuseUniformHandle, 1, this.mTmpVector, 0);
        }

    }

    public void setAmbient(Vector3f ambient) {
        int ambientUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "material.ambient");
        if(ambientUniformHandle >= 0) {
            this.mTmpVector[0] = ambient.getX();
            this.mTmpVector[1] = ambient.getY();
            this.mTmpVector[2] = ambient.getZ();
            GLES20.glUniform3fv(ambientUniformHandle, 1, this.mTmpVector, 0);
        }

    }

    public void setColour(Vector3f colour) {
        int colourUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "material.colour");
        if(colourUniformHandle >= 0) {
            this.mTmpVector[0] = colour.getX();
            this.mTmpVector[1] = colour.getY();
            this.mTmpVector[2] = colour.getZ();
            GLES20.glUniform3fv(colourUniformHandle, 1, this.mTmpVector, 0);
        }

    }

    public void setShininess(float shininess) {
        int shininessUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "shininess");
        if(shininessUniformHandle >= 0) {
            GLES20.glUniform1f(shininessUniformHandle, shininess);
        }

    }

    public void setAlpha(float alpha) {
        int alphaUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "alpha");
        if(alphaUniformHandle >= 0) {
            GLES20.glUniform1f(alphaUniformHandle, alpha);
        }

    }

    public void setReflectivity(float reflectivity) {
        int reflectivityUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "reflectivity");
        if(reflectivityUniformHandle >= 0) {
            GLES20.glUniform1f(reflectivityUniformHandle, reflectivity);
        }

    }

    public void setSpecular(Vector3f specular) {
        int specularUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "material.specular");
        if(specularUniformHandle >= 0) {
            this.mTmpVector[0] = specular.getX();
            this.mTmpVector[1] = specular.getY();
            this.mTmpVector[2] = specular.getZ();
            GLES20.glUniform3fv(specularUniformHandle, 1, this.mTmpVector, 0);
        }

    }

    public void setUniforms() {
        ARRenderer renderer = ARRenderer.getInstance();
        int lightUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "light.direction");
        if(lightUniformHandle >= 0) {
            this.mTmpVector[0] = renderer.getLightPosition().getX();
            this.mTmpVector[1] = renderer.getLightPosition().getY();
            this.mTmpVector[2] = renderer.getLightPosition().getZ();
            GLES20.glUniform3fv(lightUniformHandle, 1, this.mTmpVector, 0);
        }

        int texSamplerUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "texSampler");
        if(texSamplerUniformHandle >= 0) {
            GLES20.glUniform1i(texSamplerUniformHandle, 1);
        }

        int cubeSamplerUniformHandle = GLES20.glGetUniformLocation(this.mShaderID, "cubeSampler");
        if(cubeSamplerUniformHandle >= 0) {
            GLES20.glUniform1i(cubeSamplerUniformHandle, 0);
        }

    }

    public void setBones(List<Matrix4f> bones, int numberOfBones) {
        int offset = 0;
        int offsetRotation = 0;

        int boneMatrixHandle;
        for(boneMatrixHandle = 0; boneMatrixHandle < numberOfBones; ++boneMatrixHandle) {
            Matrix4f bone = (Matrix4f)bones.get(boneMatrixHandle);
            Matrix3f boneRotation = new Matrix3f();
            bone.toRotationMatrix(boneRotation);
            bone.get(this.mTmpMatrices, offset, false);
            boneRotation.get(this.mTmpMatricesRotations, offsetRotation, false);
            offset += 16;
            offsetRotation += 9;
        }

        boneMatrixHandle = GLES20.glGetUniformLocation(this.mShaderID, "bones");
        if(boneMatrixHandle >= 0) {
            GLES20.glUniformMatrix4fv(boneMatrixHandle, bones.size(), false, this.mTmpMatrices, 0);
        }

        int boneMatrixRotationHandle = GLES20.glGetUniformLocation(this.mShaderID, "bonesRotation");
        if(boneMatrixRotationHandle >= 0) {
            GLES20.glUniformMatrix3fv(boneMatrixRotationHandle, bones.size(), false, this.mTmpMatricesRotations, 0);
        }

    }
}
