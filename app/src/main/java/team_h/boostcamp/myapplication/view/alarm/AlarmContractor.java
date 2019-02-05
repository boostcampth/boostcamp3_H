package team_h.boostcamp.myapplication.view.alarm;

import team_h.boostcamp.myapplication.view.BasePresenter;
import team_h.boostcamp.myapplication.view.BaseView;

public interface AlarmContractor {

    interface View extends BaseView {
        void showToast(String message);
    }

    interface Presenter extends BasePresenter {

    }
}
