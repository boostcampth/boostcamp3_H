package team_h.boostcamp.myapplication.view.diaryList;

import team_h.boostcamp.myapplication.view.BasePresenter;
import team_h.boostcamp.myapplication.view.BaseView;

/*
 * DiaryContract 튜터링 시간 내용 기억 */
public interface DiaryContract {
    interface View extends BaseView {
        /* 오류 발생시 메시지 출력 용도 */
        void showToastMessage(String message, int exposedTime);
    }

    interface Presenter extends BasePresenter {
        void onRecordButtonClicked();
        void onDoneButtonClicked();
        void setHashTagListModelAdapter(HashTagListAdapter adapter);
        void setHashTagListViewAdapter(HashTagListAdapter adapter);
    }
}
