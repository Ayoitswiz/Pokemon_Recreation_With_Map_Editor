package menus.ViewPokeSlots;

import menus.ExitFunctionException;
import menus.pokemon.Pokemon;
import menus.Styles.PokemonProgressBar;
import menus.gui;
import lombok.Getter;
import lombok.NoArgsConstructor;
import utilities.Lambda;
import utilities.LambdaWithParam;
import utilities.LambdaWithReturnVal;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

@NoArgsConstructor
public class PokemonInPartyPanel extends JPanel {

private GridBagConstraints c;
private JPanel pnlPokemonInParty;
Border raisedetched, loweredetched, raisedbevel, loweredbevel;
protected LambdaWithReturnVal<Pokemon> getSelectedPoke;
private Lambda onPartyClose = () -> {};
@Getter
private LambdaWithParam<Lambda> setOnClose = lam -> onPartyClose = lam;



public PokemonInPartyPanel open(LinkedList<Pokemon> pokeslots, LambdaWithParam<Pokemon> poke) {
	pnlPokemonInParty = new JPanel(new GridBagLayout()) {{setOpaque(false);}};
	c = new GridBagConstraints();
	setLayout(new BorderLayout());

	JLabel errorLbl = new JLabel(){{setVisible(false);}};
	JButton confirm = new JButton("Confirm") {{
		addActionListener(a -> {
			try {
				poke.foo(getSelectedPoke.foo());
				onPartyClose.foo();
			} catch (ExitFunctionException ex) {
				errorLbl.setText(ex.getMessage());
				errorLbl.setVisible(true);
			}
		});
	}};
	createEachPokemonsPanel(pokeslots);
	setOpaque(false);



	if (!pokeslots.getFirst().isFainted()) {
		add(new JButton("CLOSE") {{ addActionListener(a -> onPartyClose.foo()); }}, BorderLayout.SOUTH);
	}
	GridBagConstraints nc = new GridBagConstraints() {{
		fill = 1;
		weighty = .5;
		weightx = 1;
	}};

	add(new JPanel(new GridBagLayout()) {{
		add(confirm, nc);
		nc.gridy = 1;
		add(errorLbl, nc);
	}}, BorderLayout.NORTH);
	add(pnlPokemonInParty, BorderLayout.CENTER);

	gui.addNewBackgroundImage(getClass().getSimpleName(), new ImageIcon("src/img/UI/Pokeball.png").getImage());
	gui.addNewUI(getClass().getSimpleName(), this);
	gui.setBackgroundImage(getClass().getSimpleName());
	gui.setUI(getClass().getSimpleName());
	return this;
}

public PokemonInPartyPanel onPartyClose(Lambda l) {
	this.onPartyClose = l;
	return this;
}

private void createEachPokemonsPanel(LinkedList<Pokemon> pokeslots) {
	int i = 0;
	for (Pokemon p : pokeslots) {
		c.gridy = 0;
		c.gridx = i++;
		c.fill = 1;
		c.weightx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = .10;
		c.ipady = 0;
		pnlPokemonInParty.add(new JPanel() {{
			setOpaque(false);
		}}, c);
		c.weighty = .30;
		c.insets = new Insets(0, 5, 0, 5);
		c.anchor = GridBagConstraints.SOUTH;
		c.gridy = 1;
		pnlPokemonInParty.add(new PokemonPanelNameLbl(Color.WHITE, p.getName(), p.getType()), c);
		c.weighty = 1;
		c.gridy = 2;
		c.fill = 1;
		c.anchor = GridBagConstraints.CENTER;
		GifBtn gBtn = new GifBtn(p, pokeslots);

		pnlPokemonInParty.add(gBtn, c);
		c.ipadx = 0;
		c.insets = new Insets(0, 5, 0, 5);
		c.gridy = 3;
		c.ipady = 10;
		c.fill = 2;
		c.anchor = GridBagConstraints.NORTH;
		pnlPokemonInParty.add(new PokemonProgressBar(p.getHP(), p.getMaxHP()), c);
	}
}


private class GifBtn extends JButton {
	private static int i = 0;
	private static Pokemon isSelectedPokemon;
	private final LinkedList<Pokemon> pokeslots;
	private Image background;
	private final int id = i;
	private GridBagConstraints gifBtnC;
	private boolean mouseEnteredInsetsIncreased = false;
	private final GridBagLayout gbl = new GridBagLayout();

	// Pokemon fainted
	GifBtn(Pokemon p, LinkedList<Pokemon> pokeslots) {
		this.pokeslots = pokeslots;
		this.isSelectedPokemon = pokeslots.getFirst();
		getSelectedPoke = pokeslots::getFirst;
		createBtns(p, pokeslots);
	}

	private void createBtns(Pokemon p, LinkedList<Pokemon> pokeslots) {
		background = new ImageIcon("src/img/PokeFront/" + p.getName() + ".gif").getImage();

		JPanel jp = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				//Adds a box to the gifBtn indicating this is the poke currently in battle
				if (p == pokeslots.getFirst()) {
					g.setColor(Color.BLUE);
					g.fillRect(0, 0, getWidth(), getHeight());
					getParent().setBackground(Color.BLUE);
				}
				g.drawImage(background,
				-GifBtn.this.getInsets().left,
				0,
				getWidth() + (GifBtn.this.getInsets().right * 2),
				getHeight(),
				this);
				g.setColor(Color.RED);
				Graphics2D g2 = (Graphics2D) g;
				// Draw X over fainted poke
				if (p.isFainted()) {
					g2.setStroke(new BasicStroke(4));
					g2.drawLine(0, 0, getWidth(), getHeight());
					g2.drawLine(getWidth(), 0, 0, getHeight());
					g2.setColor(new Color(0, 0, 0, 130));
					g2.fillRect(0, 0, getWidth(), getHeight());
				}
				if (isSelectedPokemon == p) {
					GifBtn.this.setBackground(new Color(255, 0, 0));
					g.fillArc(-15, getHeight() - 35, 50, 50, 45, 20);
				} else if (mouseEnteredInsetsIncreased) {
					gifBtnC.ipadx = 0;
					GifBtn.this.gbl.setConstraints(this, gifBtnC);
					GifBtn.this.revalidate();
					GifBtn.this.setBackground(UIManager.getColor("control"));
					pnlPokemonInParty.validate();
					mouseEnteredInsetsIncreased = false;
				}
			}
		};
		addActionListener(a -> {
			isSelectedPokemon = p;
			mouseEnteredInsetsIncreased = true;
			getSelectedPoke = () -> p;
		});
		jp.setOpaque(false);
		setLayout(gbl);
		setOpaque(false);
		gifBtnC = new GridBagConstraints();
		gifBtnC.fill = GridBagConstraints.BOTH;
		gifBtnC.weightx = 1;
		gifBtnC.weighty = 1;
		if (pokeslots.getFirst() == p) {
			gifBtnC.ipadx = 50;
		}
		setContentAreaFilled(false);
		makeButtonLookNice();
		add(jp, gifBtnC);
		setMouseListeners(jp, p);
	}


	private void setMouseListeners(JPanel jp, Pokemon p) {
		this.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public synchronized void mouseEntered(MouseEvent evt) {
				gifBtnC.ipadx = 50;
				GifBtn.this.gbl.setConstraints(jp, gifBtnC);
				GifBtn.this.revalidate();

				try {
					//the color change to the background was executing before the previous object resized,
					// occasionally. The wait syncs the two up much more nicely
					wait(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				setBackground(new Color(246, 173, 35));
				pnlPokemonInParty.repaint();
				pnlPokemonInParty.validate();
			}

			@Override
			public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
				if (isSelectedPokemon != p && p != pokeslots.getFirst()) {
					gifBtnC.ipadx = 0;
					GifBtn.this.gbl.setConstraints(jp, gifBtnC);
					GifBtn.this.revalidate();
					try {
						wait(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					setBackground(UIManager.getColor("control"));
					pnlPokemonInParty.validate();
				}
			}
		});
	}

	private void makeButtonLookNice() {
		setOpaque(false);
		setBorderPainted(true);
		setContentAreaFilled(false);
		//setRolloverEnabled(true);
		raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		raisedbevel = BorderFactory.createRaisedBevelBorder();
		loweredbevel = BorderFactory.createLoweredBevelBorder();
		Border blueborder = BorderFactory.createLineBorder(new Color(0, 0, 255), 15);
		Border compound;
		Border thick = (BorderFactory.createStrokeBorder(new BasicStroke(10.0f)));
		compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
		compound = BorderFactory.createCompoundBorder(blueborder, compound);
		compound = BorderFactory.createCompoundBorder(thick, compound);
		compound = BorderFactory.createCompoundBorder(loweredetched, compound);
		compound = BorderFactory.createCompoundBorder(raisedetched, compound);
		//empty = BorderFactory.createEmptyBorder();
		setBorder(compound);
	}
}
}
