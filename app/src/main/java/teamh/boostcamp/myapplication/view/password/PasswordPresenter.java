package teamh.boostcamp.myapplication.view.password;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PasswordPresenter {

    @NonNull
    private PasswordView passwordView;
    private LockManager lockManager;
    private LockHelper lockHelper;

    PasswordPresenter(@NonNull PasswordView passwordView, @NonNull Context context) {
        this.passwordView = passwordView;
        this.lockManager = LockManager.getInstance();
        this.lockHelper = lockManager.getLockHelper(context);
    }

    boolean checkPassword(String password) {
        return lockHelper.checkPassword(password);
    }

    void savePassword(@Nullable String password) {
        lockHelper.savePassword(password);
    }

    void onDestroyView() {
        passwordView = null;
        lockManager = null;
        lockHelper = null;
    }
}
