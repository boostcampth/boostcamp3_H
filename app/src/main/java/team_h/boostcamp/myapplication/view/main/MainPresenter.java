package team_h.boostcamp.myapplication.view.main;

public class MainPresenter implements MainContractor.Presenter {

    private MainContractor.View view;

    public MainPresenter(MainContractor.View view) {
        this.view = view;
    }

    @Override
    public void onViewAttached() {

    }

    @Override
    public void onViewDetached() {

    }
}


