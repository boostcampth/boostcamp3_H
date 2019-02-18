package teamh.boostcamp.myapplication.view.setting;

public interface SettingView {
    void showInitializationMessage();
    void showLoginMessage();


    void showBackUpSuccessMsg();
    void showBackUpFailMsg();
    void showLoadSuccessMsg();
    void showLoadFailMsg();

    void showNotLoginMsg();

    void dismissDialog();
    void showDialog();
}
