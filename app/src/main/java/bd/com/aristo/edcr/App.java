package bd.com.aristo.edcr;

import android.app.Application;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import bd.com.aristo.edcr.dependency.AppComponent;
import bd.com.aristo.edcr.dependency.DaggerAppComponent;
import bd.com.aristo.edcr.networking.RequestServices;
import io.realm.Realm;

/**
 * Created by Tariqul.Islam on 5/17/17.
 */

public class App extends Application {

    private static AppComponent appComponent;

    private static App mInstance;

    private RequestServices requestServices = new RequestServices();


    public static synchronized App getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        appComponent = DaggerAppComponent.Initializer.init(this, requestServices);
        //initialize Realm
        Realm.init(this);
    }

    public static AppComponent getComponent(){
        return appComponent;
    }


    public App getActivity(){
        return this;
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    public void logUser(String id, String name) {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        FirebaseCrashlytics.getInstance().setUserId(id);
        FirebaseCrashlytics.getInstance().setCustomKey("str_key", name);
    }

}
