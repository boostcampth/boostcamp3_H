package team_h.boostcamp.myapplication.view.memories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.FragmentMemoriesBinding;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.view.BaseFragment;


public class MemoriesFragment extends BaseFragment<FragmentMemoriesBinding> implements MemoriesContractor.View{

    private MemoriesContractor.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // binding할 때 만들어지는 View를 바로 리턴하면 된다.
        // getRoot()를 통해서!
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        mBinding.rvCard.setHasFixedSize(true);
        MemoriesCardAdapter adapter = new MemoriesCardAdapter(getContext());
        mBinding.rvCard.setAdapter(adapter);
        mBinding.rvCard.setLayoutManager(new LinearLayoutManager(getContext()));


        mBinding.rvCard.setVerticalScrollbarPosition(0);
        //dummy data
        List<String> diaryList = Arrays.asList("1", "2", "3", "4", "5");
        adapter.addItem(new Memory("Happy of January", diaryList));
        adapter.addItem(new Memory("Sad of January", diaryList));
        adapter.addItem(new Memory("Not bad of January", diaryList));
        adapter.addItem(new Memory("Not bad of January", diaryList));
        adapter.addItem(new Memory("Not bad of January", diaryList));
        adapter.addItem(new Memory("Not bad of January", diaryList));
        adapter.addItem(new Memory("Not bad of January", diaryList));
        adapter.addItem(new Memory("Not bad of January", diaryList));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_memories;
    }

    @Override
    public MemoriesContractor.Presenter generatePresenter() {
        if(mPresenter == null){
            mPresenter = new MemoriesPresenter(MemoriesFragment.this);
        }
        return mPresenter;
    }
}
