package wtf.jishe.tictacbot;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ButtonInteractionListener extends ListenerAdapter {
	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		// TODO handle button clicks for game moves
		// TODO extract position from button id
		// TODO get game from GameManager
		// TODO process players move
		// TODO update board message
		// TODO check for any game ends conditions
	}
}
