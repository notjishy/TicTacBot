package wtf.jishe.tictacbot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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
            case "ping" -> {
                // Get the bot's latency
                event.reply("Pong! " + event.getJDA().getGatewayPing() + "ms")
                    .queue();
            }
            case "tictactoe" -> {
                // get the user who invoked the command
                String player1Id = event.getUser().getId();

                // get player 2 from the command options
                String player2Id = Objects.requireNonNull(event.getOption("player2")).getAsUser().getId();

                // get current channel id
                String channelId = event.getChannel().getId();

                // ensure player 2 isn't the same person
                if (player1Id.equals(player2Id)) {
                    event.reply("You can't play against yourself!").setEphemeral(true).queue();
                    return;
                }

                TicTacToeGame game = new TicTacToeGame(player1Id, player2Id);
                try {
                    GameManager.getInstance().addGame(channelId, game);
                } catch (IllegalStateException ignored) {
                    event.reply("A game is already active in this channel.").setEphemeral(true).queue();
                }
            }
        }
    }
}
