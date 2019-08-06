package com.freeoda.franktirkey.smfe;

import android.app.Application;

import com.backendless.Backendless;

public class ApplicationClass extends Application {
    public static final String APPLICATION_ID = "";
    public static final String API_KEY = "";
    public static final String SERVER_URL = "";

        public static final String DEFAULT_CHANNEL = "chat";

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }
}
