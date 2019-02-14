package teamh.boostcamp.myapplication.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

public class AppInitializer extends Application {

    public enum ApplicationStatus {
        BACKGROUND,
        RETURNED_TO_FOREGROUND,
        FOREGROUND
    }

    private ApplicationStatus applicationStatus = ApplicationStatus.FOREGROUND;


    public AppInitializer getAppInitializer(Context context) {
        return (AppInitializer) context.getApplicationContext();
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ApplicationActivityLifecycleCallbacks());
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        Stetho.initializeWithDefaults(this);
    }


    public class ApplicationActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        private int runningActivityCount = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        /*FIXME
         *
         * */
        @Override
        public void onActivityStarted(Activity activity) {

            if (++runningActivityCount == 1) {
                Log.v("10476 onStarted", String.valueOf(runningActivityCount) + " " +activity.getClass().getSimpleName()+"\n");
                applicationStatus = ApplicationStatus.RETURNED_TO_FOREGROUND;
            } else if (runningActivityCount > 1) {
                Log.v("10477 onStarted", String.valueOf(runningActivityCount) + " "+activity.getClass().getSimpleName()+"\n");
                applicationStatus = ApplicationStatus.FOREGROUND;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {


        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (--runningActivityCount == 0) {
                Log.v("10478 onStopped", String.valueOf(runningActivityCount) + " " +activity.getClass().getSimpleName()+"\n");
                applicationStatus = ApplicationStatus.BACKGROUND;
            }

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
