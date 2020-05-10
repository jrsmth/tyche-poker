package com.tyche.poker.requests;

public class TurnRequest {

    String uuid;
    String action;
    String betValue;


    public TurnRequest(String uuid, String action, String betValue) {
        this.uuid = uuid;
        this.action = action;
        this.betValue = betValue;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBetValue() {
        return betValue;
    }

    public void setBetValue(String betValue) {
        this.betValue = betValue;
    }
}
