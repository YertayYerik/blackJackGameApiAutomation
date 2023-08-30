package com.deckofcardsapi.tests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
//wrapper class for JSON response
public class DrawnCardsResponse {
    private boolean success;
    private String deck_id;
    private List<CardInfo> cards;
    private int remaining;
}
