package teamh.boostcamp.myapplication.view.alarm;

import java.util.Calendar;

import androidx.annotation.NonNull;

public class AlarmPresenter {
    private AlarmView view;
    private AlarmHelper alarmHelper;

    AlarmPresenter(AlarmView view, @NonNull AlarmHelper alarmHelper) {
        this.view = view;
        this.alarmHelper = alarmHelper;
    }

    void setAlarm(Calendar calendar) {
        alarmHelper.setAlarm(calendar);
    }

    void cancelAlarm() {
        boolean isCanceled = alarmHelper.cancelAlarm();
        view.updateCancelTimeText(isCanceled);
    }

    void loadTimeText(Calendar calendar) {
        String userSettingTime = alarmHelper.loadCalendar(calendar);
        //view.updateTimeText(userSettingTime);
    }
}
