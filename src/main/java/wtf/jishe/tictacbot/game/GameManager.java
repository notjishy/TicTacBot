package wtf.jishe.tictacbot.game;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private static final GameManager instance = new GameManager();
    private final Map<String, TicTacToeGame> activeGames;

    private GameManager() {
        activeGames = new HashMap<>();
    }

    public static GameManager getInstance() {
        return instance;
    }

    public void addGame(String channelId, TicTacToeGame game) {
        if (activeGames.containsKey(channelId)) {
            throw new IllegalStateException("A game is already active in this channel.");
        }
        activeGames.put(channelId, game);
    }

    public TicTacToeGame getGame(String channelId) {
        return activeGames.get(channelId);
    }

    public void removeGame(String channelId) {
        activeGames.remove(channelId);
    }

    public boolean hasActiveGame(String channelId) {
        return activeGames.containsKey(channelId);
    }
}
