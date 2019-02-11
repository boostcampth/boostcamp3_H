package teamh.boostcamp.myapplication.view;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

public class AppInitializer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        Stetho.initializeWithDefaults(this);
    }
}
