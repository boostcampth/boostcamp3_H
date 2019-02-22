package teamh.boostcamp.myapplication.view.password;

import android.content.Context;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;

/*
 * LockHelper 구현체
 * 패스워드 관련 로직 구현체
 * */
public class LockHelperImpl extends LockHelper {

    private static LockHelperImpl INSTANCE;
    private SharedPreferenceManager sharedPreferenceManager;


    private LockHelperImpl(@NonNull Context context){
        this.sharedPreferenceManager = SharedPreferenceManager.getInstance(context.getApplicationContext());
    }

    public static LockHelperImpl getInstance(@NonNull Context context){
        if(INSTANCE == null){
            synchronized (LockHelperImpl.class){
                if(INSTANCE == null){
                    INSTANCE = new LockHelperImpl(context);
                }
            }
        }
        return INSTANCE;
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

    @Override
    public void freeSharedPreferenceManager() {
        sharedPreferenceManager = null;
    }


}
