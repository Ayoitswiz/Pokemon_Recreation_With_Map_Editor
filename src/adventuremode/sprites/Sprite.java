package adventuremode.sprites;

import menus.ExitFunctionException;
import menus.gui;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

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

public void setToCellPos(int cellNum) {
	setX((myCellWidth  = gui.getWidth()/COLS)			*			(myColNum = cellNum % COLS)								+		getWRemainder());
	setY((myCellHeight = gui.getHeight()/ROWS)		*			(myRowNum = (cellNum - myColNum) /COLS)		+		getHRemainder());
	if (cw == 0) cw = myCellWidth;
	if (ch == 0) ch = myCellHeight;
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

protected Rectangle getHitbox() {
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
}
