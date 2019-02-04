package team_h.boostcamp.myapplication.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.stetho.Stetho;

import androidx.viewpager.widget.ViewPager;
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
    private MainContractor.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        presenter = new MainPresenter(this);

        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initView() {
        Log.e(TAG, "initView");
        tabAdapter = new MainTabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new MemoriesFragment());
        tabAdapter.addFragment(DiaryListFragment.newInstance());
        tabAdapter.addFragment(new GraphFragment());
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
                        binding.tvMainTitle.setText("Memories");
                        break;
                    case 1:
                        binding.tvMainTitle.setText("Todays");
                        break;
                    case 2:
                        binding.tvMainTitle.setText("Statics");
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
