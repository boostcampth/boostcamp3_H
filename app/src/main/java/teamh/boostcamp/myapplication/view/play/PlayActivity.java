package teamh.boostcamp.myapplication.view.play;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;
import teamh.boostcamp.myapplication.databinding.ActivityPlayBinding;
import teamh.boostcamp.myapplication.view.BaseActivity;

public class PlayActivity extends BaseActivity<ActivityPlayBinding> implements PlayerView {

    private static final String TAG = "PlayActivity";
    private static final String EXTRA = "recall";

    private PlayContractor.Presenter presenter;
    private PlayDiaryAdapter playDiaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
        String s = getIntent().getParcelableExtra("recall");

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
        //initPresenter();
        //initRecyclerView();
        //initTitleTextView();
    }

    private void initTitleTextView() {
    }

    private void initRecyclerView() {
    }

    private void initPresenter() {
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
