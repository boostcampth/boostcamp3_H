package teamh.boostcamp.myapplication.view.password;

import android.content.Context;

public class LockManager {

    private static volatile LockManager INSTANCE;
    private LockHelper lockHelper;

    private LockManager() {

    }

    public static LockManager getInstance() {
        if (INSTANCE == null) {
            synchronized (LockManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LockManager();
                }
            }
        }
        return INSTANCE;
    }

    public LockHelper getLockHelper(Context context) {
        if (lockHelper == null) {
            lockHelper = new LockHelperImpl(context.getApplicationContext());
        }
        return lockHelper;
    }
}
