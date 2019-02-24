package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.NonNull;

public enum Emotion {

    VERY_BAD(0,"\uD83D\uDE21", "오늘은 많이 힘든 하루셨나요?"),
    BAD(1,"\uD83D\uDE1E", "오늘은 별로였나보네요.."),
    NEUTRAL(2,"\uD83D\uDE10", "평범한 날들 중 하루였군요"),
    GOOD(3,"\uD83D\uDE0A", "오늘은 좋은 일이 있었나보네요 !"),
    VERY_GOOD(4,"\uD83D\uDE0D", "개발자도 행복해지고싶어..");

    private final int emotion;
    private final String emoji;
    private final String comment;

    Emotion(int emotion, @NonNull final String emoji, @NonNull final String comment) {
        this.emotion = emotion;
        this.emoji = emoji;
        this.comment = comment;
    }

    public int getEmotion() {
        return emotion;
    }

    public String getEmoji(){
        return emoji;
    }

    public String getComment() {return comment;}

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
