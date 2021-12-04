
/**
 * This  class will create the board by JButton, return board icon, 
 * Make a board, paint the board, add stone to the pit
 */

import java.util.*;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;
import javax.swing.JButton;
import java.util.ArrayList;

public class Board implements BoardDesign {
    JButton playerA = new JButton();
    JButton playerB = new JButton();

    @Override
    public Icon boardCreater() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255));
                Rectangle2D.Double rectangle = new Rectangle2D.Double(0, 0, getIconWidth(), getIconHeight());
                g2.fill(rectangle);
            }

            @Override
            public int getIconWidth() {
                return 1500;
            }

            @Override
            public int getIconHeight() {
                return 550;
            }
        };
    }

    /**
     * This method is to add stones to the pit
     * 
     * @param pits This is the pits parameter
     * @param pitsData This is the number of stones parameter
     */
    @Override
    public void addStone(ArrayList<JButton> pits, int[] pitsData) {
        for (int i = 0; i < pitsData.length; i++) {
            Stone stones = new Stone(pitsData[i]);
            stones.setIconHeight(100);
            stones.setIconWidth(90);
            if (i == 6)
                playerA.setIcon(stones);
            else if (i == 13)
                playerB.setIcon(stones);
            else if (i > 6)
                pits.get(i - 1).setIcon(stones);
            else
                pits.get(i).setIcon(stones);
        }
    }

    /**
     * Add all the pits and labels to the board
     * 
     * @param pits  - list of all JButtons of pits
     * @param label - main Mancala Board
     */
    @Override
    public void addPit(ArrayList<JButton> pits, JLabel label) {

        // Create A and B Mancala pits as Buttons
        Color dark = new Color(128, 70, 0);
        playerA.setBackground(dark);
        playerA.setPreferredSize(new Dimension(100, 500));
        playerB.setBackground(dark);
        playerB.setPreferredSize(new Dimension(100, 500));

        // Create Mancala A and B
        JButton mancalaA = new JButton("Player A's" + "\n" + " Mancala");
        mancalaA.setFont(new Font("Courier", Font.BOLD, 20));
        mancalaA.setBackground(new Color(255, 255, 255));

        JButton mancalaB = new JButton("Player B's" + "\n" + " Mancala");
        mancalaB.setFont(new Font("Courier", Font.BOLD, 20));
        mancalaB.setBackground(new Color(255, 255, 255));

        // Set layout for the board and add Mancala A and Mancala B
        label.setLayout(new FlowLayout());
        label.add(mancalaB);
        label.add(playerB);

        // Panel to hold A and B pits + labels
        JPanel mainPanel = new JPanel();
        // mainPanel.setOpaque(false);
        mainPanel.setLayout(new GridLayout(4, 6, 10, 10));

        // Add pit labels for B to JPanel
        for (int i = 6; i > 0; i--) {
            JButton pitLabels = new JButton("B" + i);
            pitLabels.setFont(new Font("Courier", Font.BOLD, 20));
            pitLabels.setBackground(new Color(226, 236, 0));
            ;
            pitLabels.setOpaque(true);
            pitLabels.setBorderPainted(false);
            mainPanel.add(pitLabels);
        }

        // Add pits for B as a Button
        for (int i = pits.size() - 1; i >= 6; i--) {
            mainPanel.add(pits.get(i));
        }

        // Add pits for A as a Button
        for (int i = 0; i < 6; i++) {
            mainPanel.add(pits.get(i));
        }

        // Add pit labels for A to JPanel
        for (int i = 1; i <= 6; i++) {
            JButton pitLabels = new JButton("A" + i);
            pitLabels.setFont(new Font("Courier", Font.BOLD, 20));
            pitLabels.setBackground(new Color(25, 229, 5));
            pitLabels.setOpaque(true);
            pitLabels.setBorderPainted(false);
            mainPanel.add(pitLabels);
        }
        label.add(mainPanel);
        label.add(playerA);
        label.add(mancalaA);
    }
}