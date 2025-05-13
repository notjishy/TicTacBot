package wtf.jishe.tictacbot;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import wtf.jishe.tictacbot.game.GameManager;
import wtf.jishe.tictacbot.game.TicTacToeGame;

import java.util.Objects;

public class SlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "leave" -> event.reply("Bye bye!")
                    .setEphemeral(true)
                    .flatMap(m -> Objects.requireNonNull(event.getGuild()).leave())
                    .queue();
            case "ping" -> // Get the bot's latency
                    event.reply("Pong! " + event.getJDA().getGatewayPing() + "ms")
                        .queue();
            case "tictactoe" -> {
                // get the user who invoked the command
                User player1 = event.getUser();
                String player1Id = player1.getId();

                // get player 2 from the command option
                User player2 = Objects.requireNonNull(event.getOption("player2")).getAsUser();
                String player2Id = player2.getId();

                // get current channel id
                String channelId = event.getChannel().getId();

                // ensure player 2 isn't the same person or a bot
                if (player1Id.equals(player2Id)) {
                    event.reply("You can't play against yourself!").setEphemeral(true).queue();
                    return;
                } else if (player2.isBot()) {
                    event.reply("You can't play against a bot!").setEphemeral(true).queue();
                    return;
                }

                TicTacToeGame game = new TicTacToeGame(player1Id, player2Id);
                try {
                    GameManager.getInstance().addGame(channelId, game);
                } catch (IllegalStateException ignored) {
                    event.reply("A game is already active in this channel.").setEphemeral(true).queue();
                    return;
                }

                // create message with board display and buttons for moves
                MessageCreateBuilder message = new MessageCreateBuilder()
                        .setContent("Game started! " + player1.getAsMention() + " vs " + player2.getAsMention())
                        .addActionRow(
                                Button.primary("ttt_1", "1"),
                                Button.primary("ttt_2", "2"),
                                Button.primary("ttt_3", "3"),
                                Button.primary("ttt_4", "4"),
                                Button.primary("ttt_5", "5"),
                                Button.primary("ttt_6", "6"),
                                Button.primary("ttt_7", "7"),
                                Button.primary("ttt_8", "8"),
                                Button.primary("ttt_9", "9")
                        );

                event.reply(message.build()).queue();
            }
        }
    }
}
