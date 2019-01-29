package team_h.boostcamp.myapplication.view.graph;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.FragmentGraphBinding;
import team_h.boostcamp.myapplication.view.BaseFragment;

/**
 * 추상 클래스인 BaseFragment를 상속받음.
 * 추상 클래스의 추상 메소드를 구현해야 한다.
 */
public class GraphFragment extends BaseFragment<FragmentGraphBinding, GraphPresenter>
        implements GraphContractor.View {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.onViewAttached();
        // Presenter 설정
        mBinding.setPresenter(mPresenter);
        mBinding.lcEmotionGraph.setBackgroundColor(Color.TRANSPARENT);
        mBinding.lcEmotionGraph.setDoubleTapToZoomEnabled(false);
        mBinding.lcEmotionGraph.setDrawGridBackground(false);
        mBinding.lcEmotionGraph.animateY(2000, Easing.EaseInCubic);
        mBinding.lcEmotionGraph.invalidate();

    }

    @Override
    public void showToastMessage(String message, int exposedTime) {

    }

    @Override
    public void setLineData(LineData lineData) {
        mBinding.lcEmotionGraph.setData(lineData);
    }

    @Override
    public XAxis getXAxis() {
        return mBinding.lcEmotionGraph.getXAxis();
    }

    @Override
    public YAxis getYLeftAxis() {
        return mBinding.lcEmotionGraph.getAxisLeft();
    }

    @Override
    public YAxis getYRightAxis() {
        return mBinding.lcEmotionGraph.getAxisRight();
    }

    @Override
    public void setDescription(Description description) {
        mBinding.lcEmotionGraph.setDescription(description);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_graph;
    }

    @Override
    protected GraphPresenter getPresenter() {
        return new GraphPresenter(GraphFragment.this, getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onViewDetached();
    }
}
