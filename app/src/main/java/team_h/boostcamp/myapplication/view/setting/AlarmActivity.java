package team_h.boostcamp.myapplication.view.setting;

import android.os.Bundle;

import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivityAlarmBinding;
import team_h.boostcamp.myapplication.view.BaseActivity;

public class AlarmActivity extends BaseActivity<ActivityAlarmBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.setActivity(AlarmActivity.this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm;
    }

    public void onClickButton(int id) {
        switch (id) {
            case R.id.iv_alarm_back_button:
                finish();
                break;
        }
    }

}
