package teamh.boostcamp.myapplication.data.model;

public enum EmotionType {

    selectedEmotion(0), analyzedEmotion(1);

    private final int emotionType;

    EmotionType(int emotionType) {
        this.emotionType = emotionType;
    }

    public int getEmotionType() {
        return emotionType;
    }

}
