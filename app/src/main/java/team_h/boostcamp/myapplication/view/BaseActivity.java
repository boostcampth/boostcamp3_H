package team_h.boostcamp.myapplication.view;

import android.content.pm.ActivityInfo;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public abstract class BaseActivity<B extends ViewDataBinding, P extends BasePresenter> extends AppCompatActivity {

    /**
     * 제네릭 / 템플릿 키워드 검색
     *
     * */
    protected B binding;
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 세로 화면 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 굳이 안해도 되고 할 만한 액티비티에서 매니페스트 지정하는게 나음.

        // bindingUtil 설정
        binding = DataBindingUtil.setContentView(this, getLayoutId());

        // 사용할 Presenter 설정
        presenter = getPresenter();
    }

    // 공통으로 빼는 것 주의해야 한다.
    // 삭제해야 함.
    public void showToastMessage(String message, int exposedTime) {
        Toast.makeText(this, message, exposedTime).show();
    }

    protected abstract int getLayoutId();
    protected abstract P getPresenter();
}
