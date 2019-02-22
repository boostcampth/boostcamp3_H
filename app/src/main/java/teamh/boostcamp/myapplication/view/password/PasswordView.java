package teamh.boostcamp.myapplication.view.password;

import androidx.annotation.StringRes;

public interface PasswordView {

    void showToast(@StringRes final int id);

    void showPasswordErrorMessage();

    void clearView();

}
