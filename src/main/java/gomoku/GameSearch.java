package gomoku;

import java.util.*;
import javax.swing.JFrame;

public abstract class GameSearch extends JFrame {

    public static final boolean DEBUG = false;

    /*
     * Note: the abstract Position also needs to be
     *       subclassed to write a new game program.
     */
 /*
     * Note: the abstract class Move also needs to be subclassed.
     *       
     */
    public static boolean PROGRAM = false;
    public static boolean HUMAN = true;
    public static int PLAYER1 = 1;
    public static int PLAYER2 = 2;

    /**
     * Notes: PROGRAM false -1, HUMAN true 1
     */

    /*
     * Abstract methods:
     */
    public abstract boolean drawnPosition(Position p);

    public abstract boolean wonPosition(Position p, boolean player);

    public abstract float positionEvaluation(Position p, boolean player);

    public abstract Position[] possibleMoves(Position p, boolean player);

    public abstract boolean reachedMaxDepth(Position p, int depth);

    // public abstract Move createMove();

    /*
     * For 2 Players:
     */
    public abstract boolean wonPosition(Position p, int player);

    /*
     * Search utility methods:
     */
    protected Vector alphaBeta(int depth, Position p, boolean player) {
        Vector v = alphaBetaHelper(depth, p, player, 1000000.0f, -1000000.0f);
        // System.out.println("^^ v(0): " + v.elementAt(0) + ", v(1): " + v.elementAt(1));
        return v;
    }

    protected Vector alphaBetaHelper(int depth, Position p,
            boolean player, float alpha, float beta) {
        if (reachedMaxDepth(p, depth)) {
            Vector v = new Vector(2);
            float value = positionEvaluation(p, player);
            v.addElement(new Float(value));
            v.addElement(null);
            if (GameSearch.DEBUG) {
                System.out.println(" alphaBetaHelper: mx depth at " + depth
                        + ", value=" + value);
            }
            return v;
        }
        Vector best = new Vector();
        Position[] moves = possibleMoves(p, player);
        for (int i = 0; i < moves.length; i++) {
            Vector v2 = alphaBetaHelper(depth + 1, moves[i], !player, -beta, -alpha);
            //if (v2 == null || v2.size() < 1) continue;
            float value = -((Float) v2.elementAt(0)).floatValue();
            if (value > beta) {
                beta = value;
                best = new Vector();
                best.addElement(moves[i]);
                Enumeration enum2 = v2.elements();
                enum2.nextElement(); // skip previous value
                while (enum2.hasMoreElements()) {
                    Object o = enum2.nextElement();
                    if (o != null) {
                        best.addElement(o);
                    }
                }
            }
            /**
             * Use the alpha-beta cutoff test to abort search if we found a move
             * that proves that the previous move in the move chain was dubious
             */
            if (beta >= alpha) {
                break;
            }
        }
        Vector v3 = new Vector();
        v3.addElement(new Float(beta));
        Enumeration enum2 = best.elements();
        while (enum2.hasMoreElements()) {
            v3.addElement(enum2.nextElement());
        }
        return v3;
    }
}
