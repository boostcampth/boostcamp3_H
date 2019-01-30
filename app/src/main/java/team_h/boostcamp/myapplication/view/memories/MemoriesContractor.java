package team_h.boostcamp.myapplication.view.memories;

import team_h.boostcamp.myapplication.view.BasePresenter;
import team_h.boostcamp.myapplication.view.BaseView;

public interface MemoriesContractor {

    interface View extends BaseView {

        void makeToast(String string);

        void navigateToPlayActivity(String string);

    }

    interface Presenter extends BasePresenter {

        void onRecommendButtonClicked();

        void onDeleteButtonClicked();

        void onPlayButtonClicked();

        void setMemoriesCardAdapterView(MemoriesCardAdapter adapter);

        void setMemoriesCardAdapterModel(MemoriesCardAdapter adapter);

        void loadData();

    }
}
