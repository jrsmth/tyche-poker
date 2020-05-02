package com.tyche.poker;

import com.tyche.poker.model.PokerTable;
import com.tyche.poker.model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;

public class Card {


    final String[] suits = {"♣", "♠", "♥", "♦"};


    final String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};



    String rank;


    String suit;


    Card() {
        rank = ranks[new SecureRandom().nextInt(ranks.length)];
        suit = suits[new SecureRandom().nextInt(suits.length)];
    }


    private static final SecureRandom random = new SecureRandom();


    // STEP #2: which card is highest - delete me?
//    public static List<User> compareCardz(List<User> userList){
//
//        // find highest value, return the user(s) with this value
//            // sort userList based on stringToValue[user.getCard01()] <- need to extract the rank!
//            // find the highest value
//            // return all users with this value
//
//        ArrayList<User> topDogs = new ArrayList<>();
//        int highestRank = 0;
//
//        for(User user : userList){
//            String[] rankValueList = user.getCard0().split(", ");
//            int rank = stringToValue.get(rankValueList[0]);
//            if(rank > highestRank) { highestRank = rank; }
//            System.out.println("highest rank: " + highestRank);
//        }
//
//        for(User user:userList){
//            String[] rankValueList = user.getCard0().split(", ");
//            int rank = stringToValue.get(rankValueList[0]);
//            System.out.println("highest rank check: " + highestRank);
//            System.out.println("rank check: " + highestRank);
//            if(rank == highestRank) {
//                topDogs.add(user);
//                System.out.println("winner: " + user.getName());
//            }
//        }
//
//        return topDogs;
//    }


    // STEP #3: which hand is best
    public static List<User> compareCards(List<User> userList, PokerTable thisTable){

        // out of all the hands, where user has not folded, which hand(s) win
        // return the users with that hand(s)

        // what are the hands? 7 cards (table + user)
        ArrayList<String[]> userHands = new ArrayList<>();
        StringBuilder body = new StringBuilder("{ \"userHands\": [");
        for (User user : userList){
            String thisHand = user.getCard0() + user.getCard1() + thisTable.getFlop0() + thisTable.getFlop1() + thisTable.getFlop2() + thisTable.getTurn()+ thisTable.getRiver();
            String thisUserHand = "{ \"uuid\": \"" + user.getUuid() + "\", \"cards\": \"" + thisHand + "\" }";
            body.append(thisUserHand + ", ");
        }

        body.setLength(body.length() - 1);
        body.append("] }");

        System.out.println(body); //test

        // call tyche-evaluate to get the winning uuid(s)
        String url = "http://localhost:3000/winners";
        try {
            URL serverUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);

            // build the body:

        } catch (Exception e) {
            e.printStackTrace();
        }
        



        return null;
    }


    @Override
    public String toString() {
        return rank + " " + suit;
    }

}
