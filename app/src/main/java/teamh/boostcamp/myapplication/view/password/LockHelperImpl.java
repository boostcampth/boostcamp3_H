package teamh.boostcamp.myapplication.view.password;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

/*
 * LockHelper 구현체
 * */
public class LockHelperImpl extends LockHelper implements LifecycleListener {

    private static final String TAG = "LockHelperImpl";
    private static final String TEAM_H = "TEAM_H";
    private static final String PASSWORD_KEY = "password";

    @NonNull
    private SharedPreferences sharedPreferences;

    private int liveCount;
    private int visibleCount;
    private long lastActive;

    LockHelperImpl(Application application) {
        this.sharedPreferences = application.getApplicationContext()
                .getSharedPreferences(TEAM_H, Context.MODE_PRIVATE);
    }

    @Override
    public void enable() {
        LifecycleManageActivity.setLifeCycleListener(this);
    }

    // 비밀번호 저장.
    @Override
    public boolean setPassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (password == null) {
            editor.remove(PASSWORD_KEY);
            editor.apply();
        } else {
            // 비밀번호 저장
            Log.v(PASSWORD_KEY, "비밀번호 저장");
            editor.putString(PASSWORD_KEY, password);
            editor.apply();
        }

        return true;
    }

    // input과 저장된 비밀번호가 같은지 확인
    @Override
    public boolean checkPassword(String password) {
        // 저장된 비밀번호 가지고오기
        String storedPassword = "";
        if (sharedPreferences.contains(PASSWORD_KEY)) {
            storedPassword = sharedPreferences.getString(PASSWORD_KEY, "");

        }

        // 저장된 값과 인풋을 비교
        if (password.equals(storedPassword)) {
            return true;
        } else {
            return false;
        }
    }

    // 저장된 비밀번호 존재하는지 확인.
    @Override
    public boolean isPasswordSet() {
        if (sharedPreferences.contains(PASSWORD_KEY)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isIgnoreActivity(@NonNull Activity activity) {
        String className = activity.getClass().getSimpleName();

        if (ignoredActivities.contains(className)) {
            Log.v(TAG, "ignore Activity" + className);
            return true;
        }

        return false;

    }

    @Override
    public void onActivityCreated(Activity activity) {
        String className = activity.getClass().getSimpleName();
        Log.v(TAG, "onActivityCreated");

        if (isIgnoreActivity(activity)) {
            return;
        }

        liveCount++;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        String className = activity.getClass().getSimpleName();
        Log.v(TAG, "onActivityStarted");

        if (isIgnoreActivity(activity)) {
            return;
        }

        visibleCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        String className = activity.getClass().getSimpleName();
        Log.v(TAG, "onActivityResumed");

        if (isIgnoreActivity(activity)) {
            return;
        }

        // 비밀번호 입력 화면을 다시 띄운다.
        // back으로 빠졌다가 다시 앱으로 돌아올 때 onResume이 호출되기 때문
        if (setLockScreen(activity)) {
            Intent intent = new Intent(activity.getApplicationContext(), PasswordActivity.class);
            intent.putExtra(LockHelper.EXTRA_TYPE, LockHelper.UNLOCK_PASSWORD);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.getApplication().startActivity(intent);
        }

        // 시간 0으로 초기화
        lastActive = 0;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        String className = activity.getClass().getSimpleName();
        Log.v(TAG, "onActivityPaused");

        if (isIgnoreActivity(activity)) {
            return;
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        String className = activity.getClass().getSimpleName();
        Log.v(TAG, "onActivityStopped");

        if (isIgnoreActivity(activity)) {
            return;
        }

        // onStop 될 때마다
        // 화면에 보이는 액티비티는 사라지기 때문에
        // visibleCount 감소
        visibleCount--;
        if (visibleCount == 0) {
            lastActive = System.currentTimeMillis();
            Log.v(TAG, "set last active " + lastActive);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity) {
        String className = activity.getClass().getSimpleName();
        Log.d(TAG, "onActivitySaveInstanceState " + className);
        if (isIgnoreActivity(activity)) {
            return;
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        String className = activity.getClass().getSimpleName();
        Log.d(TAG, "onActivityDestroyed " + className);

        if (isIgnoreActivity(activity)) {
            return;
        }

        liveCount--;
        if (liveCount == 0) {
            lastActive = System.currentTimeMillis();
            Log.d(TAG, "set last active " + lastActive);
        }
    }

    private boolean setLockScreen(Activity activity) {

        // already unlock(비밀번호 입력하고 열림)
        if (activity instanceof PasswordActivity) {
            PasswordActivity passwordActivity = (PasswordActivity) activity;
            if (passwordActivity.getType() == LockHelper.UNLOCK_PASSWORD) {
                Log.d(TAG, "already unlock activity");
                return false;
            }
        }

        // no pass code set
        if (!isPasswordSet()) {
            Log.d(TAG, "lock passcode not set.");
            return false;
        }

        // no enough timeout
        long passedTime = System.currentTimeMillis() - lastActive;
        if (lastActive > 0 && passedTime <= lockTimeOut) {
            Log.d(TAG, "no enough timeout " + passedTime + " for "
                    + lockTimeOut);
            return false;
        }

        // start more than one page
        if (visibleCount > 1) {
            return false;
        }

        return true;
    }
}
