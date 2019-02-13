package teamh.boostcamp.myapplication.view.password;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/*
* LifecycleManageActivity를 상속받은 Activity는
* 이 ManageActivity에 의해서 생명주기를 관리 받는 형식.
* */
public class LifecycleManageActivity extends AppCompatActivity {

    @NonNull
    private static LifecycleListener lifeCycleListener;

    public static void setLifeCycleListener(@NonNull LifecycleListener listener) {
        lifeCycleListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (lifeCycleListener != null) {
            lifeCycleListener.onActivityCreated(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (lifeCycleListener != null) {
            lifeCycleListener.onActivityStarted(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (lifeCycleListener != null) {
            lifeCycleListener.onActivityResumed(this);
        }
    }

    /*@Override
    protected void onPause() {
        super.onPause();

        if (lifeCycleListener != null) {
            lifeCycleListener.onActivityPaused(this);
        }
    }*/

    @Override
    protected void onStop() {
        super.onStop();

        if (lifeCycleListener != null) {
            lifeCycleListener.onActivityStopped(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (lifeCycleListener != null) {
            lifeCycleListener.onActivityDestroyed(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (lifeCycleListener != null) {
            lifeCycleListener.onActivitySaveInstanceState(this);
        }
    }
}
