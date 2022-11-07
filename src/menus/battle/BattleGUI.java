package menus.battle;

import adventuremode.components.DialogBox;
import adventuremode.sprites.DefaultNPC;
import adventuremode.sprites.MySprite;
import menus.backpack.Backpack;
import menus.battle.trainers.HumanTrainer;
import menus.gui;
import menus.in_game_items.Item;
import menus.pokemon.CreatePokemon;
import menus.pokemon.Pokemon;
import menus.pokemon.moves.Moves;
import utilities.Lambda;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Map;

import static menus.gui.x;
import static menus.gui.y;
import static menus.pokemon.moves.eMoves.*;

public class BattleGUI extends JComponent {
private Lambda onClose = () -> {};
DialogBox dBox = new DialogBox();
private LinkedList<Pokemon> aiP;
private LinkedList<Pokemon> userP;
Object[][][] o;
Pokemon p;
interface O<T> {T f();}

public BattleGUI open(HumanTrainer user, Opponent ai) {
	this.userP = user.getPokeSlots();
	this.aiP = ai.getPokeSlots();
	p = aiP.getFirst();

	o = new Object[][][]{
	{
	{(O) () -> p,						.005,		.033,		 .00, .00},
	{(O) () -> p.getGender(),					.005,		.100,		 .00, .00},
	{(O) () -> p.getStatus(),					.005,		.166,		 .00, .00},
	{(O) () -> "Lvl: " +  p.getLvl(),	.350,		.033,		 .00, .00},
	{(O) () -> "HP",									.160,		.100,		 .00, 2.0},
	{(O) () -> p.getHPRatio(),				.310,		.100,		 .00, 1.0}
	},
	{
	{(O) () -> p.getMove0(),					.175,		.750,		-.25, 1.0},
	{(O) () -> p.pp0(),								.175, 	.750,		+.75, 1.0},
	{(O) () -> p.getMove1(),					.525,		.750,		-.25, 1.0},
	{(O) () -> p.pp1(),								.525, 	.750,		+.75, 1.0},
	{(O) () -> p.getMove2(),					.175,		.916,		-.25, 1.0},
	{(O) () -> p.pp2(),								.175, 	.916,		+.75, 1.0},
	{(O) () -> p.getMove3(),					.525,		.916,		-.25, 1.0},
	{(O) () -> p.pp3(),								.525, 	.916,		+.75, 1.0},
	{(O) () -> "Close",								.850,		.833,		+.25, 1.0}
	},
	{
	{(O) () -> "FIGHT",								.775,		.750,		 .25, 1.0},
	{(O) () -> "BAG", 								.925,		.750,		 .25, 1.0},
	{(O) () -> "POKEMON",							.775,		.916,		 .25, 1.0},
	{(O) () -> "RUN",									.925,		.916,		 .25, 1.0}},
	};

	setOpaque(false);
	gui.addNewUIAndBackgroundImg(this, new ImageIcon("src/img/UI/PokeballFlat.png").getImage());
	gui.setUIAndBackgroundImg(BattleGUI.class.getSimpleName());
	add(dBox.setDefaultBattleDialog());

	BattleManager battleManager = new BattleManager()
																.open(user, ai, dBox.open())
																.onTurnEnd(dBox::setDefaultBattleDialog)
																.onBattleEnd(onClose);

	addMouseListener(new MouseAdapter() {
		void a(MouseEvent e, double x, double y, double w, double h, Lambda l) {
			if (new Rectangle(x(x), y(y), x(w), y(h)).contains(e.getPoint())) l.foo();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			if (!dBox.isVisible()) {
				for (var m : new double[][] {{0, .000, .666}, {1, .350, .666},
				 														 {2, .000, .833}, {3, .350, .833}})
				{ a(e, m[1], m[2],     .350, .166, () -> {dBox.setVisible(true); user.setChosenMove((int) m[0]); battleManager.start();});}
				a(e, .700, .666, .300, .333, () ->	dBox.setVisible(true));}
			else if (!dBox.hasNext()) {
				a(e, .700, .666, .150, .166, () ->	dBox.setVisible(false));
				a(e, .850, .666, .150, .166, () ->	new Backpack()
																						.open(user)
																						.onBackpackClose(() -> gui.setUIAndBackgroundImg(BattleGUI.class.getSimpleName()))
																						.onUsedItem(battleManager::start));
				a(e, .700, .833, .150, .166, () ->	user.faintedSwap(battleManager::start));
				a(e, .850, .833, .150, .166, () ->	dBox.setDialog(ai::getDialogIfUserTrysToFleeBattle)
																						.onDialogEnd(() -> {
																							gui.setUiOrDefault("AdventureModeMain", "MainMenuGUI");
																							onClose.foo();
																						}));
			}
		}
	});
	return this;
}

/*
 * could create fn
 * fn(Lambda one, Lambda two) {
 * if (!dbox.isVis: one.foo
 * else if (!dBox.hasNext: two.foo
 * merge both dupped logic trees
 * */

@Override
protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.setColor(new Color(40, 80, 225));
	g.fillRect(x(0), y(.66), x(1), y(.33));
	drawImg(g, "Front/" + (p = aiP  .getFirst()), .500, .000);
	drawInfo(g);
	drawImg(g, "Back/"  + (p = userP.getFirst()), .000, .333);
	drawInfo(g.create(x(.525), y(.466), x(1), y(1)));

	if (!dBox.isVisible()) {
		g.drawLine(x(.35), y(.66), x(.35), y(1.0));
		g.drawLine(x(.00), y(.83), x(.70), y(.83));
		drawss(g, 1);
	} else if (!dBox.hasNext()) {
		g.drawLine(x(.85), y(.66), x(.85), y(1.0));
		g.drawLine(x(.70), y(.83), x(1.0), y(.83));
		drawss(g, 2);
		dBox.setbounds(x(.00), y(.66), x(.70), y(.33));
	} else {dBox.setbounds(x(.00), y(.66), x(1.0), y(.33));}
}

void drawInfo(Graphics g) {
	g.setColor(new Color(220, 220, 190));
	g.fillRect(x(0), y(0), x(.5), y(.20));
	hpBar(g, Color.white, 1);
	hpBar(g, Color.green, (double) p.getHP() / p.getMaxHP());
	g.setColor(Color.black);
	drawss(g, 0);
}

void drawss(Graphics g, int i) {
	for (Object[] s :o[i]) {
		g.drawString(((O<?>) s[0]).f().toString(),
		(int) (x(s[1]) - (double) s[4] * g.getFontMetrics().getStringBounds(((O<?>) s[0]).f().toString(), g).getWidth() * .5),
		(int) (y(s[2]) + (double) s[3] * g.getFontMetrics().getHeight()));
	}
}

void hpBar(Graphics g, Color c, double d) {
	g.setColor(c);
	g.fillRect(x(.160),				y(.1) - g.getFontMetrics().getAscent(),
	(int) (x(.30) * d),						g.getFontMetrics().getHeight());
}

void drawImg(Graphics g, String s, double x, double y) {
	g.drawImage(new ImageIcon(getClass().getResource("../../img/Poke" + s + ".gif")).getImage(),
	x(x),
	y(y),
	x(.50),
	y(.33),
	this);
}

public BattleGUI onClose(Lambda onClose) {
	this.onClose = onClose;
	return this;
}

public static void main(String[] args) {
	HumanTrainer ht = new MySprite(new BigDecimal("10000.00"), "Ash");
	Pokemon rayquaza = CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, WEAK_MOVE, ICE_BEAM), ht);
	Pokemon articuno = CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, WEAK_MOVE, BLIZZARD), ht);
	Pokemon gengar = CreatePokemon.MegaGengar(9, Moves.Move(ICE_BEAM, WEAK_MOVE, SHADOW_BALL, STRONG_MOVE), ht);
	ht.getPokeSlots().add(rayquaza);
	ht.getPokeSlots().add(articuno);
	ht.getPokeSlots().add(gengar);
	Map<String, Item> items;
	items = new Item().createItem();
	ht.getItems()
	.computeIfAbsent("Max Potion", items::get)
	.setQuantity(10);
	ht.getItems()
	.computeIfAbsent("Hyper Potion", items::get)
	.setQuantity(11);
	ht.getItems()
	.computeIfAbsent("Super Potion", items::get)
	.setQuantity(10);
	ht.getItems()
	.computeIfAbsent("Potion", items::get)
	.setQuantity(10);
	Opponent ai = new DefaultNPC();
	new BattleGUI().open(ht, ai);
}

}
