package swe681poker;

//This is a dummy comment to check if commit is working

public class Card implements Comparable<Card>{
	Suit suit;
	Value value;
	int intvalue;
	
	
	public Card(int value){
		intvalue = value;
		suit = Suit.values()[value/13];
		this.value = Value.values()[value%13];
	}

	@Override
	public int compareTo(Card card) {
		return intvalue - card.intvalue;
	}
}
