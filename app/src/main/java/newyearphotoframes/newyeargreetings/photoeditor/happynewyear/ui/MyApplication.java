package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui;

import android.app.Application;
import android.content.Context;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Constants;

public class MyApplication extends Application {

    static {
        System.loadLibrary("nativelibrary");
    }

    public static native String apiUrlFromJNI();

    public static native String authTokenFromJNI();


    public static Context context;

    public static Context getContext() {
        return getContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Constants.API_URL = apiUrlFromJNI();
        Constants.AUTH_TOKEN = authTokenFromJNI();
        Constants.IMAGE_BASE_URL = Constants.API_URL + "upload/";
    }


}
