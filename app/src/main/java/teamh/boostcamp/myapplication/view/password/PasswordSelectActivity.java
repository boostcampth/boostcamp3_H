package teamh.boostcamp.myapplication.view.password;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivityPasswordSelectBinding;
import teamh.boostcamp.myapplication.view.AppInitializer;

public class PasswordSelectActivity extends AppCompatActivity {

    private static final String TAG = PasswordSelectActivity.class.getClass().getSimpleName();
    private ActivityPasswordSelectBinding binding;
    private LockManager lockManager;
    private AppInitializer application;
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_select);

        init();
        isExistPasswordState();
    }

    private void init() {
        application = new AppInitializer();
        lockManager = LockManager.getInstance();
        lockManager.enableLock(getApplication());
        initBinding();
        initSwitch();
        initActionBar();
    }

    private void initActionBar() {
        toolbar = binding.toolbarPasswordSelect;
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // back 버튼 표시
        actionBar.setDisplayShowTitleEnabled(false); // 툴바 타이틀 Not Showing
    }

    private void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_select);
        binding.setActivity(PasswordSelectActivity.this);
    }

    private void initSwitch() {
        binding.switchPasswordSelect.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked) {
                if (lockManager.getLockHelper().isPasswordSet()) {
                    // 체크된 상태 + 비밀번호가 설정된 사람이면 패스워드 띄우지 말아야 함.
                    Log.v(TAG, String.valueOf(lockManager.getLockHelper().isPasswordSet()));
                } else {
                    // 이 경우 스위치버튼 클릭 시 비밀번호 설정
                    int type = LockHelper.ENABLE_PASSWORD;

                    Intent intent = new Intent(this, PasswordActivity.class);
                    intent.putExtra(LockHelper.EXTRA_TYPE, type);
                    startActivityForResult(intent, type);
                }
            } else {
                // 저장된 비밀번호 제거.
                lockManager.getLockHelper().setPassword(null);
                binding.tvPasswordChangeButton.setTextColor(getApplicationContext().getResources().getColor(R.color.password_text_gray));
                binding.tvPasswordChangeButton.setEnabled(false); // 변경 버튼 비활성화
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                this.overridePendingTransition(R.anim.anim_left_to_right, R.anim.anim_right_to_left);
                break;
        }
        return super.onOptionsItemSelected(item);
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
                Log.v(TAG, getApplicationContext().getResources().getString(R.string.password_not_set_text));
            }
        }

        isSavePassword();
    }


    private void isSavePassword() {
        if (lockManager.getLockHelper().isPasswordSet()) {
            // 비밀번호 설정되어 있음.
            binding.switchPasswordSelect.setChecked(true);
            binding.tvPasswordChangeButton.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
        } else {
            // 설정되어 있지 않음.
            binding.switchPasswordSelect.setChecked(false);
            binding.tvPasswordChangeButton.setTextColor(getApplicationContext().getResources().getColor(R.color.password_text_gray));
            binding.tvPasswordChangeButton.setEnabled(false); // 변경 버튼 비활성화
        }
    }

    public void onButtonClick(int id) {
        switch (id) {
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
            binding.tvPasswordChangeButton.setEnabled(true);
        } else {
            // 설정한 비밀번호가 없을 때
            binding.tvPasswordChangeButton.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toolbar = null;
        lockManager = null;
        binding = null;
    }
}
