package teamh.boostcamp.myapplication.view.recall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.Recall;
import teamh.boostcamp.myapplication.data.repository.RecallRepositoryImpl;
import teamh.boostcamp.myapplication.databinding.FragmentRecallBinding;

public class RecallFragment extends Fragment implements RecallView {

    @NonNull
    private RecallPresenter recallPresenter;
    @NonNull
    private FragmentRecallBinding binding;
    @NonNull
    private RecallListAdapter recallListAdapter;

    public RecallFragment() {
    }

    public static RecallFragment newInstance() {
        return new RecallFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recall, container, false);
        initPresenter();
        initViews();
        recallPresenter.loadRecallList();
        return binding.getRoot();
    }

    @Override
    public void addRecallList(@NonNull List<Recall> recallList) {
        recallListAdapter.updateItems(recallList);
    }

    private void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        binding.rvCard.setHasFixedSize(true);
        binding.rvCard.setVerticalScrollbarPosition(0);
        recallListAdapter = new RecallListAdapter(getContext());
        binding.rvCard.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCard.setAdapter(recallListAdapter);
    }

    private void initPresenter() {
        recallPresenter = new RecallPresenter(this,
                RecallRepositoryImpl.getInstance(AppDatabase.getInstance(getContext())));
    }

}
