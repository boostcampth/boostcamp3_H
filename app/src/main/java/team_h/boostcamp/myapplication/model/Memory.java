package team_h.boostcamp.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Memory implements Parcelable {

    //dummy용 파일입니다.

    private String title;
    private List<String> memories;

    protected Memory(Parcel in) {
        title = in.readString();
        memories = in.createStringArrayList();
    }

    public static final Creator<Memory> CREATOR = new Creator<Memory>() {
        @Override
        public Memory createFromParcel(Parcel in) {
            return new Memory(in);
        }

        @Override
        public Memory[] newArray(int size) {
            return new Memory[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getMemories() {
        return memories;
    }

    public void setMemories(List<String> memories) {
        this.memories = memories;
    }

    public Memory(String title, List<String> memories) {
        this.title = title;
        this.memories = memories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringList(memories);
    }
}
