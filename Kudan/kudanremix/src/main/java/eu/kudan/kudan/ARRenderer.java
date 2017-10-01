//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.content.res.AssetManager;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.util.Log;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ARRenderer {
    private static ARRenderer renderer;
    public List<ARRendererListener> mListeners;
    private boolean isInitialised;
    private long renderTime;
    private Matrix4f mProjectionMatrix;
    private Matrix4f mModelViewMatrix;
    private Matrix4f mModelMatrix;
    private Matrix4f mModelViewProjectionMatrix = new Matrix4f();
    private Matrix3f mNormalMatrix;
    private Vector3f mWorldCameraPosition;
    private Vector3f mLightPosition = new Vector3f();
    private float mBlendInfluence;
    private List<Matrix4f> mBones;
    private ARCamera mCamera;
    private AssetManager mAssetManager;
    private String mDataDir;
    public Vector2f cameraFBOResolution;
    private List<ARRenderTarget> mRenderTargets = new ArrayList();
    private ARRenderTarget mDefaultRenderTarget;
    private List<MediaPlayer> mMediaPlayers = new ArrayList();
    private List<ARVertexBuffer> mVertexBuffers = new ArrayList();
    private List<ARIndexBuffer> mIndexBuffers = new ArrayList();
    private List<ARTexture> mTextures = new ArrayList();
    private List<ARShaderProgram> mShaders = new ArrayList();
    private ConcurrentLinkedQueue<Runnable> mPreRenderEvents = new ConcurrentLinkedQueue();
    private boolean mIsContextLoaded = false;
    public Vector2f mScreenSize;
    private Vector2f mCameraSize;
    private ARActivity mActivity;
    private Point mTouchCoords = null;
    private boolean mRenderForCapture;
    private ARVideoTexture mActiveVideoTexture = null;
    private int mCameraFBO;
    private int mCaptureFBO;
    private Vector3f mNextColour = new Vector3f(0.0F, 0.0F, 8.0F);
    private boolean[] vertexAttributes = new boolean[10];

    public ARRenderer() {
    }

    public static ARRenderer getInstance() {
        if(renderer == null || !renderer.isInitialised) {
            renderer = new ARRenderer();
            renderer.initialise();
            renderer.isInitialised = true;
        }

        return renderer;
    }

    public long getRenderTime() {
        return this.renderTime;
    }

    public void setBlendInfluence(float influence) {
        this.mBlendInfluence = influence;
    }

    public float getBlendInfluence() {
        return this.mBlendInfluence;
    }

    public void setBones(List<Matrix4f> bones) {
        this.mBones = bones;
    }

    public List<Matrix4f> getBones() {
        return this.mBones;
    }

    public void addRenderTarget(ARRenderTarget renderTarget) {
        this.mRenderTargets.add(renderTarget);
    }

    public List<ARRenderTarget> getRenderTargets() {
        return this.mRenderTargets;
    }

    public ARRenderTarget getDefaultRenderTarget() {
        return this.mDefaultRenderTarget;
    }

    public String getDataDir() {
        return this.mDataDir;
    }

    public void setDataDir(String dataDir) {
        this.mDataDir = dataDir;
    }

    public ARActivity getActivity() {
        if(this.mActivity == null) {
            this.mActivity = (ARActivity)ARActivity.getContext();
        }

        return this.mActivity;
    }

    public void setActivity(ARActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void setScreenSize(int x, int y) {
        this.mScreenSize = new Vector2f((float)x, (float)y);
    }

    public void setCameraSize(int x, int y) {
        this.mCameraSize = new Vector2f((float)x, (float)y);
    }

    public Matrix4f getProjectionMatrix() {
        return this.mProjectionMatrix;
    }

    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.mProjectionMatrix = projectionMatrix;
    }

    public Matrix4f getModelViewMatrix() {
        return this.mModelViewMatrix;
    }

    public void setModelViewMatrix(Matrix4f modelViewMatrix) {
        this.mModelViewMatrix = modelViewMatrix;
    }

    public Matrix4f getModelMatrix() {
        return this.mModelMatrix;
    }

    public void setModelMatrix(Matrix4f modelMatrix) {
        this.mModelMatrix = modelMatrix;
    }

    public Matrix3f getNormalMatrix() {
        return this.mNormalMatrix;
    }

    public void setNormalMatrix(Matrix3f normalMatrix) {
        this.mNormalMatrix = normalMatrix;
    }

    public Vector3f getWorldCameraPosition() {
        return this.mWorldCameraPosition;
    }

    public void setWorldCameraPosition(Vector3f worldCameraPosition) {
        this.mWorldCameraPosition = worldCameraPosition;
    }

    public Vector3f getLightPosition() {
        return this.mLightPosition;
    }

    public void setLightPosition(Vector3f lightPosition) {
        this.mLightPosition.set(lightPosition);
    }

    public Matrix4f getModelViewProjectionMatrix() {
        return this.mProjectionMatrix.mult(this.mModelViewMatrix);
    }

    public ARCamera getCamera() {
        return this.mCamera;
    }

    public void setCamera(ARCamera camera) {
        this.mCamera = camera;
    }

    private void flattenScene(ARNode node, List<ARNode> flatNodes) {
        if(node.getVisible()) {
            flatNodes.add(node);
            Iterator var3 = node.getChildren().iterator();

            while(var3.hasNext()) {
                ARNode child = (ARNode)var3.next();
                this.flattenScene(child, flatNodes);
            }

        }
    }

    private boolean nearEnough(int a, int b) {
        return Math.abs(a - b) > 2?false:Math.abs(b - a) <= 2;
    }

    public boolean getRenderForCapture() {
        return this.mRenderForCapture;
    }

    public void cameraDraw() {
        GLES20.glBindFramebuffer('赀', this.mCameraFBO);
    }

    public void render() {
        this.renderTime = System.currentTimeMillis();

        while(!this.mPreRenderEvents.isEmpty()) {
            Runnable event = (Runnable)this.mPreRenderEvents.poll();
            event.run();
        }

        List var8 = this.mListeners;
        Iterator var2;
        ARRendererListener listener;
        synchronized(this.mListeners) {
            var2 = this.mListeners.iterator();

            while(true) {
                if(!var2.hasNext()) {
                    break;
                }

                listener = (ARRendererListener)var2.next();
                listener.preRender();
            }
        }

        Collections.sort(this.mRenderTargets, new Comparator<ARRenderTarget>() {
            public int compare(ARRenderTarget lhs, ARRenderTarget rhs) {
                int a = lhs.getPriority();
                int b = rhs.getPriority();
                return a - b;
            }
        });
        Iterator var9 = this.mRenderTargets.iterator();

        while(var9.hasNext()) {
            ARRenderTarget renderTarget = (ARRenderTarget)var9.next();
            renderTarget.draw();
        }

        var8 = this.mListeners;
        synchronized(this.mListeners) {
            var2 = this.mListeners.iterator();

            while(var2.hasNext()) {
                listener = (ARRendererListener)var2.next();
                listener.postRender();
            }

        }
    }

    public void draw() {
        List<ARNode> flatNodes = new ArrayList();
        List<ARNode> occlusionNodes = new ArrayList();
        List<ARNode> opaqueNodes = new ArrayList();
        List<ARNode> translucentNodes = new ArrayList();
        this.flattenScene(this.getCamera(), flatNodes);
        Iterator var5 = flatNodes.iterator();

        ARNode node;
        while(var5.hasNext()) {
            node = (ARNode)var5.next();
            node.preRender();
        }

        this.mRenderForCapture = false;
        var5 = flatNodes.iterator();

        while(var5.hasNext()) {
            node = (ARNode)var5.next();
            if(node instanceof ARMeshNode) {
                ARMeshNode meshNode = (ARMeshNode)node;
                if(meshNode.getMaterial() != null) {
                    if(meshNode.getMaterial().getDepthOnly()) {
                        occlusionNodes.add(meshNode);
                    } else if(!meshNode.getMaterial().getTransparent()) {
                        opaqueNodes.add(meshNode);
                    } else if(meshNode.getMaterial().getTransparent()) {
                        translucentNodes.add(meshNode);
                    }
                }
            }
        }

        var5 = occlusionNodes.iterator();

        while(var5.hasNext()) {
            node = (ARNode)var5.next();
            node.render();
        }

        var5 = opaqueNodes.iterator();

        while(var5.hasNext()) {
            node = (ARNode)var5.next();
            node.render();
        }

        Collections.sort(translucentNodes, new Comparator<ARNode>() {
            public int compare(ARNode a, ARNode b) {
                Vector3f centre = new Vector3f(0.0F, 0.0F, 0.0F);
                Vector3f centreA = a.getFullTransform().mult(centre);
                Vector3f centreB = b.getFullTransform().mult(centre);
                float distanceA = centre.distance(centreA);
                float distanceB = centre.distance(centreB);
                return distanceA > distanceB?-1:(distanceB > distanceA?1:0);
            }
        });
        GLES20.glDisable(2884);
        var5 = translucentNodes.iterator();

        while(var5.hasNext()) {
            node = (ARNode)var5.next();
            node.render();
        }

        GLES20.glEnable(2884);
    }

    public AssetManager getAssetManager() {
        if(this.mAssetManager == null) {
            this.mAssetManager = ARActivity.getContext().getAssets();
        }

        return this.mAssetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.mAssetManager = assetManager;
    }

    public void initialise() {
        this.mMediaPlayers = new ArrayList();
        this.mVertexBuffers = new ArrayList();
        this.mIndexBuffers = new ArrayList();
        this.mTextures = new ArrayList();
        this.mShaders = new ArrayList();
        this.mRenderTargets = new ArrayList();
        ARShaderManager shaderManager = ARShaderManager.getInstance();
        shaderManager.reset();
        this.mActivity = null;
        this.mTouchCoords = null;
        this.mListeners = new ArrayList();
        this.mPreRenderEvents = new ConcurrentLinkedQueue();
        this.mIsContextLoaded = false;
    }

    public void initialise(ARActivity mARActivity) {
        this.setAssetManager(mARActivity.getAssets());
        this.setActivity(mARActivity);
        this.setDataDir(mARActivity.getApplicationContext().getApplicationInfo().dataDir);
    }

    public void reset() {
        this.mMediaPlayers = new ArrayList();
        this.mVertexBuffers = new ArrayList();
        this.mIndexBuffers = new ArrayList();
        this.mTextures = new ArrayList();
        this.mShaders = new ArrayList();
        this.mRenderTargets = new ArrayList();
        ARShaderManager shaderManager = ARShaderManager.getInstance();
        shaderManager.reset();
        this.mActivity = null;
        this.mTouchCoords = null;
        this.mListeners = new ArrayList();
        this.mPreRenderEvents = new ConcurrentLinkedQueue();
        this.mIsContextLoaded = false;
        this.isInitialised = false;
    }

    public void makeActiveVideoTexture(ARVideoTexture videoTexture) {
        if(this.mActiveVideoTexture != null) {
            this.mActiveVideoTexture.spill();
        }

        this.mActiveVideoTexture = videoTexture;
        this.mActiveVideoTexture.open();
    }

    public void addMediaPlayer(MediaPlayer mediaPlayer) {
        this.mMediaPlayers.add(mediaPlayer);
    }

    public List<MediaPlayer> getMediaPlayers() {
        return this.mMediaPlayers;
    }

    public List<ARVertexBuffer> getVertexBuffers() {
        return this.mVertexBuffers;
    }

    public List<ARIndexBuffer> getIndexBuffers() {
        return this.mIndexBuffers;
    }

    public List<ARTexture> getTextures() {
        return this.mTextures;
    }

    public List<ARShaderProgram> getShaders() {
        return this.mShaders;
    }

    public void addVertexBuffer(final ARVertexBuffer vertexBuffer) {
        this.mVertexBuffers.add(vertexBuffer);
        if(this.mIsContextLoaded) {
            this.queuePreRenderEvent(new Runnable() {
                public void run() {
                    vertexBuffer.loadData();
                }
            });
        }

    }

    public void addIndexBuffer(final ARIndexBuffer indexBuffer) {
        this.mIndexBuffers.add(indexBuffer);
        if(this.mIsContextLoaded) {
            this.queuePreRenderEvent(new Runnable() {
                public void run() {
                    indexBuffer.loadData();
                }
            });
        }

    }

    public void addTexture(final ARTexture texture) {
        this.mTextures.add(texture);
        if(this.mIsContextLoaded) {
            this.queuePreRenderEvent(new Runnable() {
                public void run() {
                    texture.loadData();
                }
            });
        }

    }

    public void addShader(final ARShaderProgram shader) {
        this.mShaders.add(shader);
        if(this.mIsContextLoaded) {
            this.queuePreRenderEvent(new Runnable() {
                public void run() {
                    shader.compileShaders();
                }
            });
        }

    }

    public void queuePreRenderEvent(Runnable runnable) {
        this.mPreRenderEvents.add(runnable);
    }

    public void loadContext() {
        Iterator var1 = this.mIndexBuffers.iterator();

        while(var1.hasNext()) {
            ARIndexBuffer indexBuffer = (ARIndexBuffer)var1.next();
            indexBuffer.loadData();
        }

        var1 = this.mVertexBuffers.iterator();

        while(var1.hasNext()) {
            ARVertexBuffer vertexBuffer = (ARVertexBuffer)var1.next();
            vertexBuffer.loadData();
        }

        var1 = this.mTextures.iterator();

        while(var1.hasNext()) {
            ARTexture texture = (ARTexture)var1.next();
            texture.loadData();
        }

        var1 = this.mShaders.iterator();

        while(var1.hasNext()) {
            ARShaderProgram shader = (ARShaderProgram)var1.next();
            shader.compileShaders();
        }

        var1 = this.mRenderTargets.iterator();

        while(var1.hasNext()) {
            ARRenderTarget renderTarget = (ARRenderTarget)var1.next();
            renderTarget.create();
        }

        this.mIsContextLoaded = true;
    }

    public void setupCameraFBO() {
        int[] n = new int[1];
        GLES20.glGenFramebuffers(1, n, 0);
        int fboID = n[0];
        this.mCameraFBO = fboID;
        GLES20.glBindFramebuffer('赀', fboID);
        GLES20.glGenTextures(1, n, 0);
        int textureID = n[0];
        GLES20.glBindTexture(3553, textureID);
        Log.i("CAPTURE", "texture ID: " + textureID);
        GLES20.glTexParameterf(3553, 10241, 9729.0F);
        GLES20.glTexParameterf(3553, 10240, 9729.0F);
        GLES20.glTexParameterf(3553, 10243, 33071.0F);
        GLES20.glTexParameterf(3553, 10242, 33071.0F);
        GLES20.glTexImage2D(3553, 0, 6408, (int)this.cameraFBOResolution.getX(), (int)this.cameraFBOResolution.getY(), 0, 6408, 5121, (Buffer)null);
        GLES20.glFramebufferTexture2D('赀', '賠', 3553, textureID, 0);
    }

    public void setupCaptureFBO() {
        int[] n = new int[1];
        GLES20.glGenFramebuffers(1, n, 0);
        int fboID = n[0];
        this.mCaptureFBO = fboID;
        GLES20.glBindFramebuffer('赀', fboID);
        GLES20.glGenTextures(1, n, 0);
        int textureID = n[0];
        GLES20.glBindTexture(3553, textureID);
        GLES20.glTexParameterf(3553, 10241, 9729.0F);
        GLES20.glTexParameterf(3553, 10240, 9729.0F);
        GLES20.glTexParameterf(3553, 10243, 33071.0F);
        GLES20.glTexParameterf(3553, 10242, 33071.0F);
        GLES20.glTexImage2D(3553, 0, 6408, 1, 1, 0, 6408, 5121, (Buffer)null);
        GLES20.glFramebufferTexture2D('赀', '賠', 3553, textureID, 0);
        GLES20.glGenRenderbuffers(1, n, 0);
        int depthBuffer = n[0];
        GLES20.glBindRenderbuffer('赁', depthBuffer);
        GLES20.glRenderbufferStorage('赁', '膥', 1, 1);
        GLES20.glFramebufferRenderbuffer('赀', '贀', '赁', depthBuffer);
    }

    public void setTouchCoords(Point point) {
        Log.i("AR", "set touch coords");
        this.mTouchCoords = point;
    }

    public Vector3f getNextCaptureColour() {
        Vector3f nextColour = new Vector3f(this.mNextColour);
        float r = this.mNextColour.getX();
        float g = this.mNextColour.getY();
        float b = this.mNextColour.getZ();
        b += 8.0F;
        if(b >= 256.0F) {
            b = 0.0F;
            g += 8.0F;
        }

        if(g >= 256.0F) {
            g = 0.0F;
            r += 8.0F;
        }

        this.mNextColour.set(r, g, b);
        return nextColour;
    }

    public void enableVertexAttribute(int n) {
        if(!this.vertexAttributes[n]) {
            GLES20.glEnableVertexAttribArray(n);
            this.vertexAttributes[n] = true;
        }
    }

    public void disableVertexAttribute(int n) {
        if(this.vertexAttributes[n]) {
            GLES20.glDisableVertexAttribArray(n);
            this.vertexAttributes[n] = false;
        }
    }

    public void pause() {
        Iterator var1 = this.mListeners.iterator();

        while(var1.hasNext()) {
            ARRendererListener listener = (ARRendererListener)var1.next();
            listener.rendererDidPause();
        }

    }

    public synchronized void addListener(ARRendererListener listener) {
        this.mListeners.add(listener);
    }

    public synchronized void removeListener(ARRendererListener listener) {
        if(this.mListeners != null) {
            this.mListeners.remove(listener);
        }

    }

    public void resume() {
        Iterator var1 = this.mListeners.iterator();

        while(var1.hasNext()) {
            ARRendererListener listener = (ARRendererListener)var1.next();
            listener.rendererDidResume();
        }

    }
}
