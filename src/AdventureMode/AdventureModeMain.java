package AdventureMode;

import java.awt.*;
import MainMenu.GUIManager;
import javax.swing.*;

public class AdventureModeMain extends JPanel {

    public AdventureModeMain(MySprite humantrainer, GUIManager gui) {
        gui.addNewUi(this);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.weighty = 1;
        c.weightx = 1;
        AdventureModeUiPanel adventureModeUiPanel = new AdventureModeUiPanel(humantrainer, gui);
        add(adventureModeUiPanel, c);
    }
}
