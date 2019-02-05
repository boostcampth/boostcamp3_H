package team_h.boostcamp.myapplication.view.play;

import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.model.source.local.AppDatabase;
import team_h.boostcamp.myapplication.view.BasePresenter;
import team_h.boostcamp.myapplication.view.BaseView;
import team_h.boostcamp.myapplication.view.memories.MemoriesCardAdapter;

public interface PlayContractor {

    interface View extends BaseView {

        void makeToast(String string);

    }

    interface Presenter extends BasePresenter {

    }
}
