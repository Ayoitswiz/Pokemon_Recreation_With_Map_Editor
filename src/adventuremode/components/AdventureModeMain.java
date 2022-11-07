package adventuremode.components;

import adventuremode.sprites.MySprite;
import menus.gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class AdventureModeMain extends JPanel {

AdventureModeUiPanel adventureModeUiPanel = new AdventureModeUiPanel();

public AdventureModeMain(MySprite humanTrainer) {
	gui.addNewUI(this);
	gui.addNewBackgroundImage(this.getClass().getSimpleName(), null);
	setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	setBackground(Color.black);
	c.fill = 1;
	c.weighty = 1;
	c.weightx = 1;
	add(adventureModeUiPanel, c);
	gui.setUI(this.getClass().getSimpleName());
	gui.setBackgroundImage(null);
	adventureModeUiPanel.createUI(humanTrainer);
}



public void startUI() {
	validate();
	adventureModeUiPanel.repaint();
	adventureModeUiPanel.validate();
}
}
