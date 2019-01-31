package team_h.boostcamp.myapplication.view.memories;

import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.model.source.local.AppDatabase;
import team_h.boostcamp.myapplication.view.BasePresenter;
import team_h.boostcamp.myapplication.view.BaseView;

public interface MemoriesContractor {

    interface View extends BaseView {

        void makeToast(String string);

        void navigateToPlayActivity(Memory memory);

    }

    interface Presenter extends BasePresenter {

        void onRecommendMemory();

        void onDeleteButtonClicked(int position);

        void onPlayButtonClicked(int position);

        void setDatabase(AppDatabase appDatabase);

        void setMemoriesCardAdapterView(MemoriesCardAdapter adapter);

        void setMemoriesCardAdapterModel(MemoriesCardAdapter adapter);

        void loadData();

    }
}
