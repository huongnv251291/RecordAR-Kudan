//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

public class ARFragment extends Fragment {
    private static final String LOGTAG = "ARFragment";
    private ARActivity mARActivity;
    private ARView mGlView;
    private Handler handler = null;
    private Runnable runnable = null;
    final int focusDelay = 3000;

    public ARFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        Log.d("ARFragment", "onCreate");
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    public void onAttach(Activity activity) {
        Log.d("ARFragment", "onAttach");
        super.onAttach(activity);
        ARRenderer renderer = ARRenderer.getInstance();
        this.mARActivity = (ARActivity)activity;
        renderer.initialise(this.mARActivity);
        this.initApplicationAR(this.mARActivity);
        ViewGroup rootLayout = (ViewGroup)activity.getWindow().getDecorView().getRootView();
        ViewGroup oldLayout = (ViewGroup)this.mGlView.getParent();
        oldLayout.removeView(this.mGlView);
        rootLayout.addView(this.mGlView, 0);
    }

    public void onResume() {
        Log.i("ARFragment", "onResume");
        super.onResume();
        ARGyroManager.getInstance().start();
        if(this.mGlView != null) {
            this.mGlView.setVisibility(0);
            this.mGlView.onResume();
        }

    }

    public void onPause() {
        Log.d("ARFragment", "onPause");
        super.onPause();
        ARGyroManager.getInstance().stop();
        if(this.handler != null) {
            this.handler.removeCallbacks(this.runnable);
            this.handler = null;
            this.runnable = null;
        }

        if(this.mGlView != null) {
            this.mGlView.setVisibility(4);
            this.mGlView.onPause();
        }

    }

    public void onDestroy() {
        Log.d("ARFragment", "onDestroy");
        super.onDestroy();
        ARArbiTrack.deinitialise();
        ARGyroPlaceManager.getInstance().deinitialise();
        ARGyroManager.getInstance().deinitialise();
        ARImageTracker.getInstance().deinitialise();
        ARRenderer renderer = ARRenderer.getInstance();
        renderer.reset();
        System.gc();
    }

    private void initApplicationAR(ARActivity activity) {
        this.mGlView = new ARView(activity);
        this.mGlView.setPreserveEGLContextOnPause(true);
        ViewGroup rootLayout = (ViewGroup)activity.getWindow().getDecorView().getRootView();
        rootLayout.addView(this.mGlView, 0);
    }

    public ARView getARView() {
        return this.mGlView;
    }

    static {
        System.loadLibrary("Kudan");
    }
}
