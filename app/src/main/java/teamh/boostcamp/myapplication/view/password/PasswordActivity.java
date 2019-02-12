package teamh.boostcamp.myapplication.view.password;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivityPasswordBinding;
import teamh.boostcamp.myapplication.view.BaseActivity;

public class PasswordActivity extends AppCompatActivity implements PasswordView {

    private PasswordPresenter presenter;
    private ActivityPasswordBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initPresenter();
    }

    private void init(){
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        binding.setActivity(PasswordActivity.this);
    }

    private void initPresenter(){
        presenter = new PasswordPresenter(this);
    }

    protected int getLayoutId() {
        return R.layout.activity_password;
    }

    public void onPasswordButton(int id) {
        switch (id) {
            case R.id.iv_password_back_button:
                finish();
                break;
            case R.id.tv_password_done_button:
                String currentPassword = binding.etPasswordCurrent.getText().toString();
                String changePassword = binding.etPasswordChange.getText().toString();
                String checkPassword = binding.etPasswordCheck.getText().toString();
                //presenter.comparePassword(currentPassword, changePassword, checkPassword);
                showToast("done");
                break;
        }
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
