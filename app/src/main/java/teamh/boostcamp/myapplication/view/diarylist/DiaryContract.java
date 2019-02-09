package teamh.boostcamp.myapplication.view.diarylist;

import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;

public interface DiaryContract {
    interface View {

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

        /* 저장 성공 + 알려주면 ADAPTER 에서 가져오기 + 저장 VIEW 없애기 */
        void showDiaryItemSaved();

        /* 저장 실패 */
        void showDiaryItemSaveFail();

        /* 불러온 아이템 넘겨주기 */
        void showMoreDiaryItems(@NonNull List<LegacyDiary> diaryList);

        /* 시간 초과 메시지 출력 */
        void showTimeOutMessage();

        /* Tag 초기화*/
        void clearTagEditText();
    }

    interface Presenter {
        /* 녹음 기능 */
        void recordDiaryItem();

        /* 아이템 저장 */
        void saveDiaryItem(@NonNull final String tags);

        /* View 가 없어질 때 */
        void onViewDetached();

        /* View 가 붙을 때*/
        void onViewAttached();

        /* 아이템 불러오기 */
        void loadMoreDiaryItems();

        /* 감정 선택 */
        void setSelectedEmotion(int emotion);
    }
}
