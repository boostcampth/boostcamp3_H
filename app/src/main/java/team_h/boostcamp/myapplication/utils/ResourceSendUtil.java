package team_h.boostcamp.myapplication.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/*
 * Presenter 에서 Resource 접근을 위한 Class 작성 */
public class ResourceSendUtil {

    private Context mContext;

    public ResourceSendUtil(@NonNull Context mContext) {
        this.mContext = mContext;
    }

    /* Depreciated 된 함수 수정 */
    public Drawable getDrawable(@NonNull final int id) {
        return ContextCompat.getDrawable(mContext, id);
    }

    public int getColor(@NonNull final int id) {
        return ContextCompat.getColor(mContext, id);
    }

    public String getString(@NonNull final int id) {
        return mContext.getResources().getString(id);
    }

    public String[] getStringArray(@NonNull final int id) {
        return mContext.getResources().getStringArray(id);
    }
}
