package team_h.boostcamp.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    // Constants
    private static final String TEAM_H = "TEAM_H";

    private static final String PREF_PASSWORD = "PREF_PASSWORD";
    private static final String PREF_PUSH_TIME = "PREF_PUSH_TIME";

    // fields
    private SharedPreferences mPreferences;

    // Constructor
    private SharedPreference(){

    }

    // getInstance()
    public static SharedPreference getInstance(){
        return LazyHolder.INSTANCE;
    }

    public void loadSharedPreference(Context context){
        getPreference(context);
    }

    public void getPreference(Context context){
        if(mPreferences == null){
            mPreferences = context.getSharedPreferences(TEAM_H, Context.MODE_PRIVATE);
        }
    }

    public void setPreferencePassword(String password){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREF_PASSWORD,password);
        editor.commit();
    }

    public String getPreferencePassword(String defaultPassword){
        return mPreferences.getString(PREF_PASSWORD, defaultPassword);
    }

    public String getPreferencePassword(){
        return getPreferencePassword("");
    }

    public void setPreferencePushTime(int pushTime){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(PREF_PUSH_TIME, pushTime);
        editor.commit();
    }

    public int getPreferencePushTime(int defaultPushTime){
        return mPreferences.getInt(PREF_PUSH_TIME, defaultPushTime);
    }

    public int getPreferencePushTime(){
        return getPreferencePushTime(0);
    }

    public void removeAllData(){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.commit();
    }

    // LazyHolder 클래스 - 싱글톤
    private static class LazyHolder{
        public static final SharedPreference INSTANCE = new SharedPreference();
    }



}