
/**
 * This is he View and Controller.
 * Take in the model, strategy.
 * Construct to the board view with or without model
 * Display the menu for user to choose between 3 or 4 stones sto start the game.
 * Display the game board
 */
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MancalaView extends JFrame implements ChangeListener {
    private static final int undoMax = 3;
    private static final int turnA = 0;
    private static final int turnB = 1;
    private static final int AFinalPit = 5;
    private static final int BFinalPit = 11;
    private static final int BFirstPit = 6;
    private BoardDesign boardDesign; // Strategy
    private BoardModel model; // Model

    private ArrayList<JButton> pits = new ArrayList<>(); // Pits
    private boolean atMancala = false; // If a player lands in their respective Mancala Pit
    private int player_A_undo;
    private int player_B_undo;
    private int player_turn; // Turn A: 0, Turn B: 1

    public MancalaView() {
        this.model = null;
        player_A_undo = undoMax;
        player_B_undo = undoMax;
        player_turn = turnA;
        Menu();
    }

    /**
     * @param model This is the model parameter
     */
    public MancalaView(BoardModel model) {
        this.model = model;
        player_A_undo = undoMax;
        player_B_undo = undoMax;
        player_turn = turnA;
        Menu();
    }

    /**
     * This is the list of menu where player can choose between 3 or 4 stone to
     * play.
     */
    public void Menu() {
        JFrame frame = new JFrame("Mancala Maker Group 9");
        frame.setLayout(new FlowLayout());
        JButton threeStones = new JButton("Play with 3 stones");
        JButton fourStones = new JButton("Play with 4 stones");
        threeStones.addActionListener(e -> {
            model.initializeTheBoard(3);
            boardDesign = new Board();
            frame.dispose();
            displayBoard();
        });

        fourStones.addActionListener(e -> {
            model.initializeTheBoard(4);
            boardDesign = new Board();
            frame.dispose();
            displayBoard();
        });
        frame.add(threeStones);
        frame.add(fourStones);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Display the game board
     */
    public void displayBoard() {
        setLayout(new BorderLayout(100, 100));
        // Pits for Player A
        JButton pit1AButton = new JButton();
        JButton pit2AButton = new JButton();
        JButton pit3AButton = new JButton();
        JButton pit4AButton = new JButton();
        JButton pit5AButton = new JButton();
        JButton pit6AButton = new JButton();
        // Pits for Player B
        JButton pit1BButton = new JButton();
        JButton pit2BButton = new JButton();
        JButton pit3BButton = new JButton();
        JButton pit4BButton = new JButton();
        JButton pit5BButton = new JButton();
        JButton pit6BButton = new JButton();
        // Add Pits to ArrayList
        pits.add(pit1AButton);
        pits.add(pit2AButton);
        pits.add(pit3AButton);
        pits.add(pit4AButton);
        pits.add(pit5AButton);
        pits.add(pit6AButton);
        pits.add(pit1BButton);
        pits.add(pit2BButton);
        pits.add(pit3BButton);
        pits.add(pit4BButton);
        pits.add(pit5BButton);
        pits.add(pit6BButton);

        // Message Board
        JTextArea messageBoard = new JTextArea();
        messageBoard.setColumns(40);
        messageBoard.setRows(1);
        messageBoard.setText("Game Start!");

        /**
         * THIS IS THE CONTROLLER
         */
        for (int i = 0; i < pits.size(); i++) {
            pits.get(i).setBackground(new Color(188, 107, 0));
            pits.get(i).setPreferredSize(new Dimension(100, 120));
            pits.get(i).setBorderPainted(false);

            /**
             * Add a listener for each pit in the ArrayList, No Listener is added to the
             * Mancala Pits
             */
            pits.get(i).addMouseListener(new Pit(i) {
                public void mousePressed(MouseEvent e) {
                    int mouseID = this.getMouseListenerID();
                    /**
                     * Stop Player B from using Player A's Pits
                     */
                    if (player_turn == turnB && mouseID <= AFinalPit) {
                        messageBoard.setText("Player B's Turn, must use Pits B1-B6");
                    }
                    /**
                     * Stop player A from using Player B's Pits
                     */
                    else if (player_turn == turnA && mouseID <= BFinalPit && mouseID >= BFirstPit) {
                        messageBoard.setText("Player A's Turn, must use Pits A1-A6");
                    }
                    /**
                     * Stop player B from using an Empty Pit
                     */
                    else if (mouseID >= 6 && model.getAmountInPit(mouseID + 1) == 0) {
                        messageBoard.setText("No Stones, choose another Pit");
                    }
                    /**
                     * Stop player A from using an Empty Pit
                     */
                    else if (mouseID < 6 && model.getAmountInPit(mouseID) == 0) {
                        messageBoard.setText("No Stones, choose another Pit");
                    } else if (player_turn == turnA && mouseID <= AFinalPit) {
                        model.move(mouseID);
                        /**
                         * Checks if Player A landed in Mancala
                         */
                        if (model.isMancalaMaker()) {
                            player_turn = turnA;
                            messageBoard.setText("Player A Mancala! Go Again!");
                            atMancala = true;
                        } else {
                            player_turn = turnB;
                            messageBoard.setText("Player B's Turn");
                            atMancala = false;
                        }
                        player_B_undo = undoMax;
                    } else if (player_turn == turnB && mouseID <= BFinalPit && mouseID >= BFirstPit) {
                        model.move(mouseID);
                        /**
                         * Checks if Player B landed in Mancala
                         */
                        if (model.isMancalaMaker()) {
                            player_turn = turnB;
                            messageBoard.setText("Player B Mancala! Go Again!");
                            atMancala = true;
                        } else {
                            player_turn = turnA;
                            messageBoard.setText("Player A's Turn");
                            atMancala = false;
                        }
                        player_A_undo = undoMax;
                    }
                    /**
                     * Flag gameover
                     */
                    int flag_gameover = model.checkIfGameOver();
                    if (flag_gameover > 0) {
                        int winner = model.checkWinner(flag_gameover);
                        if (winner == 1)
                            messageBoard.setText("Player A Wins the Game!");
                        else if (winner == 2)
                            messageBoard.setText("Player B Wins the Game!");
                        else if (winner == 3)
                            messageBoard.setText("No Winner!");
                    }
                }
            });
        }
        /**
         * Adding Board to the View
         */
        int[] game_data = model.getCurrentGameBoard();
        //
        JLabel center = new JLabel(boardDesign.boardCreater());
        boardDesign.addStone(pits, game_data);
        boardDesign.addPit(pits, center);
        add(center, BorderLayout.CENTER);
        /**
         * Adding message board to the View
         */
        JPanel northPanel = new JPanel();
        northPanel.add(messageBoard);
        add(northPanel, BorderLayout.NORTH);
        /**
         * Undo Button
         */
        JPanel southPanel = new JPanel();
        JTextField undoCountText = new JTextField(100);
        undoCountText.setText("Press Undo To Undo your Move!");

        /**
         * THIS IS THE CONTROLLER of the game
         */
        JButton undoButton = new JButton("UNDO");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Arrays.equals(model.getCurrentGameBoard(), model.getpreBoard())) {
                    undoCountText.setText("No Moves to undo");
                } else if (player_turn == turnB && player_A_undo <= 0) {
                    undoCountText.setText("No More Undo's!");
                } else if (player_turn == turnA && player_B_undo <= 0) {
                    undoCountText.setText("No More Undo's!");
                } else if (atMancala && player_turn == turnB) {
                    player_B_undo--;
                    undoCountText.setText("Player B has " + player_B_undo + " more undo's");
                    model.undo_click();
                    if (Arrays.equals(model.getCurrentGameBoard(), model.getpreBoard())) {
                        player_turn = turnB;// B's turn because PLayer B Reached Mancala
                    }
                    atMancala = false;
                } else if (atMancala && player_turn == turnA) {
                    player_A_undo--;
                    undoCountText.setText("Player A has " + player_A_undo + " more undo's");
                    model.undo_click();
                    if (Arrays.equals(model.getCurrentGameBoard(), model.getpreBoard())) {
                        player_turn = turnA;// A's turn because PLayer A Reached Mancala
                    }
                    atMancala = false;
                } else if (player_turn == turnA) { // Switch Turns because Mancala was not reached the 'previous' turn
                    player_B_undo--;
                    undoCountText.setText("Player B has " + player_B_undo + " more undo's");
                    model.undo_click();
                    player_turn = turnB;
                } else if (player_turn == turnB) { // Switch Turns because Mancala was not reached the 'previous' turn
                    player_A_undo--;
                    undoCountText.setText("Player A has " + player_A_undo + " more undo's");
                    model.undo_click();
                    player_turn = turnA;
                }
            }
        });
        southPanel.add(undoCountText);
        southPanel.add(undoButton);
        add(southPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /**
     * Called when the data in the model is changed.
     * 
     * @param event This is the event change parameter
     */
    public void stateChanged(ChangeEvent event) {
        int[] game_data = model.getCurrentGameBoard();
        boardDesign.addStone(pits, game_data);
        repaint();
    }
}