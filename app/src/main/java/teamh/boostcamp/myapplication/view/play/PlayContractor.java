package teamh.boostcamp.myapplication.view.play;

import java.util.List;

import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.view.BasePresenter;

public interface PlayContractor {

    interface View{

        void setDiaryList(List<Diary> diaryList);
        void makeToast(String string);

    }

    interface Presenter extends BasePresenter {

        void loadData(int MemoryId);
        void playMemory();
        void stopMemory();

    }
}
