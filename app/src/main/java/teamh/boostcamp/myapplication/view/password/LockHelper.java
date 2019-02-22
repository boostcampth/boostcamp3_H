package teamh.boostcamp.myapplication.view.password;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
 * 추상 클래스
 * */
public abstract class LockHelper {

    public static final int ENABLE_PASSWORD = 0; // 비밀번호 설정 가능
    public static final int DISABLE_PASSWORD = 1; // 비밀번호 해제 가능
    public static final int CHANGE_PASSWORD = 2; // 비밀번호 변경 가능
    public static final int UNLOCK_PASSWORD = 3; // 비밀번호 풀기
    public static final int SPLASH_PASSWORD = 4; // Splash 화면에서 분기 처리를 위한 상수

    static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    public abstract void savePassword(@Nullable String password);

    public abstract boolean checkPassword(@NonNull String password);

    public abstract boolean isPasswordSet();

    public abstract void freeSharedPreferenceManager();
}
