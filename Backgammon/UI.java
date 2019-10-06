/*
 * ShaggyRogers group
 * Written by:
 * 	-Rafal Koziel 17472032
 * 	-Jordan Ahere 17482772
 * 	-Oisin McPhillips 17409914
 */
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class UI extends JLabel{
    // UI is the top level interface to the user interface

    private static final int FRAME_WIDTH = 1100;
    private static final int FRAME_HEIGHT = 600;

    private final BoardPanel boardPanel;
    private final InfoPanel infoPanel;
    private final CommandPanel commandPanel;
    private BufferedImage imageArray[] = new BufferedImage[6];
    JLabel doubleDice1= new JLabel();
	JLabel doubleDice2 = new JLabel();
	JLabel x = new JLabel();
	JLabel y = new JLabel();
	ImageIcon icon1 = new ImageIcon();
    ImageIcon icon2 = new ImageIcon();
    JFrame frame = new JFrame();
    
    UI (Board board, Players players) {
        infoPanel = new InfoPanel();
        commandPanel = new CommandPanel();
        
        boardPanel = new BoardPanel(board,players);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setTitle("Backgammon");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(boardPanel, BorderLayout.LINE_START);
        frame.add(infoPanel, BorderLayout.LINE_END);
        frame.add(commandPanel, BorderLayout.PAGE_END);
        frame.setResizable(false);
        frame.setVisible(true);
        readInDice();
    }
   
    public void disableFrame() {
    	frame.setVisible(false);
    }
    public String getString() {
        return commandPanel.getString();
    }

    public void display() {
        boardPanel.refresh();
    }

    public void displayString(String string) {
        infoPanel.addText(string);
    }

    public void displayStartOfGame() {
        displayString("Welcome to Backgammon");
    }

    public void promptPlayerName() {
        displayString("Enter a player name:");
    }

    public void displayPlayerColor(Player player) {
        displayString(player + " uses " + player.getColorName() + " checkers.");
    }

    public void displayRoll(Player player) {
        displayString(player + " (" + player.getColorName() + ") rolls " + player.getDice());
    }

    public void displayDiceEqual() {
        displayString("Equal. Roll again");
    }

    public void displayDiceWinner(Player player) {
        displayString(player + " wins the roll and goes first.");
    }

    public void displayGameWinner(Player player) {
        displayString(player + " WINS THE GAME!!!");
    }

    public void promptCommand(Player player) {
        displayString(player + " (" + player.getColorName() + ") enter your move, quit or double:");
    }

    public Command getCommand(Plays possiblePlays) {
        Command command;
        do {
            String commandString = commandPanel.getString();
            command = new Command(commandString,possiblePlays);
            if (!command.isValid()) {
                displayString("Error: Command not valid.");
            }
        } while (!command.isValid());
        return command;
    }

    public void displayPlays(Player player, Plays plays) {
        displayString(player + " (" + player.getColorName() + ") available moves...");
        int index = 0;
        for (Play play : plays) {
            String code;
            if (index<26) {
                code = "" + (char) (index%26 + (int) 'A');
            } else {
                code = "" + (char) (index/26 - 1 + (int) 'A') + (char) (index % 26 + (int) 'A');
            }
            displayString(code + ". " + play);
            index++;
        }
    }

    public void displayNoMove(Player player) throws InterruptedException {
        displayString(player + " has no valid moves.");
        TimeUnit.SECONDS.sleep(1);
    }

    public void displayForcedMove(Player player) throws InterruptedException {
        displayString(player + " has a forced move.");
        TimeUnit.SECONDS.sleep(1);
    }
    /*
     * Displays dice on the board
     */
    public void displayDice(int d1, int d2) {
    	
    	icon1 = new ImageIcon(imageArray[d1 - 1]);
    	icon2 = new ImageIcon(imageArray[d2 - 1]);
    	
    	x.setIcon(icon1);
    	x.setBounds(125,250,50,50);
    	
    	y.setIcon(icon2);
    	y.setBounds(225,250,50,50);  
    	
    	boardPanel.add(x);
    	boardPanel.add(y);
    }
    public void removeDice() {
    	boardPanel.remove(x);
    	boardPanel.remove(y);
    }
    /*
     * Displays double dice when you roll the same number on both die
     */
    public void displayDiceDouble(int value) {
    	ImageIcon icon = new ImageIcon(imageArray[value - 1]);
    	
    	doubleDice1.setIcon(icon);
    	doubleDice1.setBounds(450,250,50,50);
    	
    	doubleDice2.setIcon(icon);
    	doubleDice2.setBounds(550,250,50,50);
    	
    	boardPanel.add(doubleDice1);
        boardPanel.add(doubleDice2);
    }
    /*
     * Removes extra dice when double dice is rolled
     */
    
    public void removeDiceDouble() {
    	boardPanel.remove(doubleDice1);
    	boardPanel.remove(doubleDice2);
    }
    /*
     * Saves image for each side of the dice
     */
    public void readInDice() {
    	for (int i = 0; i < 6 ;i++){
			int temp = i + 1;
			
			try {
				imageArray[i] = ImageIO.read(this.getClass().getResource("side"+temp+".png"));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    /*
     * Animates dice on the board
     */
    void animateDice() throws InterruptedException{
		for(int i = 0;i < 10;i++) {
			
			displayDice((1 + (int)(Math.random() * 6)),(1 + (int)(Math.random() * 6)));
			display();
			TimeUnit.MILLISECONDS.sleep(100);
		}
	}
    /*
     * Displays score on the board
     */
	public void displayScore(Players players) {
		displayString("Score: \n"+players.get(0).getScore()+"\n"+players.get(1).getScore()+"\n");
	}
	
	/*
     * Following function checks input and manages double dice
     * eg. Allows players to accept or decline the double, keeps track of who owns the double dice and 
     * manages cases where using the doubling dice is invalid 
     */
	public Command checkInputDoubleDice(Plays possiblePlays,boolean firstMoveDouble, Players players, Board board) {
    	Command command = new Command();
    	if(players.deadCube() && players.canDouble() &&( players.getCurrent().hasDoubleDice() == false || players.deadCube())) {
    		displayString("ERROR: the cube is dead there, is no reason for you to double\nPlease insert a move to continue the game:");
     		command = getCommand(possiblePlays);
            displayString("> " + command);
    	}
    	else if(players.getCurrent().hasDoubleDice() && firstMoveDouble && players.canDouble()) {
    		if(proposeDouble(players)) {
    			display();
	    		displayString(players.getCurrent().toString()+" used the Double Die");
	     		displayString(players.getNext().toString()+" now has the Double Die");
	     		players.advanceDoubleDice();
	     		players.getNext().giveDoubleDice();
	     		players.getCurrent().takeDoubleDice();
	     		displayString("Please insert a move to continue the game:");
	     		command = getCommand(possiblePlays);
	            displayString("> " + command);
    		}
    		else {
    			displayString(players.getNext().toString()+" has declined the Double Die");
    			board.declineDoubleDice(players.getCurrent());
    		}
     	}
    	else if (players.getCurrent().hasDoubleDice() == false && players.getNext().hasDoubleDice() == false && firstMoveDouble && players.canDouble()) {
    		if(proposeDouble(players)) {
    			display();
	    		displayString(players.getCurrent().toString()+" used the Double Die");
	     		displayString(players.getNext().toString()+" now has the Double Die");
	     		players.advanceDoubleDice();
	     		players.getNext().giveDoubleDice();
	     		players.getCurrent().takeDoubleDice();
	     		displayString("Please insert a move to continue the game:");
	     		command = getCommand(possiblePlays);
	            displayString("> " + command);
	    	}
    		else {
    			displayString(players.getNext().toString()+" has declined the Double Die");
    			board.declineDoubleDice(players.getCurrent());
    		}
    	}
    	else if(!firstMoveDouble) {
    		displayString("ERROR: you cannot use the double die on the first move\nPlease insert a move to continue the game:");
     		command = getCommand(possiblePlays);
            displayString("> " + command);
    	}
    	else if (!players.canDouble()) {
    		displayString("ERROR: you cannot use the double die at this time\n(Crawford rule in effect)\nPlease insert a move to continue the game:");
     		command = getCommand(possiblePlays);
            displayString("> " + command);
    	}
     	else {
     		displayString("ERROR: you cannot use the double die at this time\nPlease insert a move to continue the game:");
     		command = getCommand(possiblePlays);
            displayString("> " + command);
     	}
    	
    	return command;
    }
	/*
	 * Function to ask the opponent if they accept the double and to read their answer
	 */
    private boolean proposeDouble(Players players) {
		displayString(players.getCurrent().toString()+" Proposes a double");
		displayString(players.getNext().toString()+" Do you accept this double?\n(yes/no)");
		return checkInput(getString());
	}
    /*
     * function to check if the user inputs 'yes' or 'no'
     */
    public boolean checkInput(String input) {
    	boolean answer = false;
    	if(input.equals("yes")) {
    		answer = true;
     	}
     	else if(input.equals("no")){
     		answer = false;
     	}
     	else {
     		displayString("Please enter either 'yes' or 'no' ");
     		checkInput(getString());
     	}
    	return answer;
    }
    /*
     * function to force a player to propose a double if it is in their favor to
     */
    public void forceDouble(Players players, Board board) {
    	if(proposeDouble(players)) {
			display();
    		displayString(players.getCurrent().toString()+" used the Double Die");
     		displayString(players.getNext().toString()+" now has the Double Die");
     		players.advanceDoubleDice();
     		players.getNext().giveDoubleDice();
     		players.getCurrent().takeDoubleDice();
		}
    	else {
			displayString(players.getNext().toString()+" has declined the Double Die");
			board.declineDoubleDice(players.getCurrent());
		}
		
	}
	

	
}