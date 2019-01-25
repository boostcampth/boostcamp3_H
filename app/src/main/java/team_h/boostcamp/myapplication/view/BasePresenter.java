package team_h.boostcamp.myapplication.view;

/*
 * Created by Jongseong */
public interface BasePresenter {

    /*
     * View 가 생성되었을 때 호출 */
    void onViewAttached();

    /*
     * View 가 사라질 때 호출 */
    void onViewDetached();
}
