package gomoku;

public class GomokuPosition extends Position {

    final static public int BLANK = 0;
    final static public int PLAYER1 = 1;
    final static public int PLAYER2 = 2;
    final static public int PROGRAM = -1;
    int[][] board = new int[19][19];

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                sb.append("" + board[i][j] + ",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
