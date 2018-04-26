package com.example.ryo2.jordicontacts.Applications;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ryo2 on 24/04/2018.
 */

public class ApplicationRealm extends Application {

    //AtomicInteger contactID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        //configuracion basica de realm
        setupRealmConfig();
        Realm realm = Realm.getDefaultInstance();
        realm.close();
    }
    private void setupRealmConfig(){
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}