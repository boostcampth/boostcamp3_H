package teamh.boostcamp.myapplication.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivityMainBinding;
import teamh.boostcamp.myapplication.view.AppInitializer;
import teamh.boostcamp.myapplication.view.diarylist.DiaryListFragment;
import teamh.boostcamp.myapplication.view.graph.StatisticsFragment;
import teamh.boostcamp.myapplication.view.password.LockHelper;
import teamh.boostcamp.myapplication.view.password.LockManager;
import teamh.boostcamp.myapplication.view.password.PasswordActivity;
import teamh.boostcamp.myapplication.view.recall.RecallFragment;
import teamh.boostcamp.myapplication.view.setting.SettingActivity;


public class MainActivity extends AppCompatActivity implements MainActivityView {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainPresenter presenter;
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private RecallFragment recallFragment;
    private DiaryListFragment diaryListFragment;
    private StatisticsFragment statisticsFragment;
    private AppInitializer application;
    private LockManager lockManager;
    private LockHelper lockHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recall:
                    changeFragment(recallFragment, getString(R.string.main_toolbar_recall_title));
                    return true;
                case R.id.navigation_diary:
                    changeFragment(diaryListFragment, getString(R.string.main_toolbar_diary_title));
                    return true;
                case R.id.navigation_statistics:
                    changeFragment(statisticsFragment, getString(R.string.main_toolbar_statistics_title));
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // bindingUtil 설정
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        binding.setActivity(this);

        application = new AppInitializer();
        presenter = new MainPresenter(this);

        // 아래 초기화 하지 않으면 에러 발생
        lockManager = LockManager.getInstance();
        lockHelper = lockManager.getLockHelper(getApplicationContext());
        //lockManager.enableLock(getApplication());

        recallFragment = RecallFragment.newInstance();
        diaryListFragment = DiaryListFragment.newInstance();
        statisticsFragment = StatisticsFragment.newInstance();

        initBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (application.getAppInitializer(this.getApplicationContext()).getApplicationStatus()
                == AppInitializer.ApplicationStatus.RETURNED_TO_FOREGROUND) {
            if (lockHelper.isPasswordSet()) {
                Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
                intent.putExtra(LockHelper.EXTRA_TYPE, LockHelper.UNLOCK_PASSWORD);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Log.v(TAG, getApplicationContext().getResources().getString(R.string.password_not_set_text));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statisticsFragment = null;
        recallFragment = null;
        diaryListFragment = null;
    }

    private void initBottomNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_diary);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, diaryListFragment).commitAllowingStateLoss();
    }

    private void changeFragment(Fragment fragment, String title) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment).commit();
        binding.tvMainTitle.setText(title);
    }

    public void startSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_stop);
    }
}
