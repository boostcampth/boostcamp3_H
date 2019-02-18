package teamh.boostcamp.myapplication.view.setting;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import androidx.work.WorkRequest;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.data.repository.RecallRepositoryImpl;
import teamh.boostcamp.myapplication.data.repository.firebase.FirebaseRepositoryImpl;
import teamh.boostcamp.myapplication.databinding.ActivitySettingBinding;
import teamh.boostcamp.myapplication.view.AppInitializer;
import teamh.boostcamp.myapplication.view.alarm.AlarmActivity;
import teamh.boostcamp.myapplication.view.password.LockHelper;
import teamh.boostcamp.myapplication.view.password.LockManager;
import teamh.boostcamp.myapplication.view.password.PasswordActivity;
import teamh.boostcamp.myapplication.view.password.PasswordSelectActivity;

public class SettingActivity extends AppCompatActivity implements SettingView {

    private static final String TAG = SettingActivity.class.getClass().getSimpleName();
    private ActivitySettingBinding binding;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private static final int SIGN_IN_CODE = 4899;
    private AppInitializer appInitializer;
    private LockManager lockManager;
    private LockHelper lockHelper;
    private SettingPresenter presenter;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth auth;

    private ProgressDialog progressDialog;

    private CompositeDisposable compositeDisposable;

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
        initLock();
        initPresenter();
        progressDialog = new ProgressDialog(this);
    }

    private void initPresenter() {
        presenter = new SettingPresenter(this,
                RecallRepositoryImpl.getInstance(AppDatabase.getInstance(getApplicationContext())),
                DiaryRepositoryImpl.getInstance(AppDatabase.getInstance(
                        getApplicationContext()).diaryDao(),
                        DeepAffectApiClient.getInstance()),
                FirebaseRepositoryImpl.getInstance(),
                SharedPreferenceManager.getInstance(getApplicationContext()));

        compositeDisposable = new CompositeDisposable();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (appInitializer.getAppInitializer(getApplicationContext()).getApplicationStatus()
                == AppInitializer.ApplicationStatus.RETURNED_TO_FOREGROUND) {
            if (lockHelper.isPasswordSet()) {
                // 저장된 비밀번호가 존재하는지 확인.
                Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
                intent.putExtra(LockHelper.EXTRA_TYPE, LockHelper.UNLOCK_PASSWORD);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Log.v(TAG, getApplicationContext().getResources().getString(R.string.password_not_set_text));
            }
        }
    }

    private void initFirebaseAuth() {
        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initLock() {
        appInitializer = new AppInitializer();
        lockManager = LockManager.getInstance();
        lockHelper = lockManager.getLockHelper(getApplicationContext());
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
                signIn();
                showToastMessage(R.string.login_success);
                break;
            case R.id.rl_setting_logout:
                //auth.signOut();
                FirebaseAuth.getInstance().signOut();
                showToastMessage(R.string.logout_success);
            case R.id.rl_setting_backup:
                TedRx2Permission.with(this)
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setRationaleMessage(R.string.setting_permission_load_rational_msg)
                        .setRationaleTitle(R.string.setting_permission_load_title)
                        .request()
                        .subscribe(tedPermissionResult -> {
                            if(tedPermissionResult.isGranted()) {
                                progressDialog.setMessage(getString(R.string.do_backup));
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
                           if(tedPermissionResult.isGranted()) {
                               progressDialog.setMessage(getString(R.string.do_load));
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

    private void initInitialization() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        alertDialogBuilder.setTitle("초기화")
                .setMessage("정말로 초기화 하시겠습니까. 모든 추억들이 사라집니다.")
                .setPositiveButton("초기화", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WorkRequest initializationWorkRequest = new OneTimeWorkRequest.Builder(InitializationWorker.class).build();
                        WorkManager.getInstance().enqueue(initializationWorkRequest);
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN_CODE);
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

    @Override
    protected void onPause() {
        super.onPause();
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showBackUpSuccessMsg() {
        showToast(R.string.backup_success);
    }

    @Override
    public void showInitializationMessage() {
        showToastMessage(R.string.setting_initialization_success);
    }

    public void showBackUpFailMsg() {
        progressDialog.dismiss();
        showToast(R.string.backup_fail);
    }

    @Override
    public void showLoadSuccessMsg() {
        showToast(R.string.load_success);
    }

    @Override
    public void showLoadFailMsg() {
        showToast(R.string.load_fail);
    }

    @Override
    public void showNotLoginMsg() {
        showToast(R.string.not_login);
    }

    @Override
    public void dismissDialog() {
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showDialog() {
        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void showToast(String message) {
        Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showToastMessage(@StringRes final int stringId) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show();
    }
}
