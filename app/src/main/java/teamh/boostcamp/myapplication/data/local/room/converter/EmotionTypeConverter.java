package teamh.boostcamp.myapplication.data.local.room.converter;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;
import teamh.boostcamp.myapplication.data.model.Emotion;

public class EmotionTypeConverter {

    @TypeConverter
    public static int emotionToInteger(@NonNull Emotion emotion) {
        return emotion.getEmotion();
    }

    @TypeConverter
    public static Emotion IntegerToEmotion(final int emotion) {
        return Emotion.fromInteger(emotion);
    }
}
