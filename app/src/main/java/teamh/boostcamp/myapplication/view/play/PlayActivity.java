package teamh.boostcamp.myapplication.view.play;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;
import teamh.boostcamp.myapplication.databinding.ActivityPlayBinding;
import teamh.boostcamp.myapplication.data.model.Memory;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.view.BaseActivity;

public class PlayActivity extends BaseActivity<ActivityPlayBinding> implements PlayContractor.View {

    private static final String TAG = "PlayActivity";
    private static final String EXTRA_MEMORY = "memory";

    private PlayContractor.Presenter presenter;
    private Memory memory;
    private PlayDiaryAdapter playDiaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
        memory = getIntent().getParcelableExtra(EXTRA_MEMORY);

        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDetached();
        presenter = null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    public void setDiaryList(List<LegacyDiary> diaryList) {
        playDiaryAdapter.addItems(diaryList);
    }

    private void initViews() {
        initPresenter();
        initRecyclerView();
        initTitleTextView();
    }

    private void initTitleTextView() {
        binding.tvTitle.setText(memory.getTitle());
    }

    private void initRecyclerView() {
        playDiaryAdapter = new PlayDiaryAdapter(getApplicationContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setAdapter(playDiaryAdapter);

        presenter.loadData(memory.getId());
    }

    private void initPresenter() {
        presenter = new PlayPresenter(
                AppDatabase.getInstance(getApplicationContext()),
                RecordPlayerImpl.getINSTANCE(),
                this);
    }

    public void onCloseButtonClicked(View view) {
        Log.d(TAG, "onCloseButtonClicked: ");
        finish();
        overridePendingTransition(R.anim.anim_stop, R.anim.anim_slide_out_bottom);
    }

    public void onPlayButtonClicked(View view) {
        presenter.playMemory();
    }

    @Override
    public void makeToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }

}
