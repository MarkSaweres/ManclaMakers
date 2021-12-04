/**
 * This is he View and Controller
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

    private static final int AFinalPit = 5;
    private static final int BFinalPit = 11;
    private static final int BFirstPit = 6;
    private static final int turnA = 0;
    private static final int turnB = 1;
    private static final int undoMax = 3;
    private BoardModel model;  //Model
    private BoardDesign boardDesign; // Strategy
    private ArrayList<JButton> pits = new ArrayList<>();  //Pits
    private boolean atMancala = false;  //If a player lands in their respective Mancala Pit
    private int undoMoveA;
    private int undoMoveB;
    private int currentTurn; // Turn A: 0, Turn B: 1

    public MancalaView() {
        this.model = null;
        undoMoveA = undoMax;
        undoMoveB = undoMax;
        currentTurn = turnA;
        DisplayMainMenu();
    }

    public MancalaView(BoardModel model) {
        this.model = model;
        undoMoveA = undoMax;
        undoMoveB = undoMax;
        currentTurn = turnA;
        DisplayMainMenu();
    }

    public void DisplayMainMenu() {

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
         * CONTROLLER
         */
        for (int i = 0; i < pits.size(); i++) {
            pits.get(i).setBackground(new Color(188, 107, 0));
            pits.get(i).setPreferredSize(new Dimension(100, 120));
            pits.get(i).setBorderPainted(false);

            // Add a listener for each pit in the ArrayList, No Listener is added to the Mancala Pits
            pits.get(i).addMouseListener(new Pit(i) {
                public void mousePressed(MouseEvent e) {
                    int mouseID = this.getMouseListenerID();
                    // Prevents Player B from using Player A's Pits
                    if (currentTurn == turnB && mouseID <= AFinalPit) {
                        messageBoard.setText("Player B's Turn, must use Pits B1-B6");
                    }
                    // prevents player A from using Player B's Pits
                    else if (currentTurn == turnA && mouseID <= BFinalPit && mouseID >= BFirstPit) {
                        messageBoard.setText("Player A's Turn, must use Pits A1-A6");
                    } 
                    //Prevents player B from using an Empty Pit
                    else if (mouseID >= 6 && model.getAmountInPit(mouseID + 1) == 0) {
                        messageBoard.setText("No Stones, choose another Pit");
                    } 
                    //Prevents player A from using an Empty Pit
                    else if (mouseID < 6 && model.getAmountInPit(mouseID) == 0) {
                        messageBoard.setText("No Stones, choose another Pit");
                    } 
                    else if (currentTurn == turnA && mouseID <= AFinalPit) {
                        model.move(mouseID);
                        //Checks if Player A landed in Mancala
                        if (model.isMancalaMaker()) {
                            currentTurn = turnA;
                            messageBoard.setText("Player A Mancala! Go Again!");
                            atMancala = true;
                        } else {
                            currentTurn = turnB;
                            messageBoard.setText("Player B's Turn");
                            atMancala = false;
                        }
                        undoMoveB = undoMax;
                    } else if (currentTurn == turnB && mouseID <= BFinalPit && mouseID >= BFirstPit) {
                        model.move(mouseID);
                        //Checks if Player B landed in Mancala
                        if (model.isMancalaMaker()) {
                            currentTurn = turnB;
                            messageBoard.setText("Player B Mancala! Go Again!");
                            atMancala = true;
                        } else {
                            currentTurn = turnA;
                            messageBoard.setText("Player A's Turn");
                            atMancala = false;
                        }
                        undoMoveA = undoMax;
                    }

                    // Check if the game is over
                    int gameOverFlag = model.checkIfGameOver();
                    if (gameOverFlag > 0) {
                        int winner = model.checkWinner(gameOverFlag);
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

        int[] mancalaData = model.getCurrentBoard();

        //adding Board to the View
        JLabel center = new JLabel(boardDesign.createBoard());
        boardDesign.addStonesToPits(pits, mancalaData);
        boardDesign.addPitsToBoard(pits, center);
        add(center, BorderLayout.CENTER);

        //adding message baord to the View
        JPanel northPanel = new JPanel();
        northPanel.add(messageBoard);
        add(northPanel, BorderLayout.NORTH);

        // undo Button
        JPanel southPanel = new JPanel();
        JTextField undoCountText = new JTextField(100);
        undoCountText.setText("Press Undo To Undo your Move!");

         /**
         * CONTROLLER
         */
        JButton undoButton = new JButton("UNDO");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Arrays.equals(model.getCurrentBoard(), model.getPreviousBoard())) {
                    undoCountText.setText("No Moves to undo");
                } else if (currentTurn == turnB && undoMoveA <= 0) {
                    undoCountText.setText("No More Undo's!");
                } else if (currentTurn == turnA && undoMoveB <= 0) {
                    undoCountText.setText("No More Undo's!");
                } else if (atMancala && currentTurn == turnB) {
                    undoMoveB--;
                    undoCountText.setText("Player B has " + undoMoveB + " more undo's");
                    model.undoMove();
                    if (Arrays.equals(model.getCurrentBoard(), model.getPreviousBoard())) {
                        currentTurn = turnB;// B's turn because PLayer B Reached Mancala
                    }
                    atMancala = false;
                } else if (atMancala && currentTurn == turnA) {
                    undoMoveA--;
                    undoCountText.setText("Player A has " + undoMoveA + " more undo's");
                    model.undoMove();
                    if (Arrays.equals(model.getCurrentBoard(), model.getPreviousBoard())) {
                        currentTurn = turnA;// A's turn because PLayer A Reached Mancala
                    }
                    atMancala = false;
                } else if (currentTurn == turnA) { //Switch Turns because Mancala was not reached the 'previous' turn
                    undoMoveB--;
                    undoCountText.setText("Player B has " + undoMoveB + " more undo's");
                    model.undoMove();
                    currentTurn = turnB;
                } else if (currentTurn == turnB) { //Switch Turns because Mancala was not reached the 'previous' turn
                    undoMoveA--;
                    undoCountText.setText("Player A has " + undoMoveA + " more undo's");
                    model.undoMove();
                    currentTurn = turnA;
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
     * @param e the event representing the change
     */
    public void stateChanged(ChangeEvent e) {
        int[] mancalaData = model.getCurrentBoard();
        boardDesign.addStonesToPits(pits, mancalaData);
        repaint();
    }
}