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
		JDA jda = initializeJDA();
		registerCommands(jda);
	}

	public static JDA initializeJDA() {
		Properties config = new Properties();
		Logger logger = Logger.getLogger(Main.class.getName());
		try (FileInputStream fis = new FileInputStream("config.properties")) {
			config.load(fis);
			String token = config.getProperty("token").trim();

			if (token.isEmpty()) {
				throw new IllegalStateException("Token not found in config file");
			}

			// build JDA instance
			return JDABuilder.createLight(token, Collections.emptyList())
					.addEventListeners(new SlashCommandListener())
					.build();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to load config file", e);
			throw new RuntimeException("Failed to load bot due to config file error", e);
		} catch (InvalidTokenException e) {
			logger.log(Level.SEVERE, "Invalid token!", e);
			throw new RuntimeException("Failed to load bot due to invalid token", e);
		} catch (IllegalStateException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new RuntimeException("Failed to load bot due to missing token", e);
		}
	}

	public static void registerCommands(JDA jda) {
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
