package table;

import java.util.HashMap;
import java.util.Map;

public class GameCoordinator {

    Map<String,Game> clientGameMap = new HashMap<String,Game>();
    
    public GameCoordinator()
    {
	
    }
    
    public boolean addClientToANewGame(String clientUsername, Game game)
    {
	clientGameMap.put(clientUsername, game);
	return game.addPlayerToGame(clientUsername);
    }
    
    public boolean checkClient(String clientUsername)
    {
	return clientGameMap.containsKey(clientUsername);
    }
    
    public String checkForGames()
    {
	String returnString="currentGameData";
	Game[] gameArray = (Game[]) clientGameMap.values().toArray();
	for(Integer i=0;i<gameArray.length;i++)
	{
	    returnString+=returnString+"+"+gameArray[i].getGameId()+"+"+gameArray[i].remainingSeats();
	}
	return returnString;
    }
    
    public boolean addClientToAnExistingGame(String clientUsername, Integer gameId)
    {
	Game[] gameArray = (Game[]) clientGameMap.values().toArray();
	for(Integer i=0;i<gameArray.length;i++)
	{
	    if(gameArray[i].getGameId()==gameId)
	    {
		return gameArray[i].addPlayerToGame(clientUsername);
	    }
	}
	return false;
    }
}
