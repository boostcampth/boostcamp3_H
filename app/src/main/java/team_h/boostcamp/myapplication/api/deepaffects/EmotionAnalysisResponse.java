package team_h.boostcamp.myapplication.api.deepaffects;

/*
 * 분석된 감정 결과를 나타내는 Data class */
public class EmotionAnalysisResponse {

    private String emotion;
    private Float start;
    private Float end;

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public Float getStart() {
        return start;
    }

    public void setStart(Float start) {
        this.start = start;
    }

    public Float getEnd() {
        return end;
    }

    public void setEnd(Float end) {
        this.end = end;
    }
}
