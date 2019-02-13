package teamh.boostcamp.myapplication.view.diarylist;

import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Emotion;

public interface DiaryListView {

    void addDiaryList(@NonNull List<Diary> diaryList);

    void insertDiaryList(@NonNull Diary diary);

    void showLoadDiaryListFailMsg();

    void showSaveDiaryFail();

    void showRecordTimeOutMsg();

    void showRecordNotFinished();

    void showEmotionNotSelected();

    void showRecordFileNotFound();

    void setIsSaving(boolean isSaving);

    void setRecordCardVisibilityGone();

    void onPlayFileChanged(final int lastPlayedIndex, final boolean isFinished);
}
