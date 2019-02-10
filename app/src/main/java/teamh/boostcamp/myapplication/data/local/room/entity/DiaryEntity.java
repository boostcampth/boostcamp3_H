package teamh.boostcamp.myapplication.data.local.room.entity;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;

/* Legacy 와 table 이름 충돌때문에 임시로 _ 설정 */
@Entity(tableName = "diaries")
public class DiaryEntity {

    // DB 에서 인식하기 위한 PK
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final int id;

    // 저장한 날
    @ColumnInfo(name = "recordDate")
    @NonNull
    private final Date recordDate;

    // 저장된 파일 경로
    @ColumnInfo(name = "recordFilePath")
    @NonNull
    private final String recordFilePath;

    // 설정한 태그들
    @ColumnInfo(name = "tags")
    @Nullable
    private final List<String> tags;

    // 선택한 감정 번호
    @ColumnInfo(name = "selectedEmotion")
    @NonNull
    private final Emotion selectedEmotion;

    // API 를 통해 분석된 감정
    @ColumnInfo(name = "analyzedEmotion")
    @NonNull
    private final Emotion analyzedEmotion;

    public DiaryEntity(final int id,
                       @NonNull Date recordDate,
                       @NonNull String recordFilePath,
                       @Nullable List<String> tags,
                       @NonNull Emotion selectedEmotion,
                       @NonNull Emotion analyzedEmotion) {
        this.id = id;
        this.recordDate = recordDate;
        this.recordFilePath = recordFilePath;
        this.tags = tags;
        this.selectedEmotion = selectedEmotion;
        this.analyzedEmotion = analyzedEmotion;
    }
    public int getId() {
        return id;
    }

    @NonNull
    public Date getRecordDate() {
        return recordDate;
    }

    @Nullable
    public List<String> getTags() {
        return tags;
    }

    @NonNull
    public String getRecordFilePath() {
        return recordFilePath;
    }

    @NonNull
    public Emotion getSelectedEmotion() {
        return selectedEmotion;
    }

    @NonNull
    public Emotion getAnalyzedEmotion() {
        return analyzedEmotion;
    }


    @NonNull
    public static DiaryEntity[] generateSampleDiaryData() {
        final String filePath = "/storage/emulated/0/2019-02-08.acc";
        final File file = new File("/storage/emulated/0/2019-02-08.acc");

        if (!file.exists()) {
            try {
                boolean isCreated = file.createNewFile();
                if (!isCreated) {
                    Log.e("Test", "파일 생성 실패");
                } else {
                    Log.e("Test", "파일 생성 성공");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Random random = new Random();

        List<DiaryEntity> samples = new ArrayList<>();

        final long TODAY = new Date().getTime();
        final long DAY = 86400L;

        for (int i = 1; i <= 20; ++i) {
            samples.add(new DiaryEntity(
                    i,
                    new Date(TODAY - DAY * i),
                    filePath,
                    Arrays.asList("#%2d번"),
                    Emotion.fromValue(Math.abs(random.nextInt() % 5)),
                    Emotion.fromValue(Math.abs(random.nextInt() % 5))
            ));
        }

        DiaryEntity[] temp = new DiaryEntity[samples.size()];
        return samples.toArray(temp);
    }
}
