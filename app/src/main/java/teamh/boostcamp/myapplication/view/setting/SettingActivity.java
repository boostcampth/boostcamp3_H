package teamh.boostcamp.myapplication.view.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivitySettingBinding;
import teamh.boostcamp.myapplication.view.AppInitializer;
import teamh.boostcamp.myapplication.view.alarm.AlarmActivity;
import teamh.boostcamp.myapplication.view.password.LockHelper;
import teamh.boostcamp.myapplication.view.password.LockManager;
import teamh.boostcamp.myapplication.view.password.PasswordActivity;
import teamh.boostcamp.myapplication.view.password.PasswordSelectActivity;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = SettingActivity.class.getClass().getSimpleName();
    ActivitySettingBinding binding;
    private AppInitializer appInitializer;
    private LockManager lockManager;
    private SettingPresenter settingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        initBinding();
        initLock();
    }

    private void initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setActivity(SettingActivity.this);
    }

    private void initLock(){
        appInitializer = new AppInitializer();
        lockManager = LockManager.getInstance();
        lockManager.enableLock(getApplication());
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_button:
                finish();
                overridePendingTransition(R.anim.anim_stop, R.anim.anim_slide_out_bottom);
                break;
            case R.id.rl_setting_alarm:
                startActivity(new Intent(SettingActivity.this, AlarmActivity.class));
                break;
            case R.id.rl_setting_password:
                startActivity(new Intent(SettingActivity.this, PasswordSelectActivity.class));
                break;
            case R.id.rl_setting_login:
                showToast("Login 준비 중");
                break;
            case R.id.rl_setting_logout:
                showToast("Logout 준비 중");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appInitializer.getAppInitializer(getApplicationContext()).getApplicationStatus()
                == AppInitializer.ApplicationStatus.RETURNED_TO_FOREGROUND) {
            if (lockManager.getLockHelper().isPasswordSet()) {
                // 저장된 비밀번호가 존재하는지 확인.
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_stop, R.anim.anim_slide_out_bottom);
    }

    private void showToast(String message) {
        Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
