import java.util.*;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;

public interface BoardDesign {
    Icon boardCreater();

    void addStone(ArrayList<JButton> pits, int[] pitsData);

    void addPit(ArrayList<JButton> pits, JLabel label);
}
