//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eu.kudan.kudan;

import android.util.Log;

public class ARAPIKey {
    private static ARAPIKey singletonInstance;
    private String mApiKey = "";
    private final String LOGTAG = "KudanAR Licensing";

    private native void verifyAPILicenseKeyN(String var1);

    private native boolean licenseKeyIsValidN();

    private native boolean isFreeN();

    public static ARAPIKey getInstance() {
        if(singletonInstance == null) {
            singletonInstance = new ARAPIKey();
        }

        return singletonInstance;
    }

    private ARAPIKey() {
    }

    public final void setAPIKey(String key) {
        if(key != null && this.mApiKey.isEmpty()) {
            this.mApiKey = key;
            this.checkKey();
            this.printValidityStatus();
        } else {
            Log.e("KudanAR Licensing", "Couldn't set API Key. Please ensure are you not passing in an empty string.");
        }

    }

    final boolean isFree() {
        return this.isFreeN();
    }

    private void printValidityStatus() {
        if(this.licenseKeyIsValidN()) {
            if(this.isFreeN()) {
                Log.i("KudanAR Licensing", "The API License Key is valid. A watermark will appear with this license key");
            } else {
                Log.i("KudanAR Licensing", "The API License Key is valid");
            }
        } else {
            Log.e("KudanAR Licensing", "The API License Key is NOT valid");
        }

    }

    public final boolean licenseKeyIsValid() {
        return this.licenseKeyIsValidN();
    }

    private void checkKey() {
        this.verifyAPILicenseKeyN(this.mApiKey);
    }

    static {
        System.loadLibrary("Kudan");
    }
}
