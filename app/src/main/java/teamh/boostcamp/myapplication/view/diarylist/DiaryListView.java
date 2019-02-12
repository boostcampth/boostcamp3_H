package teamh.boostcamp.myapplication.view.diarylist;

import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Emotion;

public interface DiaryListView {

    void addDiaryList(@NonNull List<Diary> diaryList);

    void showLoadDiaryListFailMsg();

    void showSaveDiaryFail();

    void notifyTodayDiarySaved();

    void showRecordTimeOutMsg();

    void showRecordNotFinished();

    void showEmotionNotSelected();

    void showRecordFileNotFound();
}
