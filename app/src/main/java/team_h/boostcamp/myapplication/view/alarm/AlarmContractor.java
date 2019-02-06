package team_h.boostcamp.myapplication.view.alarm;

import java.util.Calendar;

import team_h.boostcamp.myapplication.view.BasePresenter;
import team_h.boostcamp.myapplication.view.BaseView;

public interface AlarmContractor {

    interface View extends BaseView {
        void showToast(String message);

        void updateTimeText(String timeText);

        void checkState();

        void setVisibility(boolean isChecked);
    }

    interface Presenter extends BasePresenter {
        void setAlarm(Calendar calendar);

        void loadCalendar(Calendar calendar);
    }
}
