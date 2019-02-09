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
}
