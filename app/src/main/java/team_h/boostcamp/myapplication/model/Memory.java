package team_h.boostcamp.myapplication.model;

import java.util.List;

public class Memory {

    //dummy용 파일입니다. edited by hwajung

    private String title;
    private List<String> memories;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getMemories() {
        return memories;
    }

    public void setMemories(List<String> memories) {
        this.memories = memories;
    }

    public Memory(String title, List<String> memories) {
        this.title = title;
        this.memories = memories;
    }
}
