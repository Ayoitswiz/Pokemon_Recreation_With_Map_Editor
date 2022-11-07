package menus.Styles;
import javax.swing.*;
import java.awt.*;

public class PokemonProgressBar extends JProgressBar{


    public PokemonProgressBar(int currentValue, int maxValue){
            UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
            this.setMaximum(maxValue);
            this.setValue(currentValue);
            this.setStringPainted(true);
            this.setForeground(new Color(112, 248, 168));
            this.setBackground(Color.WHITE);
            this.setPreferredSize(new Dimension(50, 15));
        }
}
