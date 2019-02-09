package teamh.boostcamp.myapplication.data.model;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recall")
public class RecallEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "createdDate")
    @NonNull
    private Date createdDate;

    @ColumnInfo(name = "emotion")
    @NonNull
    private int emotion;

    public RecallEntity(int id, @NonNull Date createdDate, int emotion) {
        this.id = id;
        this.createdDate = createdDate;
        this.emotion = emotion;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public Date getCreatedDate() {
        return createdDate;
    }

    public int getEmotion() {
        return emotion;
    }
}
