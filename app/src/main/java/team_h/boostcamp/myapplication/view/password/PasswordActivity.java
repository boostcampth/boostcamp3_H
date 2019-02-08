package team_h.boostcamp.myapplication.view.password;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivityPasswordBinding;
import team_h.boostcamp.myapplication.view.BaseActivity;

public class PasswordActivity extends BaseActivity<ActivityPasswordBinding> implements PasswordContractor.View {

    private PasswordPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new PasswordPresenter(PasswordActivity.this);
        binding.setActivity(PasswordActivity.this);
    }

    @Override
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
                presenter.comparePassword(currentPassword, changePassword, checkPassword);
                showToast("done");
                break;
        }
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
