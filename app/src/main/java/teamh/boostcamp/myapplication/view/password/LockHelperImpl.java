package teamh.boostcamp.myapplication.view.password;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

/*
 * LockHelper 구현체
 * */
public class LockHelperImpl extends LockHelper {

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

    }

    @Override
    public void disable() {

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
}
