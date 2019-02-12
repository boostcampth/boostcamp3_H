package teamh.boostcamp.myapplication.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

/*
 * KeyPad Util : SoftKeyPad 숨기기 기능 */
public class KeyPadUtil {
    public static void closeKeyPad(Context context, EditText editText) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        editText.clearFocus();

        if(inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }
}
