package AdventureMode;

import java.awt.*;
import gg.GUIManager;
import javax.swing.*;

public class AdventureModeMain extends JPanel {

    public AdventureModeMain(MySprite humantrainer) {
        GUIManager.addNewUi(this);
        GUIManager.addNewBackgroundImage(this.getClass().getSimpleName(), null);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.weighty = 1;
        c.weightx = 1;
        AdventureModeUiPanel adventureModeUiPanel = new AdventureModeUiPanel(humantrainer);
        add(adventureModeUiPanel, c);
    }
}
