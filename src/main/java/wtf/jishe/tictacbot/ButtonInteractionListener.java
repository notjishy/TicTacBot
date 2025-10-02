package wtf.jishe.tictacbot;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import wtf.jishe.tictacbot.game.Buttons;
import wtf.jishe.tictacbot.game.GameManager;
import wtf.jishe.tictacbot.game.GameState;
import wtf.jishe.tictacbot.game.TicTacToeGame;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
			event.reply("not your turn yet calm down").setEphemeral(true).queue();
			return;
		}

		// "!" suffix indicates a completed row and column button
		if (!Objects.requireNonNull(event.getButton().getId()).endsWith("!")) {
			event.editMessage(event.getMessage().getContentRaw())
					.setActionRow(Buttons.getColumnButtons(event.getButton().getId()))
					.queue();
		} else {
			String[] parts = event.getButton().getId().split("_");

			if (parts[3].equals("back!")) {
				event.editMessage(event.getMessage().getContentRaw())
						.setActionRow(Buttons.getRowButtons())
						.queue();
				return;
			}

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
				event.editMessage(event.getMessage().getContentRaw())
						.setActionRow(Buttons.getRowButtons())
						.queue();
				event.getChannel().sendMessage("that cell is taken already dumbass").queue(
						message -> message.delete().queueAfter(5, TimeUnit.SECONDS)
				);
			} else {
				StringBuilder message = new StringBuilder();
				String calloutMessage = "";

				// check for any win conditions
				if (game.getState() != GameState.ACTIVE) {
					switch (game.getState()) {
						case GameState.PLAYER1_WIN, GameState.PLAYER2_WIN ->
							calloutMessage += "\n" + game.getCurrentPlayer().getAsMention() + " has won!\n";
						case GameState.DRAW -> calloutMessage += "\ndraw! kinda lame\n";
					}

					message.append(calloutMessage);
					message.append(game.getBoardDisplay());

					// update board message
					game.editLastMessage(event.getChannel(), message.toString());

					// game is over, remove game from GameManager
					GameManager.getInstance().removeGame(event.getChannel().getId());
				} else {
					// update board message
					message.append(game.getCurrentPlayer().getAsMention());
					message.append("'s turn!\n");
					message.append(game.getBoardDisplay());

					game.editLastMessage(event.getChannel(), message.toString());

					/*event.getChannel().sendMessage(message)
							.setActionRow(Buttons.getRowButtons())
							.queue(sentMessage -> game.setLastMessageId(sentMessage.getId()));*/
				}
			}
		}
	}
}
