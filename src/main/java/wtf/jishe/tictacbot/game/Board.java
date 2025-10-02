package wtf.jishe.tictacbot.game;

import java.util.Objects;

public class Board {
	private final String[][] grid;
	private final String emptySpace = "⬛";

	public Board() {
		grid = new String[3][3];
		initializeBoard();
	}

	private void initializeBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				grid[i][j] = emptySpace;
			}
		}
	}

	public boolean makeMove(int row, int col, String symbol) {
		if (Objects.equals(grid[row][col], emptySpace)) {
			grid[row][col] = symbol;

			return true; // move successful
		} else {
			return false; // cell is already taken
		}
	}

	public boolean checkWin(String symbol) {
		// check rows
		for (int i = 0; i < 3; i++) {
			if (Objects.equals(grid[i][0], symbol) && Objects.equals(grid[i][1], symbol) && Objects.equals(grid[i][2], symbol)) {
				return true; // found winning row
			}
		}

		// check columns
		for (int j = 0; j < 3; j++) {
			if (Objects.equals(grid[0][j], symbol) && Objects.equals(grid[1][j], symbol) && Objects.equals(grid[2][j], symbol)) {
				return true; // found winning column
			}
		}

		// check diagonals
		return (Objects.equals(grid[0][0], symbol) && Objects.equals(grid[1][1], symbol) && Objects.equals(grid[2][2], symbol)) ||
				Objects.equals(grid[0][2], symbol) && Objects.equals(grid[1][1], symbol) && Objects.equals(grid[2][0], symbol);
	}

	public boolean isDraw() {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (Objects.equals(grid[i][j], emptySpace)) {
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
		display.append("       1        2       3\n"); // column labels

		for (int i = 0; i < 3; i++) {
			display.append((char)('a' + i)); // row labels
			for (int j = 0; j < 3; j++) {
				if (grid[i][j].equals(emptySpace)) {
					display.append(" | ").append(emptySpace); // empty cell
				} else {
					display.append(" | ").append(grid[i][j]); // occupied cell
				}

			}
			display.append(" |\n");

			// horizontal divider
			if (i < 2) {
				display.append("   -----------------\n");
			}
		}
		return display.toString();
	}
}
