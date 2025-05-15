package wtf.jishe.tictacbot.game;

public class TicTacToeGame {
	private final Board board;
    private String currentTurn;
    private final String player1Id;
    private final String player2Id;
    private final GameState state;

    public TicTacToeGame(String player1Id, String player2Id) {
		this.board = new Board();
        this.currentTurn = player1Id;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.state = GameState.ACTIVE;
    }

    public boolean makeMove(String playerId, int position) {
        if (!currentTurn.equals(playerId)) {
            return false; // not this player's turn
        }

        // convert position (1-9) to row and column
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;

        // make move on board
		boolean moveMade = board.makeMove(row, col, currentTurn.equals(player1Id) ? 'X' : 'O');
        if (!moveMade) {
            return false; // cell taken cannot make move
        }

        // switch player turn
        currentTurn = currentTurn.equals(player1Id) ? player2Id : player1Id;
        return true;
	}

    public String getCurrentPlayerId() {
        return currentTurn;
    }

    public GameState getState() {
        return state;
    }

    public String getBoardDisplay() {
        return board.getBoardDisplay();
    }
}
