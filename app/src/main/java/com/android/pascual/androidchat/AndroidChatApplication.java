package com.android.pascual.androidchat;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by pascual on 6/8/2016.
 */
public class AndroidChatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setupFirebase();
    }

    private void setupFirebase() {
        //pasarle el contexto
        Firebase.setAndroidContext(this);
        //configurar para que trabaje offline
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
