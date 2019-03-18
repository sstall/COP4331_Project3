package course.oop.controller;

import course.oop.game.board;
import course.oop.game.player;
import course.oop.game.standardBoard;

public class TTTControllerImpl implements TTTControllerInterface{
	
	private player player1;
	private player player2;
	private board T;
	private int numPlayers;
	private int timeoutInSecs;
	
	@Override
	public void startNewGame(int numPlayers, int timeoutInSecs) {
		this.numPlayers = numPlayers;
		this.timeoutInSecs = timeoutInSecs;
		T = new standardBoard();
	}

	@Override
	public void createPlayer(String username, String marker, int playerNum) {
		if(playerNum == 1) {
			player1 = new player(username, marker);
		}
		else if(playerNum == 2) {
			player2 = new player(username, marker);
		}
		else {
			System.out.println("Error: Invalid player number");
		}
		
	}

	@Override
	public boolean setSelection(int row, int col, int currentPlayer) {
		return T.placeMove(row, col, currentPlayer);
	}

	@Override
	public int determineWinner() {
		if(T.checkWin(1)) {
			return 1;
		}
		else if(T.checkWin(2)) {
			return 2;
		}
		else if(T.full()) {
			return 3;
		}
		return 0;
	}

	@Override
	public String getGameDisplay() {
		if(T instanceof standardBoard) {
			int pr[][] = T.displayBoard();
			String ret = "";
			
			for(int i = 0; i < 3; i++) {
				if(i == 1) {
					ret = ret + "\n----------\n";
				}
				
				for(int j = 0; j < 3; j++) {
					if(j == 1) {
						ret = ret + "| ";
					}
					
					if(pr[i][j] == 0)  {
						ret = ret + "- ";
					}
					else if(pr[i][j] == 1){
						ret = ret + player1 + " ";
					}
					else {
						ret = ret + player2 + " ";
					}
					
					if(j == 1) {
						ret = ret + "| ";
					}
				}
				
				if(i == 1) {
					ret = ret + "\n----------\n";
				}
			}
			ret = ret + "\n";
			return ret;
		}
		
		return null;
	}
	
	public int getTimer() {
		return timeoutInSecs;
	}
	
	public void computerMove() {
		T.computerMove();
	}
}
