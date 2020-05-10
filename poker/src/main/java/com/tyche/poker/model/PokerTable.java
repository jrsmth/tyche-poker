package com.tyche.poker.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PokerTable {

    @Id
    private String uuid;
    private int pot;
    private String flop0;
    private String flop1;
    private String flop2;
    private String turn;
    private String river;
    private int currentBet;


    public PokerTable() {
        this.flop0 = "reverse";
        this.flop1 = "reverse";
        this.flop2 = "reverse";
        this.turn = "reverse";
        this.river = "reverse";
    }


    public PokerTable(String flop0, String flop1, String flop2, String turn, String river) {
        this.flop0 = flop0;
        this.flop1 = flop1;
        this.flop2 = flop2;
        this.turn = turn;
        this.river = river;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    public String getFlop0() {
        return flop0;
    }

    public void setFlop0(String flop0) {
        this.flop0 = flop0;
    }

    public String getFlop1() {
        return flop1;
    }

    public void setFlop1(String flop1) {
        this.flop1 = flop1;
    }

    public String getFlop2() {
        return flop2;
    }

    public void setFlop2(String flop2) {
        this.flop2 = flop2;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getRiver() {
        return river;
    }

    public void setRiver(String river) {
        this.river = river;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
    }


}
