package teamh.boostcamp.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivitySplashBinding;
import teamh.boostcamp.myapplication.view.main.MainActivity;
import teamh.boostcamp.myapplication.view.password.LockHelper;
import teamh.boostcamp.myapplication.view.password.LockManager;
import teamh.boostcamp.myapplication.view.password.PasswordActivity;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LockHelper.DISABLE_PASSWORD:
                break;
            case LockHelper.ENABLE_PASSWORD:
            case LockHelper.CHANGE_PASSWORD:
                break;
            case LockHelper.SPLASH_PASSWORD:
                if (resultCode == RESULT_OK) {
                    Log.v("Test4433 splash", "splash");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                break;
            default:
                break;
        }
    }*/

    private void initBinding() {
        binding = DataBindingUtil.setContentView(SplashActivity.this, R.layout.activity_splash);
        binding.setActivity(SplashActivity.this);
    }

    private void initView() {
        Glide.with(getApplicationContext()).load(R.drawable.splash_background).into(binding.ivSplashBackground);
        Glide.with(getApplicationContext()).load(R.drawable.splash_icon).into(binding.ivSplashIcon);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
        binding = null;
    }
}