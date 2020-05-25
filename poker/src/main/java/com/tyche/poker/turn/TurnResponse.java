package com.tyche.poker.turn;

public class TurnResponse {

    String content;
    String icon;
    String mood;

    public TurnResponse(String content, String icon, String mood){
        this.content = content;
        this.icon = icon;
        this.mood = mood;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}






