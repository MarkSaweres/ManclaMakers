import java.util.*;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;

public interface BoardDesign {
    Icon createBoard();

    void addStonesToPits(ArrayList<JButton> pits, int[] pitsData);

    void addPitsToBoard(ArrayList<JButton> pits, JLabel label);
}
