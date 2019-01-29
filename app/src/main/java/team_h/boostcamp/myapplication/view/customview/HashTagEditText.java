package team_h.boostcamp.myapplication.view.customview;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import team_h.boostcamp.myapplication.view.adapter.AdapterContract;

/*
 * 어답터와 연결하여 사용하는 HashTagEditText */
public class HashTagEditText extends AppCompatEditText {

    private AdapterContract.Model<String> mHashListAdapter;

    public HashTagEditText(Context context) {
        super(context);
    }

    public HashTagEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HashTagEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
     * 입력된 해시태그가 저장될 어답터를 등록하여 사용
     * 스페이스가 입력되면 이전까지의 내용을 하나의 아이템으로 Adapter 에 추가 */
    public void setHashListAdapter(@NonNull AdapterContract.Model<String> adapter) {
        this.mHashListAdapter = adapter;
        initFilter();
    }

    private void initFilter() {
        // HashTag 필터로 Space 단위로 문자열을 구분한다.
        setFilters(new InputFilter[]{
                (source, start, end, dest, dStart, dEnd) -> {
                    for (int i = start; i < end; ++i) {
                        if (Character.isSpaceChar(source.charAt(i))) {
                            mHashListAdapter.addItem("#" + getText().toString().trim());
                            setText("");
                            return null;
                        }
                    }
                    return null;
                }
        });
    }
}
