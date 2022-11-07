package adventuremode.components;

import adventuremode.sprites.MySprite;
import menus.backpack.Backpack;
import menus.gui;
import menus.ViewPokeSlots.PokemonInPartyPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class StartMenu extends JPanel {
private final MySprite user;

public StartMenu(MySprite user) {
	this.user = user;

	setLayout(new GridBagLayout());

	Border raisedetched, loweredetched, raisedbevel, loweredbevel;
	raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	raisedbevel = BorderFactory.createRaisedBevelBorder();
	loweredbevel = BorderFactory.createLoweredBevelBorder();
	Color menu = new Color(200, 55, 55);
	Border redLine = BorderFactory.createLineBorder(menu, 5);
	Border thick = (BorderFactory.createStrokeBorder(new BasicStroke(5.0f)));
	Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
	compound = BorderFactory.createCompoundBorder(redLine, compound);
	compound = BorderFactory.createCompoundBorder(thick, compound);
	compound = BorderFactory.createCompoundBorder(loweredetched, compound);
	compound = BorderFactory.createCompoundBorder(raisedetched, compound);

	setBorder(compound);

	GridBagConstraints c = new GridBagConstraints();
	c.fill = 1;
	c.weightx = 1;
	c.weighty = 1;
	c.gridwidth = 50;
	c.gridheight = 1;
	c.gridx = 0;
	c.anchor = GridBagConstraints.WEST;
	add(new StartMenuButton("POKeDEX"), c);
	c.gridy = 1;add(new StartMenuButton("POKeMON"), c);
	c.gridy = 2;add(new StartMenuButton("BAG"), c);
	c.gridy = 3;add(new StartMenuButton("POKeNAV"), c);
	c.gridy = 4;add(new StartMenuButton(user.getName()), c);
	c.gridy = 5;add(new StartMenuButton("SAVE"), c);
	c.gridy = 6;add(new StartMenuButton("OPTIONS"), c);
	c.gridy = 7;add(new StartMenuButton("Main Menu"), c);
	c.gridy = 8;add(new StartMenuButton("Close"), c);
	c.gridy = 9;
	add( new DialogBoxTextArea(), c);
	setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	setVisible(false);
}



@Override
public int getX() {
	setBounds(getBounds());
	return super.getX();
}

@Override
public int getY() {
	setBounds(getBounds());
	return super.getY();
}

@Override
public int getWidth() {
	setBounds(getBounds());
	return super.getWidth();
}

@Override
public int getHeight() {
	setBounds(getBounds());
	return super.getHeight();
}

@Override
public Rectangle getBounds() {
	setBounds(
	gui.getWidth() - (gui.getWidth() / 4),
	gui.getHeight() / 16,
	gui.getWidth() / 4,
	gui.getHeight() - (gui.getHeight() / 8));
	return super.getBounds();
}

class DialogBoxTextArea extends JTextArea implements FocusListener {
	DialogBoxTextArea() {
		setRows(2);
		setColumns(20);
		setWrapStyleWord(true);
		setLineWrap(true);
		setFont(new Font("Times New Roman", Font.PLAIN, 24));
		setText("sw\nco");
		setVisible(true);
		setBorder(UIManager.getBorder("Label.border"));
		setLayout(new BorderLayout());

		addFocusListener(this);
		add(new JButton("Enter") {{
				addActionListener(e -> {
					String[] commandAndValue = getText().split("\\s");
					switch (commandAndValue[0]) {
						case "sw":
							//Adjust users speed. Lower == faster.
							user.setSpeedWeight(Integer.parseInt(commandAndValue[1]));
							break;
						case "co":
							// TODO: need to update this based on code changes to work again.
							StartMenu.this.setVisible(false);
							break;
						default: DialogBoxTextArea.this.setText("commands are either: co, sw <any number>");
					}
				});
			}
		}, BorderLayout.SOUTH);
	}

	@Override
	public void focusGained(FocusEvent e) {
		setText("");
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(getText().length()<1) {
			setText("Enter Command");
		}
	}
}

class StartMenuButton extends JButton {
	String text;

	StartMenuButton(String text) {
		this.text = text;
		setBackground(new Color(59, 89, 182));
		setForeground(Color.WHITE);
		setFocusPainted(false);
		setVisible(true);
		setHorizontalAlignment(SwingConstants.LEFT);

		addActionListener(e -> {
			switch (text){
				case "POKeDEX":
					break;
				case "POKeMON":
					new PokemonInPartyPanel()
					.open(
					user.getPokeSlots(),
					user.getPokeSlots()::addFirst)
					.onPartyClose(() -> gui.setUIAndBackgroundImg(AdventureModeMain.class.getSimpleName()));
					break;
				case "BAG":
					new Backpack()
					.open(user)
					.onBackpackClose(() -> gui.setUIAndBackgroundImg(AdventureModeMain.class.getSimpleName()));
					break;
				case "POKeNAV":
					break;
				case "NAME":
					System.out.println(user.getName());
					break;
				case "SAVE":
					break;
				case "OPTIONS":
					DialogBox dialogBox = new DialogBox();
					dialogBox.open().introDialog();
					getParent().getParent().add(dialogBox);
					getParent().getParent().validate();
					StartMenu.this.setVisible(false);
					break;
				case "Main Menu":
					gui.setUIAndBackgroundImg("MainMenuGUI");
					break;
				case "Close":
					getParent().setVisible(false);
					break;
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setFont(
							g.getFont().deriveFont(
																			g.getFont().getSize() * ((getWidth() + getHeight()) / 100f)
																		)
							);

		g.drawString(
		text,
		getInsets().left/4,
		getInsets().top + g.getFontMetrics().getHeight());
	}
}
}
