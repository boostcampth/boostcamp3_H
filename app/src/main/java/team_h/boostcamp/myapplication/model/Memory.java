package team_h.boostcamp.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "memory")
public class Memory implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    private int id;

    @ColumnInfo(name = "title")
    @NonNull
    private String title;

    @ColumnInfo(name = "date")
    @NonNull
    private String date;

    @ColumnInfo(name = "selectedEmotion")
    @NonNull
    private int selectedEmotion;

    public Memory(int id, @NonNull String title, @NonNull String date, int selectedEmotion) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.selectedEmotion = selectedEmotion;
    }

    protected Memory(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
        selectedEmotion = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeInt(selectedEmotion);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public int getSelectedEmotion() {
        return selectedEmotion;
    }

    public void setSelectedEmotion(int selectedEmotion) {
        this.selectedEmotion = selectedEmotion;
    }
}
