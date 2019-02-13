package teamh.boostcamp.myapplication.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivityMainBinding;
import teamh.boostcamp.myapplication.view.diarylist.DiaryListFragment;
import teamh.boostcamp.myapplication.view.graph.StatisticsFragment;
import teamh.boostcamp.myapplication.view.password.LifecycleManageActivity;
import teamh.boostcamp.myapplication.view.password.LockManager;
import teamh.boostcamp.myapplication.view.recall.RecallFragment;
import teamh.boostcamp.myapplication.view.setting.SettingActivity;


public class MainActivity extends LifecycleManageActivity implements MainActivityView{

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainTabAdapter tabAdapter;
    private MainPresenter presenter;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainPresenter(this);
        LockManager.getInstance().enableLock(getApplication());


        // bindingUtil 설정
        binding = DataBindingUtil.setContentView(this, getLayoutId());

        initView();
    }

    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initView() {
        Log.e(TAG, "initView");
        tabAdapter = new MainTabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(RecallFragment.newInstance());
        tabAdapter.addFragment(DiaryListFragment.newInstance());
        tabAdapter.addFragment(StatisticsFragment.newInstance());
        binding.vpMain.setAdapter(tabAdapter);
        binding.vpMain.setOffscreenPageLimit(3);
        // 녹음 화면을 첫 화면으로 설정
        binding.vpMain.setCurrentItem(1);
        binding.setActivity(MainActivity.this);
        binding.vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.tvMainTitle.setText(getResources().getString(R.string.Memories));
                        break;
                    case 1:
                        binding.tvMainTitle.setText(getResources().getString(R.string.main_toolbar_diary_title));
                        break;
                    case 2:
                        binding.tvMainTitle.setText(getResources().getString(R.string.main_toolbar_graph_title));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // 상단 Toolbar 클릭 시 설정 화면으로 이동
    public void startSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_stop);

    }
}
