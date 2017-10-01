//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

public class ARActivity extends Activity {
    private static final String TAG_AR_FRAGMENT = "ar_fragment";
    private ARFragment mARFragment;
    private static Context mContext;

    public ARActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if(this.hasCameraPermission()) {
            FragmentManager fm = this.getFragmentManager();
            this.mARFragment = (ARFragment)fm.findFragmentByTag("ar_fragment");
            if(this.mARFragment == null) {
                this.mARFragment = new ARFragment();
                fm.beginTransaction().add(this.mARFragment, "ar_fragment").commit();
            }

            this.getWindow().addFlags(128);
        } else {
            System.out.println("No Camera Permission");
        }

    }

    public int getRotation() {
        Display display = ((WindowManager)this.getSystemService("window")).getDefaultDisplay();
        return display.getRotation();
    }

    public void setup() {
    }

    public static Context getContext() {
        return mContext;
    }

    public ARView getARView() {
        return this.mARFragment.getARView();
    }

    public boolean hasCameraPermission() {
        Context context = this.getApplicationContext();
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        int hasPermisson = packageManager.checkPermission("android.permission.CAMERA", context.getApplicationContext().getPackageName());
        return hasPermisson == 0;
    }
}
