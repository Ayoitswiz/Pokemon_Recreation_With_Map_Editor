package adventuremode.sprites;

import adventuremode.cells.Cell;
import adventuremode.cells.Grass;
import adventuremode.components.AdventureModeUiPanel.Wall;
import adventuremode.components.AdventureModeUiPanel.Warp;
import adventuremode.components.DialogBox;
import menus.battle.trainers.HumanTrainer;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MySprite extends HumanTrainer {
private static final String SPRITE_SHEET_PATH = "src/adventuremode/img/userspritesheet.gif";
private final Map<Direction, Image> standingImgMap = new EnumMap<>(Direction.class);
private final Map<Direction, List<Image>> movingImgMap = new EnumMap<>(Direction.class);
private Direction directionWhenWarping;


public MySprite(BigDecimal money, String name) {
	setMoney(money);
	setName(name);
	setMaxMovingIndex(4);
	createSpriteSheetImg();
}

void draw(Graphics g) {
	Image img = !this.isMoving()
							? standingImgMap.get(getDir())
							: movingImgMap.get(getDir()).get(getMovingIndex());
	g.setColor(Color.white);
	g.drawRect(getX(), getY(), getWidth(), getHeight());
	g.drawImage(img, getX(), getY(), getWidth(), getHeight(), null);
	g.setColor(Color.black);
	g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	hitbox = getHitbox();
}


public void createSpriteSheetImg() {
	try {
		File spriteSheetUrl = new File(SPRITE_SHEET_PATH);
		createSprites(ImageIO.read(spriteSheetUrl));
	} catch (IOException ex) {
		ex.printStackTrace();
	}
}

public void createSprites(BufferedImage img) {
	// get sub-images (sprites) from the sprite sheet
	int x0 = 0;
	int y0 = 64;
	int rW = 32;
	int rH = 32;
	for (int row = 0; row < 4; row++) {
		Direction dir =
		switch (row) {
			case 0 -> Direction.D;
			case 1 -> Direction.L;
			case 2 -> Direction.U;
			case 3 -> Direction.R;
			default -> throw new IllegalStateException("Unexpected value: " + row);
		};
		List<Image> imgList = new ArrayList<>();
		movingImgMap.put(dir, imgList);
		int rY = y0 + row * rH;
		for (int col = 0; col < 5; col++) {
			int rX = x0 + col * rW;
			BufferedImage subImg = img.getSubimage(rX, rY, rW, rH);
			if (col == 0) {
				// first image is standing
				standingImgMap.put(dir, subImg);
			} else {
				// all others are moving
				imgList.add(subImg);
			}
		}
	}
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
		case Warp w && dir.turnAround().equals(directionWhenWarping) -> {
			directionWhenWarping = dir;
			setMoving(false);
			Warp.enter.foo(w);
			setToCellPos(w.getMoveUserToCell());
			return;
		}
		case Grass s -> s.determineIfPokemonIsThere(this, dbox);
		default -> {}
	}


	double stepDis = getStepDis();

	for (var npc: npcs) {
		if (npc.intersects(hitbox)) {
			// TODO: Doesn't check for nearest npc
			stepDis = switch (dir){
				case L -> getHitbox().x - (npc.getHitboxMaxX()+1);
				case R -> npc.getHitbox().x - 1 - getHitboxMaxX();
				case U -> getHitbox().y - npc.getHitboxMaxY();
				case D -> npc.getHitbox().y - getHitboxMaxY();
			};
		}
	}

	switch (dir) {
		case L, R -> {	hitbox.y += 1;		hitbox.height -= 2; }
		case D, U -> {	hitbox.x += 1;		hitbox.width -= 2; }
	}


	//The array always gets executed all the way through. The order of the array goes from cells farthest away to
	//closest, ending with cellWeAreIn. This is because when the user collides with an non-enterable cell their position
	//gets set right outside the edge of that cell. Because of this if the cell farthest away was returned the
	//users position would be based on that, and cause the user to jump forward a cell even if theirs a Wall there

	for(var cell: cols) {
		if (cell.intersects(hitbox)) {
			switch (cell) {
				case Wall w ->
				stepDis = Math.min(switch (dir) {
					case L -> getHitbox().x - w.getMaxX();
					case R -> w.getX() - 1 - getHitboxMaxX();
					case U -> getHitbox().y - w.getMaxY();
					case D -> w.getY() - getHitboxMaxY();
				}, stepDis);
				case Warp w && !(cellWeAreIn instanceof Warp) -> {
					directionWhenWarping = dir;
					setMoving(false);
					Warp.enter.foo(w);
					setToCellPos(w.getMoveUserToCell());
					return;
				}
				default -> {}
			}
		}
	}
	step(stepDis);
}
}
