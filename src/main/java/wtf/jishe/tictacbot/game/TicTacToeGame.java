package wtf.jishe.tictacbot.game;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;

public class TicTacToeGame {
	private final Board board;
	private UserSnowflake currentTurn;
	private final UserSnowflake player1;
	private final UserSnowflake player2;
	private GameState state;

	public TicTacToeGame(String player1Id, String player2Id) {
		this.board = new Board();
		this.player1 = User.fromId(player1Id);
		this.player2 = User.fromId(player2Id);
		this.currentTurn = player1;
		this.state = GameState.ACTIVE;
	}

	public boolean makeMove(int row, int col) {
		// make move on board
		boolean moveMade = board.makeMove(row, col, currentTurn.equals(player1) ? 'X' : 'O');
		if (!moveMade) {
			return false; // cell taken cannot make move
		}

		// check for win conditions
		if (board.checkWin(currentTurn.equals(player1) ? 'X' : 'O')) {
			state = currentTurn.equals(player1) ? GameState.PLAYER1_WIN : GameState.PLAYER2_WIN;
		}
		// check for draw
		else if (board.isDraw()) {
			state = GameState.DRAW;
		}

		// switch player turn
		if (state == GameState.ACTIVE) {
			currentTurn = currentTurn.equals(player1) ? player2 : player1;
		}

		return true;
	}

	public UserSnowflake getCurrentPlayer() {
		return currentTurn;
	}

	public GameState getState() {
		return state;
	}

	public String getBoardDisplay() {
		return board.getBoardDisplay();
	}
}
