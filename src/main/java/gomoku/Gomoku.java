package gomoku;

import java.util.Scanner;

public class Gomoku extends GameSearch {

    public Gomoku() {
        super();
    }

    @Override
    public boolean drawnPosition(Position p) {
        if (GameSearch.DEBUG) {
            System.out.println("drawnPosition(" + p + ")");
        }
        boolean ret = true;
        GomokuPosition pos = (GomokuPosition) p;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == GomokuPosition.BLANK) {
                    ret = false;
                    break;
                }
            }
        }
        if (GameSearch.DEBUG) {
            System.out.println("     ret=" + ret);
        }
        return ret;
    }

    @Override
    public boolean wonPosition(Position p, boolean player) {
        if (GameSearch.DEBUG) {
            System.out.println("wonPosition(" + p + "," + player + ")");
        }
        boolean ret = false;
        GomokuPosition pos = (GomokuPosition) p;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (winCheck(i, j, i, j + 1, i, j + 2, i, j + 3, i, j + 4, player, pos)) {
                    ret = true;
                } else if (winCheck(i, j, i + 1, j, i + 2, j, i + 3, j, i + 4, j, player, pos)) {
                    ret = true;
                }
                if (j < 14) {
                    if (winCheck(i, j + 1, i + 1, j + 2, i + 2, j + 3, i + 3, j + 4, i + 4, j + 5, player, pos)) {
                        ret = true;
                    }
                }
            }
            if (winCheck(i, i, i + 1, i + 1, i + 2, i + 2, i + 3, i + 3, i + 4, i + 4, player, pos)) {
                ret = true;
            }
        }
        if (GameSearch.DEBUG) {
            System.out.println("     ret=" + ret);
        }
        return ret;
    }

    @Override
    public boolean wonPosition(Position p, int player) {
        if (GameSearch.DEBUG) {
            System.out.println("wonPosition(" + p + "," + player + ")");
        }
        boolean ret = false;
        GomokuPosition pos = (GomokuPosition) p;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (winCheck(i, j, i, j + 1, i, j + 2, i, j + 3, i, j + 4, player, pos)) {
                    ret = true;
                } else if (winCheck(i, j, i + 1, j, i + 2, j, i + 3, j, i + 4, j, player, pos)) {
                    ret = true;
                }
                if (j < 14) {
                    if (winCheck(i, j + 1, i + 1, j + 2, i + 2, j + 3, i + 3, j + 4, i + 4, j + 5, player, pos)) {
                        ret = true;
                    }
                }
            }
            if (winCheck(i, i, i + 1, i + 1, i + 2, i + 2, i + 3, i + 3, i + 4, i + 4, player, pos)) {
                ret = true;
            }
        }
        if (GameSearch.DEBUG) {
            System.out.println("     ret=" + ret);
        }
        return ret;
    }

    private boolean winCheck(int i1, int j1, int i2, int j2, int i3, int j3, int i4, int j4, int i5, int j5,
            boolean player, GomokuPosition pos) {
        int b;
        if (player) {
            b = GomokuPosition.PLAYER1;
        } else {
            b = GomokuPosition.PROGRAM;
        }
        if (pos.board[i1][j1] == b
                && pos.board[i2][j2] == b
                && pos.board[i3][j3] == b
                && pos.board[i4][j4] == b
                && pos.board[i5][j5] == b) {
            return true;
        }
        return false;
    }

    private boolean winCheck(int i1, int j1, int i2, int j2, int i3, int j3, int i4, int j4, int i5, int j5,
            int player, GomokuPosition pos) {
        if (pos.board[i1][j1] == player
                && pos.board[i2][j2] == player
                && pos.board[i3][j3] == player
                && pos.board[i4][j4] == player
                && pos.board[i5][j5] == player) {
            return true;
        }
        return false;
    }

    @Override
    public float positionEvaluation(Position p, boolean player) {
        int count = 0;
        GomokuPosition pos = (GomokuPosition) p;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                // System.out.println("Pos :" + pos.board[i][j]);
                if (pos.board[i][j] == 0) {
                    count++;
                }
            }
        }
        count = 362 - count;
        // prefer the center square:
        float base = 1.0f;
        if (pos.board[0][0] == GomokuPosition.PLAYER1
                && player) {
            base += 0.4f;
        }
        if (pos.board[0][0] == GomokuPosition.PROGRAM
                && !player) {
            base -= 0.4f;
        }
        float ret = (base - 1.0f);
        if (wonPosition(p, player)) {
            return base + (1.0f / count);
        }
        if (wonPosition(p, !player)) {
            return -(base + (1.0f / count));
        }
        return ret;
    }

    @Override
    public void printPosition(Position p, boolean PlayerVsPlayer) {
        System.out.println("Board position:");
        GomokuPosition pos = (GomokuPosition) p;
        int rowCount = 0;
        for (int row = 0; row < 19; row++) {
            System.out.println();
            int colCount = 0;
            for (int col = 0; col < 19; col++) {
                if (pos.board[rowCount][colCount] == GomokuPosition.PLAYER1 && !PlayerVsPlayer) {
                    System.out.print("H");
                } else if (pos.board[rowCount][colCount] == GomokuPosition.PROGRAM) {
                    System.out.print("P");
                } else if (pos.board[rowCount][colCount] == GomokuPosition.PLAYER1 && PlayerVsPlayer) {
                    System.out.print("1");
                } else if (pos.board[rowCount][colCount] == GomokuPosition.PLAYER2) {
                    System.out.print("2");
                } else {
                    System.out.print("0");
                }
                colCount++;
            }
            rowCount++;
        }
        System.out.println();
    }

    @Override
    public Position[] possibleMoves(Position p, boolean player) {
        if (GameSearch.DEBUG) {
            System.out.println("posibleMoves(" + p + "," + player + ")");
        }
        GomokuPosition pos = (GomokuPosition) p;
        int count = 0;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == 0) {
                    count++;
                }
            }
        }
        if (count == 0) {
            return null;
        }
        Position[] ret = new Position[count];
        count = 0;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == 0) {
                    GomokuPosition pos2 = new GomokuPosition();
                    for (int k = 0; k < 19; k++) {
                        for (int l = 0; l < 19; l++) {
                            pos2.board[k][l] = pos.board[k][l];
                        }
                    }
                    if (player) {
                        pos2.board[i][j] = 1;
                    } else {
                        pos2.board[i][j] = -1;
                    }
                    ret[count++] = pos2;
                    if (GameSearch.DEBUG) {
                        System.out.println("    " + pos2);
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public Position makeMove(Position p, boolean player, Move move) {
        if (GameSearch.DEBUG) {
            System.out.println("Entered Gomoku.makeMove");
        }
        GomokuMove m = (GomokuMove) move;
        GomokuPosition pos = (GomokuPosition) p;
        GomokuPosition pos2 = new GomokuPosition();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                pos2.board[i][j] = pos.board[i][j];
            }
        }
        int pp;
        if (player) {
            pp = 1;
        } else {
            pp = -1;
        }
        if (GameSearch.DEBUG) {
            System.out.println("makeMove: m.moveRowIndex = " + m.moveRowIndex + ", m.moveColIndex = " + m.moveColIndex);
        }
        if (pos2.board[m.moveRowIndex][m.moveColIndex] != 0) {
            System.out.println("Please choose an empty square.");
            Move move1 = createMove();
            return makeMove(p, player, move1);
        } else {
            pos2.board[m.moveRowIndex][m.moveColIndex] = pp;
        }
        return pos2;
    }

    @Override
    public Position makeMove(Position p, int player, Move move) {
        if (GameSearch.DEBUG) {
            System.out.println("Entered Gomoku.makeMove");
        }
        GomokuMove m = (GomokuMove) move;
        GomokuPosition pos = (GomokuPosition) p;
        GomokuPosition pos2 = new GomokuPosition();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                pos2.board[i][j] = pos.board[i][j];
            }
        }
        if (GameSearch.DEBUG) {
            System.out.println("makeMove: m.moveRowIndex = " + m.moveRowIndex + ", m.moveColIndex = " + m.moveColIndex);
        }
        if (pos2.board[m.moveRowIndex][m.moveColIndex] != 0) {
            System.out.println("Please choose an empty square.");
            Move move1 = createMove();
            return makeMove(p, player, move1);
        } else {
            pos2.board[m.moveRowIndex][m.moveColIndex] = player;
        }
        return pos2;
    }

    @Override
    public boolean reachedMaxDepth(Position p, int depth) {
        boolean ret = false;
        if (depth >= 3) {
            return true;
        }
        if (wonPosition(p, false)) {
            ret = true;
        } else if (wonPosition(p, true)) {
            ret = true;
        } else if (drawnPosition(p)) {
            ret = true;
        }
        if (GameSearch.DEBUG) {
            System.out.println("reachedMaxDepth: pos=" + p.toString() + ", depth=" + depth
                    + ", ret=" + ret);
        }
        return ret;
    }

    @Override
    public Move createMove() {
        if (GameSearch.DEBUG) {
            System.out.println("Enter blank square index [0,8]:");
        }
        int row = 0;
        int col = 0;
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("row= ");
            row = sc.nextInt();

            System.out.print("col= ");
            col = sc.nextInt();
            System.out.println("row=" + row + ", col=" + col);
        } catch (Exception e) {
        }
        GomokuMove mm = new GomokuMove();
        mm.moveRowIndex = row;
        mm.moveColIndex = col;
        return mm;
    }

    static public void main(String[] args) {
        GomokuPosition p = new GomokuPosition();
        Gomoku gomoku = new Gomoku();
        gomoku.playGame(p, true);
    }
}
