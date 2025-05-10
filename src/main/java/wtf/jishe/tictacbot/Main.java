package wtf.jishe.tictacbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        String token = "TOKEN";

        // build JDA instance
        JDA jda = JDABuilder.createLight(token, Collections.emptyList())
                .addEventListeners(new SlashCommandListener())
                .build();

        // Register slash commands
        CommandListUpdateAction commands = jda.updateCommands();

        // add slash commands to this action instance
        commands.addCommands(
                Commands.slash("leave", "Makes the bot leave the server")
                        .setContexts(InteractionContextType.GUILD) // doesnt make sense in dms
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED) // admin only
        );

        // send commands to discord
        commands.queue();
    }
}
