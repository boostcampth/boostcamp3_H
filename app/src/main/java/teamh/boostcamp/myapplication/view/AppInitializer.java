package teamh.boostcamp.myapplication.view;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;
import teamh.boostcamp.myapplication.view.password.LockHelper;
import teamh.boostcamp.myapplication.view.password.LockHelperImpl;
import teamh.boostcamp.myapplication.view.password.PasswordActivity;

public class AppInitializer extends Application {

    private static final String TAG = "AppInitializer";
    private LockHelper lockHelper;

    public enum ApplicationStatus {
        BACKGROUND,
        RETURNED_TO_FOREGROUND,
        FOREGROUND
    }

    private ApplicationStatus applicationStatus = ApplicationStatus.FOREGROUND;

    public boolean isReturnedForeground() {
        return applicationStatus.ordinal() == ApplicationStatus.RETURNED_TO_FOREGROUND.ordinal();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        lockHelper = LockHelperImpl.getInstance(getApplicationContext());
        //new LockHelperImpl(getApplicationContext());
        registerActivityLifecycleCallbacks(new ApplicationActivityLifecycleCallbacks());
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        Stetho.initializeWithDefaults(this);

        initWorker();
    }

    private void initWorker() {
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(getApplicationContext());
        if (!sharedPreferenceManager.getWorkerState()) {
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

        @Override
        public void onActivityStarted(Activity activity) {
            if (++runningActivityCount == 1) {
                applicationStatus = ApplicationStatus.RETURNED_TO_FOREGROUND;
            } else if (runningActivityCount > 1) {
                applicationStatus = ApplicationStatus.FOREGROUND;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (activity instanceof PasswordActivity) {
                Log.v(TAG, String.valueOf(((PasswordActivity) activity).getType()));
            } else if (activity instanceof SplashActivity) {

            } else if (isReturnedForeground()) {
                if (lockHelper.isPasswordSet()) {
                    startPasswordActivity(LockHelper.UNLOCK_PASSWORD);
                }
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (--runningActivityCount == 0) {
                applicationStatus = ApplicationStatus.BACKGROUND;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.MANUFACTURER.equals("samsung")) {
                    Object systemService = getSystemService(Class.forName("com.samsung.android.content.clipboard.SemClipboardManager"));
                    Field mContext = systemService.getClass().getDeclaredField("mContext");
                    mContext.setAccessible(true);
                    mContext.set(systemService, null);
                }
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                //ignored
                // }
            }
        }

        private void startPasswordActivity(int type) {
            Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
            intent.putExtra(LockHelper.EXTRA_TYPE, type);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
}
