package teamh.boostcamp.myapplication.data.local.room.converter;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;
import teamh.boostcamp.myapplication.data.model.Emotion;

public class EmotionTypeConverter {

    @TypeConverter
    public static int fromEmotion(@NonNull Emotion emotion) {
        return emotion.getEmotion();
    }

    @TypeConverter
    public static Emotion fromValue(final int emotion) {
        return Emotion.fromValue(emotion);
    }
}
