package adventuremode.components;

import adventuremode.sprites.MySprite;
import menus.ViewPokeSlots.PokemonInPartyPanel;
import menus.backpack.Backpack;
import menus.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.StrokeBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import static adventuremode.components.StartMenu.Options.*;

public class StartMenu extends JPanel {
private final MySprite user;

public enum Options {
	POKeDEX,
	POKeMON,
	BAG,
	POKeNAV,
	TRAINER,
	SAVE,
	OPTIONS,
	MainMenu,
	CLOSE

}

public StartMenu(MySprite user) {
	this.user = user;

	setLayout(new GridLayout(10, 1, 0 , 0));
	setBorder(new EtchedBorder(EtchedBorder.RAISED));
	setBorder(new CompoundBorder(getBorder(), new EtchedBorder(EtchedBorder.LOWERED)));
	setBorder(new CompoundBorder(getBorder(), new StrokeBorder(new BasicStroke(5.0f))));
	setBorder(new CompoundBorder(getBorder(), new LineBorder(new Color(200, 55, 55), 5)));
	setBorder(new CompoundBorder(getBorder(), new BevelBorder(BevelBorder.RAISED)));
	setBorder(new CompoundBorder(getBorder(), new BevelBorder(BevelBorder.LOWERED)));

	add(new StartMenuButton(POKeDEX));
	add(new StartMenuButton(POKeMON));
	add(new StartMenuButton(BAG));
	add(new StartMenuButton(POKeNAV));
	add(new StartMenuButton(TRAINER));
	add(new StartMenuButton(SAVE));
	add(new StartMenuButton(OPTIONS));
	add(new StartMenuButton(MainMenu));
	add(new StartMenuButton(CLOSE));
	add(new DialogBoxTextArea());

	setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	setVisible(false);
}

public StartMenu toggle() {
	setVisible(!isVisible());
	return this;
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
	(int) (gui.getWidth() * .6),
	(int) (gui.getHeight() * .0625),
	(int) (gui.getWidth() * .4),
	(int) (gui.getHeight() * .875));
	return super.getBounds();
}

class DialogBoxTextArea extends JTextArea implements FocusListener {
	DialogBoxTextArea() {
		super("Enter \"help\" for command list.");
		setWrapStyleWord(true);
		setLineWrap(true);
		setFont(new Font("Times New Roman", Font.PLAIN, 14));
		setBorder(UIManager.getBorder("Label.border"));
		setLayout(new BorderLayout());

		addFocusListener(this);
		add(new JButton("Enter") {{
			// TODO: This stuff is broke
			addActionListener(e -> {
				String[] commandAndValue = DialogBoxTextArea.this.getText().split("\\s");
				switch (commandAndValue[0]) {
					case "sw" -> user.setSpeedWeight(Integer.parseInt(commandAndValue[1]));
					case "co" -> StartMenu.this.setVisible(false);
					case "help", default -> DialogBoxTextArea.this.setText("sw, co");
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
		if (getText().isBlank()) {
			setText("Enter \"help\" for command list.");
		}
	}
}



class StartMenuButton extends JButton {
	StartMenuButton(Options option) {
		super(option.toString());
		setFocusPainted(false);
		setForeground(Color.WHITE);
		setBackground(new Color(59, 89, 182));
		setMargin(new Insets(0, 14, 0, 14));
		setBorder(BorderFactory.createLoweredBevelBorder());
		setHorizontalAlignment(SwingConstants.LEFT);
		addActionListener(e -> {
			switch (option) {
				case POKeDEX:
					break;
				case POKeMON:
					new PokemonInPartyPanel()
					.open(
					user.getPokeSlots(),
					user.getPokeSlots()::addFirst)
					.onPartyClose(() -> gui.setUIAndBackgroundImg(AdventureModeMain.class.getSimpleName()));
					break;
				case BAG:
					new Backpack()
					.open(user)
					.onBackpackClose(() -> gui.setUIAndBackgroundImg(AdventureModeMain.class.getSimpleName()));
					break;
				case POKeNAV:
					break;
				case TRAINER:
					System.out.println(user.getName());
					break;
				case SAVE:
					break;
				case OPTIONS:
					new DialogBox(){{
						open().introDialog();
						StartMenu.this.getParent().add(this);
					}};
					StartMenu.this.getParent().validate();
					StartMenu.this.setVisible(false);
					break;
				case MainMenu:
					gui.setUIAndBackgroundImg("MainMenuGUI");
					break;
				case CLOSE:
					StartMenu.this.setVisible(false);
					break;
			}
		});

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				FontRenderContext frc = getGraphics().getFontMetrics().getFontRenderContext();

				Insets i = getBorder().getBorderInsets(StartMenuButton.this);
				Insets m = getMargin();

				Rectangle viewableArea = new Rectangle(
				getWidth()	-		(m.right + m.left   + i.left + i.right),
				getHeight()	-		(m.top   + m.bottom + i.top  + i.bottom)
				);

				int size = 1;
				Rectangle2D box;
				do {
					box = getFont()
								.deriveFont(size++ + 0f)
								.createGlyphVector(frc, getText())
								.getVisualBounds();

				} while (!(box.getHeight()	> viewableArea.getHeight()
							||	 box.getWidth()		> viewableArea.getWidth()));
				setFont(getFont().deriveFont((float) Math.min(48, Math.max(6, --size))));
			}});
	}
}
}
