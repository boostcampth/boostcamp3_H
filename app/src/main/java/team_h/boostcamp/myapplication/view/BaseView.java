package team_h.boostcamp.myapplication.view;

/*
 * Created by Jongseong */
public interface BaseView<P extends BasePresenter> {

    /* View(Activity,Fragment) 에서 가져야할 공통 기능 */
    P generatePresenter();
}
