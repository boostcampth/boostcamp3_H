package team_h.boostcamp.myapplication.view.main;

/*
 * 안드로이드에 의존성이 최대한 없게 작성하기
 * 테스트 가능한 코드를 작성 -> 저절로 관심사의 분리가 생성된다.
 * 인터페이스를 뽑는 이유를 이해해야함
 * Context API 가 필요하다. */
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
