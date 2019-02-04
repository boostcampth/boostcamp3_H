package team_h.boostcamp.myapplication.view.setting;

import android.os.Bundle;
import android.view.View;

import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivitySettingBinding;
import team_h.boostcamp.myapplication.view.BaseActivity;

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

    public void onButtonClick(View view){
        switch (view.getId()){
            case R.id.iv_close_button:
                finish();
                break;
        }
    }
}
