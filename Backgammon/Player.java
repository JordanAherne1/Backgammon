/*
 * ShaggyRogers group

 * Written by:
 * 	-Rafal Koziel 17472032
 * 	-Jordan Ahere 17482772
 * 	-Oisin McPhillips 17409914
 */
import java.awt.*;

public class Player {
    // Player holds the details for one player

    private int id;
    private String colorName;
    private Color color;
    private String name;
    private Dice dice;
    private int score;
    private boolean hasDoubleDice;
    
    Player(int id, String colorName, Color color) {
        this.id = id;
        name = "";
        this.colorName = colorName;
        this.color = color;
        score=0;
        hasDoubleDice = false;
        dice = new Dice();
    }

    Player(Player player) {
        id = player.id; 
        name = player.name;
        colorName = player.colorName;
        color = player.color;
        score=player.score;
        hasDoubleDice = player.hasDoubleDice;
        dice = new Dice(player.dice);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getColorName() {
        return this.colorName;
    }

    public Color getColor() {
        return this.color;
    }

    public Dice getDice() { 
    	return dice; 
    }
    public int getScore() { 
    	return score; 
    }
    public void setScore(int score) { 
    	this.score+=score; 
    }
    public void giveDoubleDice() {
    	hasDoubleDice = true;
    }
    public void takeDoubleDice() {
    	hasDoubleDice = false;
    }
    public boolean hasDoubleDice() {
    	return hasDoubleDice;
    }
    public String toString() {
        return name;
    }
    
}
