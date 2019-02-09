package teamh.boostcamp.myapplication.data.model;

public enum Emotion {

    VERY_BAD(0), BAD(1), NEUTRAL(2), GOOD(3), VERY_GOOD(4);

    private final int emotion;

    Emotion(int emotion) {
        this.emotion = emotion;
    }

    public int getEmotion() {
        return emotion;
    }

    public static Emotion fromInteger(final int emotion) {
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
