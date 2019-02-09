package teamh.boostcamp.myapplication.view.password;

import teamh.boostcamp.myapplication.view.BasePresenter;
import teamh.boostcamp.myapplication.view.BaseView;

public interface PasswordContractor {

    interface View extends BaseView {
        void showToast(String message);
    }

    interface Presenter extends BasePresenter {
        void comparePassword(String currentPassword, String changePassword, String checkPassword);
    }
}
