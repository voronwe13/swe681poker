package swe681poker;

public enum Suit {
	CLUBS("clubs", "♣"),
	DIAMONDS("diamonds", "♦"),
	HEARTS("hearts", "♥"),
	SPADES("spades", "♠");
	
	public final String name, symbol;
	
	Suit(String name, String symbol){
		this.name = name;
		this.symbol = symbol;
	}
	
}
