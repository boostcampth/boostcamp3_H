package teamh.boostcamp.myapplication.view.password;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PasswordPresenter {

    @NonNull
    private PasswordView passwordView;
    private LockHelper lockHelper;

    PasswordPresenter(@NonNull PasswordView passwordView, @NonNull Context context) {
        this.passwordView = passwordView;
        this.lockHelper = LockHelperImpl.getInstance(context.getApplicationContext());
    }

    boolean checkPassword(String password) {
        return lockHelper.checkPassword(password);
    }

    void savePassword(@Nullable String password) {
        lockHelper.savePassword(password);
    }

    void onDestroyView() {
        passwordView = null;
        lockHelper = null;
    }
}
