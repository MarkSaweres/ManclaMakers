import java.util.*;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class serves as the model of the MVC. This model contains the data for
 * the stones
 * in a particular pit at the moment, which is held in 2 arrays. One array
 * contains the current state of the board while the other contains the previous
 * state of the board to allow a user a certain amount of undo moves per turn.
 * An ArrayList is used to hold the ChangeListeners that perform an
 * action once the model notifies them of changes. This class also contains
 * accessors and mutator methods for the data and states of the game.
 */

public class BoardModel {

    private int[] previousBoard;
    private int[] currentBoard;
    private boolean mancalaMaker;
    private ArrayList<ChangeListener> arrayOfListeners; // will only contain View Listener
    private int numberOfUndos;
    private static final int numPits = 14;

    // Position of Mancala A and B in the Array
    //[Apit1,Apit2,Apit3,Apit4,Apit5,Apit6,mancalaA,Bpit1,Bpit2,Bpit3,Bpit4,Bpit5,Bpit6,mancalaB]
    private static final int mancalaA = 6;
    private static final int mancalaB = 13;

    /**
     * Constructs an empty MancalaBoardModel.
     */
    public BoardModel() {
        currentBoard = new int[numPits];
        previousBoard = currentBoard.clone();
        mancalaMaker = false;
        arrayOfListeners = new ArrayList<>();
        numberOfUndos = 0;
    }

    /**
     * Sets the number of stones in each pit in the mancala board, excluding the
     * mancalas.
     * 
     * @param numStones the number of stones in each pit
     */
    public void initializeTheBoard(int numStones) {

        for (int i = 0; i < currentBoard.length; i++) {
            if (i == mancalaA || i == mancalaB) {
                currentBoard[i] = 0;
            } else {
                currentBoard[i] = numStones;
            }
        }
        previousBoard = currentBoard.clone();
    }

    /**
     * Constructs a Mancala Board with specified number of stones in each pit, and 0
     * stones in Mancalas
     * 
     * @param numStones the number of stones each pit initially contains
     */
    public BoardModel(int numStones) {
        /*
         * index 0 - 6: player A, index 7 - 13: B
         * index 6 and 13 correspond to Mancalas
         * Layout of the Board:
         * A1 - A6 - Mancala A
         * B1 - B6 - Mancala B
         */
        currentBoard = new int[] {
                numStones, numStones, numStones, numStones,
                numStones, numStones, 0, numStones, numStones,
                numStones, numStones, numStones, numStones, 0
        };
        previousBoard = currentBoard.clone();
        mancalaMaker = false;
        arrayOfListeners = new ArrayList<>();
        numberOfUndos = 0;
    }

    /**
     * Gets the current board.
     * 
     * @return the current board
     */
    public int[] getCurrentBoard() {
        return currentBoard.clone();
    }

    /**
     * Gets the previous board.
     * 
     * @return the previous board
     */
    public int[] getPreviousBoard() {
        return previousBoard.clone();
    }

    /**
     * Gets the number of stones in specified pit number.
     * 
     * @param pitNumber the pit number
     * @return the number of stones in the specified pit number
     */
    public int getAmountInPit(int pitNumber) {
        return currentBoard[pitNumber];
    }

    /**
     * Checks if the last stone was dropped in a Mancala.
     * 
     * @return the boolean value of lastStoneInMancala; true allows player another
     *         turn
     */
    public boolean isMancalaMaker() // we will use this in Control to prompt player to go again
    {
        return mancalaMaker;
    }

    /**
     * Determines the winner of the game by comparing the number of stones collected
     * in each players Mancala after
     * all the remaining stones in the board are properly distributed.
     * The winner has the greater number of stones.
     * 
     * @param emptyPitFlag value passed in that dictates the player that receives
     *                     the remaining stones on the board
     * @return 1 if winner is A or
     *         2 if the winner is B or
     *         3 if there is a tie
     */
    public int checkWinner(int emptyPitFlag) {

        if (emptyPitFlag == 1) { // If A's Pits are empty
            moveStonesToMancala(7, mancalaB); // move stones in BPits to B's mancala pit

        } else if (emptyPitFlag == 2) { // If B's Pits are empty
            moveStonesToMancala(0, mancalaA); // move stones in APits to A's mancala pit
        }

        // Compare A's Mancala Pit to B's
        if (currentBoard[mancalaA] > currentBoard[mancalaB])
            return 1;
        else if (currentBoard[mancalaA] < currentBoard[mancalaB])
            return 2;
        else
            return 3;
    }

    /**
     * Checks if the game is over when pits belonging to either player are all
     * empty.
     * 
     * @return 0 if game is not over
     *         1 if game is over and only A pits are empty
     *         2 if game is over and only B pits are empty
     */
    public int checkIfGameOver() {

        boolean mancalaA_Empty = false, mancalaB_Empty = false;

        // Check if all A's pits are empty
        for (int pitA = 0; pitA < mancalaA; pitA++) {
            if (currentBoard[pitA] != 0) {
                mancalaA_Empty = false;
                break;
            } else
                mancalaA_Empty = true;
        }

        // Check if all B's pits are empty
        for (int pitB = 7; pitB < mancalaB; pitB++) {
            if (currentBoard[pitB] != 0) {
                mancalaB_Empty = false;
                break;
            } else
                mancalaB_Empty = true;
        }
        
        //returns which side is empty, if a side is empty
        if(mancalaA_Empty){
            return 1;
        }
        else if(mancalaB_Empty){
            return 2;
        }
        else{
            return 0;
        }

    }

    /**
     * Sums up the remaining stones on the board and adds that to the mancala
     * specified by the mancalaPos,
     * then notifies the listeners of the change in the model's state.
     * 
     * @param pitPos     the starting position of the summation.
     * @param mancalaPos the position of the mancala that will receive the rest of
     *                   the stones.
     */
    public void moveStonesToMancala(int pitPos, int mancalaPos) {
        for (int i = pitPos; i < mancalaPos; i++) {
            currentBoard[mancalaPos] += currentBoard[i];
            currentBoard[i] = 0;
        }
        for (ChangeListener l : arrayOfListeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * Given the pit number and the number of stones currently in the pit, the
     * function redistributes the stones across
     * the board according to the rules of the mancala game.
     * First exception is when a player has their last stone dropped in their own
     * Mancala, resulting in a free turn.
     * Second exception is when a player's last stone dropped is in an empty pit on
     * their side of the board.
     * This results in the player getting to add the stones in the last pit and the
     * stones in the pit across from that on
     * the opponent's side into their own Mancala.
     * 
     * @param pitNumber the pit number chosen by the user
     */
    public void move(int pitNumber) { 

        boolean turnA = true;
        int ownPitNumber = pitNumber;
        if (pitNumber > 5) {
            turnA = false;
            pitNumber = pitNumber + 1;
            ownPitNumber = pitNumber - 7;
        }
        if (currentBoard[pitNumber] != 0) {
            previousBoard = currentBoard.clone(); // save current board to previous board to allow for undo's
            mancalaMaker = false;
            int stoneCount = currentBoard[pitNumber]; //saves number of stones in selected pit
            int oPitNumber = pitNumber; //saves origional pit number
            int endingPit = pitNumber + stoneCount; //calculates ending pit number

            //Opponent mancala will always be 13 away from ownPitNumber
            boolean opponMancReached = false;
            if (ownPitNumber + stoneCount >= 13) {
                opponMancReached = true;
            }

            //if stones end in your own mancala, set mancalaMaker true for an extra turn
            if (ownPitNumber + stoneCount == 6) {
                mancalaMaker = true;
                // System.out.println("You get another turn!");
            }

            // distributes stones from selected pit
            for (int i = 1; i <= stoneCount; i++) {
                if ((pitNumber + i) == 14) {
                    pitNumber = -1 * i; //after reaching opponents last pit, start from beggining
                }
                currentBoard[pitNumber + i] = currentBoard[pitNumber + i] + 1; //adds a stone to each pit
            }
            // removes stones from selected pit
            currentBoard[oPitNumber] = 0;

            /*
             * A's last stone dropped lands in an empty pit on A's side, so A gets to add
             * the stones in that last pit and the stones
             * on the opponent's side that is across from that last pit, into A's Mancala.
             */
            if (turnA && endingPit <= 5){  //Player A's turn and ending in A's pits
                if( previousBoard[endingPit] == 0 && !opponMancReached) {
                    currentBoard[endingPit] = 0;
                    int opponStones = currentBoard[endingPit + (2 * (6 - endingPit))];
                    currentBoard[mancalaA] = currentBoard[mancalaA] + opponStones + 1;
                    currentBoard[endingPit + (2 * (6 - endingPit))] = 0;
                 }
             }   
            /*
             * This time, B's last stone dropped lands in an empty pit on B's side, the same
             * rule applies here.
             */
            if (!turnA && endingPit > 6){
                if(endingPit < 13 && previousBoard[endingPit] == 0 && !opponMancReached) {
                    currentBoard[endingPit] = 0;
                    int opponStones = currentBoard[endingPit - (2 * (endingPit - 6))];
                    currentBoard[mancalaB] = currentBoard[mancalaB] + opponStones + 1;
                    currentBoard[endingPit - (2 * (endingPit - 6))] = 0;
                }
            }

            // When A passes through opponents side and reaches A's side with the last
            // stone, will require at least 8 stones in the starting pit.
            if (turnA && opponMancReached) {
                currentBoard[13] = previousBoard[13];
                int nextPitToGetStone = ownPitNumber + stoneCount + 1;
                if (nextPitToGetStone > 13) {
                    nextPitToGetStone = nextPitToGetStone - 14;
                }
                currentBoard[nextPitToGetStone] = currentBoard[nextPitToGetStone] + 1;
                // When A passes through opponent's side and also lands in an empty pit
                if (previousBoard[nextPitToGetStone] == 0) {
                    currentBoard[nextPitToGetStone] = 0;
                    int opponStones = currentBoard[nextPitToGetStone + (2 * (6 - nextPitToGetStone))];
                    currentBoard[mancalaA] = currentBoard[mancalaA] + opponStones + 1;
                    currentBoard[nextPitToGetStone + (2 * (6 - nextPitToGetStone))] = 0;
                }
            }
            // When B passes through opponents side and reaches B's side with the last stone
            if (!turnA && opponMancReached) {
                currentBoard[6] = previousBoard[6];
                int nextPitToGetStone = ownPitNumber + stoneCount + 1;
                if (nextPitToGetStone > 13) {
                    nextPitToGetStone = nextPitToGetStone - 14;
                }
                currentBoard[nextPitToGetStone + 7] = currentBoard[nextPitToGetStone + 7] + 1;
                // B passes through the opponent's side and also lands in an empty pit
                if (previousBoard[nextPitToGetStone + 7] == 0) {
                    currentBoard[nextPitToGetStone + 7] = 0;
                    int opponStones = currentBoard[nextPitToGetStone + 7 + (2 * (6 - (nextPitToGetStone + 7)))];
                    currentBoard[mancalaB] = currentBoard[mancalaB] + opponStones + 1;
                    currentBoard[nextPitToGetStone + 7 + (2 * (6 - (nextPitToGetStone + 7)))] = 0;
                }
            }

            // to alert listeners of change
            for (ChangeListener l : arrayOfListeners) {
                l.stateChanged(new ChangeEvent(this));
            }
        }
    }

    /**
     * Makes the current board equivalent to the previous board.
     */
    public void undoMove() {
        currentBoard = previousBoard.clone();
        // to alert listeners of change
        for (ChangeListener l : arrayOfListeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }

    /*
     * Adds a listener to ArrayList of listeners in the model.
     */
    public void attach(ChangeListener l) {
        arrayOfListeners.add(l);
    }
}