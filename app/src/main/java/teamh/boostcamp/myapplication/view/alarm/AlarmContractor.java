package teamh.boostcamp.myapplication.view.alarm;

import java.util.Calendar;

import teamh.boostcamp.myapplication.view.BasePresenter;
import teamh.boostcamp.myapplication.view.BaseView;

public interface AlarmContractor {

    interface View extends BaseView {
        void showToast(String message);

        void updateTimeText(String timeText);

        void checkState();

        void setVisibility(boolean isChecked);
    }

    interface Presenter extends BasePresenter {

        void setAlarm(Calendar calendar);

        void cancelAlarm();

        void loadCalendar(Calendar calendar);
    }
}
