package teamh.boostcamp.myapplication.view.play;

import java.util.List;

import teamh.boostcamp.myapplication.data.model.LegacyDiary;
import teamh.boostcamp.myapplication.view.BasePresenter;

public interface PlayContractor {

    interface View{

    }

    interface Presenter extends BasePresenter {

        void loadData(int MemoryId);
        void playMemory();
        void stopMemory();

    }
}
