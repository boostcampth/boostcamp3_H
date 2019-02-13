package teamh.boostcamp.myapplication.view.alarm;

public interface AlarmView {

    void showToast(String message);

    void updateTimeText(String timeText);

    void checkState();

    void setVisibility(boolean isChecked);

    void updateCancelTimeText(boolean isCanceled);

}
