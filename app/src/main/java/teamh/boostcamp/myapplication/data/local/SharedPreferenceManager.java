package teamh.boostcamp.myapplication.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SharedPreferenceManager {
    private static final String TEAM_H = "TEAM_H";
    private static final String PREF_PASSWORD = "PREF_PASSWORD";
    private static final String PREF_PUSH_TIME = "PREF_PUSH_TIME";
    private static final String PREF_LAST_SAVE_TIME = "PREF_LAST_SAVE_TIME";
    private static final String PREF_WORKER = "PREF_WORKER";

    // fields
    public static SharedPreferences preferences;

    // Constructor
    private SharedPreferenceManager() {

    }

    // getInstance()
    public static SharedPreferenceManager getInstance(@NonNull Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(TEAM_H, Context.MODE_PRIVATE);
        }
        return LazyHolder.INSTANCE;
    }

    public void setPreferencePassword(@NonNull String password) {
        SharedPreferences.Editor editor = preferences.edit();
        if (password == null) {
            editor.remove(PREF_PASSWORD);
            editor.apply();
        } else {
            editor.putString(PREF_PASSWORD, password);
            editor.apply();
        }
    }

    public String getPreferencePassword(String defaultPassword) {
        return preferences.getString(PREF_PASSWORD, defaultPassword);
    }

    public String getPreferencePassword() {
        return getPreferencePassword("");
    }

    public boolean checkPassword(@Nullable String inputPassword) {
        return inputPassword.equals(preferences.getString(PREF_PASSWORD, ""));
    }

    public void setPreferencePushTime(String pushTime) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_PUSH_TIME, pushTime);
        editor.apply();
    }

    public String getPreferencePushTime(String defaultPushTime) {
        return preferences.getString(PREF_PUSH_TIME, defaultPushTime);
    }

    public String getPreferencePushTime() {
        return getPreferencePushTime(null);
    }

    public void removePreferencePushTime() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(PREF_PUSH_TIME);
        editor.apply();
    }

    public void removeAllData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setLastDiarySaveTime(@NonNull Date lastSaveTime) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_LAST_SAVE_TIME,
                new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(lastSaveTime));
        editor.apply();
    }

    @NonNull
    public String getLastDiarySaveTime() {
        return preferences.getString(PREF_LAST_SAVE_TIME, "");
    }

    public void setWorkerState(boolean workerState) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_WORKER, workerState);
        editor.apply();
    }

    public boolean getWorkerState() {
        return preferences.getBoolean(PREF_WORKER, false);
    }

    // LazyHolder 클래스 - 싱글톤
    private static class LazyHolder {
        public static final SharedPreferenceManager INSTANCE = new SharedPreferenceManager();
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public boolean isPasswordSaved(){
        return preferences.contains(PREF_PASSWORD);
    }
}