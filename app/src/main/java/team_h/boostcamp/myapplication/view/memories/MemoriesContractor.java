package team_h.boostcamp.myapplication.view.memories;

import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.view.BasePresenter;
import team_h.boostcamp.myapplication.view.BaseView;

public interface MemoriesContractor {

    interface View extends BaseView {

        void makeToast(String string);

        void navigateToPlayActivity(Memory memory);

    }

    interface Presenter extends BasePresenter {

        void onRecommendButtonClicked(android.view.View v);

        void onDeleteButtonClicked(int position);

        void onPlayButtonClicked(int position);

        void setMemoriesCardAdapterView(MemoriesCardAdapter adapter);

        void setMemoriesCardAdapterModel(MemoriesCardAdapter adapter);

        void loadData();

    }
}
