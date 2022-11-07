package adventuremode.sprites;

import adventuremode.cells.Cell;
import adventuremode.components.AdventureModeUiPanel;
import adventuremode.components.AdventureModeUiPanel.CellArray;
import adventuremode.components.AdventureModeUiPanel.Warp;
import adventuremode.components.DialogBox;
import adventuremode.components.StartMenu;
import lombok.AllArgsConstructor;
import menus.battle.BattleGUI;
import utilities.LambdaWithParam;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static adventuremode.components.AdventureModeUiPanel.EDIT_MODE;
import static java.awt.event.KeyEvent.*;
import static javax.swing.KeyStroke.getKeyStroke;

public class UserSpriteManager extends JPanel {

private final Timer timer = new Timer(50, new TimerListener());
private final MySprite user;
private DialogBox dialogBox;
private final StartMenu startMenu;
private final List<Cell> colls = new ArrayList<>(); //list of collumns to check for collisions in dir of user movement.
private final AdventureModeUiPanel.CellArray cells;
//private final LambdaWithReturnVal<List<NPC>> npcs;
public Warp currentarea;

public UserSpriteManager(CellArray cells, MySprite user, Warp currentarea) {
	user.setDir(Direction.D);
	this.user = user;
	this.cells = cells;
	//this.npcs = npcs;
	this.currentarea = currentarea;
	setKeyBindings(VK_A, Direction.L);
	setKeyBindings(VK_D, Direction.R);
	setKeyBindings(VK_S, Direction.D);
	setKeyBindings(VK_W, Direction.U);
	setKeyBindings(VK_M, 0);
	setKeyBindings(VK_I, InputEvent.CTRL_DOWN_MASK);
	setKeyBindings(VK_D, InputEvent.CTRL_DOWN_MASK);
	setKeyBindings(VK_E, InputEvent.CTRL_DOWN_MASK);
	add(dialogBox = new DialogBox());
	add(startMenu = new StartMenu(this.user));
	setDoubleBuffered(true);
	setOpaque(false);
	dialogBox.open().introDialog();
}

public void start() {
	timer.start();
}

private void setKeyBindings(int keyCode, Direction dir) {
	setKeyBindings(keyCode,
	0,
	new MoveAction(dir, false),
	new MoveAction(dir, true));
}

private void setKeyBindings(int keyCode, int modifier) {
	setKeyBindings(keyCode,
	modifier,
	new ControlAction(keyCode));
}

private void setKeyBindings(int keyCode, int modifier, AbstractAction... action) {
	LambdaWithParam<Boolean> c = b -> {
		var ks = getKeyStroke(keyCode, modifier, b);
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ks, ks.toString());
		getActionMap().put(ks.toString(), action[b ? 1 : 0]);
	};
	c.foo(false);

	if (action.length > 1) {
		c.foo(true);
	}
}

@AllArgsConstructor
private class ControlAction extends AbstractAction {
	private final int keyCode;

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (keyCode) {
			case VK_I -> user.setSpeedWeight(user.getSpeedWeight() + 10);
			case VK_D -> user.setSpeedWeight(user.getSpeedWeight() - 10);
			case VK_M -> startMenu.setVisible(!startMenu.isVisible());
			case VK_E -> EDIT_MODE = !EDIT_MODE;
		}
	}
}

/**
 * TODO: If a movement key is held, and ctrl is held after, if movement key released...sprite keeps moving
 * 	Holding the ctrl key stops this actionPerformed method from being called
 * <p>
 * When a key is pressed or released <function>actionPerformed</function> is invoked.
 * if the current MoveAction is of a pressed keystroke, add it to the list of pressed keys.
 * This is used to keep track of what keys are pressed simultaneously, and the order they were pressed in.
 * <p>
 * If a key is held, then another (so 2 keys held simultaneously),
 * when the 1st key is released, starts moving in the 2nd keys direction w/ no delay.
 * W/o this code, you'd have to release each key before pressing another.
 * <p>
 * The released condition is an identifier to condition on whether the current action is of
 * the keyReleased or keyPressed. Only released variants will have<code>released = true</code>.
 */
@AllArgsConstructor
private class MoveAction extends AbstractAction {
	private static final Set<Direction> pressedOrder = new LinkedHashSet<>();
	private final Direction dir;
	private final boolean released;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (released) {
			pressedOrder.remove(dir);
		} else {
			pressedOrder.add(dir);
		}
		pressedOrder.forEach(user::setDir);
		user.setMoving(!pressedOrder.isEmpty());
	}
}

private class TimerListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		// if disableUserMovement = sprite.hasOpponent() || startmenu.isvisible()
		// NPC.canMove = disableUserMovement
		// so here it would be if (sprite.isMoving() && !sprite.disableUserMovement)
		if (user.isMoving() & (NPC.canMove() && !startMenu.isVisible())) {
			colls.clear();
			colls.addAll(switch (user.getDir()) {
				case R -> cells.getCell(user,
				1, -52, 54,
				2, -51, 55,
				3, -50, 56,
				4, -49, 57);
				case L -> cells.getCell(user,
				-1, 52, -54,
				-2, 51, -55,
				-3, 50, -56,
				-4, 49, -57);
				case D -> cells.getCell(user,
				52, 53, 54,
				105, 106, 107,
				158, 159, 160,
				211, 212, 213);
				case U -> cells.getCell(user,
				-52, -53, -54,
				-105, -106, -107,
				-158, -159, -160,
				-211, -212, -213);
			});
			colls.add(cells.getCell(user.getHitboxCenterPoint()));
			user.tick(colls, cells.getCell(user.getHitboxCenterPoint()), currentarea.getNpcs(), dialogBox);
		}
		repaint();
	}
}

@Override
protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	draw(g);
	NpcHandler(g);
}

Color redTrans = new Color(255, 0, 0, 100);

public void draw(Graphics g) {
	g.setColor(redTrans);
	colls.forEach(c -> g.fillRect(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
	user.draw(g);
}

private void NpcHandler(Graphics g) {
	for (var npc: currentarea.getNpcs()) {
		g.setColor(Color.RED);

		npc.npcHandler(
		cells,
		user.getHitbox(),
		g,
		() -> {
			user.setDir(npc.dir.turnAround());
			switch (npc.dir) {
				case L, R -> user.setY(npc.getY());
				case D, U -> user.setX(npc.getX());
			}

			dialogBox
			.open()
			.setDialog(npc.getName() + " wants to battle!")
			.cycleDialog(npc.preBattleDialog)
			.onDialogEnd(() ->
									 new BattleGUI()
									 .onClose(() -> {
										 dialogBox.reset();
										 npc.setCaughtUser(false);
										 NPC.canMove(true);
									 })
									 .open(user, npc));
		});
		npc.draw(g);
	}
}
}
