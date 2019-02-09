package teamh.boostcamp.myapplication.view.main;

public class MainPresenter implements MainContractor.Presenter {

    /* 관심사의 분리
     * View 가 Contract 를 구현한 녀석이면 누구나 상관없다. */
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


