package team_h.boostcamp.myapplication.view.main;

import android.os.Bundle;
import android.widget.Toast;

import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivityMainBinding;
import team_h.boostcamp.myapplication.view.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainPresenter> implements MainContractor.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "젠킨스 빌드 테스트", Toast.LENGTH_SHORT).show();

        System.out.println("젠킨스 빌드 테스트 by woo");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter(MainActivity.this);
    }
}
