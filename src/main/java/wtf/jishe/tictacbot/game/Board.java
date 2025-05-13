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
        // TODO check rows, columns, and diagonals for a win. use TicTacGo for references
        return false; // placeholder return
    }
    
    public boolean isDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == ' ') {
                    return false; // found empty cell, game not draw
                }
            }
        }
        return true; // game is tied
    }
    
    public String getBoardDisplay() {
        // TODO return formatted string of board for discord message
        return ""; // placeholder return
    }
}
