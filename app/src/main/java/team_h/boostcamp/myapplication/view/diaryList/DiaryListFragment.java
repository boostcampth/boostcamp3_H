package team_h.boostcamp.myapplication.view.diaryList;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.FragmentDiaryListBinding;
import team_h.boostcamp.myapplication.view.BaseFragment;
import team_h.boostcamp.myapplication.view.BasePresenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DiaryListFragment extends BaseFragment<FragmentDiaryListBinding> {

    public DiaryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_diary_list;
    }

    @Override
    public BasePresenter generatePresenter() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
