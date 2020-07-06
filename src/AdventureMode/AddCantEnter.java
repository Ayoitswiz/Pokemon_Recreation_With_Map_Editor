package AdventureMode;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;


public class AddCantEnter extends JButton {
    int i;
    ArrayList<Integer> CELLS_THAT_CANNOT_BE_ENTERED;
    public AddCantEnter(int count, ArrayList<Integer> CELLS_THAT_CANNOT_BE_ENTERED, String areaToLoad) {
        //would need grassCellsToLoad, CELLS_That_Are_Grass in here
        //Then I would need the same thing for cut cells, surf cells etc. and item cells, berry cells
        //Or reload area and pass in new values
        i = count;
        this.CELLS_THAT_CANNOT_BE_ENTERED = CELLS_THAT_CANNOT_BE_ENTERED;
        setPreferredSize(new Dimension(16,16));
        setContentAreaFilled(false);
        setBorder(BorderFactory.createEmptyBorder());
        addActionListener(e -> {
            if (CELLS_THAT_CANNOT_BE_ENTERED.contains(i)) {

                File inputFile = new File(areaToLoad);
                File tempFile = new File("PewterCityTemp.txt");

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                    String lineToRemove = Integer.toString(i);
                    String currentLine;

                    while ((currentLine = reader.readLine()) != null) {
                        // trim newline when comparing with lineToRemove
                        String trimmedLine = currentLine.trim();
                        if (trimmedLine.equals(lineToRemove)) continue;
                        writer.write(currentLine + System.getProperty("line.separator"));
                    }
                    writer.close();
                    reader.close();
                    String name = inputFile.getName();
                    boolean deleted = inputFile.delete();
                    boolean successful = tempFile.renameTo(inputFile);
                    if (successful) {
                        CELLS_THAT_CANNOT_BE_ENTERED.remove(Integer.valueOf(i));
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    File inputFile = new File(areaToLoad);
                    FileWriter fw = new FileWriter(inputFile.getAbsoluteFile(), true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write((i + System.getProperty("line.separator")));
                    CELLS_THAT_CANNOT_BE_ENTERED.add(i);
                    bw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (CELLS_THAT_CANNOT_BE_ENTERED.contains(i)) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }
        g.setFont(g.getFont().deriveFont((float) ((getWidth() + getHeight()) / 4)));
        g.drawString(Integer.toString(i), 0, getHeight() - getInsets().bottom);
    }
}
