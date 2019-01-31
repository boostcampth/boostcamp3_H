package team_h.boostcamp.myapplication.view.play;

import androidx.appcompat.app.AppCompatActivity;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.ActivityPlayBinding;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.view.BaseActivity;
import team_h.boostcamp.myapplication.view.BasePresenter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class PlayActivity extends BaseActivity<ActivityPlayBinding> {

    private static final String TAG = "PlayActivity";
    private static final String EXTRA_MEMORY = "memory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
        Memory memory = (Memory) getIntent().getParcelableExtra(EXTRA_MEMORY);
        binding.tvTitle.setText(memory.getTitle());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    public BasePresenter generatePresenter() {
        return null;
    }

    public void onCloseButtonClicked(View view){
        Log.d(TAG, "onCloseButtonClicked: ");
        finish();
        overridePendingTransition(R.anim.anim_stop, R.anim.anim_slide_out_bottom);
    }
}
