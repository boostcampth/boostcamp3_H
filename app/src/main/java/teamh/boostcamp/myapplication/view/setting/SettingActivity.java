package teamh.boostcamp.myapplication.view.setting;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.data.repository.RecallRepositoryImpl;
import teamh.boostcamp.myapplication.data.repository.firebase.FirebaseRepositoryImpl;
import teamh.boostcamp.myapplication.databinding.ActivitySettingBinding;
import teamh.boostcamp.myapplication.view.alarm.AlarmActivity;
import teamh.boostcamp.myapplication.view.password.PasswordSelectActivity;

public class SettingActivity extends AppCompatActivity implements SettingView {

    private static final int SIGN_IN_CODE = 4899;
    private ActivitySettingBinding binding;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private SettingPresenter presenter;
<<<<<<< HEAD
=======

>>>>>>> issue-219/예외처리 추가 및 crashlytics 추가
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth auth;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser() !=null){
            binding.rlSettingLoginText.setText(R.string.logout_success);
        }
    }

    private void init() {
        initBinding();
        initActionBar();
        initFirebaseAuth();
        initPresenter();
<<<<<<< HEAD
        progressDialog = new ProgressDialog(this);
        compositeDisposable = new CompositeDisposable();
=======
>>>>>>> issue-219/예외처리 추가 및 crashlytics 추가
    }

    private void initPresenter() {
        presenter = new SettingPresenter(this,
                DiaryRepositoryImpl.getInstance(AppDatabase.getInstance(
                        getApplicationContext()).diaryDao(),
                        DeepAffectApiClient.getInstance()),
                FirebaseRepositoryImpl.getInstance(),
                RecallRepositoryImpl.getInstance(AppDatabase.getInstance(getApplicationContext())));
    }

    private void initBinding() {
        binding = DataBindingUtil.setContentView(SettingActivity.this, R.layout.activity_setting);
        binding.setActivity(SettingActivity.this);
    }

    private void initActionBar() {
        toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
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
        Log.d("Test", "Test");
        presenter.onDestroy();
        toolbar = null;
        actionBar = null;
    }

    @Override
    public void showLoginMessage() {
        showToast(R.string.login_success);
    }

    @Override
    public void showBackUpStartMsg() {
        showToast(R.string.backup_start);
    }

    public void showLoadStartMsg() {
        showToast(R.string.download_start);
    }

    private void showToast(@StringRes final int id) {
        Toast.makeText(this, getString(id), Toast.LENGTH_SHORT).show();
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
                loginAndLogout(binding.rlSettingLoginText.getText().toString());
                break;
            case R.id.rl_setting_backup:
                TedRx2Permission.with(this)
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setRationaleMessage(R.string.setting_permission_load_rational_msg)
                        .setRationaleTitle(R.string.setting_permission_load_title)
                        .request()
                        .subscribe(tedPermissionResult -> {
                            if (tedPermissionResult.isGranted()) {
                                presenter.backupLocalDataToFirebaseRepository();
                            }
                        }, Throwable::printStackTrace);
                break;
            case R.id.rl_setting_restore:
                TedRx2Permission.with(this)
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setRationaleMessage(R.string.setting_permission_load_rational_msg)
                        .setRationaleTitle(R.string.setting_permission_load_title)
                        .request()
                        .subscribe(tedPermissionResult -> {
                            if (tedPermissionResult.isGranted()) {
                                presenter.downloadAllBackupFilesFromFirebase();
                            } else {
                                showToast(R.string.permission_denied);
                            }
                        }, Throwable::printStackTrace);
                break;
            case R.id.rl_setting_initialization:
                initInitialization();
                break;
        }
    }

    private void loginAndLogout(String name){
        switch (name){
            case "로그인":
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, SIGN_IN_CODE);
                break;
            case "로그아웃":
                FirebaseAuth.getInstance().signOut();
                updateUI(null);
                break;
        }
    }

    private void initInitialization() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        alertDialogBuilder.setTitle(getString(R.string.setting_initialization))
                .setMessage(getString(R.string.dialog_initialization_message))
                .setPositiveButton(getString(R.string.setting_initialization), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(DeleteDiaryFileWorker.class).build();
                        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
                        SharedPreferenceManager.getInstance(getApplicationContext()).removeLastDiarySaveTime();
                        compositeDisposable.add(presenter.initialize().subscribe());
                    }
                }).setNegativeButton(getString(R.string.dialog_initialization_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
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
            binding.rlSettingLoginText.setText(R.string.logout_success);
            showLoginMessage();
        } else {
            binding.rlSettingLoginText.setText(R.string.login_success);
            showNotLoginMsg();
        }
    }

    @Override
    public void startWorker(OneTimeWorkRequest request) {
        WorkManager.getInstance().enqueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        // 시작 -> 처리될 애니메이션
        this.overridePendingTransition(R.anim.anim_left_to_right, R.anim.anim_right_to_left);
    }
    @Override
    public void showInitializationMessage() {
        showToastMessage(R.string.setting_initialization_success);
    }

    @Override
    public void showNotLoginMsg() {
        showToast(R.string.not_login);
    }

    @SuppressWarnings("SameParameterValue")
    private void showToastMessage(@StringRes final int stringId) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show();
    }
}
