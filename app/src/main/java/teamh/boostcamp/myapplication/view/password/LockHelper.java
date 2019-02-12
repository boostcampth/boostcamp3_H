package teamh.boostcamp.myapplication.view.password;

import java.util.HashSet;

/*
 * 추상 클래스
 * */
public abstract class LockHelper {

    public static final int ENABLE_PASSWORD = 0; // 비밀번호 설정 가능
    public static final int DISABLE_PASSWORD = 1; // 비밀번호 해제 가능
    public static final int CHANGE_PASSWORD = 2; // 비밀번호 변경 가능
    public static final int UNLOCK_PASSWORD = 3; // 비밀번호 풀기

    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    public static final int DEFAULT_TIMEOUT = 0; // 2000ms

    protected int lockTimeOut;
    protected HashSet<String> ignoredActivities;

    public LockHelper() {
        // 무시되는 액티비티들을 모아놓음.
        ignoredActivities = new HashSet<String>();
        lockTimeOut = DEFAULT_TIMEOUT;
    }

    public abstract void enable();

    public abstract void disable();

    public abstract boolean setPassword(String password);

    public abstract boolean checkPassword(String password);

    public abstract boolean isPasswordSet();
}
