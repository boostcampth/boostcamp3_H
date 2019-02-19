package teamh.boostcamp.myapplication.view.setting;

import androidx.work.OneTimeWorkRequest;

public interface SettingView {
    void showInitializationMessage();
    void showLoginMessage();

    void showBackUpStartMsg();
    void showLoadStartMsg();
    void showNotLoginMsg();

    void startWorker(OneTimeWorkRequest request);
}
