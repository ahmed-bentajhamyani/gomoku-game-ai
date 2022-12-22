package gomoku;

import static gomoku.GameSearch.HUMAN;
import static gomoku.GameSearch.PLAYER1;
import static gomoku.GameSearch.PLAYER2;
import static gomoku.GameSearch.PROGRAM;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Gomoku extends GameSearch {

    private int row, col;
    private final JLabel jblGomoku = new JLabel("Gomoku Game");
    private JButton jbtStart = new JButton("Start!");
    private final JButton jbtUndo = new JButton("Undo");
    private final JButton jbtHelp = new JButton("Help");
    private final JButton jbtExit = new JButton("Exit");
    private JButton jbtSave = new JButton("Previous Game!");
    private GomokuBoard gomokuboard = new GomokuBoard();
    private GomokuPosition pos = new GomokuPosition();
    private boolean gameState = false;  // to show statue of game: true for continue, false for end.
    private boolean canClick = true;
    private boolean player = true; // true for human, false for program 
    private int currentPlayer = 1; // for Player Vs Player by default Player 1 who plays first

    private final JRadioButton jrbPlayerVsProgram = new JRadioButton("Human Vs Computer", false);
    private final JRadioButton jrbPlayerVsPlayer = new JRadioButton("Human Vs Human", true);

    // levels
    private JLabel jblLevel = new JLabel("Level");
    private JRadioButton jrbEasy = new JRadioButton("Easy", false);
    private JRadioButton jrbNormal = new JRadioButton("Normal", true);
    private JRadioButton jrbHard = new JRadioButton("Hard", false);

    private int maxDepth = 2;

    static Thread gameTimeThread;

    // save
    Save save = new Save();
    int tab[][] = new int[GomokuPosition.GOMOKUBOARD_SIZE][GomokuPosition.GOMOKUBOARD_SIZE];

    public Gomoku() {
        setLayout(null);  // manuel layout
        setPreferredSize(new Dimension(700, 600)); // frame dimension

        // add gomokuboard
        add(gomokuboard);

        // title
        jblGomoku.setFont(new Font("Serif", Font.BOLD, 25));
        jblGomoku.setBackground(new Color(128, 128, 128));
        add(jblGomoku);

        // start button
        jbtStart.setBackground(new Color(60, 179, 113));
        add(jbtStart);

        // Player vs Player or ...
        jrbPlayerVsProgram.setBackground(new Color(255, 255, 255));
        add(jrbPlayerVsProgram);
        jrbPlayerVsPlayer.setBackground(new Color(255, 255, 255));
        add(jrbPlayerVsPlayer);

        ButtonGroup playerVsPlayer = new ButtonGroup();
        playerVsPlayer.add(jrbPlayerVsProgram);
        playerVsPlayer.add(jrbPlayerVsPlayer);

        // levels
        jblLevel.setBackground(new Color(255, 255, 255));
        jblLevel.setVisible(false);
        add(jblLevel);

        // easy
        jrbEasy.setBackground(new Color(255, 255, 255));
        jrbEasy.setVisible(false);
        add(jrbEasy);

        // normal
        jrbNormal.setBackground(new Color(255, 255, 255));
        jrbNormal.setVisible(false);
        add(jrbNormal);

        // hard
        jrbHard.setBackground(new Color(255, 255, 255));
        jrbHard.setVisible(false);
        add(jrbHard);

        ButtonGroup level = new ButtonGroup();
        level.add(jrbEasy);
        level.add(jrbNormal);
        level.add(jrbHard);

        // undo button
        jbtUndo.setBackground(new Color(70, 130, 180));
        add(jbtUndo);

        // help button
        jbtHelp.setBackground(new Color(70, 130, 180));
        add(jbtHelp);

        // save button
        jbtSave.setBackground(new Color(95, 158, 160));
        add(jbtSave);

        // exit button
        jbtExit.setBackground(new Color(250, 78, 78));
        add(jbtExit);


        /* Set the position and size of each component by calling
           its setBounds() method. */
        gomokuboard.setBounds(20, 20, 460, 460);
        jblGomoku.setBounds(510, 20, 450, 30);
        jbtStart.setBounds(510, 80, 150, 30);
        jbtUndo.setBounds(510, 120, 150, 30);
        jbtHelp.setBounds(510, 120, 150, 30);
        jrbPlayerVsPlayer.setBounds(510, 160, 150, 30);
        jrbPlayerVsProgram.setBounds(510, 190, 150, 30);
        jblLevel.setBounds(510, 220, 150, 30);
        jrbEasy.setBounds(510, 250, 60, 30);
        jrbNormal.setBounds(570, 250, 70, 30);
        jrbHard.setBounds(640, 250, 70, 30);
        jbtSave.setBounds(510, 450, 130, 30);
        jbtExit.setBounds(650, 450, 80, 30);

        jrbPlayerVsProgram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!gameState && jrbPlayerVsProgram.isSelected()) {
                    jblLevel.setVisible(true);
                    jrbEasy.setVisible(true);
                    jrbNormal.setVisible(true);
                    jrbHard.setVisible(true);
                    jbtUndo.setVisible(false);
                    jbtHelp.setVisible(true);
                }
            }
        });

        jrbPlayerVsPlayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!gameState && jrbPlayerVsPlayer.isSelected()) {
                    jblLevel.setVisible(false);
                    jrbEasy.setVisible(false);
                    jrbNormal.setVisible(false);
                    jrbHard.setVisible(false);
                    jbtUndo.setVisible(true);
                    jbtHelp.setVisible(false);
                }
            }
        });

        jbtStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!gameState) {
                    gameState = true;
                    jbtStart.setText("Restart");
                    jbtSave.setText("Save Game");

                    if (jrbEasy.isSelected()) {
                        maxDepth = 1;
                    } else if (jrbNormal.isSelected()) {
                        maxDepth = 2;
                    } else if (jrbHard.isSelected()) {
                        maxDepth = 3;
                    }
                } else {
                    setDefaultStatue();
                    gomokuboard.repaint();
                }
            }
        });

        jbtUndo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pos.board[row][col] = GomokuPosition.BLANK;
                if (currentPlayer == 1) {
                    currentPlayer = PLAYER2;
                } else if (currentPlayer == 2) {
                    currentPlayer = PLAYER1;
                }
                repaint();
            }
        });

        jbtHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread thread3 = new Thread(new Runnable() {
                    public void run() {
                        Vector v = alphaBeta(0, (Position) pos, PROGRAM);
                        Enumeration enum2 = v.elements();
                        while (enum2.hasMoreElements()) {
                            System.out.println(" next element: " + enum2.nextElement());
                        }
                        pos = (GomokuPosition) v.elementAt(1);
                        for (int i = 0; i < 19; i++) {
                            for (int j = 0; j < 19; j++) {
                                tab[i][j] = pos.board[i][j];
                            }
                        }
                        repaint();
                        player = HUMAN;
                        canClick = true;
                        if (wonPosition(pos, PROGRAM)) {
                            JOptionPane.showMessageDialog(null, "Computer win!");
                            gameState = false;
                            return;
                        } else if (drawnPosition(pos)) {
                            JOptionPane.showMessageDialog(null, "Draw game!");
                            gameState = false;
                            return;
                        }
                    }
                });

                Thread thread2 = new Thread(new Runnable() {
                    public void run() {
                        Vector v = alphaBeta(0, (Position) pos, HUMAN);
                        Enumeration enum2 = v.elements();
                        while (enum2.hasMoreElements()) {
                            System.out.println(" next element: " + enum2.nextElement());
                        }
                        pos = (GomokuPosition) v.elementAt(1);
                        for (int i = 0; i < 19; i++) {
                            for (int j = 0; j < 19; j++) {
                                tab[i][j] = pos.board[i][j];
                            }
                        }
                        repaint();
                        player = PROGRAM;
                        canClick = false;
                        if (wonPosition(pos, HUMAN)) {
                            JOptionPane.showMessageDialog(null, "Human win!");
                            gameState = false;
                            return;
                        } else if (drawnPosition(pos)) {
                            JOptionPane.showMessageDialog(null, "Draw game!");
                            gameState = false;
                            return;
                        }

                        // Program turn
                        thread3.setPriority(Thread.MAX_PRIORITY);
                        thread3.start();
                    }
                });

                thread2.setPriority(Thread.MAX_PRIORITY);
                thread2.start();
            }
        });

        jbtSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jbtSave.getText().equals("Save Game")) {

                    save.saveGomoko(tab);
                    jbtSave.setText("Previous Game!");
                    setDefaultStatue();
                    gomokuboard.repaint();
                } else {
                    pos.board = save.getSavedGomoko();
                    gameState = true;
                    jbtStart.setText("Restart");
                    jbtSave.setText("Save Game");
                    repaint();

                }

            }
        });

        jbtExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public void setDefaultStatue() {
        pos.setDefaultState();

        gameState = false;
        player = true;

        jbtStart.setText("Start!");

        jrbNormal.setSelected(true);
        maxDepth = 2;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Thread() {
            public void run() {
                Gomoku frame = new Gomoku();
                frame.setTitle("Gomoku Game");
                frame.getContentPane().setBackground(new Color(255, 255, 255));
                frame.setSize(770, 550);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    @Override
    public boolean drawnPosition(Position p) {
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
        return ret;
    }

    @Override
    public boolean wonPosition(Position p, boolean player) {
        boolean ret = false;
        GomokuPosition pos = (GomokuPosition) p;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (j < 15 && winCheck(i, j, i, j + 1, i, j + 2, i, j + 3, i, j + 4, player, pos)) {
                    ret = true;
                } else if (i < 15 && winCheck(i, j, i + 1, j, i + 2, j, i + 3, j, i + 4, j, player, pos)) {
                    ret = true;
                } else if (i < 15 && j < 14 && winCheck(i, j + 1, i + 1, j + 2, i + 2, j + 3, i + 3, j + 4, i + 4, j + 5, player, pos)) {
                    ret = true;
                } else if (i < 15 && j > 3 && winCheck(i, j, i + 1, j - 1, i + 2, j - 2, i + 3, j - 3, i + 4, j - 4, player, pos)) {
                    ret = true;
                }

            }
            if (i < 15 && winCheck(i, i, i + 1, i + 1, i + 2, i + 2, i + 3, i + 3, i + 4, i + 4, player, pos)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override
    public boolean wonPosition(Position p, int player) {
        boolean ret = false;
        GomokuPosition pos = (GomokuPosition) p;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (j < 15 && winCheck(i, j, i, j + 1, i, j + 2, i, j + 3, i, j + 4, player, pos)) {
                    ret = true;
                } else if (i < 15 && winCheck(i, j, i + 1, j, i + 2, j, i + 3, j, i + 4, j, player, pos)) {
                    ret = true;
                } else if (i < 15 && j < 14 && winCheck(i, j + 1, i + 1, j + 2, i + 2, j + 3, i + 3, j + 4, i + 4, j + 5, player, pos)) {
                    ret = true;
                } else if (i < 15 && j > 3 && winCheck(i, j, i + 1, j - 1, i + 2, j - 2, i + 3, j - 3, i + 4, j - 4, player, pos)) {
                    ret = true;
                }

            }
            if (i < 15 && winCheck(i, i, i + 1, i + 1, i + 2, i + 2, i + 3, i + 3, i + 4, i + 4, player, pos)) {
                ret = true;
            }
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
        int[][] myConnects = connectN(p, player, 5);
        int[][] enemyConnects = connectN(p, !player, 5);

        // if there is a connect 5 or two open connect 4, then return positive infinity or negative infinity
        if (myConnects[3][0] > 0 || myConnects[2][2] > 0) {
            return Float.POSITIVE_INFINITY;
        }
        if (enemyConnects[3][0] > 0 || enemyConnects[2][2] > 0) {
            return Float.NEGATIVE_INFINITY;
        }

        int ret = 0;
        GomokuPosition pos = (GomokuPosition) p;

        int[] score = {10, 100, 1000};

        int myStone;
        int enemyStone;
        if (player) {
            myStone = GomokuPosition.PLAYER1;
            enemyStone = GomokuPosition.PROGRAM;
        } else {
            myStone = GomokuPosition.PROGRAM;
            enemyStone = GomokuPosition.PLAYER1;
        }

        for (int i = 0; i < pos.board.length; i++) {
            for (int j = 0; j < pos.board[0].length; j++) {
                if (pos.board[i][j] == myStone) {
                    if (distanceToBoundary(p, i, j) == 0) {
                        ret += 1;
                    } else if (distanceToBoundary(p, i, j) == 1) {
                        ret += 2;
                    } else if (distanceToBoundary(p, i, j) == 2) {
                        ret += 4;
                    } else {
                        ret += 8;
                    }
                } else if (pos.board[i][j] == enemyStone) {
                    if (distanceToBoundary(p, i, j) == 0) {
                        ret -= 1;
                    } else if (distanceToBoundary(p, i, j) == 1) {
                        ret -= 2;
                    } else if (distanceToBoundary(p, i, j) == 2) {
                        ret -= 4;
                    } else {
                        ret -= 8;
                    }
                }
            }
        }

        ret += myConnects[0][1] * score[0];
        ret += myConnects[0][2] * (int) (score[1] * 0.9);
        ret += myConnects[1][1] * score[1];
        ret += myConnects[1][2] * (int) (score[2] * 0.9);
        ret += myConnects[2][1] * score[2];

        ret -= enemyConnects[0][1] * score[0];
        ret -= enemyConnects[0][2] * (int) (score[1] * 0.9);
        ret -= enemyConnects[1][1] * score[1];
        ret -= enemyConnects[1][2] * (int) (score[2] * 0.9);
        ret -= enemyConnects[2][1] * score[2];

        return ret;
    }

    /**
     * return number of connect 2, 3, ..., number, the result is stored in an
     * array[][]. the first dimension is connect number, 0 represents 2,...
     * second dimension is number of connects, 0 represents all, 1 represents
     * one end open, 2 represents two ends open
     *
     * @param p
     * @param player
     * @param number
     * @return
     */
    private int[][] connectN(Position p, boolean player, int number) {
        GomokuPosition pos = (GomokuPosition) p;
        int b;
        if (player) {
            b = GomokuPosition.PLAYER1;
        } else {
            b = GomokuPosition.PROGRAM;
        }
        int count[][] = new int[number - 1][3];
        for (int i = 0; i < count.length; i++) {
            for (int j = 0; j < count[0].length; j++) {
                count[i][j] = 0;
            }
        }

        for (int n = 2; n <= number; n++) {
            for (int i = 0; i < pos.board.length; i++) {
                for (int j = 0; j < pos.board[0].length; j++) {
                    if (i + n - 1 < pos.board.length) {
                        if (downSame(p, i, j, n - 1, b)) {
                            while (true) {
                                if (i + n < pos.board.length) {
                                    if (pos.board[i + n][j] == b) {
                                        break;
                                    }
                                }
                                if (i - 1 >= 0) {
                                    if (pos.board[i - 1][j] == b) {
                                        break;
                                    }
                                }

                                count[n - 2][0]++;

                                if (i - 1 >= 0 && i + n >= pos.board.length) {
                                    if (pos.board[i - 1][j] == GomokuPosition.BLANK) {
                                        count[n - 2][1]++;
                                    }
                                } else if (i - 1 < 0 && i + n < pos.board.length) {
                                    if (pos.board[i + n][j] == GomokuPosition.BLANK) {
                                        count[n - 2][1]++;
                                    }
                                } else if (i - 1 >= 0 && i + n < pos.board.length) {
                                    if (pos.board[i - 1][j] == GomokuPosition.BLANK
                                            && pos.board[i + n][j] != GomokuPosition.BLANK) {
                                        count[n - 2][1]++;
                                    }
                                    if (pos.board[i + n][j] == GomokuPosition.BLANK
                                            && pos.board[i - 1][j] != GomokuPosition.BLANK) {
                                        count[n - 2][1]++;
                                    }

                                    if (pos.board[i - 1][j] == GomokuPosition.BLANK
                                            && pos.board[i + n][j] == GomokuPosition.BLANK) {
                                        count[n - 2][2]++;
                                    }
                                }
                                break;
                            }
                        }

                        if (j + n - 1 < pos.board[0].length) {
                            if (rightSame(p, i, j, n - 1, b)) {
                                while (true) {
                                    if (j + n < pos.board[0].length) {
                                        if (pos.board[i][j + n] == b) {
                                            break;
                                        }
                                    }
                                    if (j - 1 >= 0) {
                                        if (pos.board[i][j - 1] == b) {
                                            break;
                                        }
                                    }
                                    count[n - 2][0]++;

                                    if (j - 1 >= 0 && j + n >= pos.board[0].length) {
                                        if (pos.board[i][j - 1] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if (j - 1 < 0 && j + n < pos.board[0].length) {
                                        if (pos.board[i][j + n] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if (j - 1 >= 0 && j + n < pos.board[0].length) {
                                        if (pos.board[i][j - 1] == GomokuPosition.BLANK
                                                && pos.board[i][j + n] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                        if (pos.board[i][j + n] == GomokuPosition.BLANK
                                                && pos.board[i][j - 1] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }

                                        if (pos.board[i][j - 1] == GomokuPosition.BLANK
                                                && pos.board[i][j + n] == GomokuPosition.BLANK) {
                                            count[n - 2][2]++;
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                        if (i + n - 1 < pos.board.length && j + n - 1 < pos.board[0].length) {
                            if (rightDownSame(p, i, j, n - 1, b)) {
                                while (true) {
                                    if (i + n < pos.board.length && j + n < pos.board[0].length) {
                                        if (pos.board[i + n][j + n] == b) {
                                            break;
                                        }
                                    }
                                    if (i - 1 >= 0 && j - 1 >= 0) {
                                        if (pos.board[i - 1][j - 1] == b) {
                                            break;
                                        }
                                    }
                                    count[n - 2][0]++;

                                    if ((i - 1 >= 0 && j - 1 >= 0) && (i + n >= pos.board.length || j + n >= pos.board[0].length)) {
                                        if (pos.board[i - 1][j - 1] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if ((i - 1 < 0 || j - 1 < 0) && (i + n < pos.board.length && j + n < pos.board[0].length)) {
                                        if (pos.board[i + n][j + n] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if ((i - 1 >= 0 && j - 1 >= 0) && (i + n < pos.board.length && j + n < pos.board[0].length)) {
                                        if (pos.board[i - 1][j - 1] == GomokuPosition.BLANK
                                                && pos.board[i + n][j + n] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                        if (pos.board[i + n][j + n] == GomokuPosition.BLANK
                                                && pos.board[i - 1][j - 1] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                        if (pos.board[i - 1][j - 1] == GomokuPosition.BLANK && pos.board[i + n][j + n] == GomokuPosition.BLANK) {
                                            count[n - 2][2]++;
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                        if (i - n + 1 >= 0 && j + n - 1 < pos.board[0].length) {
                            if (rightUpSame(p, i, j, n - 1, b)) {
                                while (true) {
                                    if (i - n >= 0 && j + n < pos.board[0].length) {
                                        if (pos.board[i - n][j + n] == b) {
                                            break;
                                        }
                                    }
                                    if (i + 1 < pos.board.length && j - 1 >= 0) {
                                        if (pos.board[i + 1][j - 1] == b) {
                                            break;
                                        }
                                    }
                                    count[n - 2][0]++;

                                    if ((i - n >= 0 && j + n < pos.board[0].length) && (i + 1 >= pos.board.length || j - 1 < 0)) {
                                        if (pos.board[i - n][j + n] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if ((i - n < 0 || j + n > pos.board[0].length) && (i + 1 < pos.board.length && j - 1 >= 0)) {
                                        if (pos.board[i + 1][j - 1] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if ((i - n >= 0 && j + n < pos.board[0].length) && (i + 1 < pos.board.length && j - 1 >= 0)) {
                                        if (pos.board[i - n][j + n] == GomokuPosition.BLANK
                                                && pos.board[i + 1][j - 1] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                        if (pos.board[i + 1][j - 1] == GomokuPosition.BLANK
                                                && pos.board[i - n][j + n] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                        if (pos.board[i - n][j + n] == GomokuPosition.BLANK && pos.board[i + 1][j - 1] == GomokuPosition.BLANK) {
                                            count[n - 2][2]++;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    private boolean downSame(Position p, int i, int j, int n, int b) {
        GomokuPosition pos = (GomokuPosition) p;
        for (int k = 0; k <= n; k++) {
            if (pos.board[i + k][j] != b) {
                return false;
            }
        }
        return true;
    }

    private boolean rightSame(Position p, int i, int j, int n, int b) {
        GomokuPosition pos = (GomokuPosition) p;
        for (int k = 0; k <= n; k++) {
            if (pos.board[i][j + k] != b) {
                return false;
            }
        }
        return true;
    }

    private boolean rightDownSame(Position p, int i, int j, int n, int b) {
        GomokuPosition pos = (GomokuPosition) p;
        for (int k = 0; k <= n; k++) {
            if (pos.board[i + k][j + k] != b) {
                return false;
            }
        }
        return true;
    }

    private boolean rightUpSame(Position p, int i, int j, int n, int b) {
        GomokuPosition pos = (GomokuPosition) p;
        for (int k = 0; k <= n; k++) {
            if (pos.board[i - k][j + k] != b) {
                return false;
            }
        }
        return true;
    }

    private int distanceToBoundary(Position p, int x, int y) {
        GomokuPosition pos = (GomokuPosition) p;
        int xToUp = x;
        int xToDown = pos.board.length - 1 - x;
        int yToLeft = y;
        int yToRight = pos.board[0].length - 1 - y;

        xToUp = Math.min(xToUp, xToDown);
        xToUp = Math.min(xToUp, yToLeft);
        xToUp = Math.min(xToUp, yToRight);
        return xToUp;
    }

    @Override
    public Position[] possibleMoves(Position p, boolean player) {
        GomokuPosition pos = (GomokuPosition) p;
        int count = 0;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (pos.board[i][j] == GomokuPosition.BLANK) {
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
                if (pos.board[i][j] == GomokuPosition.BLANK) {
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
    public boolean reachedMaxDepth(Position p, int depth) {
        boolean ret = false;
        if (wonPosition(p, false)) {
            ret = true;
        } else if (wonPosition(p, true)) {
            ret = true;
        } else if (drawnPosition(p)) {
            ret = true;
        }
        if (depth >= maxDepth) {
            return true;
        }
        return ret;
    }

    class GomokuBoard extends JPanel {

        private int width;
        private int height;
        private int widthStep;
        private int heightStep;

        private int xStart;
        private int yStart;
        private int xEnd;
        private int yEnd;

        public GomokuBoard() {
            setBackground(new Color(222, 184, 135));

            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (gameState && canClick && jrbPlayerVsProgram.isSelected()) {
                        int mouseX = 0;
                        int mouseY = 0;
                        int boardX = 0;
                        int boardY = 0;

                        mouseX = e.getX();
                        mouseY = e.getY();
                        if (mouseX >= xStart && mouseX <= xEnd && mouseY >= yStart && mouseY <= yEnd) {
                            boardX = (mouseX - xStart) / widthStep;
                            boardY = (mouseY - yStart) / heightStep;

                            tab[boardX][boardY] = (int) currentPlayer;

                            if (pos.board[boardX][boardY] == GomokuPosition.BLANK) {
                                pos.board[boardX][boardY] = GomokuPosition.PLAYER1;
                                repaint();
                                if (wonPosition(pos, HUMAN)) {
                                    JOptionPane.showMessageDialog(null, "Human win!");
                                    gameState = false;
                                    return;
                                } else if (drawnPosition(pos)) {
                                    JOptionPane.showMessageDialog(null, "Draw game!");
                                    gameState = false;
                                    return;
                                }

                                player = PROGRAM;
                                canClick = false;

                                Thread thread2 = new Thread(new Runnable() {
                                    public void run() {
                                        Vector v = alphaBeta(0, (Position) pos, PROGRAM);
                                        Enumeration enum2 = v.elements();
                                        while (enum2.hasMoreElements()) {
                                            System.out.println(" next element: " + enum2.nextElement());
                                        }
                                        pos = (GomokuPosition) v.elementAt(1);
                                        for (int i = 0; i < 19; i++) {
                                            for (int j = 0; j < 19; j++) {
                                                tab[i][j] = pos.board[i][j];
                                            }
                                        }
                                        repaint();
                                        player = HUMAN;
                                        canClick = true;
                                        if (wonPosition(pos, PROGRAM)) {
                                            JOptionPane.showMessageDialog(null, "Computer win!");
                                            gameState = false;
                                            return;
                                        } else if (drawnPosition(pos)) {
                                            JOptionPane.showMessageDialog(null, "Draw game!");
                                            gameState = false;
                                            return;
                                        }
                                    }
                                });

                                thread2.setPriority(Thread.MAX_PRIORITY);
                                thread2.start();
                            }
                        }
                    } else if (gameState && jrbPlayerVsPlayer.isSelected()) {
                        int mouseX = 0;
                        int mouseY = 0;
                        int boardX = 0;
                        int boardY = 0;

                        mouseX = e.getX();
                        mouseY = e.getY();
                        if (mouseX >= xStart && mouseX <= xEnd && mouseY >= yStart && mouseY <= yEnd) {
                            row = boardX = (mouseX - xStart) / widthStep;
                            col = boardY = (mouseY - yStart) / heightStep;

                            tab[boardX][boardY] = (int) currentPlayer;

                            if (pos.board[boardX][boardY] == GomokuPosition.BLANK) {
                                pos.board[boardX][boardY] = currentPlayer;
                                repaint();
                                if (wonPosition(pos, currentPlayer) && currentPlayer == 1) {
                                    JOptionPane.showMessageDialog(null, "Black win!");
                                    gameState = false;
                                    return;
                                } else if (wonPosition(pos, currentPlayer) && currentPlayer == 2) {
                                    JOptionPane.showMessageDialog(null, "White win!");
                                    gameState = false;
                                    return;
                                } else if (drawnPosition(pos)) {
                                    JOptionPane.showMessageDialog(null, "Draw game!");
                                    gameState = false;
                                    return;
                                }

                                if (currentPlayer == 1) {
                                    currentPlayer = PLAYER2;
                                } else if (currentPlayer == 2) {
                                    currentPlayer = PLAYER1;
                                }
                            }
                        }
                    }
                }
            });
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            width = (int) (getWidth() * 0.98);
            height = (int) (getHeight() * 0.98);
            widthStep = width / GomokuPosition.GOMOKUBOARD_SIZE;
            heightStep = height / GomokuPosition.GOMOKUBOARD_SIZE;

            xStart = (int) (width * 0.01);
            yStart = (int) (height * 0.01);
            xEnd = xStart + GomokuPosition.GOMOKUBOARD_SIZE * widthStep;
            yEnd = yStart + GomokuPosition.GOMOKUBOARD_SIZE * heightStep;

            // draw GomokuBoard
            g.setColor(Color.BLACK);
            g.drawLine(xStart, yStart, xStart, yEnd);
            g.drawLine(xStart, yStart, xEnd, yStart);
            g.drawLine(xEnd, yStart, xEnd, yEnd);
            g.drawLine(xStart, yEnd, xEnd, yEnd);
            for (int i = 1; i < GomokuPosition.GOMOKUBOARD_SIZE; i++) {
                g.drawLine(xStart + i * widthStep, yStart, xStart + i * widthStep, yEnd);
                g.drawLine(xStart, yStart + i * heightStep, xEnd, yStart + i * heightStep);
            }

            // draw stone
            int gomokuRadius = (int) (Math.min(widthStep, heightStep) * 0.9 * 0.5);
            for (int i = 0; i < pos.board.length; i++) {
                for (int j = 0; j < pos.board[0].length; j++) {
                    if (pos.board[i][j] == GomokuPosition.PLAYER1) {
                        g.setColor(Color.BLACK);
                        int xCenter = (int) (xStart + (i + 0.5) * widthStep);
                        int yCenter = (int) (yStart + (j + 0.5) * heightStep);
                        g.fillOval(xCenter - gomokuRadius, yCenter - gomokuRadius, 2 * gomokuRadius, 2 * gomokuRadius);
                    } else if (pos.board[i][j] == GomokuPosition.PROGRAM || pos.board[i][j] == GomokuPosition.PLAYER2) {
                        g.setColor(Color.WHITE);
                        int xCenter = (int) (xStart + (i + 0.5) * widthStep);
                        int yCenter = (int) (yStart + (j + 0.5) * heightStep);
                        g.fillOval(xCenter - gomokuRadius, yCenter - gomokuRadius, 2 * gomokuRadius, 2 * gomokuRadius);
                    }
                }
            }
        }
    }
}
