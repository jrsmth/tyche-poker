package com.tyche.poker;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tyche.poker.model.PokerTable;
import com.tyche.poker.model.User;

import java.security.SecureRandom;
import java.util.*;

public class Card {


    final String[] suits = {"♣", "♠", "♥", "♦"};


    final String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};



    String rank;


    String suit;


    Card() {
        boolean cardUnique = false;

        dupTest:
            while(!cardUnique){
                rank = ranks[new SecureRandom().nextInt(ranks.length)];
                suit = suits[new SecureRandom().nextInt(suits.length)];
                String cardTest = rank + suit;
                for(String card : cardsInPlay){
                    if(card.equals(cardTest)){
                        System.out.println("Caught a duplicate: " + cardTest);
                        continue dupTest;
                    }
                }
                cardUnique = true;
            }
    }


    public static List<String> cardsInPlay = new ArrayList<>(); // prevent duplicate cards from being in play


    public static List<User> compareCards(List<User> userList, PokerTable thisTable){

        // out of all the hands, where user has not folded, which hand(s) win
        // return the users with that hand(s)

        // what are the hands? 7 cards (table + user)
        ArrayList<String[]> userHands = new ArrayList<>();
        StringBuilder body = new StringBuilder("{ \"userHands\": [");
        int i = 0;
        for (User user : userList){
            String thisHand = user.getCard0() + " " + user.getCard1() + " " + thisTable.getFlop0() + " " + thisTable.getFlop1() + " " + thisTable.getFlop2() + " " + thisTable.getTurn() + " " + thisTable.getRiver();
            String thisUserHand = "{ \"uuid\": \"" + user.getUuid() + "\", \"cards\": \"" + thisHand + "\" }";
            body.append(thisUserHand);
            if(i < userList.size() - 1)  body.append(", ");
            i++;
        }

        body.append("] }");
        System.out.println(body);

        List<User> topDogs = new ArrayList<>();

        // call tyche-evaluate to get the winning uuid(s)
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.post("http://localhost:3000/winners")
                    .header("Content-Type", "application/json")
                    .body(body.toString())
                    .asString();
            String responseStr = response.getBody().substring(2,response.getBody().length()-2);
            String[] dogs = responseStr.split(",");
            for (String dog : dogs){
                for (User user : userList){
                    if (user.getUuid().equals(dog)) topDogs.add(user);
                }
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        for(User dog : topDogs){
            System.out.println("winner: " + dog.getUuid());
        }

        return topDogs;
    }


    @Override
    public String toString() {
        return rank + suit;
    }

}
