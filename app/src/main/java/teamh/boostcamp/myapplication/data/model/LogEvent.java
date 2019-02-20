package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.NonNull;

public enum LogEvent {

    RECORD_DIARY_BUTTON_CLICK("buttonClick", "recordDiary", "recordButtonClick", "buttonClick"),
    DONE_BUTTON_CLICK("buttonClick", "recordDone", "recordDoneButtonClick", "save record");

    private final String eventName;
    private final String itemId;
    private final String itemName;
    private final String contentType;

    LogEvent(@NonNull String eventName,
             @NonNull String itemId,
             @NonNull String itemName,
             @NonNull String contentType) {
        this.eventName = eventName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.contentType = contentType;
    }

    @NonNull
    public String getEventName() {
        return eventName;
    }

    @NonNull
    public String getItemId() {
        return itemId;
    }

    @NonNull
    public String getItemName() {
        return itemName;
    }

    @NonNull
    public String getContentType() {
        return contentType;
    }
}
