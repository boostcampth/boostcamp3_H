package teamh.boostcamp.myapplication.view.diarylist;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

public class HashTagInputEditText extends AppCompatEditText implements TextWatcher {

    private HashTagListAdapter adapter;

    public HashTagInputEditText(Context context) {
        super(context);
        this.addTextChangedListener(this);
    }

    public HashTagInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addTextChangedListener(this);
    }

    public HashTagInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.addTextChangedListener(this);
    }

    public void setHashTagListAdapter(@NonNull HashTagListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void afterTextChanged(Editable editable) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (adapter != null && count != 0 && !TextUtils.isEmpty(charSequence)) {
            if (charSequence.charAt(start) == '#') {
                setText(getText().toString().substring(0, start));
                setSelection(start);
            } else if (Character.isSpaceChar(charSequence.charAt(start))) {
                adapter.addItem("#" + getText().toString().trim());
                setText("");
            }
        }
    }
}
