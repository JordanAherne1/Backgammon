/*
 * ShaggyRogers group

 * Written by:
 * 	-Rafal Koziel 17472032
 * 	-Jordan Ahere 17482772
 * 	-Oisin McPhillips 17409914
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.*;

class   BoardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 736, FRAME_HEIGHT = 552;  // must be multiples of 4
	private static final int BORDER_TOP = 40, BORDER_BOTTOM = 75, BORDER_LEFT = 66, BORDER_RIGHT = 60;
	private static final int PIP_WIDTH = 47, BAR_WIDTH = 66;
	private static final int CHECKER_RADIUS = 16, CHECKER_DEPTH = 8, LINE_WIDTH = 2;   // must be even
	private static final int DOUBLEDICE_MID = 236;
	private static final int DOUBLEDICE_TOP = 35;
	private static final int DOUBLEDICE_BOT = 462;

	private Color[] checkerColors;
	private Board board;
	private Players players;
	private BufferedImage boardImage;
	private Graphics2D g2;

	BoardPanel(Board board, Players players) {
		setLayout(null);
		this.board = board;
		this.players = players;
		checkerColors = new Color[Players.NUM_PLAYERS];
		checkerColors[0] = players.get(0).getColor();
		checkerColors[1] = players.get(1).getColor();
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(Color.YELLOW);
		try {
			boardImage = ImageIO.read(this.getClass().getResource("board.jpg"));
		} catch (IOException ex) {
			System.out.println("Could not find the image file " + ex.toString());
		}
	}

	private void displayChecker (int player, int x, int y) {
		g2.setColor(Color.BLACK);
		Ellipse2D.Double ellipseBlack = new Ellipse2D.Double(x,y,2*CHECKER_RADIUS,2*CHECKER_RADIUS);
		g2.fill(ellipseBlack);
		Ellipse2D.Double ellipseColour = new Ellipse2D.Double(x+LINE_WIDTH,y+LINE_WIDTH,2*(CHECKER_RADIUS-LINE_WIDTH),2*(CHECKER_RADIUS-LINE_WIDTH));
		g2.setColor(checkerColors[player]);
		g2.fill(ellipseColour);
	}

	private void displayCheckerSide (int player, int x, int y) {
		g2.setColor(Color.BLACK);
		Rectangle2D.Double rectangleBlack = new Rectangle2D.Double(x,y,2*CHECKER_RADIUS,CHECKER_DEPTH);
		g2.fill(rectangleBlack);
		Rectangle2D.Double rectangleColour = new Rectangle2D.Double(x+LINE_WIDTH,y+LINE_WIDTH,2*(CHECKER_RADIUS-LINE_WIDTH),CHECKER_DEPTH-2*LINE_WIDTH);
		g2.setColor(checkerColors[player]);
		g2.fill(rectangleColour);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 =(Graphics2D) g;
		g2.drawImage(boardImage, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, this);
		for (int player=0; player<Backgammon.NUM_PLAYERS; player++) {
			int x,y;
			// Display Pip Numbers
			for (int pip=1; pip<=Board.NUM_PIPS; pip++) {
				if (pip>3*Board.NUM_PIPS/4) {
					x = FRAME_WIDTH/2 + BAR_WIDTH/2 + (pip-3*Board.NUM_PIPS/4-1)*PIP_WIDTH+PIP_WIDTH/4;
				} else if (pip>Board.NUM_PIPS/2) {
					x = BORDER_LEFT + (pip-Board.NUM_PIPS/2-1)*PIP_WIDTH+PIP_WIDTH/4;
				} else if (pip>Board.NUM_PIPS/4) {
					x = BORDER_LEFT + (Board.NUM_PIPS/2-pip)*PIP_WIDTH+PIP_WIDTH/4;
				} else {
					x = FRAME_WIDTH/2 + BAR_WIDTH/2 + (Board.NUM_PIPS/4-pip)*PIP_WIDTH+PIP_WIDTH/4;
				}
				if (pip>Board.NUM_PIPS/2) {
					y = 3*BORDER_TOP/4;
				} else {
					y = FRAME_HEIGHT-BORDER_BOTTOM/4;
				}
				g2.setColor(players.getCurrent().getColor());
				g2.setFont(new Font("Courier",Font.BOLD,16));
				if (players.getCurrent().getId()==0) {
					g2.drawString(Integer.toString(pip), x, y);
				} else {
					g2.drawString(Integer.toString(Board.NUM_PIPS-pip+1), x, y);
				}
			}
			// Display Bar
			for (int count=1; count<=board.getNumCheckers(player, Board.BAR); count++) {
				x = FRAME_WIDTH/2-CHECKER_RADIUS;
				if (player==0) {
					y = FRAME_HEIGHT/4+(count-1)*CHECKER_RADIUS;
				} else {
					y = 3*FRAME_HEIGHT/4-(count-1)*CHECKER_RADIUS;
				}
				displayChecker(player,x,y);
			}
			// Display Main Board
			for (int pip=1; pip<=Board.NUM_PIPS; pip++) {
				for (int count=1; count<=board.getNumCheckers(player,pip); count++) {
					if (pip>3*Board.NUM_PIPS/4) {
						x = FRAME_WIDTH/2 + BAR_WIDTH/2 + (pip-3*Board.NUM_PIPS/4-1)*PIP_WIDTH;
					} else if (pip>Board.NUM_PIPS/2) {
						x = BORDER_LEFT + (pip-Board.NUM_PIPS/2-1)*PIP_WIDTH;
					} else if (pip>Board.NUM_PIPS/4) {
						x = BORDER_LEFT + (Board.NUM_PIPS/2-pip)*PIP_WIDTH;
					} else {
						x = FRAME_WIDTH/2 + BAR_WIDTH/2 + (Board.NUM_PIPS/4-pip)*PIP_WIDTH;
					}
					if ( (player==0 && pip>Board.NUM_PIPS/2) || (player==1 && pip<Board.NUM_PIPS/2) ){
						y = BORDER_TOP + (count-1)*2*CHECKER_RADIUS;
					} else {
						y = FRAME_HEIGHT - BORDER_BOTTOM - (count-1)*2*CHECKER_RADIUS;
					}
					displayChecker(player,x,y);
				}
			}
			// Display Bear Off
			for (int count=1; count<=board.getNumCheckers(player,Board.BEAR_OFF); count++) {
				x = FRAME_WIDTH - BORDER_RIGHT/2 - CHECKER_RADIUS;
				if (player==0) {
					y = FRAME_HEIGHT - BORDER_BOTTOM - (count-1)*CHECKER_DEPTH;
				} else {
					y = BORDER_TOP + (count-1)*CHECKER_DEPTH;
				}
				displayCheckerSide(player,x,y);
			}
		}
		
		int pos = 0;
		//changes the color and position of the double dice depending on which player is holding the dice
		if(players.get(1).hasDoubleDice()) { 
				g.setColor(checkerColors[1]);
				pos = DOUBLEDICE_TOP; 
		}
		else if(players.get(0).hasDoubleDice()){
				g.setColor(checkerColors[0]);
				pos = DOUBLEDICE_BOT;
		}
		else if(players.getCurrent().hasDoubleDice() == false && players.getNext().hasDoubleDice() == false){
				g.setColor(Color.WHITE);
				pos = DOUBLEDICE_MID;
		}
		//changes size of text on dice if it is too big
		if(players.getDoubleDice() >= 16) {
			g.setFont(new Font("Courier",Font.BOLD,35));
		}
		else {
			g.setFont(new Font("Courier",Font.BOLD,50));
		}
		g.fillRect(344, pos, 50, 50);
		g.setColor(Color.BLACK);
		
		//changes position of double dice depending on who is holding it
		if(players.getDoubleDice() >= 16) {
			g.drawString(""+players.getDoubleDice(), 348, pos + 35);
			//puts the text ' double dice ' above the dice 
			g.setFont(new Font("Courier",Font.BOLD,16));
			g.drawString("Double",340,pos+10);
			g.drawString("Dice",350,pos+10);
		}
		else {
			g.drawString(""+players.getDoubleDice(), 355, pos + 40);
			g.setFont(new Font("Courier",Font.BOLD,16));
			g.drawString("Double",340,pos-15);
			g.drawString("Dice",350,pos-3);
		}
		
		//Display match points
		//changes size of text if matchpoint is too big
		if(players.getMatchPoint() >= 10) {
			g.setFont(new Font("Courier",Font.BOLD,35));
		}
		else {
			g.setFont(new Font("Courier",Font.BOLD,50));
		}
		g.setColor(Color.YELLOW);
		g.fillRect(686, FRAME_HEIGHT/2 - 40, 50,50);
		g.setColor(Color.BLUE);
		
		if(players.getMatchPoint() >= 10) {
			g.drawString(""+players.getMatchPoint(), 690, FRAME_HEIGHT/2 - 5);

		}
		else {
			g.drawString(""+players.getMatchPoint(), 697, FRAME_HEIGHT/2);
		}
		g.setFont(new Font("Courier",Font.BOLD,16));
		g.drawString("Match", 687, FRAME_HEIGHT/2-57);
		g.drawString("Point", 687, FRAME_HEIGHT/2-45);
		
		//Display each players points after each 'round'
		//player 1
		if(players.get(1).getScore() >= 10) {
			g.setFont(new Font("Courier",Font.BOLD,35));
		}
		else {
			g.setFont(new Font("Courier",Font.BOLD,50));
		}
		g.setColor(Color.GREEN);
		g.fillRect(0, 35, 50,50);
		g.setColor(Color.BLACK);
		if(players.get(1).getScore() >= 10) {
			g.drawString("" +players.get(1).getScore(), 3, 70);
		}
		else {
			g.drawString("" +players.get(1).getScore(), 10, 75);	
		}
		g.setFont(new Font("Courier",Font.BOLD,16));
		g.drawString(players.get(1).toString(),1,18);
		g.drawString("score:",1,30);
		

		//player 0
		if(players.get(0).getScore() >= 10) {
			g.setFont(new Font("Courier",Font.BOLD,35));
		}
		else {
			g.setFont(new Font("Courier",Font.BOLD,50));
		}
		g.setColor(Color.RED);
		g.fillRect(0, FRAME_HEIGHT-90, 50,50);
		g.setColor(Color.BLACK);
		if(players.get(0).getScore() >= 10) {
			g.drawString("" +players.get(0).getScore(), 3, FRAME_HEIGHT - 53);
		}
		else {
			g.drawString("" +players.get(0).getScore(), 10, FRAME_HEIGHT - 48);
		}
		g.setFont(new Font("Courier",Font.BOLD,16));
		g.drawString(players.get(0).toString(),1,FRAME_HEIGHT - 107);
		g.drawString("score:",1,FRAME_HEIGHT - 95);



	}

	

	public void refresh() {
		revalidate();
		repaint();
	}

}
