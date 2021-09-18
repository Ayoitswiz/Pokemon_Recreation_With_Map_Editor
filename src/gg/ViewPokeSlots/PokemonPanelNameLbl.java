package gg.ViewPokeSlots;

import gg.Pokemon.AllTypes.eTypes;

import javax.swing.*;
import java.awt.*;

public class PokemonPanelNameLbl extends JPanel {
    private String one, two;
    PokemonPanelNameLbl(Color color, String text, eTypes[] type){

        one = text;
        two = "(" + type[0].name();
        if(type.length > 1) {
            two = two + "/" + type[1].name() + ")";
        } else {
            two += ")";
        }
        setDoubleBuffered(true);
        setBackground(color);
    }

    private void fitTextToAvailableSpace2(Graphics g, String str) {
        Font labelFont = g.getFont();
        int stringWidth = g.getFontMetrics(labelFont).stringWidth(str);
        int componentWidth = getWidth();

// Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = getHeight() / 4;

// Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

// Set the label's font size to the newly determined size.
        g.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        fitTextToAvailableSpace2(g, one);
        g.drawString(one, (getWidth() - g.getFontMetrics().stringWidth(one)) / 2, g.getFontMetrics().getHeight());
        int str1Height = g.getFontMetrics().getHeight();
        fitTextToAvailableSpace2(g, two);
        g.drawString(two, (getWidth() - g.getFontMetrics().stringWidth(two)) / 2, g.getFontMetrics().getHeight() + str1Height);
    }
}
