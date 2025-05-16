package wtf.jishe.tictacbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
	private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
		JDA jda = initializeJDA();
		registerCommands(jda);
	}

	public static JDA initializeJDA() {
		Properties config = new Properties();
		try (FileInputStream fis = new FileInputStream("config.properties")) {
			config.load(fis);
			String token = config.getProperty("token").trim();

			// build JDA instance
			return JDABuilder.createLight(token, Collections.emptyList())
					.addEventListeners(new SlashCommandListener())
					.addEventListeners(new ButtonInteractionListener())
					.build();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to load config file", e);
			System.exit(1);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, "Missing token!", e);
			System.exit(1);
		} catch (InvalidTokenException e) {
			logger.log(Level.SEVERE, "Invalid token!", e);
			System.exit(1);
		}
		return null;
	}

	public static void registerCommands(JDA jda) {
		CommandListUpdateAction commands = jda.updateCommands();

		// add slash commands to this action instance
		// catch any issues, would usually be caused by api issues on discord's end or some connection problem
		try {
			commands.addCommands(
					Commands.slash("leave", "Makes the bot leave the server")
							.setContexts(InteractionContextType.GUILD) // doesnt make sense in dms
							.setDefaultPermissions(DefaultMemberPermissions.DISABLED), // admin only
					Commands.slash("ping", "Get the bot's latency"),
					Commands.slash("tictactoe", "Play a game of tic tac toe")
							// require user to mention another user
							.addOption(OptionType.USER, "player2", "Who to challenge", true)
							.setContexts(InteractionContextType.GUILD) // doesnt make sense in dms
			).queue(
					success -> logger.info("Successfully registered application commands"),
					error -> {
						logger.log(Level.SEVERE, "Failed to register application commands", error);
						throw new RuntimeException("Failed to register commands", error);
					}
			);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, "Failed to add commands to be registered", e);
			throw new RuntimeException("Failed to register commands", e);
		}
	}
}
