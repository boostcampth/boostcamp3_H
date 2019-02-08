package team_h.boostcamp.myapplication.view.play;

import java.util.List;

import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.view.BasePresenter;

public interface PlayContractor {

    interface View{

        void setDiaryList(List<Diary> diaryList);
        void makeToast(String string);

    }

    interface Presenter extends BasePresenter {

        void loadData(int MemoryId);
        void playDiaryList();
        void stopDiaryList();

    }
}
