package teamh.boostcamp.myapplication.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.stetho.Stetho;

import androidx.viewpager.widget.ViewPager;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivityMainBinding;
import teamh.boostcamp.myapplication.utils.ResourceSendUtil;
import teamh.boostcamp.myapplication.view.BaseActivity;
import teamh.boostcamp.myapplication.view.diarylist.LegacyDiaryListFragment;
import teamh.boostcamp.myapplication.view.graph.LegacyGraphFragment;
import teamh.boostcamp.myapplication.view.memories.MemoriesFragment;
import teamh.boostcamp.myapplication.view.setting.SettingActivity;


public class MainActivity extends BaseActivity<ActivityMainBinding> implements MainContractor.View {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainTabAdapter tabAdapter;
    private MainContractor.Presenter presenter;
    private ResourceSendUtil resourceSendUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        presenter = new MainPresenter(this);

        if(resourceSendUtil == null){
            resourceSendUtil = new ResourceSendUtil(MainActivity.this);
        }
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
        tabAdapter.addFragment(LegacyDiaryListFragment.newInstance());
        tabAdapter.addFragment(new LegacyGraphFragment());
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
                        binding.tvMainTitle.setText(resourceSendUtil.getString(R.string.Memories));
                        break;
                    case 1:
                        binding.tvMainTitle.setText(resourceSendUtil.getString(R.string.main_toolbar_diary_title));
                        break;
                    case 2:
                        binding.tvMainTitle.setText(resourceSendUtil.getString(R.string.main_toolbar_graph_title));
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
