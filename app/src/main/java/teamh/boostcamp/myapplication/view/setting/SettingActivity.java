package teamh.boostcamp.myapplication.view.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.data.repository.RecallRepositoryImpl;
import teamh.boostcamp.myapplication.databinding.ActivitySettingBinding;
import teamh.boostcamp.myapplication.view.alarm.AlarmActivity;
import teamh.boostcamp.myapplication.view.password.LockManager;
import teamh.boostcamp.myapplication.view.password.PasswordSelectActivity;

public class SettingActivity extends AppCompatActivity implements SettingView {

    private static final int SIGN_IN_CODE = 4899;
    private ActivitySettingBinding binding;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private SettingPresenter presenter;
    private boolean signFlag = false;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        initBinding();
        initActionBar();
        initFirebaseAuth();
        initPresenter();
    }

    private void initPresenter() {
        presenter = new SettingPresenter(RecallRepositoryImpl.getInstance(AppDatabase.getInstance(getApplicationContext())),
                DiaryRepositoryImpl.getInstance(AppDatabase.getInstance(
                        getApplicationContext()).diaryDao(),
                        DeepAffectApiClient.getInstance()));
    }

    private void initBinding() {
        binding = DataBindingUtil.setContentView(SettingActivity.this, R.layout.activity_setting);
        binding.setActivity(SettingActivity.this);
    }

    private void initActionBar() {
        toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
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

    private void initFirebaseAuth() {
        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        toolbar = null;
        actionBar = null;
    }

    @Override
    public void showLoginMessage() {
        showToast(R.string.login_success);
    }

    private void showToast(@StringRes final int id) {
        Toast.makeText(getApplicationContext(), getString(id), Toast.LENGTH_SHORT).show();
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.setting_alarm_to_alarm:
                startActivity(new Intent(SettingActivity.this, AlarmActivity.class));
                break;
            case R.id.rl_setting_password:
                startActivity(new Intent(SettingActivity.this, PasswordSelectActivity.class));
                break;
            case R.id.rl_setting_login:
                if(!signFlag){
                    signIn();
                }else {
                    signOut();
                }
                showToastMessage(R.string.login_success);
                break;
            case R.id.rl_setting_initialization:
                showToastMessage(R.string.logout_success);
                break;
        }

    }

    private void signIn() {
        signFlag = true;
        binding.rlSettingLoginText.setText(R.string.logout_success);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN_CODE);
    }

    private void signOut() {
        signFlag = false;
        binding.rlSettingLoginText.setText(R.string.login_success);
        auth.signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        updateUI(user);
                    } else {

                        updateUI(null);
                    }
                });

    }

    private void updateUI(Object object) {
        if (object != null) {
            showLoginMessage();
        } else {
            showToast("Fail");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        // 시작 -> 처리될 애니메이션
        this.overridePendingTransition(R.anim.anim_left_to_right, R.anim.anim_right_to_left);
    }

    private void showToast(String message) {
        Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showToastMessage(@StringRes final int stringId) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show();
    }


}
