package wtf.jishe.tictacbot.game;

public class TicTacToeGame {
	private final Board board;
    private final String currentTurn;
    private final GameState state;

    public TicTacToeGame(String player1Id, String player2Id) {
		this.board = new Board();
        this.currentTurn = player1Id;
        this.state = GameState.ACTIVE;
    }

    public boolean makeMove(String playerId, int position) {
        // TODO validate playerId is currentTurn
        // TODO convert position (1-9) to row and column
        // TODO move on board
        // TODO update game state
        // TODO switch player turns
        return false; // placeholder return
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
