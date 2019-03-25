package course.oop.view;

import course.oop.game.board;
import course.oop.game.player;
import course.oop.game.standardBoard;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameView {
	private BorderPane root;
	private Scene scene; 
	private Stage primaryStage;
	private Text status;
    private final int windowWidth = 800;
    private final int windowHeight = 600;
    
    private player player1;
    private player player2;
    private player turn;
    boolean compGame;
    private board TTT;
    private int numTurns;
    private Text counter;
    
    public GameView(Stage primaryStage, String player1, String marker1, String player2, String marker2, boolean compGame) {
    	this.root = new BorderPane();
		this.scene = new Scene(root, windowWidth, windowHeight);
		this.primaryStage = primaryStage;
		
		this.player1 = new player(player1, marker1);
		this.player2 = new player(player2, marker2);
		this.turn = this.player1;
		this.compGame = compGame;
		numTurns = 1;
		this.counter = new Text("Turn #" + numTurns);
		
		this.status = new Text("Turn: " + this.player1.getPlayerName());
		
		TTT = new standardBoard();
		this.root.setCenter(this.buildBoardPane());
		this.root.setTop(status);
		this.root.setBottom(counter);
    }

	private GridPane buildBoardPane() {
		GridPane grid = new GridPane();
		
		int[][] state = TTT.displayBoard();
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(state[i][j] == 0) {
					Button space = new Button("-");
					space.setId(i + "" + j);
					
					EventHandler<MouseEvent> place = new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent e) {
							char[] coor = space.getId().toCharArray();
							TTT.placeMove(Character.getNumericValue(coor[0]), Character.getNumericValue(coor[1]), currentPlayer());
							System.out.println(TTT.checkWin(currentPlayer()));
							if(TTT.checkWin(currentPlayer())) {
								
							}
							else if(compGame) {
								TTT.computerMove();
								if(TTT.checkWin(2)) {
									
								}
							}
							else {
								switchTurn();
								status.setText("Turn: " + turn.getPlayerName());
							}
							
							root.setCenter(buildBoardPane());
						}
					};
					
					space.addEventHandler(MouseEvent.MOUSE_CLICKED, place);
					grid.add(space, j, i);
				}
				else if(state[i][j] == 1) {
					Text pSpace = new Text(player1 + "");
					grid.add(pSpace, j, i);
				}
				else {
					Text pSpace = new Text(player2 + "");
					grid.add(pSpace, j, i);
				}
				
			}
		}
		grid.setVgap(30); 
        grid.setHgap(30);   
        grid.setAlignment(Pos.CENTER);
        
		return grid;
	}
	
	private void switchTurn() {
		if(turn == player2) {
			turn = player1;
			numTurns++;
			counter.setText("Turn #" + numTurns);
		}
		else {
			turn = player2;
		}
	}
	
	private int currentPlayer() {
		if(turn == player1) {
			return 1;
		}
		else {
			return 2;
		}
	}

	public Scene getScene() {
		return scene;
	}
}
