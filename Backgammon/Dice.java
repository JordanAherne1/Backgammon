/*
 * ShaggyRogers group

 * Written by:
 * 	-Rafal Koziel 17472032
 * 	-Jordan Ahere 17482772
 * 	-Oisin McPhillips 17409914
 */
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

class Dice {

    private int[] numbers;
    private boolean oneDieRoll;

    Dice() {
        numbers = new int[]{1, 1};
        oneDieRoll = false;
    }

    Dice(Dice dice) {
        numbers = new int[]{dice.numbers[0],dice.numbers[1]};
        oneDieRoll = dice.oneDieRoll;
    }

    Dice(int firstDie, int secondDie) {
        numbers = new int[]{firstDie, secondDie};
    }

    public void rollDie() {
        numbers[0] = 1 + (int)(Math.random()*6);
        oneDieRoll = true;
    }

    public int getDie() {
        return numbers[0];
    }

    public int getDie(int index) {
        return numbers[index];
    }

    public void rollDice() {
        numbers[0] = 1 + (int)(Math.random() * 6);
        numbers[1] = 1 + (int)(Math.random() * 6);
        oneDieRoll = false;
    }

    public boolean isDouble() {
        return numbers[0] == numbers[1];
    }

    public String toString() {
        String roll;
        if (oneDieRoll) {
            roll = "[" + numbers[0] + "]";
        } else {
            roll = "[" + numbers[0] + "," + numbers[1] + "]";
        }
        return roll;
    }
    

}
