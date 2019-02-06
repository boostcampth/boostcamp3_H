package team_h.boostcamp.myapplication.view.play;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivityPlayBinding;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.model.source.local.AppDatabase;
import team_h.boostcamp.myapplication.view.BaseActivity;
import team_h.boostcamp.myapplication.view.BasePresenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PlayActivity extends BaseActivity<ActivityPlayBinding> implements PlayContractor.View {

    private static final String TAG = "PlayActivity";
    private static final String EXTRA_MEMORY = "memory";

    private PlayPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
        Memory memory = getIntent().getParcelableExtra(EXTRA_MEMORY);
        presenter = new PlayPresenter(this);
        presenter.setAppDatabase(AppDatabase.getInstance(getApplicationContext()));
        binding.setActivity(this);
        binding.tvTitle.setText(memory.getTitle());

        //MediaPlayer
        presenter.setMediaPlayer(new MediaPlayerWrapper());

        //리사이클러뷰
        PlayDiaryAdapter adapter = new PlayDiaryAdapter(getApplicationContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setAdapter(adapter);
        presenter.setPlayDiaryAdapter(adapter);

        presenter.loadData(memory.getId());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    public void onCloseButtonClicked(View view){
        Log.d(TAG, "onCloseButtonClicked: ");
        finish();
        overridePendingTransition(R.anim.anim_stop, R.anim.anim_slide_out_bottom);
    }

    public void onPlayButtonClicked(View view){
        presenter.onPlayDiaryList();
    }

    @Override
    public void makeToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDetached();
        presenter = null;
    }
}
