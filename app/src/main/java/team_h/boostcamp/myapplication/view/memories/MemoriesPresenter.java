package team_h.boostcamp.myapplication.view.memories;

import team_h.boostcamp.myapplication.view.main.MainContractor;

public class MemoriesPresenter implements MemoriesContractor.Presenter {

    private MemoriesContractor.View view;

    public MemoriesPresenter(MemoriesContractor.View view) {
        this.view = view;
    }

    @Override
    public void onViewAttached() {

    }

    @Override
    public void onViewDetached() {

    }
}
