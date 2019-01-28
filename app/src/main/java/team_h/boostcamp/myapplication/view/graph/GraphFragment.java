package team_h.boostcamp.myapplication.view.graph;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.FragmentGraphBinding;
import team_h.boostcamp.myapplication.view.BaseFragment;

public class GraphFragment extends BaseFragment<FragmentGraphBinding, GraphPresenter>
        implements GraphContractor.View {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(getLayoutId(),container,
                false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Presenter 설정
        mBinding.setPresenter(getPresenter());
    }

    @Override
    public void showToastMessage(String message, int exposedTime) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_graph;
    }

    @Override
    protected GraphPresenter getPresenter() {
        return new GraphPresenter(GraphFragment.this);
    }


}
