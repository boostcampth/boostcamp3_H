package teamh.boostcamp.myapplication.view.password;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivityPasswordSelectBinding;

import android.os.Bundle;

public class PasswordSelectActivity extends AppCompatActivity {

    private ActivityPasswordSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init(){
        initBinding();
        initViews();
    }

    private void initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_select);
        binding.setActivity(PasswordSelectActivity.this);
    }

    private void initViews(){
        //binding.tvPasswordSetButton.setText();
    }
    public void onButtonClick(int id){
        switch (id){
            case R.id.tv_password_set_button:
                break;
            case R.id.tv_password_change_button:
                break;
        }
    }
}
