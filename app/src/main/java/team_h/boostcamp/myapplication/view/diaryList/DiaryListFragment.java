package team_h.boostcamp.myapplication.view.diaryList;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.databinding.FragmentDiaryListBinding;
import team_h.boostcamp.myapplication.utils.KeyPadUtil;
import team_h.boostcamp.myapplication.utils.ResourceSendUtil;
import team_h.boostcamp.myapplication.view.BaseFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tedpark.tedpermission.rx2.TedRx2Permission;

public class DiaryListFragment extends BaseFragment<FragmentDiaryListBinding> implements DiaryContract.View{

    private Context mContext;

    private DiaryPresenter presenter;
    private HashTagListAdapter mHashTagListAdapter;

    public DiaryListFragment() {/*Required empty public constructor*/}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // presenter 생성. 1 : 1 관계 유지
        presenter = generatePresenter();
        // XML presenter 등록
        mBinding.setPresenter(presenter);

        // View 초기화
        initView();

        presenter.onViewAttached();

        return mBinding.getRoot();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_diary_list;
    }

    @Override
    public DiaryPresenter generatePresenter() {
        if(presenter == null) {
            presenter = new DiaryPresenter(this, new ResourceSendUtil(mContext));
        }
        return presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onViewDetached();
        presenter = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 확인 버튼 이벤트
        mBinding.buttonItemRecordDone.setOnClickListener(v -> presenter.onDoneButtonClicked());

        // 녹음 버튼 이벤트
        mBinding.buttonRecordItemRecord.setOnClickListener(v ->
            TedRx2Permission.with(mContext)
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                    .setRationaleMessage(getString(R.string.item_record_permission_msg))
                    .setRationaleTitle(getString(R.string.item_record_permission_title))
                    .request()
                    .subscribe(tedPermissionResult -> {
                        if(tedPermissionResult.isGranted()) {
                            // KeyPad 가 열려있으면 닫아주기
                            KeyPadUtil.closeKeyPad(mContext, mBinding.hashTagEditTextItemRecordInput);
                            // 버튼 클릭 이벤트 처리
                            presenter.onRecordButtonClicked();
                        } else {
                            // 요구한 권한이 하나라도 없으면 토스트 메시지
                            Toast.makeText(mContext, getString(R.string.item_record_permission_denied), Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                        // 에러 출력
                        Log.e("Test", error.getMessage());
                    })
        );

        // 저장 버튼 클릭
        mBinding.buttonItemRecordDone.setOnClickListener(v -> presenter.onDoneButtonClicked());
    }

    /* View 초기화 */
    private void initView() {
        // Tag RecyclerView, Adapter 설정
        mHashTagListAdapter = new HashTagListAdapter(getContext());
        mBinding.recyclerViewItemRecordTags
                .setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mBinding.recyclerViewItemRecordTags.setAdapter(mHashTagListAdapter);

        // Adapter 와 해시태그 EditText 와 연결
        mBinding.hashTagEditTextItemRecordInput.setHashListAdapter(mHashTagListAdapter);

        // Presenter 에 Adapter 등록
        presenter.setHashTagListModelAdapter(mHashTagListAdapter);
        presenter.setHashTagListModelAdapter(mHashTagListAdapter);

    }
}
