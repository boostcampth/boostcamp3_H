package teamh.boostcamp.myapplication.view;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.databinding.ActivitySplashBinding;
import teamh.boostcamp.myapplication.view.main.MainActivity;
import teamh.boostcamp.myapplication.view.password.LockHelper;
import teamh.boostcamp.myapplication.view.password.LockHelperImpl;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private CompositeDisposable disposable = new CompositeDisposable();
    private LockHelper lockHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lockHelper = LockHelperImpl.getInstance(getApplicationContext());
        initBinding();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        disposable.add(Completable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe(() -> {
                    if (lockHelper.isPasswordSet()) {
                        startActivityWhere("1");
                    } else {
                        startActivityWhere("2");
                    }
                }));
    }

    private void startActivityWhere(String type) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("tag", type);
        startActivity(intent);
        finish();
    }

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
        lockHelper = null;
    }
}