package team_h.boostcamp.myapplication.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

/*
 * Presenter 에서 Resource 접근을 위한 Class 작성 */
public class ResourceSendUtil {

    private Context mContext;

    public ResourceSendUtil(@NonNull Context mContext) {
        this.mContext = mContext;
    }

    public Drawable getDrawable(@NonNull final int id) {
        return mContext.getResources().getDrawable(id);
    }

    public int getColor(@NonNull final int id) {
        return mContext.getResources().getColor(id);
    }

    public String getString(@NonNull final int id) {
        return mContext.getResources().getString(id);
    }
}
