package teamh.boostcamp.myapplication.view;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class LeakWatcher extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
