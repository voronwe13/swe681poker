package swe681poker;

public class Card implements Comparable<Card>{
	public final Suit suit;
	public final Value value;
	public final int intvalue;
	public final String symbol;
	
	
	public Card(int value){
		intvalue = value;
		suit = Suit.values()[value/13];
		this.value = Value.values()[value%13];
		symbol = suit.symbol+this.value.symbol;
	}

	@Override
	public int compareTo(Card card) {
		return intvalue - card.intvalue;
	}
	
}
