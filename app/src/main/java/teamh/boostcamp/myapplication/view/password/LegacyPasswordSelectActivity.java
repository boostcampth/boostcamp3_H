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
import teamh.boostcamp.myapplication.databinding.ActivityLegacyPasswordSelectBinding;
import teamh.boostcamp.myapplication.view.AppInitializer;

public class LegacyPasswordSelectActivity extends AppCompatActivity {

    private static final String TAG = LegacyPasswordSelectActivity.class.getClass().getSimpleName();
    private ActivityLegacyPasswordSelectBinding binding;
    private LockManager lockManager;
    private AppInitializer application;
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legacy_password_select);

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
        toolbar = findViewById(R.id.toolbar_password_select);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // back 버튼 표시
        actionBar.setDisplayShowTitleEnabled(false); // 툴바 타이틀 Not Showing
    }

    private void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_legacy_password_select);
        binding.setActivity(LegacyPasswordSelectActivity.this);
    }

    private void initSwitch() {
        binding.switchPasswordSelect.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked) {
     /*           int type = LockManager.getInstance().getLockHelper().isPasswordSet() ?
                        LockHelper.DISABLE_PASSWORD : LockHelper.ENABLE_PASSWORD;

                Intent intent = new Intent(this, PasswordActivity.class);

                intent.putExtra(LockHelper.EXTRA_TYPE, type);
                startActivityForResult(intent, type);*/
                if (lockManager.getLockHelper().isPasswordSet()) {
                    // 체크된 상태 + 비밀번호가 설정된 사람이면 패스워드 띄우지 말아야 함.
                    Log.v("19983", String.valueOf(lockManager.getLockHelper().isPasswordSet()));
                } else {
                    // 이 경우 스위치버튼 클릭 시 비밀번호 설정
                    int type = LockHelper.ENABLE_PASSWORD;
                            /*LockManager.getInstance().getLockHelper().isPasswordSet() ?
                            LockHelper.DISABLE_PASSWORD : LockHelper.ENABLE_PASSWORD;*/

                    Intent intent = new Intent(this, PasswordActivity.class);

                    intent.putExtra(LockHelper.EXTRA_TYPE, type);
                    startActivityForResult(intent, type);
                }
            } else {
                // 저장된 비밀번호 제거하는 로직.
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
            /*            case R.id.tv_password_set_button:
             *//*
             * 저장된 비밀번호가 존재하면
             * type : 비밀번호 설정을 해제할 수 있음.
             *
             * 저장된 비밀번호가 존재하지 않으면
             * type : 비밀번호를 설정할 수 있음.
             * *//*

                int type = LockManager.getInstance().getLockHelper().isPasswordSet() ?
                        LockHelper.DISABLE_PASSWORD : LockHelper.ENABLE_PASSWORD;

                Intent intent = new Intent(this, PasswordActivity.class);

                intent.putExtra(LockHelper.EXTRA_TYPE, type);
                startActivityForResult(intent, type);
                break;*/
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
            /*binding.tvPasswordSetButton.setText(
                    getApplicationContext().getResources().getString(R.string.password_unlock_text));*/
            binding.tvPasswordChangeButton.setEnabled(true);

        } else { // 설정한 비밀번호가 없을 때
            /*binding.tvPasswordSetButton.setText(getApplicationContext()
                    .getResources().getString(R.string.password_lock_text));*/
            binding.tvPasswordChangeButton.setEnabled(false);

        }
    }
}
