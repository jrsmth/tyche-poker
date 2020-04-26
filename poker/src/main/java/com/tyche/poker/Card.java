package com.tyche.poker;

import com.tyche.poker.model.User;

import java.security.SecureRandom;
import java.util.*;

public class Card {


    enum Suit {
        HEARTS,
        SPADES,
        CLUBS,
        DIAMONDS
    }


    enum Rank {
        ACE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING
    }


    Rank rank;
    Suit suit;
    static Map<String, Integer> stringToValue = new HashMap<String, Integer>() {{
        put("TWO", 2);
        put("THREE", 3);
        put("FOUR", 4);
        put("FIVE", 5);
        put("SIX", 6);
        put("SEVEN", 7);
        put("EIGHT", 8);
        put("NINE", 9);
        put("TEN", 10);
        put("JACK", 11);
        put("QUEEN", 12);
        put("KING", 13);
        put("ACE", 14);
    }};



    Card() {
        rank = randomEnum(Rank.class);
        suit = randomEnum(Suit.class);

    }


    private static final SecureRandom random = new SecureRandom();


    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    } // shameless, not my code...


    // STEP #2: which card is highest
    public static List<User> compareCards(List<User> userList){

        // find highest value, return the user(s) with this value
            // sort userList based on stringToValue[user.getCard01()] <- need to extract the rank!
            // find the highest value
            // return all users with this value

        ArrayList<User> topDogs = new ArrayList<>();
        int highestRank = 0;

        for(User user : userList){
            String[] rankValueList = user.getCard0().split(", ");
            int rank = stringToValue.get(rankValueList[0]);
            if(rank > highestRank) { highestRank = rank; }
            System.out.println("highest rank: " + highestRank);
        }

        for(User user:userList){
            String[] rankValueList = user.getCard0().split(", ");
            int rank = stringToValue.get(rankValueList[0]);
            System.out.println("highest rank check: " + highestRank);
            System.out.println("rank check: " + highestRank);
            if(rank == highestRank) {
                topDogs.add(user);
                System.out.println("winner: " + user.getName());
            }
        }

        return topDogs;
    }


    @Override
    public String toString() {
        return rank + ", " + suit;
    }

}
