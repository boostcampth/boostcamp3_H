package team_h.boostcamp.myapplication.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.stetho.Stetho;

import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivityMainBinding;
import team_h.boostcamp.myapplication.view.BaseActivity;
import team_h.boostcamp.myapplication.view.diarylist.DiaryListFragment;
import team_h.boostcamp.myapplication.view.graph.GraphFragment;
import team_h.boostcamp.myapplication.view.memories.MemoriesFragment;
import team_h.boostcamp.myapplication.view.setting.SettingActivity;


public class MainActivity extends BaseActivity<ActivityMainBinding> implements MainContractor.View {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainTabAdapter tabAdapter;
    private MainContractor.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        mPresenter = new MainPresenter(this);
      
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initView() {
        Log.e(TAG, "initView");
        tabAdapter = new MainTabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new GraphFragment());
        tabAdapter.addFragment(new MemoriesFragment());
        tabAdapter.addFragment(DiaryListFragment.newInstance());
        binding.vpMain.setAdapter(tabAdapter);
        binding.vpMain.setOffscreenPageLimit(3);
        binding.setActivity(MainActivity.this);
    }

    public void startSetting(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);

    }
}
