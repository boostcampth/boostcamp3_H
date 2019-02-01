package team_h.boostcamp.myapplication.view.diaryList;

import java.util.List;

import team_h.boostcamp.myapplication.view.BasePresenter;
import team_h.boostcamp.myapplication.view.BaseView;

public interface DiaryContract {
    interface View extends BaseView {

        /* 녹음된 일기가 없을 때 오류 메시지 출력*/
        void showNoRecordFileMessage();

        /* 감정 선택이 되지않았을 때 오류 메시지 출력 */
        void showEmotionNotSelectedMessage();

        /* 녹음중에 저장버튼 클릭시 오류 메시지 출력*/
        void showRecordNotFinishedMessage();

        /* 저장 버튼 클릭시 키패드 숨기기*/
        void closeHashTagKeyPad();

        /* 감정 분석 실패 */
        void showEmotionAnalyzeFailMessage();

        void showDiaryItemSaved();
    }

    interface Presenter extends BasePresenter {
        /* 녹음 기능 */
        void recordDiaryItem();

        /* 아이템 저장 */
        void saveDiaryItem(List<String>tags);

        /* View 가 없어질 때 */
        void onViewDetached();
    }
}
