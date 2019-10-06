/*
 * ShaggyRogers group

 * Written by:
 * 	-Rafal Koziel 17472032
 * 	-Jordan Ahere 17482772
 * 	-Oisin McPhillips 17409914
 */
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Backgammon {
    // This is the main class for the Backgammon game. It orchestrates the running of the game.

    public static final int NUM_PLAYERS = 2;

    private final Players players = new Players();
    private Board board = new Board(players);
    private UI ui = new UI(board,players);

    private void getPlayerNames() {
        for (Player player : players) {
            ui.promptPlayerName();
            String name = ui.getString();
            ui.displayString("> " + name);
            player.setName(name);
            ui.displayPlayerColor(player);
            ui.display();
            }
    }

    private void rollToStart() throws InterruptedException {
        do {
            for (Player player : players) {
                player.getDice().rollDie();
               
                ui.displayRoll(player);
                TimeUnit.SECONDS.sleep(2);
            }
            if (players.isEqualDice()) {
                ui.displayDiceEqual();
            }
        } while (players.isEqualDice());
        
        
        
        players.setCurrentAccordingToDieRoll();
        ui.displayDiceWinner(players.getCurrent());
        ui.display();
        TimeUnit.SECONDS.sleep(2);
    }

    private void takeTurns() throws InterruptedException {
        Command command = new Command();
        boolean firstMove = true;
        boolean firstMoveDouble = false;
        do {
            Player currentPlayer = players.getCurrent();
            Dice currentDice;
            players.deadCube(false);
            if(currentPlayer.getScore() + players.getDoubleDice() >= players.getMatchPoint()) {
            	players.deadCube(true);
            }
            if (firstMove) {
                currentDice = new Dice(players.get(0).getDice().getDie(),players.get(1).getDice().getDie());
                ui.displayDice(players.get(0).getDice().getDie(),players.get(1).getDice().getDie());
                ui.display();
                firstMove = false;
            } else { 
            	
                currentPlayer.getDice().rollDice();
                
                ui.animateDice();
                ui.displayDice(currentPlayer.getDice().getDie(0), currentPlayer.getDice().getDie(1));
               
                if(currentPlayer.getDice().isDouble()) {
                	ui.displayDiceDouble(currentPlayer.getDice().getDie(0));
                }
                
                ui.display();
                ui.displayRoll(currentPlayer);
                currentDice = currentPlayer.getDice();
                firstMoveDouble = true;
              
            }
            if(checkForceDouble() && !firstMove) {
            	ui.displayString(players.getCurrent().toString()+" is forced to double since it is in their favor to");
            	ui.forceDouble(players,board);
            }
            Plays possiblePlays;
            possiblePlays = board.getPossiblePlays(currentPlayer,currentDice);
            if (possiblePlays.number()==0) {
                ui.displayNoMove(currentPlayer);
            } else if (possiblePlays.number()==1) {
                ui.displayForcedMove(currentPlayer);
                board.move(currentPlayer, possiblePlays.get(0));
            } else {
                ui.displayPlays(currentPlayer, possiblePlays);
                ui.promptCommand(currentPlayer); 
                command = ui.getCommand(possiblePlays);
                ui.displayString("> " + command);
                if(command.isDouble()) {
                	command = ui.checkInputDoubleDice(possiblePlays,firstMoveDouble,players,board);
                	
                }
                if (command.isMove() || command.isDouble()) {
                    board.move(currentPlayer, command.getPlay());
                } else if (command.isCheat()) {
                    board.cheat();
                }  
            }
            
            if(currentPlayer.getDice().isDouble()) {
            	ui.removeDiceDouble();
            }

            players.advanceCurrentPlayer();
            
            ui.display();
            
        } while (!command.isQuit() && !board.isGameOver());
    }

	private boolean checkForceDouble() {
		
		return ((players.getNext().getScore() == (players.getMatchPoint() -1)) 
				&& (players.getCurrent().getScore() + players.getDoubleDice()*2 <= players.getMatchPoint())    
				&& (players.getCurrent().hasDoubleDice() || (players.getCurrent().hasDoubleDice() == false && players.getNext().hasDoubleDice() == false)) 
				&& !players.deadCube() && players.canDouble());
	}

	/*
     * This function asks players to choose the amount of points they want to play up to
     */
    private void getMatchPoint() {
    	int matchPoint = 0;
    	int check=0;
    	String input; 
		ui.displayString("\nChoose the amount of points you want to play up to: ");
		/*
		 * Following loop checks if matchpoint is a valid number
		 */
		while(matchPoint <= 0) {
			input = ui.getString();
			try {
				matchPoint = Integer.parseInt(input);
			}
			catch (NumberFormatException ex){
				ui.displayString("Please insert a number to play up to: ");
				check=1;
			}
			if(matchPoint<=0&&check==0) {
				ui.displayString("Please insert a number greater than 0");
			}
			check=0;
		}
		
		players.setMatchPoint(matchPoint);
		ui.display();
		ui.displayString("You will play up to: "+matchPoint+" points");
	}
    


	
    
    /*
     * This function plays out the game and starts new game if match isn't over 
     */
    private boolean play() throws InterruptedException {
    	int crawfordCheck = 0; 			//checks if crawford rule is in effect
		boolean nextGame = false;		//
		ui.display();
		ui.displayStartOfGame();
		getMatchPoint();
		getPlayerNames();
		/*
		 * Loop that allows to start a new game if match isn't over after the end of a game
		 */
		while (!board.isMatchOver()) {
			if (nextGame) {
				if((players.get(0).getScore() == players.getMatchPoint() -1 || players.get(1).getScore() == players.getMatchPoint() -1) && crawfordCheck < 1) {
					players.canDouble(false);
					crawfordCheck++;
				}
				board = new Board(players);
				ui.disableFrame();
				ui = new UI(board, players);
				players.getCurrent().takeDoubleDice();
				players.getNext().takeDoubleDice();
				players.resetDoubleDice();
				ui.display();
				ui.displayString(
						"New game started\nLets roll to see who goes first\nBoth players are still the same colour");
				TimeUnit.SECONDS.sleep(2);
			}
			rollToStart();
			takeTurns();
			if (board.isGameOver()) {
				ui.displayGameWinner(board.getWinner());
				board.calculateScore();
				ui.displayScore(players);
				if (!board.isMatchOver()) {
					players.deadCube(false);					
					if(!players.canDouble() && crawfordCheck == 1) {
						players.canDouble(true);
						crawfordCheck++;
					}
					ui.displayString("Enter any key to continue to next game");
					ui.getString();
				} else {
					ui.displayString("Game over!\n" + board.getWinner().toString() + " won!");
					ui.displayString("Would you like to play again?\n (yes/no)");
					if(ui.checkInput(ui.getString())){
						ui.disableFrame();
						return true;
					}
					else {
						return false;
					}
				}
			}
			nextGame = true;
		}
		return false;
	}

    

	public static void main(String[] args) throws InterruptedException {
		//loop to start new game if input is 'yes'
		Backgammon game = new Backgammon();
		while(game.play()) {
			game = new Backgammon();
		}
		System.exit(0);
       
    }
}
