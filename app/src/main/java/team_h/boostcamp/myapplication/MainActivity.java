package team_h.boostcamp.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "젠킨스 빌드 테스트", Toast.LENGTH_SHORT).show();

        System.out.println("젠킨스 빌드 테스트 by woo");
    }
}
