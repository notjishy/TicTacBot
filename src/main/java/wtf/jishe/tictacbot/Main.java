package wtf.jishe.tictacbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
		Properties config = new Properties();
		JDA jda = null;
		try (FileInputStream fis = new FileInputStream("config.properties")) {
			config.load(fis);
			String token = config.getProperty("token").trim();

			// build JDA instance
			jda = JDABuilder.createLight(token, Collections.emptyList())
					.addEventListeners(new SlashCommandListener())
					.build();
		} catch (IOException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Failed to load config file", e);
			System.exit(0);
		} catch (InvalidTokenException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Invalid or missing token!", e);
			System.exit(0);
		}

		// Register slash commands
		CommandListUpdateAction commands = jda.updateCommands();

		// add slash commands to this action instance
		commands.addCommands(
				Commands.slash("leave", "Makes the bot leave the server")
						.setContexts(InteractionContextType.GUILD) // doesnt make sense in dms
						.setDefaultPermissions(DefaultMemberPermissions.DISABLED), // admin only
				Commands.slash("ping", "Get the bot's latency")
		);

		// send commands to discord
		commands.queue();
	}
}
