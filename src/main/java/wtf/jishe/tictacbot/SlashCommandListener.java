package wtf.jishe.tictacbot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class SlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "leave" -> event.reply("Bye bye!")
                    .setEphemeral(true)
                    .flatMap(m -> Objects.requireNonNull(event.getGuild()).leave())
                    .queue();
        }
    }
}
