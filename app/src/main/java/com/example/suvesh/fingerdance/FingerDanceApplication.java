package com.example.suvesh.fingerdance;

import android.app.Application;
import com.example.suvesh.fingerdance.utils.TypeFaceUtil;
/**
 * Created by Suvesh on 21/02/2017 AD.
 */

public class FingerDanceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Font for application
        TypeFaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Montserrat-Light.ttf");
        TypeFaceUtil.overrideFont(getApplicationContext(), "MONOSPACE", "fonts/Montserrat-Light.ttf");
    }
}
