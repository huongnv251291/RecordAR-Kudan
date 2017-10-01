//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ARShaderManager {
    private static ARShaderManager shaderManager;
    private List<ARShaderManager.ShaderProperties> mShaders = new ArrayList();

    public ARShaderManager() {
    }

    public static ARShaderManager getInstance() {
        if(shaderManager == null) {
            shaderManager = new ARShaderManager();
        }

        return shaderManager;
    }

    public ARShaderProgram findShader(Class<?> type, boolean[] properties) {
        Iterator var3 = this.mShaders.iterator();

        ARShaderManager.ShaderProperties shader;
        boolean found;
        do {
            do {
                if(!var3.hasNext()) {
                    return null;
                }

                shader = (ARShaderManager.ShaderProperties)var3.next();
            } while(!shader.getShader().getClass().equals(type));

            found = true;

            for(int i = 0; i < properties.length; ++i) {
                if(shader.getProperties()[i] != properties[i]) {
                    found = false;
                    break;
                }
            }
        } while(!found);

        return shader.getShader();
    }

    public void addShader(ARShaderProgram shader, boolean[] properties) {
        this.mShaders.add(new ARShaderManager.ShaderProperties(shader, properties));
    }

    public void reset() {
        this.mShaders = new ArrayList();
    }

    public class ShaderProperties {
        private ARShaderProgram mShader;
        private boolean[] mProperties;

        public ShaderProperties(ARShaderProgram shader, boolean[] properties) {
            this.mShader = shader;
            this.mProperties = properties;
        }

        public ARShaderProgram getShader() {
            return this.mShader;
        }

        public boolean[] getProperties() {
            return this.mProperties;
        }
    }
}
