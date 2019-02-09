package teamh.boostcamp.myapplication.view.memories;

import teamh.boostcamp.myapplication.data.local.room.LegacyAppDatabase;
import teamh.boostcamp.myapplication.data.model.Memory;
import teamh.boostcamp.myapplication.view.BasePresenter;
import teamh.boostcamp.myapplication.view.BaseView;

public interface MemoriesContractor {

    interface View extends BaseView {

        void makeToast(String string);

        void navigateToPlayActivity(Memory memory);

    }

    interface Presenter extends BasePresenter {

        void onRecommendMemory();

        void onDeleteButtonClicked(int position);

        void onPlayButtonClicked(int position);

        void setDatabase(LegacyAppDatabase appDatabase);

        void setMemoriesCardAdapterView(MemoriesCardAdapter adapter);

        void setMemoriesCardAdapterModel(MemoriesCardAdapter adapter);

        void loadData();

    }
}
