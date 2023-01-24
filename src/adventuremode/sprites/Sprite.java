package adventuremode.sprites;

import menus.ExitFunctionException;
import menus.gui;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static adventuremode.components.AdventureModeUiPanel.COLS;
import static adventuremode.components.AdventureModeUiPanel.ROWS;

public class Sprite {

private double x;
private double y;
private double cw = 0;
private double ch = 0;
private int myColNum;
private int myRowNum;
private int myCellWidth;
private int myCellHeight;
@Getter private boolean moving = false;
@Getter @Setter private int maxMovingIndex; //how many subimages there are for its directions
@Getter @Setter private int movingIndex;
@Getter @Setter public Direction dir; //Direction the user's sprite is facing
@Getter private int speedWeight = 150;
public Rectangle hitbox = new Rectangle();
@Getter private final Map<Direction, 							Image>	standingImgMap = new EnumMap<>(Direction.class);
@Getter private final Map<Direction, List<BufferedImage>> mooovingImgMap = new EnumMap<>(Direction.class);

public void setToCellPos(int cellNum) {
	setX((myCellWidth  = gui.getWidth()/COLS)			*			(myColNum = cellNum % COLS)								+		getWRemainder());
	setY((myCellHeight = gui.getHeight()/ROWS)		*			(myRowNum = (cellNum - myColNum) /COLS)		+		getHRemainder());
	cw = myCellWidth;
	ch = myCellHeight;
	myCellWidth  += gui.getWidth()  % COLS <= myColNum ? 0 : 1;
	myCellHeight += gui.getHeight() % ROWS <= myRowNum ? 0 : 1;
}


// will always be less than 53.
private int getWRemainder() { return Math.min(gui.getWidth() % COLS, myColNum); }
private int getHRemainder() { return Math.min(gui.getHeight() % ROWS, myRowNum); }

protected void setX(double x) { this.x = x - getWRemainder(); }
protected void setY(double y) { this.y = y - getHRemainder(); }

public int getX() {
	// using column width & integer division instead of framewidth, cause x only needs to change when the width changes by COLS.
	int cw = gui.getWidth() / COLS;
	// only executes on multiples of 53
	if (this.cw != cw) {
		// this.cw needs to be double to avoid integer division
		x += x * (cw / this.cw - 1);
		this.cw = cw;
	}
	myColNum = (int) ((gui.getWidth() % COLS <= myColNum ? 0 : myColNum/++cw) + x/cw);
	myCellWidth = cw;
	return (int) x + getWRemainder();
}

// TODO: the npcs are still calling this method once leaving the area
//System.out.println(this.getClass().getName() + " " + myRowNum);
public int getY() {

	int ch = gui.getHeight() / ROWS;
	if (this.ch != ch) {
		y += y * (ch / this.ch - 1);
		this.ch = ch;
	}
	myRowNum = (int) ((gui.getHeight() % ROWS <= myRowNum ? 0 : myRowNum/++ch) + y/ch);
	myCellHeight = ch;
	return (int) y + getHRemainder();
}





protected int getHeight() {
	return myCellHeight * 2;
}

protected int getWidth() {
	return myCellWidth * 2;
}

protected int getMaxX() {
	return getX() + getWidth();
}

protected int getMaxY() {
	return getY() + getHeight();
}


protected void setMoving(boolean moving) {
	this.moving = moving;
	if (!moving) {
		movingIndex = 0;
	}
}

public void setSpeedWeight(int speedWeight) {
	ExitFunctionException.If(speedWeight < 20, "Cannot set speedweight lower than 20");
	this.speedWeight = speedWeight;
	System.out.println("SpeedWeight:" + speedWeight);
}

public Rectangle getHitbox() {
	return new Rectangle(
	getX() + (myCellWidth/2),
	getY() + (myCellHeight),
	myCellWidth,
	myCellHeight);
}

public Point getHitboxCenterPoint() {
	return new Point(
	(int) getHitbox().getCenterX(),
	(int) getHitbox().getCenterY());
}

protected int getHitboxMaxX() {
	return (int) getHitbox().getMaxX();
}
protected int getHitboxMaxY() {
	return (int) getHitbox().getMaxY();
}


protected double getStepDis(){
	double stepDis = (double) switch (dir) {
		case L, R -> myCellWidth * COLS ;
		case D, U -> myCellHeight * ROWS;
	} / getSpeedWeight();


	switch (dir) {
		case L, R -> hitbox.width += stepDis;
		case D, U -> hitbox.height += stepDis;
	}

	switch (dir) {
		case L ->   	hitbox.x -= stepDis;
		case U -> 		hitbox.y -= stepDis;
	}
	return stepDis;
}

protected void step(double stepDis) {
	switch (dir) {
		case R -> setX(getX() + stepDis);
		case L -> setX(getX() - stepDis);
		case D -> setY(getY() + stepDis);
		case U -> setY(getY() - stepDis);
	}
	movingIndex++;
	movingIndex %= maxMovingIndex;
}

public Rectangle getBounds() {
	return new Rectangle(
	getX(),
	getY(),
	getWidth(),
	getHeight()
	);
}

public void createSpriteMaps(String SPRITE_SHEET_PATH, int totCols, int y0, int cw, int rh, int L, int R, int U, int D) {
	// get sub-images (sprites) from the sprite sheet
	try {
		BufferedImage img = ImageIO.read(new File(SPRITE_SHEET_PATH));

		for(var dir: Direction.values()) {
			int ry = y0 +
			switch (dir) {
				case L -> L;
				case R -> R;
				case U -> U;
				case D -> D;
			} * rh;

			// first image is standing
			standingImgMap.put(dir, img.getSubimage(0, ry, cw, rh));

			// all others are moving
			mooovingImgMap.put(dir, new ArrayList<>() {{
				int col = 0;
				while(++col < totCols)
					add(img.getSubimage(col * cw, ry, cw, rh));
				}});
		}
	} catch (IOException ex) {
		ex.printStackTrace();
	}
}


public void draw(Graphics2D g) {
	Image img = !isMoving()
							? standingImgMap.get(dir)
							: mooovingImgMap.get(dir).get(movingIndex);

	g.drawImage(img, getX(), getY(), getWidth(), getHeight(), null);

	g.setColor(Color.white); 										g.draw(getBounds());
	g.setColor(new Color(255, 255, 0, 100));		g.fill(hitbox);
	g.setColor(Color.BLACK); 										g.draw(hitbox);

	hitbox = getHitbox();
}
}
