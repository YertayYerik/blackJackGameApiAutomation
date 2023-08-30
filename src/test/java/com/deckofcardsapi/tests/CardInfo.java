package com.deckofcardsapi.tests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
//CardInfo class has properties of card from JSON response
public class CardInfo {
        private String code;
        private String image;
        private Map<String, String> images;
        private String value;
        private String suit;



        public int getValueAsNumber() {
            if (value.equals("ACE")) {
                return 1; // Aces can be counted as 1 or 11 in Blackjack
            } else if (value.equals("KING") || value.equals("QUEEN") || value.equals("JACK")) {
                return 10;
            } else {
                return Integer.parseInt(value);
            }
        }

        public boolean isAce() {
            return value.equals("ACE");
        }
    }
