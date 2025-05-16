package wtf.jishe.tictacbot.game;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class Buttons {
	public static Button[] getRowButtons() {
		return new Button[]{
				Button.primary("ttt_a", "A"),
				Button.primary("ttt_b", "B"),
				Button.primary("ttt_c", "C")
		};
	}

	public static Button[] getColumnButtons(String buttonId) {
		return new Button[] {
				// "!" suffix indicates a completed row and column button
				Button.primary("ttt_" + buttonId + "_1!", "1"),
				Button.primary("ttt_" + buttonId + "_2!", "2"),
				Button.primary("ttt_" + buttonId + "_3!", "3"),
		};
	}
}
