package adventuremode.sprites;

import adventuremode.cells.Cell;
import adventuremode.cells.Collisions;
import adventuremode.cells.Grass;
import adventuremode.components.AdventureModeUiPanel.Wall;
import adventuremode.components.AdventureModeUiPanel.Warp;
import adventuremode.components.DialogBox;
import menus.battle.trainers.HumanTrainer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MySprite extends HumanTrainer {
private Direction dirWhenWarping;


public MySprite(BigDecimal money, String name) {
	setMoney(money);
	setName(name);
	setMaxMovingIndex(4);
	createSpriteMaps("src/adventuremode/img/userspritesheet.gif",
	5,
	64,
	32,
	32,
	1,
	3,
	2,
	0);
}


void tick(List<Cell> cols,
					Cell cellWeAreIn,
					NPC[] npcs,
					DialogBox dbox) {
	/*When the user warps, he is placed inside another warp cell. This block allows the user to move
		in any direction on that warp without activating it. The warp will only be activated when the user moves in the
		opposite direction of the direction he was moving when he warped.
	*/
	switch (cellWeAreIn) {
		case Warp w when dir.turnAround().equals(dirWhenWarping) -> { warp(w); return;}
		case Grass s -> s.determineIfPokemonIsThere(this, dbox);
		default -> {}
	}


	double stepDis = getStepDis();

	for(var cell: new ArrayList<Collisions>(cols) {{addAll(List.of(npcs));}}) {
		if (cell.intersects(hitbox)) {
			if (cell instanceof Wall || cell instanceof NPC) {
				stepDis = Math.min(switch (dir){
					case L -> -cell.getHitbox().getMaxX() + getHitbox().x;
					case U -> -cell.getHitbox().getMaxY() + getHitbox().y;
					case R ->  cell.getHitbox().getMinX() - getHitboxMaxX() - 1;
					case D ->  cell.getHitbox().getMinY() - getHitboxMaxY();
				}, stepDis);
			}
			if (cell instanceof Warp w && !(cellWeAreIn instanceof Warp)) { warp(w); return;}
		}
	}
	step(stepDis);
}

private void warp(Warp w) {
	dirWhenWarping = dir;
	setMoving(false);
	Warp.enter.foo(w);
	setToCellPos(w.getMoveUserToCell());
}
}
