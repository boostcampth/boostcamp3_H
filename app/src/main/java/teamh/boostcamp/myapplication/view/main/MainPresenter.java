package teamh.boostcamp.myapplication.view.main;

public class MainPresenter  {

    /* 관심사의 분리
     * View 가 Contract 를 구현한 녀석이면 누구나 상관없다. */
    private MainActivityView view;

    public MainPresenter(MainActivityView view) {
        this.view = view;
    }

}


