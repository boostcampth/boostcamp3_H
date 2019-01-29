package team_h.boostcamp.myapplication.view.main;

import android.os.Bundle;
import android.util.Log;

import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivityMainBinding;
import team_h.boostcamp.myapplication.view.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements MainContractor.View {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainTabAdapter tabAdapter;
    private MainContractor.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = generatePresenter();

        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainContractor.Presenter generatePresenter() {
        /* 1 : 1 관계 유지 */
        if(mPresenter == null) {
            mPresenter = new MainPresenter(MainActivity.this);
        }
        return mPresenter;
    }

    private void initView() {
        Log.e(TAG, "initView");
        tabAdapter = new MainTabAdapter(getSupportFragmentManager());
        binding.vpMain.setAdapter(tabAdapter);
        binding.vpMain.setOffscreenPageLimit(3);
    }
}
