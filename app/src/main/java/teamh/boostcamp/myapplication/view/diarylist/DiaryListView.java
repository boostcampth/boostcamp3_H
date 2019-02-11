package teamh.boostcamp.myapplication.view.diarylist;

import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.model.Diary;

public interface DiaryListView {

    void addDiaryList(@NonNull List<Diary> diaryList);

    void showLoadDiaryListFailMsg();

    void notifyTodayDiarySaved();
}
