package com.tyche.poker;

import java.security.SecureRandom;

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

    Card() {
        rank = randomEnum(Rank.class);
        suit = randomEnum(Suit.class);
    }

    private static final SecureRandom random = new SecureRandom();

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    } // shameless, so not my code...

    @Override
    public String toString() {
        return rank + ", " + suit;
    }

}
