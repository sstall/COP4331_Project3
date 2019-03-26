package course.oop.view;

import java.sql.SQLException;

import course.oop.controller.TTTControllerImpl;
import course.oop.db.playerDB;
import course.oop.game.player;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
    private TTTControllerImpl TTT;
    private int numTurns;
    private Text counter;
    private Button menu;
    
    public GameView(Stage primaryStage, String player1, String marker1, String player2, String marker2, int timer, boolean compGame) {
    	this.root = new BorderPane();
    	this.root.setPadding(new Insets(10, 10, 10, 10)); 
		this.scene = new Scene(root, windowWidth, windowHeight);
		this.primaryStage = primaryStage;
		
		this.player1 = new player(player1, marker1);
		this.player2 = new player(player2, marker2);
		this.turn = this.player1;
		this.compGame = compGame;
		numTurns = 1;
		this.counter = new Text("Turn #" + numTurns);
		
		this.status = new Text("Turn: " + this.player1.getPlayerName());
		
		TTT = new TTTControllerImpl();
		TTT.startNewGame(2, timer);
		TTT.createPlayer(player1, marker1, 1);
		TTT.createPlayer(player2, marker2, 2);
		this.root.setCenter(this.buildBoardPane());
		
		GridPane top = new GridPane();
		
		top.add(status, 0, 0);
		top.add(counter, 0, 1);
		top.getStyleClass().add("centerPane");
		
		this.root.setTop(top);
		
		GridPane bot = new GridPane();
		
		menu = new Button("Exit to menu");
		
		EventHandler<MouseEvent> mainMenuEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				MainMenuView main = new MainMenuView(primaryStage);
				Scene Scene = main.getScene();
				Scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
				primaryStage.setScene(Scene);
			}
		};
		
		menu.addEventHandler(MouseEvent.MOUSE_CLICKED, mainMenuEvent);
		
		bot.add(menu, 0, 0);
		bot.getStyleClass().add("centerPane");
		this.root.setBottom(bot);
    }

	private GridPane buildBoardPane() {
		GridPane grid = new GridPane();
		
		int[][] state = TTT.getBoard();
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(state[i][j] == 0) {
					Button space = new Button("-");
					space.setId(i + "" + j);
					
					EventHandler<MouseEvent> place = new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent e) {
							char[] coor = space.getId().toCharArray();
							TTT.setSelection(Character.getNumericValue(coor[0]), Character.getNumericValue(coor[1]), currentPlayer());
							int win = TTT.determineWinner();
							if(win == 1) {
								root.setCenter(finalBoard());
								status.setText(player1.getPlayerName() + " Wins!");
								finalScreen();
								playerDB db;
								try {
									db = new playerDB();
									db.increaseNumWins(player1.getPlayerName());
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
							}
							else if(win == 2) {
								root.setCenter(finalBoard());
								status.setText(player2.getPlayerName() + " Wins!");
								finalScreen();
								playerDB db;
								try {
									db = new playerDB();
									db.increaseNumWins(player2.getPlayerName());
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
							}
							else if(win == 3) {
								root.setCenter(finalBoard());
								status.setText("Draw");
								finalScreen();
							}
							else if(compGame) {
								TTT.computerMove();
								int compWin = TTT.determineWinner();
								if(compWin == 2) {
									root.setCenter(finalBoard());
									status.setText(player2.getPlayerName() + " Wins!");
									finalScreen();
								}
								else {
									numTurns++;
									counter.setText("Turn #" + numTurns);
									root.setCenter(buildBoardPane());
								}
							}
							else {
								switchTurn();
								status.setText("Turn: " + turn.getPlayerName());
								root.setCenter(buildBoardPane());
							}
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
        grid.getStyleClass().add("centerPane");
        
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
	
	private GridPane finalBoard() {
		int[][] state = TTT.getBoard();
		GridPane grid = new GridPane();
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(state[i][j] == 0) {
					Text b = new Text("-");
					grid.add(b, j, i);
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
        grid.getStyleClass().add("centerPane");
        
		return grid;
	}
	
	private void finalScreen() {
		GridPane bot = new GridPane();
        
        Button replay = new Button("Replay?");
        
        EventHandler<MouseEvent> replayEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				TTT.startNewGame(2, TTT.getTimer());
				
				numTurns = 1;
				counter.setText("Turn #" + numTurns);
				turn = player1;
				
				root.setCenter(buildBoardPane());
			}
		};
		
		
		replay.addEventHandler(MouseEvent.MOUSE_CLICKED, replayEvent);
		
		bot.add(replay, 0, 0);
		bot.add(menu, 1, 0);
		
		bot.setVgap(30); 
        bot.setHgap(30);   
        bot.getStyleClass().add("centerPane");
		
		root.setBottom(bot);
	}
}
