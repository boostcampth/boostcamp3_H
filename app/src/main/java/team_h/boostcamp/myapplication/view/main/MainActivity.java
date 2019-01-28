package team_h.boostcamp.myapplication.view.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;

import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivityMainBinding;
import team_h.boostcamp.myapplication.view.BaseActivity;
import team_h.boostcamp.myapplication.view.graph.GraphFragment;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainPresenter> implements MainContractor.View {

    private static final String TAG = MainActivity.class.getSimpleName();

    //private DairyFragment dairyFragment;
    private MainTabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        Log.e(TAG, "initView");
        tabAdapter = new MainTabAdapter(getSupportFragmentManager());
        binding.vpMain.setAdapter(tabAdapter);
        binding.vpMain.setOffscreenPageLimit(3);
    }
}
