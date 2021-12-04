
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
import java.util.*;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BoardModel {
    private boolean mancalaMaker;
    private int[] preBoard;
    private int[] currentGameBoard;
    private ArrayList<ChangeListener> change_listener;

    /**
     * [Apit1,Apit2,Apit3,Apit4,Apit5,Apit6,mancalaA,Bpit1,Bpit2,Bpit3,Bpit4,Bpit5,Bpit6,mancalaB]
     */
    private static final int mancalaA = 6;
    private static final int mancalaB = 13;
    private static final int numPits = 14;

    public BoardModel() {
        currentGameBoard = new int[numPits];
        preBoard = currentGameBoard.clone();
        mancalaMaker = false;
        change_listener = new ArrayList<>();
    }

    /**
     * @param numStones This is the stones parameter
     */
    public void initializeTheBoard(int numStones) {
        for (int i = 0; i < currentGameBoard.length; i++) {
            if (i == mancalaA || i == mancalaB) {
                currentGameBoard[i] = 0;
            } else {
                currentGameBoard[i] = numStones;
            }
        }
        preBoard = currentGameBoard.clone();
    }

    /**
     * @param numStones This is the stones parameter
     */
    public BoardModel(int numStones) {
        currentGameBoard = new int[] {
                numStones, numStones, numStones, numStones,
                numStones, numStones, 0, numStones, numStones,
                numStones, numStones, numStones, numStones, 0
        };
        preBoard = currentGameBoard.clone();
        mancalaMaker = false;
        change_listener = new ArrayList<>();
    }

    /**
     * @return This will return the current board
     */
    public int[] getCurrentGameBoard() {
        return currentGameBoard.clone();
    }

    /**
     * @return This will return the prev board
     */
    public int[] getpreBoard() {
        return preBoard.clone();
    }

    /**
     * @param pn This is the pit number parameter
     * @return This will return stones in pit number
     */
    public int getAmountInPit(int pn) {
        return currentGameBoard[pn];
    }

    /**
     * @return This will return true if there is a last stone otherwise false
     * 
     */
    public boolean isMancalaMaker() {
        return mancalaMaker;
    }

    /**
     * 
     * @param pit_flag This is the flag parameter
     * @return This will return either player A or B is the winner
     */
    public int checkWinner(int pit_flag) {
        /**
         * If A's Pits are empty
         * -move stones in BPits to B's mancala pit.
         * If B's Pits are empty
         * -move stones in APits to A's mancala pit.
         */
        if (pit_flag == 1) {
            moveStonesToMancala(7, mancalaB);

        } else if (pit_flag == 2) {
            moveStonesToMancala(0, mancalaA);
        }

        /**
         * Compare A's Mancala Pit to B's
         */
        if (currentGameBoard[mancalaA] > currentGameBoard[mancalaB])
            return 1;
        else if (currentGameBoard[mancalaA] < currentGameBoard[mancalaB])
            return 2;
        else
            return 3;
    }

    /**
     * @return This will return a number whether player A or B pit is empty
     */
    public int checkIfGameOver() {
        boolean mancalaA_Empty = false, mancalaB_Empty = false;
        for (int pitA = 0; pitA < mancalaA; pitA++) {
            if (currentGameBoard[pitA] != 0) {
                mancalaA_Empty = false;
                break;
            } else
                mancalaA_Empty = true;
        }
        for (int pitB = 7; pitB < mancalaB; pitB++) {
            if (currentGameBoard[pitB] != 0) {
                mancalaB_Empty = false;
                break;
            } else
                mancalaB_Empty = true;
        }

        /**
         * Returns which side is empty, if a side is empty
         */
        if (mancalaA_Empty) {
            return 1;
        } else if (mancalaB_Empty) {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * @param pitposition This is the pit position parameter
     * @param manposition This is the mancala position parameter
     */
    public void moveStonesToMancala(int pitposition, int manposition) {
        for (int i = pitposition; i < manposition; i++) {
            currentGameBoard[manposition] += currentGameBoard[i];
            currentGameBoard[i] = 0;
        }
        for (ChangeListener l : change_listener) {
            l.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * @param num This is the pit number parameter
     */
    public void move(int num) {

        boolean player_A_turn = true;
        int pit_own = num;
        if (num > 5) {
            player_A_turn = false;
            num = num + 1;
            pit_own = num - 7;
        }
        if (currentGameBoard[num] != 0) {
            preBoard = currentGameBoard.clone();
            mancalaMaker = false;
            int count_stone = currentGameBoard[num];
            int pit_number = num;
            int pit_end = num + count_stone;
            /**
             * Opponent mancala will always be 13 away from pit_own
             */
            boolean opponMancReached = false;
            if (pit_own + count_stone >= 13) {
                opponMancReached = true;
            }
            /**
             * If stones end in your own mancala, set mancalaMaker true for an extra turn
             */
            if (pit_own + count_stone == 6) {
                mancalaMaker = true;
                System.out.println("Congratulation! Extra turn");
            }

            /**
             * Distributes stones from selected pit
             * after reaching opponents last pit, start from beggining
             * adds a stone to each pit
             */
            for (int i = 1; i <= count_stone; i++) {
                if ((num + i) == 14) {
                    num = -1 * i; //
                }
                currentGameBoard[num + i] = currentGameBoard[num + i] + 1;
            }
            /**
             * Removes stones from selected pit
             */
            currentGameBoard[pit_number] = 0;

            /*
             * Player A's turn and ending in A's pits
             */
            if (player_A_turn && pit_end <= 5) {
                if (preBoard[pit_end] == 0 && !opponMancReached) {
                    currentGameBoard[pit_end] = 0;
                    int opponStones = currentGameBoard[pit_end + (2 * (6 - pit_end))];
                    currentGameBoard[mancalaA] = currentGameBoard[mancalaA] + opponStones + 1;
                    currentGameBoard[pit_end + (2 * (6 - pit_end))] = 0;
                }
            }
            /*
             * This time, B's last stone dropped lands in an empty pit on B's side, the same
             * rule applies here.
             */
            if (!player_A_turn && pit_end > 6) {
                if (pit_end < 13 && preBoard[pit_end] == 0 && !opponMancReached) {
                    currentGameBoard[pit_end] = 0;
                    int opponStones = currentGameBoard[pit_end - (2 * (pit_end - 6))];
                    currentGameBoard[mancalaB] = currentGameBoard[mancalaB] + opponStones + 1;
                    currentGameBoard[pit_end - (2 * (pit_end - 6))] = 0;
                }
            }

            /**
             * When A passes through opponents side and reaches A's side with the last
             * stone, will require at least 8 stones in the starting pit.
             */
            if (player_A_turn && opponMancReached) {
                currentGameBoard[13] = preBoard[13];
                int nextPitToGetStone = pit_own + count_stone + 1;
                if (nextPitToGetStone > 13) {
                    nextPitToGetStone = nextPitToGetStone - 14;
                }
                currentGameBoard[nextPitToGetStone] = currentGameBoard[nextPitToGetStone] + 1;
                /**
                 * When A passes through opponent's side and also lands in an empty pit
                 */
                if (preBoard[nextPitToGetStone] == 0) {
                    currentGameBoard[nextPitToGetStone] = 0;
                    int opponStones = currentGameBoard[nextPitToGetStone + (2 * (6 - nextPitToGetStone))];
                    currentGameBoard[mancalaA] = currentGameBoard[mancalaA] + opponStones + 1;
                    currentGameBoard[nextPitToGetStone + (2 * (6 - nextPitToGetStone))] = 0;
                }
            }
            /**
             * When B passes through opponents side and reaches B's side with the last stone
             */
            if (!player_A_turn && opponMancReached) {
                currentGameBoard[6] = preBoard[6];
                int nextPitToGetStone = pit_own + count_stone + 1;
                if (nextPitToGetStone > 13) {
                    nextPitToGetStone = nextPitToGetStone - 14;
                }
                currentGameBoard[nextPitToGetStone + 7] = currentGameBoard[nextPitToGetStone + 7] + 1;
                /**
                 * B passes through the opponent's side and also lands in an empty pit
                 */
                if (preBoard[nextPitToGetStone + 7] == 0) {
                    currentGameBoard[nextPitToGetStone + 7] = 0;
                    int opponStones = currentGameBoard[nextPitToGetStone + 7 + (2 * (6 - (nextPitToGetStone + 7)))];
                    currentGameBoard[mancalaB] = currentGameBoard[mancalaB] + opponStones + 1;
                    currentGameBoard[nextPitToGetStone + 7 + (2 * (6 - (nextPitToGetStone + 7)))] = 0;
                }
            }
            for (ChangeListener l : change_listener) {
                l.stateChanged(new ChangeEvent(this));
            }
        }
    }

    /**
     * Change to board to the past
     */
    public void undo_click() {
        currentGameBoard = preBoard.clone();
        for (ChangeListener l : change_listener) {
            l.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * Add 1 listener to the array
     * 
     * @param listener This is the listerner parameter
     */
    public void attach(ChangeListener listener) {
        change_listener.add(listener);
    }
}