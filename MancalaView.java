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

    private static final int LAST_PIT_OF_A = 5;
    private static final int LAST_PIT_OF_B = 11;
    private static final int FIRST_PIT_OF_B = 6;
    private static final int A_TURN = 0;
    private static final int B_TURN = 1;
    private static final int MAX_NUM_OF_UNDOS = 3;
    private BoardModel theModel;
    private BoardDesign boardDesign; // general strategy
    private ArrayList<JButton> pits = new ArrayList<>();// JButtons Representing Pits
    private boolean mancalaHasReached = false;
    private int undoMoveA;
    private int undoMoveB;
    private int currentTurn; // 0 - A; 1 - B

    public MancalaView() {
        this.theModel = null;
        undoMoveA = MAX_NUM_OF_UNDOS;
        undoMoveB = MAX_NUM_OF_UNDOS;
        currentTurn = A_TURN;
        DisplayMainMenu();
    }

    public MancalaView(BoardModel theModel) {
        this.theModel = theModel;
        undoMoveA = MAX_NUM_OF_UNDOS;
        undoMoveB = MAX_NUM_OF_UNDOS;
        currentTurn = A_TURN;
        DisplayMainMenu();
    }

    public void DisplayMainMenu() {

        JFrame frame = new JFrame("Mancala Maker Group 9");
        frame.setLayout(new FlowLayout());
        JButton three_stone_board = new JButton("Play with 3 stones");
        JButton four_stone_board = new JButton("Play with 4 stones");
        three_stone_board.addActionListener(e -> {
            theModel.initializeTheBoard(3);
            boardDesign = new Board();
            frame.dispose();
            displayBoard();
        });

        four_stone_board.addActionListener(e -> {
            theModel.initializeTheBoard(4);
            boardDesign = new Board();
            frame.dispose();
            displayBoard();
        });
        frame.add(three_stone_board);
        frame.add(four_stone_board);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void displayBoard() {
        setLayout(new BorderLayout(100, 100));

        // Buttons representing pits belonging to player A
        JButton A_pit_1_Button = new JButton();
        JButton A_pit_2_Button = new JButton();
        JButton A_pit_3_Button = new JButton();
        JButton A_pit_4_Button = new JButton();
        JButton A_pit_5_Button = new JButton();
        JButton A_pit_6_Button = new JButton();

        // Buttons representing pits belonging to player B
        JButton B_pit_1_Button = new JButton();
        JButton B_pit_2_Button = new JButton();
        JButton B_pit_3_Button = new JButton();
        JButton B_pit_4_Button = new JButton();
        JButton B_pit_5_Button = new JButton();
        JButton B_pit_6_Button = new JButton();

        // Add JButtons belonging to player A to ArrayList of pits
        pits.add(A_pit_1_Button);
        pits.add(A_pit_2_Button);
        pits.add(A_pit_3_Button);
        pits.add(A_pit_4_Button);
        pits.add(A_pit_5_Button);
        pits.add(A_pit_6_Button);

        // Add JButtons belonging to player B to ArrayList of pits
        pits.add(B_pit_1_Button);
        pits.add(B_pit_2_Button);
        pits.add(B_pit_3_Button);
        pits.add(B_pit_4_Button);
        pits.add(B_pit_5_Button);
        pits.add(B_pit_6_Button);

        // northPanel
        JTextArea messageBoard = new JTextArea();
        messageBoard.setColumns(40);
        messageBoard.setRows(1);

        // messageBoard.setText("Game on!");

        for (int i = 0; i < pits.size(); i++) {
            pits.get(i).setBackground(new Color(188, 107, 0));
            pits.get(i).setPreferredSize(new Dimension(100, 120));
            pits.get(i).setBorderPainted(false);

            // Add a listener to pits and update model if button is pressed
            pits.get(i).addMouseListener(new Pit(i) {
                public void mousePressed(MouseEvent e) {
                    int mouseID = this.getMouseListenerID();
                    // prevents player A from going on player B's turn
                    if (currentTurn == B_TURN && mouseID <= LAST_PIT_OF_A) {
                        messageBoard.setText("PLAYER B 's TURN ");
                    }
                    // prevents player B from going on player A's turn
                    else if (currentTurn == A_TURN && mouseID <= LAST_PIT_OF_B && mouseID >= FIRST_PIT_OF_B) {
                        messageBoard.setText("PLAYER A 's TURN ");
                    } else if (mouseID >= 6 && theModel.getAmountInPit(mouseID + 1) == 0) {
                        messageBoard.setText(" NO MORE STONE !");
                    } else if (mouseID < 6 && theModel.getAmountInPit(mouseID) == 0) {
                        messageBoard.setText(" NO MORE STONE !");
                    } else if (currentTurn == A_TURN && mouseID <= LAST_PIT_OF_A) {
                        theModel.move(mouseID);
                        if (theModel.isLastStoneOnBoard()) {
                            currentTurn = A_TURN;
                            messageBoard.setText(" GO AGAIN !");
                            mancalaHasReached = true;
                        } else {
                            currentTurn = B_TURN;
                            mancalaHasReached = false;
                        }
                        undoMoveB = MAX_NUM_OF_UNDOS;
                    } else if (currentTurn == B_TURN && mouseID <= LAST_PIT_OF_B && mouseID >= FIRST_PIT_OF_B) {
                        theModel.move(mouseID);
                        if (theModel.isLastStoneOnBoard()) {
                            currentTurn = B_TURN;
                            messageBoard.setText(" GO AGAIN !");
                            mancalaHasReached = true;
                        } else {
                            currentTurn = A_TURN;
                            mancalaHasReached = false;
                        }
                        undoMoveA = MAX_NUM_OF_UNDOS;
                    }

                    // Check if the game is over
                    int gameOverFlag = theModel.checkIfGameOver();
                    if (gameOverFlag > 0) {
                        int winner = theModel.checkWinner(gameOverFlag);
                        if (winner == 1)
                            messageBoard.setText(" GAME OVER - PLAYER A IS THE WINNER !");
                        else if (winner == 2)
                            messageBoard.setText(" GAME OVER - PLAYER B IS THE WINNER !");
                        else if (winner == 3)
                            messageBoard.setText(" GAME OVER - NO WINNER !");
                    }
                }
            });
        }

        int[] mancalaData = theModel.getCurrentBoard();

        // Center
        JLabel center = new JLabel(boardDesign.createBoard());
        boardDesign.addStonesToPits(pits, mancalaData);
        boardDesign.addPitsToBoard(pits, center);
        add(center, BorderLayout.CENTER);

        // North
        JPanel northPanel = new JPanel();
        northPanel.add(messageBoard);
        add(northPanel, BorderLayout.NORTH);

        // South
        JPanel southPanel = new JPanel();
        JTextField undoCountText = new JTextField(100);
        undoCountText.setText(" UNDO # ");
        JButton undoButton = new JButton(" UNDO # ");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Arrays.equals(theModel.getCurrentBoard(), theModel.getPreviousBoard())) {
                    undoCountText.setText(" MOVE TO USE UNDO ");
                } else if (currentTurn == B_TURN && undoMoveA <= 0) {
                    undoCountText.setText(" NO MORE UNDO !");
                } else if (currentTurn == A_TURN && undoMoveB <= 0) {
                    undoCountText.setText(" NO MORE UNDO !");
                } else if (mancalaHasReached && currentTurn == B_TURN) {
                    undoMoveB--;
                    undoCountText.setText("PLAYER B: " + undoMoveB);
                    theModel.undoMove();
                    if (Arrays.equals(theModel.getCurrentBoard(), theModel.getPreviousBoard())) {
                        currentTurn = B_TURN;// still B's turn since B got a free turn for reaching own Mancala
                    }
                    mancalaHasReached = false;
                } else if (mancalaHasReached && currentTurn == A_TURN) {
                    undoMoveA--;
                    undoCountText.setText("PLAYER A: " + undoMoveA);
                    theModel.undoMove();
                    if (Arrays.equals(theModel.getCurrentBoard(), theModel.getPreviousBoard())) {
                        currentTurn = A_TURN;// still A's turn since A got a free turn for reaching own Mancala
                    }
                    mancalaHasReached = false;
                } else if (currentTurn == A_TURN) { // was B's turn and A did not reach Mancala, then B undo so turn
                                                    // goes back to B
                    undoMoveB--;
                    undoCountText.setText("#UNDO OF PLAYER B: " + undoMoveB);
                    theModel.undoMove();
                    currentTurn = B_TURN;
                } else if (currentTurn == B_TURN) { // was A's turn and A did not reach Mancala, then A undo so turn
                                                    // goes back to A
                    undoMoveA--;
                    undoCountText.setText("#UNDO OF PLAYER A: " + undoMoveA);
                    theModel.undoMove();
                    currentTurn = A_TURN;
                }
            }
        });
        southPanel.add(undoCountText);
        southPanel.add(undoButton);
        add(southPanel, BorderLayout.SOUTH);

        // setResizable(false);
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
        int[] mancalaData = theModel.getCurrentBoard();
        boardDesign.addStonesToPits(pits, mancalaData);
        repaint();
    }
}