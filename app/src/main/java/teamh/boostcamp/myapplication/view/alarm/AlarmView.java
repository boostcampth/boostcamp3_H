package teamh.boostcamp.myapplication.view.alarm;

import java.util.Calendar;

import androidx.annotation.StringRes;

public interface AlarmView {

    void checkState(String time);

    void setActivate(boolean isChecked);

    void updateCancelTimeText(boolean isCanceled);

    void updateCalendar(Calendar calendar);

    void showAlarmSuccessMessage();

    void showToast(@StringRes final int strindId);

}
