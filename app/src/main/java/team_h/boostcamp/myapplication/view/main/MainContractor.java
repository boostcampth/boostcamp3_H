package team_h.boostcamp.myapplication.view.main;

import team_h.boostcamp.myapplication.view.BasePresenter;
import team_h.boostcamp.myapplication.view.BaseView;

/*
 * 관심사의 분리
 * View 에서 Presenter 에서 수행하는 로직이 필요하지 않다
 * 1 : 1 ? new Presenter 신경쓰기 */
public interface MainContractor {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {

    }
}
