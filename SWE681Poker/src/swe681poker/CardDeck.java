package swe681poker;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {
	List<Card> deck;
	SecureRandom random;
	
	public CardDeck(){
		random = new SecureRandom();
		deck = new ArrayList<Card>(52);
		for(int i=0; i<52; i++){
			deck.add(new Card(i));
		}
	}
	
	public void shuffleDeck(){
		Collections.shuffle(deck, random);
	}
	
	public Card dealOne(){	
		return deck.remove(deck.size()-1);
	}
	
	public Card[] dealX(int numcards){
		if(numcards > deck.size())
			throw new IllegalArgumentException("the deck doesn't have "+numcards+" left.");
		Card[] cards = new Card[numcards];
		for(int i=0; i<numcards; i++){
			cards[i] = dealOne();
		}
		return cards;
	}
}
