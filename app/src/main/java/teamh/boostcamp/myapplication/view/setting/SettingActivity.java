package teamh.boostcamp.myapplication.view.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivitySettingBinding;
import teamh.boostcamp.myapplication.view.BaseActivity;
import teamh.boostcamp.myapplication.view.alarm.AlarmActivity;
import teamh.boostcamp.myapplication.view.password.PasswordActivity;
import teamh.boostcamp.myapplication.view.password.PasswordSelectActivity;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.setActivity(SettingActivity.this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
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

    private void showToast(String message) {
        Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
