package gomoku;

public class GomokuPosition extends Position {

    final static public int BLANK = 0; // BLANK square.
    final static public int PLAYER1 = 1; // Blanck stone.
    final static public int PLAYER2 = 2; // White stone.
    final static public int PROGRAM = -1; // White stone.

    public final static int GOMOKUBOARD_SIZE = 19;

    int[][] board = new int[GOMOKUBOARD_SIZE][GOMOKUBOARD_SIZE];

    public GomokuPosition() {
        setDefaultState();
    }

    public void setDefaultState() {
        for (int i = 0; i < GOMOKUBOARD_SIZE; i++) {
            for (int j = 0; j < GOMOKUBOARD_SIZE; j++) {
                board[i][j] = BLANK;
            }
        }
    }

    public GomokuPosition getNewPosition() {
        GomokuPosition pos = new GomokuPosition();
        for (int i = 0; i < GOMOKUBOARD_SIZE; i++) {
            for (int j = 0; j < GOMOKUBOARD_SIZE; j++) {
                pos.board[i][j] = board[i][j];
            }
        }
        return pos;
    }
}
