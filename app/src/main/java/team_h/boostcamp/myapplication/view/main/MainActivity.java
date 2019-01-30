package team_h.boostcamp.myapplication.view.main;

import android.os.Bundle;
import android.util.Log;

import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivityMainBinding;
import team_h.boostcamp.myapplication.view.BaseActivity;
import team_h.boostcamp.myapplication.view.diaryList.DiaryListFragment;
import team_h.boostcamp.myapplication.view.graph.GraphFragment;
import team_h.boostcamp.myapplication.view.memories.MemoriesFragment;



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
        if(mPresenter == null) {
            mPresenter = new MainPresenter(MainActivity.this);
        }
        return mPresenter;
    }

    private void initView() {
        Log.e(TAG, "initView");
        tabAdapter = new MainTabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new GraphFragment());
        tabAdapter.addFragment(new MemoriesFragment());
        tabAdapter.addFragment(new DiaryListFragment());
        binding.vpMain.setAdapter(tabAdapter);
        binding.vpMain.setOffscreenPageLimit(3);
    }
}
