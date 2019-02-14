package teamh.boostcamp.myapplication.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;

public class AppInitializer extends Application {

    private static final String TAG = "AppInitializer";
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

        initWorker();

    }

    private void initWorker(){
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(getApplicationContext());
        if(!sharedPreferenceManager.getWorkerState()){
            PeriodicWorkRequest weekWorkRequest = new PeriodicWorkRequest.Builder(DataInsertWorker.class, 7, TimeUnit.DAYS).build();
            WorkManager.getInstance().enqueue(weekWorkRequest);
            sharedPreferenceManager.setWorkerState(true);
        }
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
