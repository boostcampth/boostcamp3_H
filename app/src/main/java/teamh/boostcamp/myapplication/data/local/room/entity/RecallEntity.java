package teamh.boostcamp.myapplication.data.local.room.entity;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import teamh.boostcamp.myapplication.data.model.Emotion;

@Entity(tableName = "recalls")
public class RecallEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "createdDate")
    @NonNull
    private Date createdDate;

    @ColumnInfo(name = "emotion")
    @NonNull
    private Emotion emotion;

    public RecallEntity(int id, @NonNull Date createdDate, Emotion emotion) {
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

    @NonNull
    public Emotion getEmotion() {
        return emotion;
    }
}
