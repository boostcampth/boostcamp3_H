package teamh.boostcamp.myapplication.view.password;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivityPasswordSelectBinding;
import teamh.boostcamp.myapplication.view.AppInitializer;

public class PasswordSelectActivity extends AppCompatActivity {

    private static final String TAG = PasswordSelectActivity.class.getClass().getSimpleName();
    private ActivityPasswordSelectBinding binding;
    private LockManager lockManager;
    private AppInitializer application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = new AppInitializer();

        init();
        isExistPasswordState();
    }

    private void init() {
        lockManager = LockManager.getInstance();
        lockManager.enableLock(getApplication());
        initBinding();
    }

    private void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_select);
        binding.setActivity(PasswordSelectActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (application.getAppInitializer(this.getApplicationContext()).getApplicationStatus()
                == AppInitializer.ApplicationStatus.RETURNED_TO_FOREGROUND) {
            if (lockManager.getLockHelper().isPasswordSet()) {
                Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
                intent.putExtra(LockHelper.EXTRA_TYPE, LockHelper.UNLOCK_PASSWORD);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Log.v(TAG, getApplicationContext().getResources().getString(R.string.password_not_set_text));
            }
        }

    }

    public void onButtonClick(int id) {
        switch (id) {
            case R.id.tv_password_set_button:
                /*
                 * 저장된 비밀번호가 존재하면
                 * type : 비밀번호 설정을 해제할 수 있음.
                 *
                 * 저장된 비밀번호가 존재하지 않으면
                 * type : 비밀번호를 설정할 수 있음.
                 * */

                int type = LockManager.getInstance().getLockHelper().isPasswordSet() ?
                        LockHelper.DISABLE_PASSWORD : LockHelper.ENABLE_PASSWORD;

                Intent intent = new Intent(this, PasswordActivity.class);

                intent.putExtra(LockHelper.EXTRA_TYPE, type);
                startActivityForResult(intent, type);
                break;
            case R.id.tv_password_change_button:
                Intent change_password_intent = new Intent(this, PasswordActivity.class);

                change_password_intent.putExtra(LockHelper.EXTRA_TYPE, LockHelper.CHANGE_PASSWORD);
                change_password_intent.putExtra(LockHelper.EXTRA_MESSAGE,
                        getApplicationContext()
                                .getResources()
                                .getString(R.string.password_before_input_text));
                startActivityForResult(change_password_intent, LockHelper.CHANGE_PASSWORD);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LockHelper.DISABLE_PASSWORD:
                break;
            case LockHelper.ENABLE_PASSWORD:
            case LockHelper.CHANGE_PASSWORD:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this,
                            getApplicationContext()
                                    .getResources()
                                    .getString(R.string.password_lock_success_text),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

        isExistPasswordState();
    }

    // 저장된 비밀번호가 존재하는지 확인.
    private void isExistPasswordState() {
        // 설정한 비밀번호가 있을 때
        if (lockManager.getLockHelper().isPasswordSet()) {
            binding.tvPasswordSetButton.setText(
                    getApplicationContext().getResources().getString(R.string.password_unlock_text));
            binding.tvPasswordChangeButton.setEnabled(true);

        } else { // 설정한 비밀번호가 없을 때
            binding.tvPasswordSetButton.setText(getApplicationContext()
                    .getResources().getString(R.string.password_lock_text));
            binding.tvPasswordChangeButton.setEnabled(false);

        }
    }
}
