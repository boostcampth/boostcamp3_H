package teamh.boostcamp.myapplication.view.password;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivityPasswordSelectBinding;

public class PasswordSelectActivity extends AppCompatActivity {

    private static final String TAG = PasswordSelectActivity.class.getSimpleName();
    private ActivityPasswordSelectBinding binding;
    private LockManager lockManager;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private LockHelper lockHelper;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        lockManager = LockManager.getInstance();
        lockHelper = lockManager.getLockHelper(getApplicationContext());
        initBinding();
        initSwitch();
        initActionBar();
    }

    private void initActionBar() {
        toolbar = binding.toolbarPasswordSelect;
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // back 버튼 표시
            actionBar.setDisplayShowTitleEnabled(false); // 툴바 타이틀 Not Showing
        }
    }

    private void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_select);
        binding.setActivity(PasswordSelectActivity.this);
    }

    private void initSwitch() {
        binding.switchPasswordSelect.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked) {
                if (lockHelper.isPasswordSet()) { // 체크된 상태 + 비밀번호가 설정된 사람이면 패스워드 띄우지 말아야 함.
                    Log.v(TAG, String.valueOf(lockHelper.isPasswordSet()));
                } else { // 이 경우 스위치버튼 클릭 시 비밀번호 설정
                    int type = LockHelper.ENABLE_PASSWORD;

                    Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
                    intent.putExtra(LockHelper.EXTRA_TYPE, type);

                    Completable.timer(500, TimeUnit.MILLISECONDS,
                            AndroidSchedulers.mainThread())
                            .subscribe(() -> startActivityForResult(intent, type));

                }
            } else {
                // 저장된 비밀번호 제거.
                lockHelper.savePassword(null);
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
        isSavePassword();
    }

    private void isSavePassword() {
        if (lockHelper.isPasswordSet()) {
            // 비밀번호 설정되어 있음.
            binding.tvPasswordChangeButton.setEnabled(true);
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
                change_password_intent.putExtra(LockHelper.EXTRA_MESSAGE, LockHelper.EXTRA_MESSAGE);
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
        isSavePassword();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toolbar = null;
        actionBar = null;
        lockManager = null;
        binding = null;
        lockHelper = null;
        //compositeDisposable.clear();
    }
}
