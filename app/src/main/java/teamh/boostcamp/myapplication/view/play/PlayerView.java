package teamh.boostcamp.myapplication.view.play;

import java.util.List;

import teamh.boostcamp.myapplication.data.model.LegacyDiary;

public interface PlayerView {

    void setDiaryList(List<LegacyDiary> diaryList);
    void makeToast(String string);
}
