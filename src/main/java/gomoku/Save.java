package gomoku;

/**
 *
 * @author hp
 */
public class Save {

    int board[][] = new int[19][19];

    public void saveGomoko(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    public int[][] getSavedGomoko() {
        return this.board;
    }

    @Override
    public String toString() {
        return "Save{" + "gomoko=" + board + '}';
    }

}
