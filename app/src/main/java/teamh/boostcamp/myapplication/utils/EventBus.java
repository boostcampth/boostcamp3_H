package teamh.boostcamp.myapplication.utils;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import teamh.boostcamp.myapplication.data.model.Event;

public class EventBus {

    private static PublishSubject<Event> sSubject = PublishSubject.create();

    private EventBus() { }

    public static Observable<Event> get() {
        return sSubject;
    }

    public static void sendEvent(@NonNull Event message) {
        sSubject.onNext(message);
    }
}
