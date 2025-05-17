package wtf.jishe.tictacbot.game;

public class Board {
	private final char[][] grid;

	public Board() {
		grid = new char[3][3];
		initializeBoard();
	}

	private void initializeBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				grid[i][j] = ' ';
			}
		}
	}

	public boolean makeMove(int row, int col, char symbol) {
		if (grid[row][col] == ' ') {
			grid[row][col] = symbol;

			return true; // move successful
		} else {
			return false; // cell is already taken
		}
	}

	public boolean checkWin(char symbol) {
		// check rows
		for (int i = 0; i < 3; i++) {
			if (grid[i][0] == symbol && grid[i][1] == symbol && grid[i][2] == symbol) {
				return true; // found winning row
			}
		}

		// check columns
		for (int j = 0; j < 3; j++) {
			if (grid[0][j] == symbol && grid[1][j] == symbol && grid[2][j] == symbol) {
				return true; // found winning column
			}
		}

		// check diagonals
		return (grid[0][0] == symbol && grid[1][1] == symbol && grid[2][2] == symbol) ||
				grid[0][2] == symbol && grid[1][1] == symbol && grid[2][0] == symbol;
	}

	public boolean isDraw() {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (grid[i][j] == ' ') {
					count++; // found empty cell
					if (count > 1) {
						return false; // game not draw
					}
				}
			}
		}
		return true; // game is tied
	}

	public String getBoardDisplay() {
		StringBuilder display = new StringBuilder();
		display.append("     1     2    3\n"); // column labels

		for (int i = 0; i < 3; i++) {
			display.append((char)('a' + i)); // row labels
			for (int j = 0; j < 3; j++) {
				if (grid[i][j] == ' ') {
					display.append(" | ").append("   "); // empty cell (wider for alignment)
				} else {
					display.append(" | ").append(grid[i][j]); // occupied cell
				}

			}
			display.append(" |\n");

			// horizontal divider
			if (i < 2) {
				display.append("   -----------\n");
			}
		}
		return display.toString();
	}
}
