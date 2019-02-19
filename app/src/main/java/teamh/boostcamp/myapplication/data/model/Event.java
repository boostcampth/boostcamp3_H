package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.NonNull;

public enum Event {

    WRONG_EVENT(-1),
    BACK_UP_COMPLETE(0),
    CLEAR_COMPLETE(1);

    private final int event;

    Event(final int event) {
        this.event = event;
    }

    @NonNull
    public Event fromValue(final int event) {
        switch (event) {
            case 0:
                return BACK_UP_COMPLETE;
            default:
                return WRONG_EVENT;
        }
    }

    public final int getEvent() {
        return event;
    }

}
