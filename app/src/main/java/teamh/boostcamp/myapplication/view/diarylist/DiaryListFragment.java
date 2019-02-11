package teamh.boostcamp.myapplication.view.diarylist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.databinding.FragmentDiaryListBinding;

public class DiaryListFragment extends Fragment implements DiaryListView {

    private static final int LOAD_ITEM_NUM = 3;
    private static final int NEW_ITEM_LOAD = 1;

    @NonNull
    private Context context;
    @NonNull
    private DiaryListPresenter presenter;
    @NonNull
    private CompositeDisposable compositeDisposable;
    @NonNull
    private FragmentDiaryListBinding binding;
    @NonNull
    private DiaryListAdapter diaryListAdapter;

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

        presenter = new DiaryListPresenter(this,
                DiaryRepositoryImpl.getInstance(AppDatabase.getInstance(context).diaryDao(), DeepAffectApiClient.getInstance()),
                new DiaryRecorderImpl());

        diaryListAdapter = new DiaryListAdapter(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary_list, container, false);

        binding.recyclerViewMainList.setNestedScrollingEnabled(false);
        binding.recyclerViewMainList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.recyclerViewMainList.setAdapter(diaryListAdapter);
        binding.recyclerViewMainList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)) {
                    presenter.loadDiaryList(LOAD_ITEM_NUM);
                }
            }
        });

        // TODO xml 변수 바인딩
        presenter.loadDiaryList(LOAD_ITEM_NUM);

        return binding.getRoot();
    }

    @Override
    public void addDiaryList(@NonNull List<Diary> diaryList) {
        diaryListAdapter.addDiaryList(diaryList);
    }

    @Override
    public void notifyTodayDiarySaved() {
        presenter.loadDiaryList(NEW_ITEM_LOAD);
    }

    @Override
    public void showLoadDiaryListFailMsg() {
        showToastMessage(R.string.item_record_load_diary_list_fail);
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
}
