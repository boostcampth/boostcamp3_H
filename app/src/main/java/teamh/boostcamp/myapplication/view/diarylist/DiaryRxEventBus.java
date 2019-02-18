package teamh.boostcamp.myapplication.view.diarylist;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class DiaryRxEventBus {

    private static PublishSubject<String> sSubject = PublishSubject.create();

    private DiaryRxEventBus() { }

    public static Observable get() {
        return sSubject;
    }

    public static void sendEvent(@NonNull String message) {
        sSubject.onNext(message);
    }
}
