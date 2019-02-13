package teamh.boostcamp.myapplication.view.password;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivityPasswordBinding;

public class PasswordActivity extends LifecycleManageActivity implements PasswordView {

    private ActivityPasswordBinding binding;

    private PasswordPresenter presenter;
    private int type = -1;
    private String oldPassword = null;
    protected InputFilter[] filters = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_password);

        initView();

        Bundle extras = getIntent().getExtras();
        //Log.v("231", extras.getString(AppLock.MESSAGE));
        if (extras != null) {
            // 밑에 message가 존재하면 변경 버튼을 눌렀다는 의미야..
            String message = extras.getString(LockHelper.EXTRA_MESSAGE);
            if (message != null) {
                binding.tvMessage.setText(message);
            } else {
                binding.tvMessage.setText("비밀번호를 입력해주세요.");
            }

            // 이제 SettingActivity에서 넘어온 타입을 확인할 수 있음.
            type = extras.getInt(LockHelper.EXTRA_TYPE, -1);
        }

        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_stop);

    }

    private void initView() {
        presenter = new PasswordPresenter(PasswordActivity.this);
        binding.setActivity(PasswordActivity.this);
    }

    // back 키 눌렀을 때
    @Override
    public void onBackPressed() {
        if (type == LockHelper.UNLOCK_PASSWORD) {
            // back to home screen
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            this.startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void onNumberButtonClick(int id) {
        String currentPassword = "";
        switch (id) {
            case R.id.button_password_one:
                currentPassword = binding.buttonPasswordOne.getText().toString();
                break;
            case R.id.button_password_two:
                currentPassword = binding.buttonPasswordTwo.getText().toString();
                break;
            case R.id.button_password_three:
                currentPassword = binding.buttonPasswordThree.getText().toString();
                break;
            case R.id.button_password_four:
                currentPassword = binding.buttonPasswordFour.getText().toString();
                break;
            case R.id.button_password_five:
                currentPassword = binding.buttonPasswordFive.getText().toString();
                break;
            case R.id.button_password_six:
                currentPassword = binding.buttonPasswordSix.getText().toString();
                break;
            case R.id.button_password_seven:
                currentPassword = binding.buttonPasswordSeven.getText().toString();
                break;
            case R.id.button_password_eight:
                currentPassword = binding.buttonPasswordEight.getText().toString();
                break;
            case R.id.button_password_nine:
                currentPassword = binding.buttonPasswordNine.getText().toString();
                break;
            case R.id.button_password_zero:
                currentPassword = binding.buttonPasswordZero.getText().toString();
                break;
        }

        checkPassword(currentPassword);
    }

    public void onDeleteNumberButton(int id) {
        switch (id) {
            case R.id.button_password_clear:
                clearPassword();
                break;
            case R.id.button_password_erase:
                deletePassword();
                break;
        }
    }

    private void checkPassword(String currentPassword) {
        if (binding.etPasswordOne.isFocused()) {
            binding.etPasswordOne.setText(currentPassword);

            binding.etPasswordTwo.requestFocus();
            binding.etPasswordTwo.setText("");

        } else if (binding.etPasswordTwo.isFocused()) {
            binding.etPasswordTwo.setText(currentPassword);

            binding.etPasswordThree.requestFocus();
            binding.etPasswordThree.setText("");

        } else if (binding.etPasswordThree.isFocused()) {
            binding.etPasswordThree.setText(currentPassword);

            binding.etPasswordFour.requestFocus();
            binding.etPasswordFour.setText("");

        } else if (binding.etPasswordFour.isFocused()) {
            binding.etPasswordFour.setText(currentPassword);
        }

        if (binding.etPasswordFour.getText().toString().length() > 0
                && binding.etPasswordThree.getText().toString().length() > 0
                && binding.etPasswordTwo.getText().toString().length() > 0
                && binding.etPasswordOne.getText().toString().length() > 0) {
            onPasswordInput();
        }
    }

    private void onPasswordInput() {
        String lockPassword = binding.etPasswordOne.getText().toString()
                + binding.etPasswordTwo.getText().toString()
                + binding.etPasswordThree.getText().toString()
                + binding.etPasswordFour.getText().toString();

        Log.v("3911 pActivity", lockPassword);

        binding.etPasswordOne.setText("");
        binding.etPasswordTwo.setText("");
        binding.etPasswordThree.setText("");
        binding.etPasswordFour.setText("");
        binding.etPasswordOne.requestFocus();

        switch (type) {

            // 비밀번호를 해제하려고 들어온 경우
            case LockHelper.DISABLE_PASSWORD:
                if (LockManager.getInstance().getLockHelper().checkPassword(lockPassword)) {
                    // 비밀번호가 같으면
                    Log.v("391 pActivity", lockPassword);
                    setResult(RESULT_OK);
                    LockManager.getInstance().getLockHelper().setPassword(null);
                    finish();
                } else {
                    onPasswordError();
                }
                break;

            // 비밀번호를 설정하려고 들어온 경우
            case LockHelper.ENABLE_PASSWORD:
                if (oldPassword == null) {
                    binding.tvMessage.setText("비밀번호를 다시 입력해주세요.");
                    oldPassword = lockPassword;
                } else {
                    if (lockPassword.equals(oldPassword)) {
                        setResult(RESULT_OK);
                        // 비밀번호 저장
                        LockManager.getInstance().getLockHelper().setPassword(lockPassword);
                        finish();
                    } else {
                        oldPassword = null;
                        binding.tvMessage.setText("비밀번호를 입력해주세요.");
                        onPasswordError();
                    }
                }
                break;

            case LockHelper.CHANGE_PASSWORD: // 비밀번호를 변경하려고 들어온 경우
                if (LockManager.getInstance().getLockHelper().checkPassword(lockPassword)) {
                    binding.tvMessage.setText("비밀번호를 입력해주세요.");
                    type = LockHelper.ENABLE_PASSWORD;
                } else {
                    onPasswordError();
                }
                break;

            case LockHelper.UNLOCK_PASSWORD: // 비밀번호를 해제??
                if (LockManager.getInstance().getLockHelper().checkPassword(lockPassword)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    onPasswordError();
                }
                break;

            default:
                break;
        }

    }

    protected void onPasswordError() {
        Toast toast = Toast.makeText(this, "다시 입력해주세요.",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
        toast.show();

        Thread thread = new Thread() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(
                        PasswordActivity.this, R.anim.anim_shake_password_not_match);
                binding.llPassword.startAnimation(animation);
                binding.etPasswordOne.setText("");
                binding.etPasswordTwo.setText("");
                binding.etPasswordThree.setText("");
                binding.etPasswordFour.setText("");
                binding.etPasswordOne.requestFocus();
            }
        };
        runOnUiThread(thread);
    }

    private void clearPassword() {
        binding.etPasswordOne.setText("");
        binding.etPasswordTwo.setText("");
        binding.etPasswordThree.setText("");
        binding.etPasswordFour.setText("");

        binding.etPasswordOne.requestFocus();
    }

    private void deletePassword() {
        if (binding.etPasswordOne.isFocused()) {

        } else if (binding.etPasswordTwo.isFocused()) {

            binding.etPasswordOne.requestFocus();
            binding.etPasswordOne.setText("");

        } else if (binding.etPasswordThree.isFocused()) {

            binding.etPasswordTwo.requestFocus();
            binding.etPasswordTwo.setText("");

        } else if (binding.etPasswordFour.isFocused()) {

            binding.etPasswordThree.requestFocus();
            binding.etPasswordThree.setText("");
        }
    }

    public int getType() {
        return type;
    }

}
