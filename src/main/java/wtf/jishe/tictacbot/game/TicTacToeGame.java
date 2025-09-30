package wtf.jishe.tictacbot.game;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.concurrent.*;

public class TicTacToeGame {
	private final Board board;
	private UserSnowflake currentTurn;
	private final UserSnowflake player1;
	private final UserSnowflake player2;
	private GameState state;

	private final ScheduledExecutorService schedular = Executors.newSingleThreadScheduledExecutor();
	private ScheduledFuture<?> timeoutTask;
	private final MessageChannel channel;

	public TicTacToeGame(String player1Id, String player2Id, MessageChannel channel) {
		this.board = new Board();
		this.player1 = User.fromId(player1Id);
		this.player2 = User.fromId(player2Id);
		this.currentTurn = player1;
		this.state = GameState.ACTIVE;

		this.channel = channel;
		startTurnTimer();
	}

	public boolean makeMove(int row, int col) {
		// end turn timer
		cancelTurnTimer();

		// make move on board
		boolean moveMade = board.makeMove(row, col, currentTurn.equals(player1) ? 'X' : 'O');
		if (!moveMade) {
			return false; // cell taken cannot make move
		}

		// update game state
		updateGameState();

		return true;
	}

	private void updateGameState() {
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

		// start timer for next player
		if (state == GameState.ACTIVE) {
			startTurnTimer();
		}
	}

	// 30 second turn timer. ends game if timer expires
	private void startTurnTimer() {
		cancelTurnTimer();
		timeoutTask = schedular.schedule(() -> {
			state = GameState.DRAW;
			endGame();

			channel.sendMessage("Game ended in a draw due to inactivity.").queue();
		}, 30, TimeUnit.SECONDS);
	}

	// end current turn timer if active
	private void cancelTurnTimer() {
		if (timeoutTask != null && !timeoutTask.isDone()) {
			timeoutTask.cancel(true);
		}
	}

	// end the game and shutdown the scheduler
	public void endGame() {
		schedular.shutdownNow();
		GameManager.getInstance().removeGame(channel.getId());
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
