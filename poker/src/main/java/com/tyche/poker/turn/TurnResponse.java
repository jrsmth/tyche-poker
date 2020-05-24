package com.tyche.poker.turn;

public class TurnResponse {

    String uuid;
    String action;
    int betValue;
    String status;

    public TurnResponse(String uuid, String action, int betValue, String status){
        this.uuid = uuid;
        this.action = action;
        this.betValue = betValue;
        this.status = status;
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

    public int getBetValue() {
        return betValue;
    }

    public void setBetValue(int betValue) {
        this.betValue = betValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}






