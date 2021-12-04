
/**
 * This Stone class will create stones,
 * set the number stone per row, set stone color,
 * set width and height of the stone in the pit, set the number of stone in each pit.
 */
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.Icon;
import java.awt.Component;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Stone implements Icon {

    private static final int STONE_PER_ROW = 5;
    private static final int DIAMETER_OF_PIT = 15;
    private int stone_height;
    private int stone_width;
    private Color stone_color;
    private int stone_total;
    private Image img = null;
    public Stone(int c) {
        stone_total = c;
        stone_color = Color.BLACK;
        stone_width = STONE_PER_ROW * DIAMETER_OF_PIT;
        stone_height = STONE_PER_ROW * DIAMETER_OF_PIT;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        try{
            img = ImageIO.read(new File("./stone.png"));
        } catch (IOException e) {
        }

        for (int i = 1; i <= stone_total; i++) {
            if (i == 1)
                x = DIAMETER_OF_PIT;
            else if ((i - 1) % STONE_PER_ROW == 0) {
                x = DIAMETER_OF_PIT;
                y += (DIAMETER_OF_PIT + 1);
            } else
                x += (DIAMETER_OF_PIT + 1);
            g2.drawImage(img, x, y, null);
        }
    }

    /**
     * @return width This will return the width of the stone
     */
    @Override
    public int getIconWidth() {
        return stone_width;
    }

    /**
     * @return height This will return the height of the stone
     */
    @Override
    public int getIconHeight() {
        return stone_height;
    }

    /**
     * @param stone_total This is the number of stones parameter
     */
    public void setstone_total(int stone_total) {
        this.stone_total = stone_total;
    }

    /**
     * @param theColor This is the color of the stones parameter
     */
    public void setColorOfStone(Color theColor) {
        stone_color = theColor;
    }

    /**
     * @param width This is the of stone width parameter
     */
    public void setIconWidth(int width) {
        this.stone_width = width;
    }

    /**
     * @param height This is the height of stone parameter
     */
    public void setIconHeight(int height) {
        this.stone_height = height;
    }
}