/*
 * ShaggyRogers group

 * Written by:
 * 	-Rafal Koziel 17472032
 * 	-Jordan Ahere 17482772
 * 	-Oisin McPhillips 17409914
 */
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Players implements Iterable<Player>, Iterator<Player> {
    // Players creates and groups two Players

    public static int NUM_PLAYERS = 2;
    private int matchPoint;
    private ArrayList<Player> players;
    private int currentPlayer;
    private Iterator<Player> iterator;
    private int doubleDiceNum = 1;
    private boolean deadCube = false;
    private boolean canDouble = true;


    Players() {
        players = new ArrayList<Player>();
        players.add(new Player(0,"RED", Color.RED));
        players.add(new Player(1,"GREEN",Color.GREEN));
        currentPlayer = 0;
    }

    Players(Players players) {
        this.players = new ArrayList<Player>();
        for (Player player : players) {
            this.players.add(new Player(player));
        }
        currentPlayer = 0;
    }

    public void setCurrentAccordingToDieRoll() {
        if (players.get(0).getDice().getDie() > players.get(1).getDice().getDie()) {
            currentPlayer = 0;
        } else {
            currentPlayer = 1;
        }
    }

    public Player getCurrent() {
        return players.get(currentPlayer);
    }
    public Player getNext() {
        if(currentPlayer==0) {
        	return players.get(1);
        }
        else {
        	return players.get(0);
        }
    }

    public void advanceCurrentPlayer() {
        currentPlayer++;
        if (currentPlayer == NUM_PLAYERS) {
            currentPlayer = 0;
        }
    }

    public Player get(int id) {
        return players.get(id);
    }

    public boolean isEqualDice() {
        return players.get(0).getDice().getDie() == players.get(1).getDice().getDie();
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public Player next() {
        return iterator.next();
    }
     

    public Iterator<Player> iterator() {
        iterator = players.iterator();
        return iterator;
    }
    
    public void setMatchPoint(int matchPoint) {
    	this.matchPoint=matchPoint;
    }
    public int getMatchPoint() {
    	return matchPoint;
    }
    public void advanceDoubleDice() {
    	doubleDiceNum = doubleDiceNum*2;
    }
    public void resetDoubleDice() {
    	doubleDiceNum = 1;
    }
    public void deadCube(boolean state) {
    	deadCube = state;
    }
    public boolean deadCube() {
    	return deadCube;
    }
    public void canDouble(boolean state) {
    	canDouble = state;
    }
    public boolean canDouble() {
    	return canDouble;
    }
    public int getDoubleDice() {
		return doubleDiceNum;
    }
}
