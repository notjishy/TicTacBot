package wtf.jishe.tictacbot.game;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class Buttons {
	public static void setColumnButtons(ButtonInteractionEvent event) {
		String buttonId = event.getButton().getId();
		event.editMessage(event.getMessage().getContentRaw())
				.setActionRow(
						// "!" suffix indicates a completed row and column button
						Button.primary("ttt_" + buttonId + "_1!", "1"),
						Button.primary("ttt_" + buttonId + "_2!", "2"),
						Button.primary("ttt_" + buttonId + "_3!", "3")
				).queue();
	}

	public static void setRowButtons(ButtonInteractionEvent event) {
		event.editMessage(event.getMessage().getContentRaw())
				.setActionRow(getRowButtons())
				.queue();
	}

	public static Button[] getRowButtons() {
		return new Button[]{
				Button.primary("ttt_a", "A"),
				Button.primary("ttt_b", "B"),
				Button.primary("ttt_c", "C")
		};
	}
}