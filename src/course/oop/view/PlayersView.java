package course.oop.view;

import java.sql.SQLException;
import java.util.Vector;

import course.oop.db.playerDB;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PlayersView {
	private BorderPane root;
	private Scene scene; 
	private Stage primaryStage;
	private Text status;
    private final int windowWidth = 800;
    private final int windowHeight = 600;
    private playerDB db;
    private Text player1;
    private Text player2;
    private String marker1;
    private String marker2;
    
    public PlayersView(Stage primaryStage) throws SQLException {
    	this.root = new BorderPane();
		this.scene = new Scene(root, windowWidth, windowHeight);
		this.primaryStage = primaryStage;
		this.db = new playerDB();
		this.status = new Text("");
		player1 = new Text("");
		player2 = new Text("");
		marker1 = "";
		marker2 = "";
		this.root.setPadding(new Insets(10, 10, 10, 10)); 
		this.root.setCenter(this.buildPlayersPane());
		this.root.setBottom(this.selectedPlayersPane());
		Button back = new Button("Main Menu");
		
		EventHandler<MouseEvent> mainMenu = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				MainMenuView main = new MainMenuView(primaryStage);
				Scene Scene = main.getScene();
				Scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
				primaryStage.setScene(Scene);
			}
		};
		
		back.addEventHandler(MouseEvent.MOUSE_CLICKED, mainMenu);
		
		this.root.setTop(back);
    }
    
	public Scene getScene() {
		return this.scene;
	}

	private GridPane buildPlayersPane() {
		GridPane grid = new GridPane();
		
		grid.add(playersGrid(), 0, 0);
		
		
		Text newPlayerT = new Text("Player Name");
		Text newMarkerT = new Text("Player Marker");
		
		TextField newPlayer = new TextField();
		TextField newMarker = new TextField();
		Button submit = new Button("Enter");
		
		EventHandler<MouseEvent> addPlayer = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				try {
					status.setText("");
					String playerName = newPlayer.getText();
					String playerMark = newMarker.getText();
					
					if(playerName.length() == 0 || playerMark.length() == 0) {
						status.setText("Fields cannot be blank");
					} 
					else if(playerMark.length() > 2) {
						status.setText("A player's mark can only be 1 character");

					} 
					else if(playerMark.length() == 2) {
						if((int)playerMark.toCharArray()[0] >= 55357) {
							status.setText("");
							boolean add = db.addPlayer(newPlayer.getText(), newMarker.getText());
							if(!add) {
								status.setText("Can't add a name that is already entered");
							}
							root.setCenter(buildPlayersPane());
						}
						else {
							status.setText("A player's mark can only be 1 character");
						}
					}
					else if(playerMark.equals(" ")) {
						status.setText("A player's mark cannot be a space");
					}
					else {
						status.setText("");
						boolean add = db.addPlayer(newPlayer.getText(), newMarker.getText());
						if(!add) {
							status.setText("Can't add a name that is already entered");
						}
						root.setCenter(buildPlayersPane());
					}
				} catch (SQLException s) {
					status.setText("Error connecting to database");
				}
				
			}
		};
		
		submit.addEventHandler(MouseEvent.MOUSE_CLICKED, addPlayer);
		
		grid.add(newPlayerT, 0, 2);
		grid.add(newMarkerT, 1, 2);

		grid.add(newPlayer, 0, 3);
		grid.add(newMarker, 1, 3);
		grid.add(submit, 2, 3);
		
		grid.add(status, 0, 4);
		
		grid.setAlignment(Pos.CENTER);
		
		
		return grid;
	}
	
	private GridPane playersGrid() {
		GridPane grid = new GridPane();
		Vector<Vector<String>> v = new Vector<Vector<String>>();
		
		try {
			v = db.getPlayers();
		} catch(SQLException e) {
			status.setText("Error connecting to database");
			return null;
		}
		
		Text nameT = new Text("Name");
		Text markerT = new Text("Marker");
		Text numWinsT = new Text("# Wins");
		
		grid.add(nameT, 0, 0);
		grid.add(markerT, 1, 0);
		grid.add(numWinsT, 2, 0);
		
		for(int i = 0; i < v.size(); i++) {
			Text name = new Text(v.get(i).get(0));
			Text marker = new Text(v.get(i).get(1));
			Text numWins = new Text(v.get(i).get(2));
			Button selectPlayer = new Button("Select");
			selectPlayer.setId(v.get(i).get(0));
			Button removePlayer = new Button("Remove");
			removePlayer.setId(v.get(i).get(0));
			
			EventHandler<MouseEvent> removePlayerEvent = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					try {
						status.setText("");
						if(removePlayer.getId().equals(player1.getText()) || removePlayer.getId().equals(player2.getText())) {
							status.setText("Can't remove a player that is currently selected");
						} else {
							db.removePlayer(removePlayer.getId());
							root.setCenter(buildPlayersPane());
						}
					} catch (SQLException e1) {
						status.setText("Error connecting to database");
					}
				}
			};
			
			removePlayer.addEventHandler(MouseEvent.MOUSE_CLICKED, removePlayerEvent);
			
			EventHandler<MouseEvent> selectPlayerEvent = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					status.setText("");
					if(player1.getText().equals("") && !player2.getText().equals(selectPlayer.getId())) {
						player1.setText(selectPlayer.getId());
						try {
							marker1 = db.getPlayerByName(selectPlayer.getId()).get(0);
						} catch (SQLException e1) {
							status.setText("Error connecting to database");
						}
					}
					else if(player2.getText().equals("") && !player1.getText().equals(selectPlayer.getId())) {
						player2.setText(selectPlayer.getId());
						try {
							marker2 = db.getPlayerByName(selectPlayer.getId()).get(0);
						} catch (SQLException e1) {
							status.setText("Error connecting to database");
						}
					}
				}
			};
			
			selectPlayer.addEventHandler(MouseEvent.MOUSE_CLICKED, selectPlayerEvent);
			
			grid.add(name, 0, i+1);
			grid.add(marker, 1, i+1);
			grid.add(numWins, 2, i+1);
			grid.add(selectPlayer, 3, i+1);
			grid.add(removePlayer, 4, i+1);
		}
		
		grid.setVgap(5); 
        grid.setHgap(5);      
        
		return grid;
	}
	
	private GridPane selectedPlayersPane() {
		GridPane grid = new GridPane();
		
		grid.add(new Text("Player 1"), 0, 0);
		grid.add(new Text("Player 2"), 1, 0);
		
		grid.add(player1, 0, 1);
		grid.add(player2, 1, 1);
		
		Button clear1 = new Button("Clear");
		Button clear2 = new Button("Clear");
		
		EventHandler<MouseEvent> clearPlayer1 = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				status.setText("");
				player1.setText("");
			}
		};
		
		EventHandler<MouseEvent> clearPlayer2 = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				status.setText("");
				player2.setText("");
			}
		};
		
		clear1.addEventHandler(MouseEvent.MOUSE_CLICKED, clearPlayer1);
		clear2.addEventHandler(MouseEvent.MOUSE_CLICKED, clearPlayer2);
		
		Button compGame = new Button("Computer Game");
		Button playerGame = new Button("2p Game");
		
		EventHandler<MouseEvent> startCompGame = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if(!player1.getText().equals("") && player2.getText().equals("")) {
					GameView game = new GameView(primaryStage, player1.getText(), marker1, "Computer", "©", 0,true);
					Scene scene = game.getScene();
					scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
					primaryStage.setScene(scene);
				}
				else if(player1.getText().equals("") && !player2.getText().equals("")) {
					status.setText("Clear player 2 and select a player 1 to play vs a computer");
				}
				else if(player1.getText().equals("")) {
					status.setText("Select a player to play vs a computer");
				}
			}
		};
		
		EventHandler<MouseEvent> start2pGame = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if(!player1.getText().equals("") && !player2.getText().equals("")) {
					GameView game = new GameView(primaryStage, player1.getText(), marker1, player2.getText(), marker2, 0, false);
					Scene scene = game.getScene();
					scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
					primaryStage.setScene(scene);
				}
				else {
					status.setText("Select player 1 and player 2 to play a 2 player game");
				}
			}
		};
		
		compGame.addEventHandler(MouseEvent.MOUSE_CLICKED, startCompGame);
		playerGame.addEventHandler(MouseEvent.MOUSE_CLICKED, start2pGame);
		
		grid.add(clear1, 0, 2);
		grid.add(clear2, 1,	2);
		
		grid.add(compGame, 0, 4);
		grid.add(playerGame, 1, 4);
		
		grid.setAlignment(Pos.CENTER);
		
		grid.setVgap(5); 
        grid.setHgap(20);  
        grid.setPadding(new Insets(10, 10, 150, 10)); 
		
		return grid;
	}
	
}
