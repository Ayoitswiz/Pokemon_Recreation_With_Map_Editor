package AdventureMode;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static AdventureMode.AdventureModeUiPanel.EDIT_MODE;
import static AdventureMode.AdventureModeUiPanel.OPEN_OUTPUT_STREAMS;

abstract class Collision extends JComponent implements Collisions {
    protected int i;
    protected Color color;
    protected int moveUserToCell;
    private FileOutputStream out;

    public void setOut(FileOutputStream out) {
        this.out = out;
    }

    Collision() {
        setPreferredSize(new Dimension(16,16));
    }


    protected void openOutputStream(int i, ArrayList<Integer> CELLS_THAT_CANNOT_BE_ENTERED, String areaToLoad) {
        setPreferredSize(new Dimension(16,16));
        if (EDIT_MODE) {
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = 1;
            c.weightx = 1;
            c.weighty = 1;
            add(new AddCantEnter(i, CELLS_THAT_CANNOT_BE_ENTERED, areaToLoad), c);
        }
    }

    @Override
    public boolean collides(Rectangle r) {
         return getBounds().intersects(r);
    }

    protected boolean isGrassBlock() {
        return false;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!(this instanceof AdventureModeUiPanel.CanEnter)) {
            g.setColor(color);
            Graphics2D g2 = (Graphics2D) g;
            int stroke = 2;
            g2.setStroke(new BasicStroke(stroke));
            g2.drawLine(0, 0, getWidth(), getHeight());
            g2.drawLine(0, getHeight(), getWidth(), 0);
            g2.drawRect(1, 1, getWidth() - stroke, getHeight() - stroke);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, (float) ((getWidth() + getHeight()) / 4)));
            if (!OPEN_OUTPUT_STREAMS) {
                g2.setColor(Color.BLACK);
                g2.drawString(Integer.toString(i), 0, getHeight());
            }
        }
    }

    public int getMaxY() {
        return getY() + getHeight();
    }

    public int getMaxX() {
        return getX() + getWidth();
    }


    public boolean isWarp() {
        return false;
    }










    abstract boolean moveInto();
}
