package course.oop.game;

import java.util.Random;

public class standardBoard extends board {
	
	int [][] boardArray = new int[3][3];
	
	public standardBoard() {
		super();
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				boardArray[i][j] = 0;
			}
		}
	}

	@Override
	public boolean full() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(boardArray[i][j] == 0) {
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public void initBoard() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				boardArray[i][j] = 0;
			}
		}
	}

	public boolean placeMove(int row, int col, int currentPlayer) {
		if((row >= 0) && (row < 3) && (col >= 0) && (col < 3)) {
			if(boardArray[row][col] == 0) {
				boardArray[row][col] = currentPlayer;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean checkWin(int player) {
		return checkRows(player) || checkCols(player) || checkDiags(player);
	}
	
	private boolean checkRows(int player) {
		for(int i = 0; i < 3; i++) {
			if(equal3(boardArray[i][0], boardArray[i][1], boardArray[i][2], player)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkCols(int player) {
		for(int i = 0; i < 3; i++) {
			if(equal3(boardArray[0][i], boardArray[1][i], boardArray[2][i], player)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkDiags(int player) {
		return(equal3(boardArray[0][0], boardArray[1][1], boardArray[2][2], player)) || (equal3(boardArray[0][2], boardArray[1][1], boardArray[2][0], player));
	}

	private boolean equal3(int c, int d, int e, int player) {
		return ((c != 0) && (c == d) && (d == e) && (c == player));
	}

	@Override
	public int[][] displayBoard() {
		int[][] ret = new int[3][3];
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				ret[i][j] = boardArray[i][j];
			}
		}
		return ret;
	}

	@Override
	public void computerMove() {
		Random r = new Random();
		int row, col;
		boolean move = false;
		do {
			row = r.nextInt(3);
			col = r.nextInt(3);
			move = placeMove(row, col, 2);
		} while(!move);
	}

	@Override
	void randomMove() {
		// TODO Auto-generated method stub

	}

}
