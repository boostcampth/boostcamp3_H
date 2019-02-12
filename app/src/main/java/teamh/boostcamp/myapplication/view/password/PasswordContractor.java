package teamh.boostcamp.myapplication.view.password;

import teamh.boostcamp.myapplication.view.BasePresenter;

public interface PasswordContractor {

    interface View {
        void showToast(String message);
    }

    interface Presenter extends BasePresenter {
        void comparePassword(String currentPassword, String changePassword, String checkPassword);
    }
}
