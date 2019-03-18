package course.oop.view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameView {
	private BorderPane root;
	private Scene scene; 
    private final int windowWidth = 800;
    private final int windowHeight = 600;
    
	public GameView() {
		this.root = new BorderPane();
		this.scene = new Scene(root, windowWidth, windowHeight);
		this.root.setCenter(this.buildMainMenuPane());
	}
	
	public Scene getGameScene() {
		return this.scene;
	}

	private GridPane buildMainMenuPane() {
		Text TTT = new Text("Tic Tac Toe");
		TTT.setFont(Font.font("", FontWeight.BLACK, FontPosture.REGULAR, 100));
		TTT.setFill(Color.ORANGE);
		TTT.setStrokeWidth(4);
		TTT.setStroke(Color.BLUE);
		
		Button newGameButton = new Button("New Game");
		Button quitButton = new Button("Quit");
		
		EventHandler<MouseEvent> newGameEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				startGame();
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
		
		grid.add(TTT, 0, 0);
		grid.add(newGameButton, 0, 1);
		grid.add(quitButton, 0, 2);

		
		return grid;
	}

	protected void startGame() {
		// TODO Auto-generated method stub
		
	}
}
