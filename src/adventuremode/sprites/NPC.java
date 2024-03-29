package adventuremode.sprites;

import adventuremode.cells.Cell;
import adventuremode.cells.Collisions;
import adventuremode.components.AdventureModeUiPanel.CellArray;
import adventuremode.components.AdventureModeUiPanel.Wall;
import menus.battle.trainers.AITrainer;
import menus.gui;
import lombok.Getter;
import lombok.Setter;
import utilities.Lambda;

import javax.swing.Timer;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static adventuremode.components.AdventureModeUiPanel.COLS;
import static adventuremode.components.AdventureModeUiPanel.ROWS;

public abstract class NPC extends AITrainer implements Collisions {
private double distanceFromUser = 0;
protected int cellCountToMoveThrough;
@Getter @Setter private Direction startingDirection;
@Getter @Setter public boolean caughtUser = false;
protected int battleGlareRange = 0;
public String[] preBattleDialog;
public int startingCellNum;
private static boolean canMove = true;


//TODO: Need to decide on how to better handle logic of npc & user movement
//public static boolean canMove(Opponent... npcs) {
//	for (Opponent npc : npcs) {
//		if (!canMove && npc.isCaughtUser() && npc.isOutOfUsablePokemon()) {
//			canMove = true;
//			npc.setCaughtUser(false);
//		}
//		else if (npc.isOutOfUsablePokemon()) {
//			continue;
//		}
//		else if (npc.isCaughtUser()) {
//				return canMove = false;
//			}
//	}
//	return canMove;
//}


public static void canMove(boolean userisBattlingWildPoke) {
	canMove = userisBattlingWildPoke;
}

public static boolean canMove() {
	return canMove;
}



// TODO: Thought this should work, need to find out why not.
//The distance an AdventureMode.sprites.NPC can see the player from
//public boolean getBattleGlareBounds(Rectangle user){
//	Rectangle rec = new Rectangle(getHitbox());
//
//	switch (dir) {
//		case R, L: rec.width	= battleGlareRange * (getWidth() / 2);
//		case D, U: rec.height	= battleGlareRange * (getHeight() / 2);
//	}
//
//	switch (dir) {
//		case R -> rec.x = getMaxX();
//		case L -> rec.x = getX() - rec.width;
//		case D -> rec.y = getMaxY();
//		case U -> rec.y = getY() - rec.height;
//		// can you set negative heights?
//	}
//	return rec.intersects(user);
//}


public boolean getBattleGlareBounds(Rectangle user){
	Rectangle rec = new Rectangle(getHitbox());
	switch (dir) {
		case R -> {
			rec.x = getMaxX();
			rec.width = (battleGlareRange * (getWidth() / 2));
			rec.y += 5;
		}
		case L -> {
			rec.x = getX() - (battleGlareRange * (getWidth() / 2));
			rec.width = (battleGlareRange * (getWidth() / 2));
		}
		case D -> {
			rec.y = getMaxY();
			rec.height = (battleGlareRange * (getHeight() / 2));
		}
		case U -> {
			// can you set negative heights?
			rec.y = getY() - battleGlareRange * (getHeight() / 2);
			rec.height = (battleGlareRange * (getHeight() / 2));
		}
	}

	return rec.intersects(user);
}



private void tick() {
	double stepDis = getStepDis();
	step(stepDis);
}



@Override
public boolean intersects(Rectangle r) {
	return getHitbox().intersects(r);
}



public void setDistanceFromUser(Rectangle user){
	this.distanceFromUser = switch (dir) {
		case L -> getX() - user.getMaxX();
		case R -> user.getX() - getMaxX();
		case D -> user.getY() - getMaxY();
		case U -> getY() - user.getMaxY();
	};
}



public int getDistanceCanMove() {
	return
	cellCountToMoveThrough *
	switch (startingDirection) {
		case L, R -> getWidth();
		default -> getHeight();
	} / 2;
}




/* TODO: Create method that draws this stuff so can remove purely ui stuff from complex code:
	1. Draw the battleglarebounds box minus any "Wall" it stops at.
	g.fillRect(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
 */
void npcHandler(CellArray cells, Rectangle userHitbox, Graphics2D g, Lambda l) {
	if (isOutOfUsablePokemon()) {
		return;
	}
	if (caughtUser) {
		setDistanceFromUser(userHitbox);
		return;
	}

	if (!canMove) {
		return;
	}

	if (getBattleGlareBounds(userHitbox)) {
		Cell cell = cells.getCell(getHitboxCenterPoint());
		Cell temp;
		int i = 0;
		while (
		i++ <= battleGlareRange
		&& !((temp = cells.getCell(
		cell.getNum() + switch (dir) {
			case R -> 1;
			case L -> -1;
			case D -> COLS;
			case U -> -COLS;
		})) instanceof Wall)) {
			g.fill((cell = temp).getHitbox());
		}

		if (caughtUser = switch (dir) {
			case R -> userHitbox.getMaxX()	< cell.getMaxX();
			case L -> userHitbox.x					> cell.getX();
			case D -> userHitbox.getMaxY()	< cell.getMaxY();
			case U -> userHitbox.y					> cell.getY();
		}) {
			canMove = false;
			l.foo();
		}
	}
}




private void changeDir() {
	if (caughtUser && distanceFromUser > 0) {
		return;
	}
	if (switch (getStartingDirection()) {
		case R -> getStartingCellsX() >= getX();
		case L -> getStartingCellsX() <= getX();
		case D -> getStartingCellsY() >= getY();
		case U -> getStartingCellsY() <= getY();
	}) setDir(getStartingDirection());

	if (getDistanceCanMove() <= switch (getStartingDirection()) {
		case R -> -getStartingCellsX() + getX();
		case L ->  getStartingCellsX() - getX();
		case D -> -getStartingCellsY() + getY();
		case U ->  getStartingCellsY() - getY();
	}) setDir(getStartingDirection().turnAround());
}


@Override
public boolean isMoving() {
	if (caughtUser) {
		return distanceFromUser > 0;
	}
	return canMove;
}


private final Timer timer = new Timer(50, new TimerListener());
public void start() {
	setDir(startingDirection);
	timer.start();
}

private class TimerListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		if (isOutOfUsablePokemon()) timer.stop();
		if (!isMoving()) return;
		tick();
		changeDir();
	}
}

public void stop() {
	timer.stop();
}


protected int getStartingCellsY() {
	int rowNum = (startingCellNum - (startingCellNum % COLS)) /COLS;
	return (gui.getHeight() / ROWS) *rowNum + Math.min(gui.getHeight() % ROWS, rowNum);
}
protected int getStartingCellsX() {
	int colNum = startingCellNum % COLS;
	return (gui.getWidth() / COLS) *colNum + Math.min(gui.getWidth() % COLS, colNum);
}

}
