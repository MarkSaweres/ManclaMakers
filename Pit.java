
/**
 * This Pit class is use MouseAdapter to
 * identify each pit on the board to keep track of each pit when mouse is pressed
 * 
 */

import java.util.*;
import java.awt.event.MouseAdapter;

public class Pit extends MouseAdapter {
    private int identify;

    /**
     * @param identify This is the identify parameter
     */
    public Pit(int identify) {
        super();
        this.identify = identify;
    }

    /**
     * @return This will return the identify
     */
    public int getMouseListenerID() {
        return identify;
    }
}
