package course.oop.view;

import java.sql.SQLException;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenuView {
	private BorderPane root;
	private Scene scene; 
	private Stage primaryStage;
	private Text status;
    private final int windowWidth = 800;
    private final int windowHeight = 600;
    
	public MainMenuView(Stage primaryStage) {
		this.root = new BorderPane();
		this.scene = new Scene(root, windowWidth, windowHeight);
		this.primaryStage = primaryStage;
		this.status = new Text("");
		this.root.setCenter(this.buildMainMenuPane());
	}
	
	public Scene getScene() {
		return this.scene;
	}

	private GridPane buildMainMenuPane() {
		Text TTT = new Text("Tic Tac Toe");
		TTT.getStyleClass().add("title");
		
		Button newGameButton = new Button("New Game");
		Button quitButton = new Button("Quit");
		
		EventHandler<MouseEvent> newGameEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				try {
					selectPlayers();
				} catch(SQLException s) {
					status.setText("Error connecting to database");
				}
				
			}
		};
		
		newGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, newGameEvent);
		
		EventHandler<MouseEvent> quitEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.exit(0);
			}
		};
		
		quitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, quitEvent);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setAlignment(Pos.CENTER);
		GridPane.setHalignment(newGameButton, HPos.CENTER);
		GridPane.setHalignment(quitButton, HPos.CENTER);
		
		grid.add(TTT, 0, 0);
		grid.add(newGameButton, 0, 1);
		grid.add(quitButton, 0, 2);
		grid.add(status, 0, 3);
		
		return grid;
	}

	protected void selectPlayers() throws SQLException {
		PlayersView playersV = new PlayersView(primaryStage);
		Scene scene = playersV.getScene();
		primaryStage.setScene(scene);
	}
}
