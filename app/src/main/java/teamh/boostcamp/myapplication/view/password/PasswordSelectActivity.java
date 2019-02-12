package teamh.boostcamp.myapplication.view.password;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivityPasswordSelectBinding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.locks.Lock;

public class PasswordSelectActivity extends AppCompatActivity {

    private ActivityPasswordSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        isExistPasswordState();
    }

    private void init() {
        LockManager.getInstance().enableLock(getApplication());
        initBinding();
        initViews();
    }

    private void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_select);
        binding.setActivity(PasswordSelectActivity.this);
    }

    private void initViews() {
        //binding.tvPasswordSetButton.setText();
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
                change_password_intent.putExtra(LockHelper.EXTRA_MESSAGE, "이전 비밀번호 입력해주세요");
                startActivityForResult(change_password_intent, LockHelper.CHANGE_PASSWORD);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LockHelper.DISABLE_PASSWORD:
                break;
            case LockHelper.ENABLE_PASSWORD:
            case LockHelper.CHANGE_PASSWORD:
                if (resultCode == RESULT_OK) { Toast.makeText(this, "비밀번호 설정이 완료되었습니다.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

        isExistPasswordState();
    }

    // 저장된 비밀번호가 존재하는지 확인.
    private void isExistPasswordState(){
        // 설정한 비밀번호가 있을 때
        if (LockManager.getInstance().getLockHelper().isPasswordSet()) {
            binding.tvPasswordSetButton.setText("비밀 번호를 해제해주세요.");
            binding.tvPasswordChangeButton.setEnabled(true);

        } else { // 설정한 비밀번호가 없을 때.
            binding.tvPasswordSetButton.setText("비밀번호를 설정해주세요.");
            binding.tvPasswordChangeButton.setEnabled(false);

        }
    }
}
