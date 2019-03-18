package course.oop.game;

public abstract class board {
	protected int turnNum;
	
	public board() {
		turnNum = 0;
	}
	
	public abstract boolean full();
	
	private void playSound() {}
	
	abstract void initBoard();
	
	public abstract boolean checkWin(int player);
	
	public abstract int[][] displayBoard();
	
	public abstract void computerMove();
	
	abstract void randomMove();
	
	public abstract boolean placeMove(int row, int col, int currentPlayer);
}
