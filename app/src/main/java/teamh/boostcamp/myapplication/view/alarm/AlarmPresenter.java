package teamh.boostcamp.myapplication.view.alarm;

import java.util.Calendar;

import androidx.annotation.NonNull;

public class AlarmPresenter {

    private AlarmContractor.View view;
    private AlarmHelper alarmHelper;

    AlarmPresenter(AlarmContractor.View view, @NonNull AlarmHelper alarmHelper) {
        this.view = view;
        this.alarmHelper = alarmHelper;
    }

    void setAlarm(Calendar calendar) {
        alarmHelper.setAlarm(calendar);
    }

    void cancelAlarm() {
        String text = alarmHelper.cancelAlarm();
        view.updateTimeText(text);
    }

    void loadCalendar(Calendar calendar) {
        String timeText = alarmHelper.loadCalendar(calendar);
        view.updateTimeText(timeText);
    }
}
