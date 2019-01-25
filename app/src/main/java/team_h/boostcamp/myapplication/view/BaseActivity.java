package team_h.boostcamp.myapplication.view;

import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class BaseActivity<B extends ViewDataBinding, P extends BasePresenter> extends AppCompatActivity {

    protected B binding;
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 세로 화면 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // bindingUtil 설정
        binding = DataBindingUtil.setContentView(this, getLayoutId());

        // 사용할 Presenter 설정
        presenter = getPresenter();
    }

    public void showToastMessage(String message, int exposedTime) {
        Toast.makeText(this, message, exposedTime).show();
    }

    protected abstract int getLayoutId();
    protected abstract P getPresenter();
}
