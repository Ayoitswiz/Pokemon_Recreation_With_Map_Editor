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
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static adventuremode.components.StartMenu.Options.*;

public class StartMenu extends JPanel {
private final MySprite user;
final BufferedImage textImage = new BufferedImage(
100, 100,
BufferedImage.TYPE_INT_ARGB);

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
	Color menu 						= new Color(200, 55, 55);
	Border redLine 				= BorderFactory.createLineBorder(menu, 5);
	Border thick 					= BorderFactory.createStrokeBorder(new BasicStroke(5.0f));
	Border raisedetched 	= BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	Border loweredetched 	= BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	Border raisedbevel 		= BorderFactory.createRaisedBevelBorder();
	Border loweredbevel 	= BorderFactory.createLoweredBevelBorder();
	Border compound 			= BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
	compound 							= BorderFactory.createCompoundBorder(redLine, 			compound);
	compound 							= BorderFactory.createCompoundBorder(thick, 				compound);
	compound 							= BorderFactory.createCompoundBorder(loweredetched, compound);
	compound 							= BorderFactory.createCompoundBorder(raisedetched, 	compound);

	setBorder(compound);

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
	(int) (gui.getWidth() - (gui.getWidth() / 2.5)),
	gui.getHeight() / 16,
	(int) (gui.getWidth() / 2.5),
	gui.getHeight() - (gui.getHeight() / 8));
	return super.getBounds();
}

class DialogBoxTextArea extends JTextArea implements FocusListener {
	DialogBoxTextArea() {
		setRows(2);
		setColumns(20);
		setWrapStyleWord(true);
		setLineWrap(true);
		setFont(new Font("Times New Roman", Font.PLAIN, 14));
		setText("sw\nco");
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
					default:
						DialogBoxTextArea.this.setText("commands are either: co, sw <any number>");
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
		if (getText().length() < 1) {
			setText("Enter Command");
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

		final ComponentListener cl = new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				setFont(getFont().deriveFont((float) getMaxFontSizeForControls()));
			}};

		new Timer(0, e -> addComponentListener(cl)) {{
			setRepeats(false);
			start();
		}};
	}


	public int getMaxFontSizeForControls() {
		Graphics2D g = textImage.createGraphics();
		int maxSize = Math.min(48, Math.max(6, getMaxFontSizeForControl(g.getFontRenderContext())));
		g.dispose();
		return maxSize;
	}

	public int getMaxFontSizeForControl(FontRenderContext frc) {
		Insets i = getBorder().getBorderInsets(this);
		Insets m = getMargin();

		Rectangle viewableArea = new Rectangle(
		getWidth()	-				(m.right + m.left + i.left + i.right),
		getHeight()	-				(m.top + m.bottom + i.top + i.bottom)
		);

		int size = 1;
		Rectangle2D box;
		boolean tooBig = false;
		while (!tooBig) {
			box = getFont().deriveFont((float) size).createGlyphVector(frc, getText()).getVisualBounds();
			if (box.getHeight() > viewableArea.getHeight()
					|| box.getWidth() > viewableArea.getWidth()) {
				tooBig = true;
				size--;
			}
			size++;
		}
		return size;
	}
}
}
