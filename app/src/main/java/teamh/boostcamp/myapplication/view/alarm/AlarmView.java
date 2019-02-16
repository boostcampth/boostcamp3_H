package teamh.boostcamp.myapplication.view.alarm;

import java.util.Calendar;

public interface AlarmView {

    void showToast(String message);

    void updateTimeText(String timeText);

    void checkState();

    void setVisibility(boolean isChecked);

    void updateCancelTimeText(boolean isCanceled);

    void updateCalendar(Calendar calendar);

}
