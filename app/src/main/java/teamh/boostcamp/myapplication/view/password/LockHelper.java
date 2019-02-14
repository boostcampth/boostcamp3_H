package teamh.boostcamp.myapplication.view.password;

/*
 * 추상 클래스
 * */
public abstract class LockHelper {

    static final int ENABLE_PASSWORD = 0; // 비밀번호 설정 가능
    static final int DISABLE_PASSWORD = 1; // 비밀번호 해제 가능
    static final int CHANGE_PASSWORD = 2; // 비밀번호 변경 가능
    public static final int UNLOCK_PASSWORD = 3; // 비밀번호 풀기

    static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    public abstract boolean setPassword(String password);

    public abstract boolean checkPassword(String password);

    public abstract boolean isPasswordSet();
}
