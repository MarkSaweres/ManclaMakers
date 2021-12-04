import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.Icon;
import java.awt.Component;
import java.awt.geom.Ellipse2D;
public class Stone implements Icon {

    private static final int MAX_STONE_PER_ROW = 5;
    private static final int DIAMETER = 15;
    private int numberOfStones;
    private Color colorOfStone;
    private int widthOfStone;
    private int heightOfStone;

    public Stone(int count) {
        numberOfStones = count;
        colorOfStone = Color.BLACK;
        widthOfStone = MAX_STONE_PER_ROW * DIAMETER;
        heightOfStone = MAX_STONE_PER_ROW * DIAMETER;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 1; i <= numberOfStones; i++) {
            if (i == 1)
                x = DIAMETER;
            else if ((i - 1) % MAX_STONE_PER_ROW == 0) {
                x = DIAMETER;
                y += (DIAMETER + 1);
            } else
                x += (DIAMETER + 1);
            Ellipse2D.Double stone = new Ellipse2D.Double(x, y, DIAMETER, DIAMETER);
            g2.setColor(colorOfStone);
            g2.fill(stone);
        }
    }

    /**
     * Get width of the stones.
     * 
     * @return width
     */
    @Override
    public int getIconWidth() {
        return widthOfStone;
    }

    /**
     * Get height of the stones.
     * 
     * @return height
     */
    @Override
    public int getIconHeight() {
        return heightOfStone;
    }

    /**
     * Sets the number of stones in each pit.
     * 
     * @param numberOfStones number of stones per pit
     */
    public void setNumberOfStones(int numberOfStones) {
        this.numberOfStones = numberOfStones;
    }

    /**
     * Sets the color for the stones.
     * 
     * @param theColor color of the stones
     */
    public void setColorOfStone(Color theColor) {
        colorOfStone = theColor;
    }

    /**
     * Sets the width of the stone.
     * 
     * @param width of stone
     */
    public void setIconWidth(int width) {
        this.widthOfStone = width;
    }

    /**
     * sets the height of the stone.
     * 
     * @param height of stone
     */
    public void setIconHeight(int height) {
        this.heightOfStone = height;
    }
}