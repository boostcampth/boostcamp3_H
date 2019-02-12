package teamh.boostcamp.myapplication.view.password;

import android.app.Application;

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


    public void enableLock(Application application) {
        if (lockHelper == null) {
            lockHelper = new LockHelperImpl(application);
        }
        lockHelper.enable();
    }


    public LockHelper getLockHelper() {
        return lockHelper;
    }


}
