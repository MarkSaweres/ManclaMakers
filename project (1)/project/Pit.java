import java.util.*;
import java.awt.event.MouseAdapter;

public class Pit extends MouseAdapter {
    private int mouseID; // ID of mouse corresponding to a certain button/pit

    /**
     * Constructs a PitMouseListener with a particular identifier.
     * 
     * @param mouseID is the identifier of a particular pit on the board.
     */
    public Pit(int mouseID) {
        super();
        this.mouseID = mouseID;
    }

    /**
     * Accessor to the mouseID
     * 
     * @return the integer representing the mouseID
     */
    public int getMouseListenerID() {
        return mouseID;
    }

    /**
     * toString method used for testing for bugs in the game.
     */
    public String toString() {
        return "PitMouseListener [mouseID=" + mouseID + "]";
    }

}
