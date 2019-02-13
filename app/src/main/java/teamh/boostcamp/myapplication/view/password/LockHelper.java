package teamh.boostcamp.myapplication.view.password;

import java.util.HashSet;

/*
 * 추상 클래스
 * */
public abstract class LockHelper {

    static final int ENABLE_PASSWORD = 0; // 비밀번호 설정 가능
    static final int DISABLE_PASSWORD = 1; // 비밀번호 해제 가능
    static final int CHANGE_PASSWORD = 2; // 비밀번호 변경 가능
    static final int UNLOCK_PASSWORD = 3; // 비밀번호 풀기

    static final String EXTRA_MESSAGE = "message";
    static final String EXTRA_TYPE = "type";

    static final int DEFAULT_TIMEOUT = 0; // 2000ms

    public int lockTimeOut;
    public HashSet<String> ignoredActivities;

    LockHelper() {
        // 무시되는 액티비티들을 모아놓음.
        ignoredActivities = new HashSet<String>();
        lockTimeOut = DEFAULT_TIMEOUT;
    }

    public abstract void enable();

    public abstract boolean setPassword(String password);

    public abstract boolean checkPassword(String password);

    public abstract boolean isPasswordSet();
}
