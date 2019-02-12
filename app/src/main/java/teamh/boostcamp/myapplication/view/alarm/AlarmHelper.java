package teamh.boostcamp.myapplication.view.alarm;

import java.util.Calendar;

import androidx.annotation.NonNull;

public interface AlarmHelper {

    @NonNull
    String loadCalendar(@NonNull Calendar calendar);

    void setAlarm(@NonNull Calendar calendar);

    @NonNull
    boolean cancelAlarm();


}
