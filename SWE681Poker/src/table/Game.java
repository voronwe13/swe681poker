package table;

import java.security.SecureRandom;

public class Game {
    
    Integer maximumNumberOfPlayersPerGame = 4;
    Integer gameId;
    String[] currentPlayers;
    
    public Game()
    {
	//Generate a random gameId in order to prevent the attacker from guessing the next gameId
	//Here we need to check whether this value is currently in use
	gameId = new SecureRandom().nextInt();
	currentPlayers = new String[maximumNumberOfPlayersPerGame];
	for(Integer i=0;i<maximumNumberOfPlayersPerGame;i++)
	{
	    currentPlayers[i]=null;
	}
	//add the shuffling part here
    }
    
    public Integer getGameId()
    {
	return gameId;
    }
    
    public boolean addPlayerToGame(String playerUsername)
    {
	for(Integer i=0;i<maximumNumberOfPlayersPerGame;i++)
	{
	    if(currentPlayers[i]==null)
	    {
		currentPlayers[i]=playerUsername;
		return true;
	    }
	}
	return false;
    }
    
    public Integer remainingSeats()
    {
	Integer count=0;
	for(Integer i=0;i<maximumNumberOfPlayersPerGame;i++)
	{
	    if(currentPlayers[i]!=null)
	    {
		count++;
	    }
	}
	return count;
    }

}
