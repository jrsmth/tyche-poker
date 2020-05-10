package com.tyche.poker.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This tells Hibernate to make a table out of this class
@Table(name="game_user") // User is reserved in POSTGRESQL
public class User {

    @Id
    private String uuid;
    private String name;
    private int chips;
    private String card0;
    private String card1;
    private boolean myTurn;
    private boolean fold;
    private int myBet;


    public User() {
        this.card0 = "reverse";
        this.card1 = "reverse";
    }

    public User(String uuid, String card0, String card1) {
        this.uuid = uuid;
        this.card0 = card0;
        this.card1 = card1;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public String getCard0() {
        return card0;
    }

    public void setCard0(String card0) {
        this.card0 = card0;
    }

    public String getCard1() {
        return card1;
    }

    public void setCard1(String card1) {
        this.card1 = card1;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public boolean isFold() {
        return fold;
    }

    public void setFold(boolean fold) {
        this.fold = fold;
    }

    public int getMyBet() {
        return myBet;
    }

    public void setMyBet(int myBet) {
        this.myBet = myBet;
    }


}
