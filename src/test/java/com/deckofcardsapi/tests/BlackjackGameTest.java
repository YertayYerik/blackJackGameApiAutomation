package com.deckofcardsapi.tests;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BlackjackGameTest {
     CardInfo cards = new CardInfo();

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://deckofcardsapi.com";
    }

    @Test
    public void playBlackjackGame() throws JsonProcessingException {
        // Step 1: Navigate to https://deckofcardsapi.com/
        // No need to implement this step since we are using the baseURI directly.

        // Step 2: Confirm the site is up
        Response response = RestAssured.given().get("/api/deck/new/").then().log().all().assertThat().statusCode(200).extract().response();
        Assert.assertTrue(response.jsonPath().getBoolean("success"));

        // Step 3: Get a new deck
        Response newDeckResponse = RestAssured.get("/api/deck/new/shuffle/").then().log().all().assertThat().statusCode(200).extract().response();
        String deckId = newDeckResponse.jsonPath().getString("deck_id");
        Assert.assertTrue(response.jsonPath().getBoolean("success"));

        // Step 4: Shuffle the deck
        Response shuffleResponse = RestAssured.get("/api/deck/" + deckId + "/shuffle/").then().log().all().assertThat().statusCode(200).extract().response();
        int remainingCards = shuffleResponse.jsonPath().getInt("remaining");
        Assert.assertTrue(response.jsonPath().getBoolean("success"));

        // Step 5: Deal three cards to each of two players
        Response drawResponse = RestAssured.get("/api/deck/" + deckId + "/draw/?count=6").then().log().all().assertThat().statusCode(200).extract().response();
        //Deserializing response using objectMapper  into new wrapper class
        ObjectMapper objectMapper = new ObjectMapper();
        DrawnCardsResponse drawnCardsResponse = objectMapper.readValue(drawResponse.asString(), DrawnCardsResponse.class);

        List<CardInfo> drawnCards = drawnCardsResponse.getCards();

        List<CardInfo> player1CardInfos = drawnCards.subList(0, 3);
        List<CardInfo> player2CardInfos = drawnCards.subList(3, 6);


        // Step 6: Check whether either player has blackjack
        boolean player1HasBlackjack = checkBlackjack(player1CardInfos);
        boolean player2HasBlackjack = checkBlackjack(player2CardInfos);

        // Step 7: Print results
        System.out.println("Player 1 Blackjack: " + player1HasBlackjack);
        System.out.println("Player 2 Blackjack: " + player2HasBlackjack);
    }

    //method calculates the total value of the cards and checks if ACE is present
    //takes a list of CardInfo objects (representing a hand of cards)
    // as input and returns a boolean value indicating whether the hand has a blackjack.
    private boolean checkBlackjack(List<CardInfo> cards) {
        //to keep track of the total value of the cards in the hand.
        int totalValue = 0;
        //o keep track of whether the hand contains an Ace card.
        boolean hasAce = false;

        //iterates the cards one by one
        for (CardInfo card : cards) {
           // adds the numeric value of the current card to the totalValue variable
            totalValue += card.getValueAsNumber();
           // checks if there's at least one Ace in the hand
            if (card.isAce()) {
                hasAce = true;
            }
        }
         //counting an Ace as 11 results in a value less than or equal to 21.
        if (hasAce && totalValue + 10 <= 21) {
            return true; // Blackjack!
        }

        return false;
    }
}

