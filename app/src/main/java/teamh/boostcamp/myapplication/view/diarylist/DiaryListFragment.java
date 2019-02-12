package teamh.boostcamp.myapplication.view.diarylist;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tedpark.tedpermission.rx2.TedRx2Permission;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.databinding.FragmentDiaryListBinding;
import teamh.boostcamp.myapplication.utils.KeyPadUtil;
import teamh.boostcamp.myapplication.utils.NetworkStateUtil;

public class DiaryListFragment extends Fragment implements DiaryListView {

    private static final int LOAD_ITEM_NUM = 3;
    private static final int NEW_ITEM_LOAD = 1;
    public final ObservableBoolean isSaving = new ObservableBoolean(false);

    private Context context;
    private FragmentDiaryListBinding binding;
    private DiaryListPresenter presenter;
    private CompositeDisposable compositeDisposable;
    private DiaryListAdapter diaryListAdapter;
    private HashTagListAdapter hashTagListAdapter;

    private boolean isRecording = false;

    public DiaryListFragment() { /*Empty*/}

    @NonNull
    public static DiaryListFragment newInstance() {
        return new DiaryListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        compositeDisposable.clear();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        initPresenter();
        initAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary_list, container, false);
        binding.setFragment(DiaryListFragment.this);

        initView();

        presenter.loadDiaryList(LOAD_ITEM_NUM);

        return binding.getRoot();
    }


    @Override
    public void addDiaryList(@NonNull List<Diary> diaryList) {
        diaryListAdapter.addDiaryList(diaryList);
    }

    @Override
    public void notifyTodayDiarySaved() {
        isSaving.set(false);
        presenter.loadDiaryList(NEW_ITEM_LOAD);
    }

    @Override
    public void showRecordTimeOutMsg() {
        showToastMessage(R.string.item_record_time_out);
        binding.rpbRecordItemBackground.stopRippleAnimation();
    }

    @Override
    public void showLoadDiaryListFailMsg() {
        showToastMessage(R.string.item_record_load_diary_list_fail);
    }

    @Override
    public void showRecordNotFinished() {
        showToastMessage(R.string.item_record_now_recording);
    }

    @Override
    public void showEmotionNotSelected() {
        showToastMessage(R.string.item_record_emotion_not_selected);
    }

    @Override
    public void showRecordFileNotFound() {
        showToastMessage(R.string.item_record_file_not_found);
    }

    @Override
    public void showSaveDiaryFail() {
        showToastMessage(R.string.item_record_save_fail);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onViewDestroyed();
    }

    private void showToastMessage(@StringRes final int stringId) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show();
    }

    private void initPresenter() {
        presenter = new DiaryListPresenter(this,
                DiaryRepositoryImpl.getInstance(AppDatabase.getInstance(context).diaryDao(), DeepAffectApiClient.getInstance()),
                new DiaryRecorderImpl());
    }

    private void initView() {

        binding.recyclerViewMainList.setNestedScrollingEnabled(false);
        binding.recyclerViewMainList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true));
        binding.recyclerViewMainList.setAdapter(diaryListAdapter);
        binding.nsvFragmentDiaryContainer.setOnScrollChangeListener(
                (NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) -> {
                    if (v.getChildAt(0).getBottom() <= (v.getHeight() + v.getScrollY())) {
                        Log.d("Test", "마지막");
                        presenter.loadDiaryList(LOAD_ITEM_NUM);
                    }
                });

        binding.etItemRecordInput.setHashTagListAdapter(hashTagListAdapter);
        binding.recyclerViewItemRecordTags.setAdapter(hashTagListAdapter);
        binding.recyclerViewItemRecordTags.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

        binding.buttonRecordItemRecord.setOnClickListener(v -> {
            TedRx2Permission.with(context)
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                    .setRationaleTitle("임시 타이틀")
                    .setRationaleMessage("임시 설명")
                    .request()
                    .subscribe(tedPermissionResult -> {
                        if (tedPermissionResult.isGranted()) {
                            KeyPadUtil.closeKeyPad(context, binding.etItemRecordInput);

                            if (isRecording) {
                                binding.rpbRecordItemBackground.stopRippleAnimation();
                                presenter.setIsRecording(false);
                                presenter.finishRecording();
                            } else {
                                binding.rpbRecordItemBackground.startRippleAnimation();
                                presenter.setIsRecording(true);
                                presenter.startRecording();
                            }
                            isRecording = !isRecording;
                        } else {
                            showToastMessage(R.string.item_record_permission_denied);
                        }
                    }, Throwable::printStackTrace);
        });

        binding.buttonItemRecordDone.setOnClickListener(v -> {
            binding.pbItemRecordWaiting.setVisibility(View.VISIBLE);
            isSaving.set(true);
            presenter.saveDiary(hashTagListAdapter.getTags(), NetworkStateUtil.isNetworkConnected(context));
        });

        binding.tvRecordItemPgood.setOnClickListener(v -> presenter.setSelectedEmotion(Emotion.GOOD));
        binding.tvRecordItemGood.setOnClickListener(v -> presenter.setSelectedEmotion(Emotion.VERY_GOOD));
        binding.tvRecordItemNormal.setOnClickListener(v -> presenter.setSelectedEmotion(Emotion.NEUTRAL));
        binding.tvRecordItemBad.setOnClickListener(v -> presenter.setSelectedEmotion(Emotion.BAD));
        binding.tvRecordItemMad.setOnClickListener(v -> presenter.setSelectedEmotion(Emotion.VERY_BAD));

        presenter.loadDiaryList(LOAD_ITEM_NUM);
    }

    private void initAdapter() {
        diaryListAdapter = new DiaryListAdapter(context);
        hashTagListAdapter = new HashTagListAdapter(context);
        hashTagListAdapter.setItemClickListener(pos -> hashTagListAdapter.removeItem(pos));
    }

    private void clearDiary() {
        hashTagListAdapter.clearItems();
        binding.etItemRecordInput.setText("");
        presenter.setSelectedEmotion(null);
    }
}
