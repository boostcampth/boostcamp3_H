package team_h.boostcamp.myapplication.view.password;

public class PasswordPresenter implements PasswordContractor.Presenter {

    PasswordContractor.View view;

    PasswordPresenter(PasswordContractor.View view) {
        this.view = view;
    }

    @Override
    public void onViewAttached() {

    }

    @Override
    public void onViewDetached() {
        view = null;
    }

    @Override
    public void comparePassword(String currentPassword, String changePassword, String checkPassword) {

    }
}
