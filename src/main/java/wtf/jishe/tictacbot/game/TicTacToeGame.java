package wtf.jishe.tictacbot.game;

public class TicTacToeGame {
    private final String player1Id;
    private final String player2Id;
    private final Board board;
    private String currentTurn;
    private GameState state;

    public TicTacToeGame(String player1Id, String player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.board = new Board();
        this.currentTurn = player1Id;
        this.state = GameState.ACTIVE;
    }
}
