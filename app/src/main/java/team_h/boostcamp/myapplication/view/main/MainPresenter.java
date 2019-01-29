package team_h.boostcamp.myapplication.view.main;

public class MainPresenter implements MainContractor.Presenter {

    // activity든 fragment든 상관없이 View만 들고 있으면 다른 로직 처리 가능.
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


/**
 * 인터페이스로 뺀 것은 관심사의 분리와 연결됨.
 * Presenter는 안드로이드 의존성이 없게끔 해야함.
 * 테스트는 안드로이드 / 유닛 테스트
 * 안드로이드 - 컴퓨터
 * 유닛 테스트 - 폰
 * 안드로이드 유닛 테스트 - 안드로이드로 기능 테스트 가능
 * TDD : Test 가능한 코드가 나와야함.
 * 인터페이스 뽑는 이유 잘 알아야함.
 * Presenter는 View에게 데이터를 어떻게 넘겨 줄 지만 알면됨. 어떻게 그려지는지는 상관 없음.
 * */
