package teamh.boostcamp.myapplication.view.password;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.lang.reflect.Field;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivityPasswordBinding;

public class PasswordActivity extends AppCompatActivity implements PasswordView {

    private ActivityPasswordBinding binding;
    private PasswordPresenter passwordPresenter;
    private int type = -1;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Nullable
    private String oldPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        overridePendingTransition(R.anim.anim_slide_bottom_to_top, R.anim.anim_stop);
    }

    void init() {
        initPresenter();
        initBinding();
        existSaveData();
    }

    void initPresenter() {
        passwordPresenter = new PasswordPresenter(this, getApplicationContext());
    }

    void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password);
        binding.setActivity(PasswordActivity.this);
    }

    void existSaveData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // 밑에 message가 존재하면 변경 버튼을 눌렀다는 의미야..
            String message = extras.getString(LockHelper.EXTRA_MESSAGE);
            if (message != null) {
                binding.tvMessage.setText(getApplicationContext()
                        .getResources()
                        .getString(R.string.password_before_input_text));
            } else {
                binding.tvMessage.setText(getString(R.string.password_please_input_text));
            }

            // SettingActivity에서 넘어온 타입을 확인할 수 있음.
            type = extras.getInt(LockHelper.EXTRA_TYPE, -1);
        }
    }

    // back 키 눌렀을 때
    @Override
    public void onBackPressed() {
        if (type == LockHelper.UNLOCK_PASSWORD) {
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            activityFinish();
        } else {
            activityFinish();
        }
    }

    // activity 사라질 때.
    private void activityFinish() {
        finish();
        overridePendingTransition(R.anim.anim_stop, R.anim.anim_slide_out_bottom);
    }

    public void onNumberButtonClick(final int id) {
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

    public void onDeleteNumberButton(final int id) {
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

        clearPassword();

        switch (type) {

            // 비밀번호 해제
            case LockHelper.DISABLE_PASSWORD:
                if (passwordPresenter.checkPassword(lockPassword)) { // 비밀번호 일치시
                    setResult(RESULT_OK);
                    passwordPresenter.savePassword(null);
                    activityFinish();
                } else {
                    showPasswordErrorMessage();
                }
                break;

            // 비밀번호 설정
            case LockHelper.ENABLE_PASSWORD:
                if (oldPassword == null) {
                    binding.tvMessage.setText(getString(R.string.password_check_retry_text));
                    oldPassword = lockPassword;
                } else {
                    if (lockPassword.equals(oldPassword)) { // 비밀번호 저장하기 위함.
                        setResult(RESULT_OK);
                        passwordPresenter.savePassword(lockPassword);
                        activityFinish();
                    } else {
                        oldPassword = null;
                        binding.tvMessage.setText(getString(R.string.password_please_input_text));
                        showPasswordErrorMessage();
                    }
                }
                break;

            // 비밀번호 변경
            case LockHelper.CHANGE_PASSWORD:
                if (passwordPresenter.checkPassword(lockPassword)) {
                    binding.tvMessage.setText(getString(R.string.password_please_input_text));
                    type = LockHelper.ENABLE_PASSWORD;
                } else {
                    showPasswordErrorMessage();
                }
                break;

            // back -> fore
            case LockHelper.UNLOCK_PASSWORD:
                if (passwordPresenter.checkPassword(lockPassword)) {
                    setResult(RESULT_OK);
                    activityFinish();
                } else {
                    showPasswordErrorMessage();
                }
                break;
            // splash -> activity
            case LockHelper.SPLASH_PASSWORD:
                if (passwordPresenter.checkPassword(lockPassword)) {
                    setResult(RESULT_OK);
                    activityFinish();
                } else {
                    showPasswordErrorMessage();
                }
                break;
            default:
                break;
        }

    }

    private void clearPassword() {
        binding.etPasswordOne.setText("");
        binding.etPasswordTwo.setText("");
        binding.etPasswordThree.setText("");
        binding.etPasswordFour.setText("");
        binding.etPasswordOne.requestFocus();
    }

    private void deletePassword() {
        if (binding.etPasswordTwo.isFocused()) {

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

    @Override
    public void showToast(int id) {
        Toast.makeText(getApplicationContext(), getString(id), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordErrorMessage() {
        Toast toast = Toast.makeText(getApplicationContext(),
                getString(R.string.password_retry_input_text), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 50);
        toast.show();
        ErrorAnimation();
    }

    public void ErrorAnimation() {
        /*rxJava로 변경해보기.*/

        compositeDisposable.add(Completable.fromAction(() -> {
            Animation animation = AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.anim_shake_password_not_match);
            binding.llPassword.startAnimation(animation);
        }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    clearPassword();
                }, throwable -> {
                    throwable.printStackTrace();
                }));
    }
    

    @Override
    protected void onDestroy() {
        super.onDestroy();
        passwordPresenter.onDestroyView();
        clearView();
    }

    @Override
    public void clearView() {
        binding = null;
        oldPassword = null;
        passwordPresenter = null;
        compositeDisposable.dispose();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.MANUFACTURER.equals("samsung")) {
                Log.v("98980","98980");
                Object systemService = getSystemService(Class.forName("com.samsung.android.content.clipboard.SemClipboardManager"));
                Field mContext = systemService.getClass().getDeclaredField("mContext");
                mContext.setAccessible(true);
                mContext.set(systemService, null);
            }
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            Log.v("98980","98980");
            //ignored
            // }
        }
    }
}
