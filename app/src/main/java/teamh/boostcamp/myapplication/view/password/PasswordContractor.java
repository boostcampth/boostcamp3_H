package teamh.boostcamp.myapplication.view.password;

public interface PasswordContractor {

    interface View {
        void showToast(String message);
    }

    interface Presenter {
        void comparePassword(String currentPassword, String changePassword, String checkPassword);
    }
}
