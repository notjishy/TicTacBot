package wtf.jishe.tictacbot;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import wtf.jishe.tictacbot.game.Buttons;
import wtf.jishe.tictacbot.game.GameManager;
import wtf.jishe.tictacbot.game.GameState;
import wtf.jishe.tictacbot.game.TicTacToeGame;

import java.util.Objects;

public class ButtonInteractionListener extends ListenerAdapter {
	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		// handle button clicks for game moves
		int row = -1;
		int col = -1;

		// get game from GameManager
		TicTacToeGame game = GameManager.getInstance().getGame(event.getChannel().getId());

		// verify player's turn
		if (!game.getCurrentPlayer().equals(event.getUser())) {
			event.reply("It's not your turn!").setEphemeral(true).queue();
			return;
		}

		// "!" suffix indicates a completed row and column button
		if (!Objects.requireNonNull(event.getButton().getId()).endsWith("!")) {
			Buttons.setColumnButtons(event);
		} else {
			String[] parts = event.getButton().getId().split("_");
			row = switch (parts[2]) {
				case "a" -> 0;
				case "b" -> 1;
				case "c" -> 2;
				default -> -1; // invalid row
			};

			col = Integer.parseInt(parts[3].substring(0, 1)) - 1;
		}

		if (row != -1 && col != -1) {
			// process players move
			boolean success = game.makeMove(row, col);
			if (!success) {
				event.reply("Cell already taken!").setEphemeral(true).queue();
				Buttons.setRowButtons(event);
			} else {
				String message = game.getCurrentPlayer().getAsMention() + "'s turn!\n" +
						game.getBoardDisplay();

				// check for any win conditions
				if (game.getState() != GameState.ACTIVE) {
					// game is over, remove game from GameManager
					GameManager.getInstance().removeGame(event.getChannel().getId());

					switch (game.getState()) {
						case PLAYER1_WIN, PLAYER2_WIN -> message += "\n" + event.getUser().getAsMention() + " wins!";
						case DRAW -> message += "\nIt's a draw!";
					}

					// update board message
					event.getMessage().editMessage(message).queue();
				} else {
					// update board message
					event.getChannel().sendMessage(message)
							.setActionRow(Buttons.getRowButtons()).queue();
				}
			}
		}
	}
}
