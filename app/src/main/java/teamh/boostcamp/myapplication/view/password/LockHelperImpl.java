package teamh.boostcamp.myapplication.view.password;

import android.content.Context;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;

/*
 * LockHelper 구현체
 * 패스워드 관련 로직 구현체
 * */
public class LockHelperImpl extends LockHelper {

    @NonNull
    private SharedPreferenceManager sharedPreferenceManager;

    public LockHelperImpl(@NonNull Context context) {
        this.sharedPreferenceManager = SharedPreferenceManager.getInstance(context.getApplicationContext());
    }

    @Override
    public void savePassword(@NonNull String password) {
        sharedPreferenceManager.setPreferencePassword(password);
    }

    @Override
    public boolean checkPassword(@NonNull String password) {
        return sharedPreferenceManager.checkPassword(password);
    }

    @Override
    public boolean isPasswordSet() {
        return sharedPreferenceManager.isPasswordSaved();
    }

}
