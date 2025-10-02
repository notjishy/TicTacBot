package wtf.jishe.tictacbot;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import wtf.jishe.tictacbot.game.Buttons;
import wtf.jishe.tictacbot.game.GameManager;
import wtf.jishe.tictacbot.game.TicTacToeGame;

import java.util.Objects;

public class SlashCommandListener extends ListenerAdapter {
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		UserSnowflake botUser = User.fromId(event.getUser().getId());

		switch (event.getName()) {
			case "leave" -> event.reply("goodbye didnt wanna be here anyway")
					.setEphemeral(true)
					.flatMap(m -> Objects.requireNonNull(event.getGuild()).leave())
					.queue();
			case "ping" -> // Get the bot's latency
					event.reply("pong! " + event.getJDA().getGatewayPing() + "ms")
							.queue();
			case "tictactoe" -> {
				String player1Id = botUser.getId();

				// get player 2 from the command option
				User player2 = Objects.requireNonNull(event.getOption("player2")).getAsUser();
				String player2Id = player2.getId();

				// get current channel id
				String channelId = event.getChannel().getId();
				MessageChannel channel = event.getChannel();

				// ensure player 2 isn't the same person or a bot
				if (player1Id.equals(player2Id)) {
					event.reply("sorry i get youre lonely but you cant play against youreself").setEphemeral(true).queue();
					return;
				} else if (player2.isBot()) {
					event.reply("clankers are not allowed to play... except me").setEphemeral(true).queue();
					return;
				}

				TicTacToeGame game = new TicTacToeGame(player1Id, player2Id, channel);
				try {
					GameManager.getInstance().addGame(channelId, game);
				} catch (IllegalStateException ignored) {
					event.reply("one game per channel fuckass dont be greedy").setEphemeral(true).queue();
					return;
				}

				String messageText = "starting game. " + botUser.getAsMention() + " vs " + player2.getAsMention() + "\n" +
						game.getBoardDisplay();

				// create message with board display and buttons for moves
				MessageCreateBuilder message = new MessageCreateBuilder()
						.setContent(messageText)
						.addActionRow(Buttons.getRowButtons());

				event.reply(message.build())
						.queue(hook ->
								hook.retrieveOriginal()
										.queue(sentMessage ->
												game.setLastMessageId(sentMessage.getId())
										)
						);
			}
			case "endgame" -> {
				String channelId = event.getChannel().getId();
				TicTacToeGame game = GameManager.getInstance().getGame(channelId);

				if (game == null) {
					event.reply("there are no games running in this channel idiot").queue();
					return;
				}

				if (!Objects.equals(game.getPlayer1(), botUser) && !Objects.equals(game.getPlayer2(), botUser)) {
					event.reply("you arent part of this game you cant end it, fuck off").queue();
					return;
				}

				event.reply("wow okay i see how it is. ending the game now..").queue();

				game.endGame(false);
			}
		}
	}
}
