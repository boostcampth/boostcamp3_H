package team_h.boostcamp.myapplication.view.graph;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.FragmentGraphBinding;
import team_h.boostcamp.myapplication.utils.ResourceSendUtil;
import team_h.boostcamp.myapplication.view.BaseFragment;

/**
 * 추상 클래스인 BaseFragment를 상속받음.
 * 추상 클래스의 추상 메소드를 구현해야 한다.
 */
public class GraphFragment extends BaseFragment<FragmentGraphBinding> implements GraphContractor.View {

    private GraphContractor.Presenter mPresenter;
    private String[] mHashTags;
    LayoutInflater mInflater;
    View mTagView;
    TextView mTagTextView;
    private ResourceSendUtil mResourceSendUtil;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // binding할 때 만들어지는 View를 바로 리턴하면 된다.
        // getRoot()를 통해서!
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mResourceSendUtil == null) {
            mResourceSendUtil = new ResourceSendUtil(mContext);
        }
        // Presenter 설정
        mPresenter = generatePresenter();
        mPresenter.onViewAttached();

        binding.lcEmotionGraph.setBackgroundColor(Color.TRANSPARENT);
        binding.lcEmotionGraph.setDoubleTapToZoomEnabled(false);
        binding.lcEmotionGraph.setDrawGridBackground(false);
        binding.lcEmotionGraph.animateY(2000, Easing.EaseInCubic);
        binding.lcEmotionGraph.invalidate();
    }

    @Override
    public GraphContractor.Presenter generatePresenter() {
        if (mPresenter == null)
            mPresenter = new GraphPresenter(GraphFragment.this, new ResourceSendUtil(mContext));
        return mPresenter;
    }

    @Override
    public void setLineData(LineData lineData) {
        binding.lcEmotionGraph.setData(lineData);
    }

    @Override
    public XAxis getXAxis() {
        return binding.lcEmotionGraph.getXAxis();
    }

    @Override
    public YAxis getYLeftAxis() {
        return binding.lcEmotionGraph.getAxisLeft();
    }

    @Override
    public YAxis getYRightAxis() {
        return binding.lcEmotionGraph.getAxisRight();
    }

    @Override
    public void loadHastTagWord(String[] hashTags) {
        mHashTags = hashTags;
        mInflater = getLayoutInflater();

        for (int i = 0; i < mHashTags.length; i++) {
            mTagView = mInflater.inflate(R.layout.layout_graph_hash_tag, null, false);
            mTagTextView = mTagView.findViewById(R.id.tv_hash_tag);

            if (i % 3 == 0) {
                mTagTextView.setTextColor(mResourceSendUtil.getColor(R.color.graphColor));
            }
            mTagTextView.setTextSize(30f);
            mTagTextView.setText(mHashTags[i]);
            binding.hashTagCustomLayout.addView(mTagView);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_graph;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onViewDetached();
    }
}
