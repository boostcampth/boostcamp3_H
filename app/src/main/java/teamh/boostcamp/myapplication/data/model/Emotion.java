package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.NonNull;

public enum Emotion {

    VERY_BAD(0,"\uD83D\uDE21"), BAD(1,"\uD83D\uDE1E"), NEUTRAL(2,"\uD83D\uDE10"), GOOD(3,"\uD83D\uDE0A"), VERY_GOOD(4,"\uD83D\uDE0D");

    private final int emotion;
    private final String emoji;

    Emotion(int emotion, @NonNull final String emoji) {
        this.emotion = emotion;
        this.emoji = emoji;
    }

    public int getEmotion() {
        return emotion;
    }

    public String getEmoji(){
        return emoji;
    }

    public static Emotion fromValue(final int emotion) {
        switch (emotion) {
            case 0:
                return VERY_BAD;
            case 1:
                return BAD;
            case 2:
                return NEUTRAL;
            case 3:
                return GOOD;
            case 4:
                return VERY_GOOD;
            default:
                return NEUTRAL;
        }
    }
}
