package AdventureMode;

import java.awt.*;

import static AdventureMode.AdventureModeUiPanel.COLS;
import static AdventureMode.AdventureModeUiPanel.ROWS;


abstract public class Sprite {

    protected int maxMovingIndex; //how many subimages there are for its directions
    private double deltaX; //How many pixels the sprite will move on screen horizontally
    private double deltaY; //How many pixels the sprite will move on screen vertically
    protected SpriteDirection direction; //Direction the user's sprite is facing
    protected int x;
    protected int y;
    private int height;
    private int width;
    protected boolean moving = false;
    protected int movingIndex;
    protected int startingCell;
    private int startingCellsX = -1000;
    private int startingCellsY = -1000;
    private boolean isDefeated = false;
    private double componentsAwayFromcellX = -1000;
    private double componentsAwayFromcellY = -1000;
    private int speedWeight = 200;
    private double ticksFromCellZeroX = 0;
    private double ticksFromCellZeroY = 0;

    public void setLocationRelativeTo(Component locationRelativeTo) {
        this.locationRelativeTo = locationRelativeTo;
    }

    private Component locationRelativeTo;

    public Sprite(){

    }

    public void setSpriteSize(Dimension dimension) {
        setWidth(dimension.width * 2);
        setHeight(dimension.height * 2);
    }

    /**
     * Takes the dimensions of any cell on ui and multiples it by
     * how many columns/rows the ui is made up of. This method changes the
     * horizontal and vertical movement speeds of sprites based on the size
     * of the playable area and the sprites speedWeights. This method is meant for
     * resizability.
     */
    public void setDeltas() {
        setDeltaX((double) (locationRelativeTo.getWidth() * COLS) / getSpeedWeight());
        setDeltaY((double) (locationRelativeTo.getHeight() * ROWS) / getSpeedWeight());
    }

    public void setTicksFromCellZeroBasedOnOtherSprite(Point spritesNewLocation) {
        ticksFromCellZeroX = (spritesNewLocation.getX() - locationRelativeTo.getX()) / getDeltaX();
        ticksFromCellZeroY = (spritesNewLocation.getY() - locationRelativeTo.getY()) / getDeltaY();
    }

    public void setTicksFromCellZeroBasedOnComponent(Point spritesNewLocation) {
        ticksFromCellZeroX = ((spritesNewLocation.getX() - locationRelativeTo.getX()) - (double) getWidth() / 4) / getDeltaX();
        ticksFromCellZeroY = ((spritesNewLocation.getY() - locationRelativeTo.getY()) - (double) getHeight() / 2) / getDeltaY();
    }

    public void updateComponentsFromContainerStart() {
        setComponentsAwayFromcellX((getTicksFromCellZeroX() * getDeltaX()) / locationRelativeTo.getWidth());
        setComponentsAwayFromcellY((getTicksFromCellZeroY() * getDeltaY()) / locationRelativeTo.getHeight());
    }

    public void setLocation() {

        setX((int) ((getComponentsAwayFromcellX() * locationRelativeTo.getWidth()) + locationRelativeTo.getX()));
        setY((int) ((getComponentsAwayFromcellY() * locationRelativeTo.getHeight()) + locationRelativeTo.getY()));
    }

    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setLocation(Point p){
        this.x = p.x;
        this.y = p.y;
    }

    protected Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }

    public Point getLocation() {
        return new Point(x, y);
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getMaxX() {
        return getX() + getWidth();
    }
    public int getMaxY() {
        return getY() + getHeight();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isMoving() {
        return moving;
    }



    public void setMoving(boolean moving) {
        this.moving = moving;
        if (!moving) {
            movingIndex = 0;
        }
    }




    public int getMovingIndex() {
        return movingIndex;
    }

    public void setMovingIndex(int movingIndex) {
        this.movingIndex = movingIndex;
    }

    public void setMaxMovingIndex(int maxMovingIndex) {
        this.maxMovingIndex = maxMovingIndex;
    }

    public void setDeltaX(double delta) {
        this.deltaX = delta;
    }

    public void setDeltaY(double delta) {
        this.deltaY = delta;
    }

    public double getComponentsAwayFromcellX() {
        return componentsAwayFromcellX;
    }

    public void setComponentsAwayFromcellX(double componentsAwayFromcellX) {
        this.componentsAwayFromcellX = componentsAwayFromcellX;
    }

    public double getComponentsAwayFromcellY() {
        return componentsAwayFromcellY;
    }

    public void setComponentsAwayFromcellY(double componentsAwayFromcellY) {
        this.componentsAwayFromcellY = componentsAwayFromcellY;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public int getStartingCellsY() {
        return startingCellsY;
    }

    public void setStartingCellsY(int startingCellsY) {
        this.startingCellsY = startingCellsY;
    }

    public int getStartingCellsX() {
        return startingCellsX;
    }

    public void setStartingCellsX(int startingCellsX) {
        this.startingCellsX = startingCellsX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public int getSpeedWeight() {
        return speedWeight;
    }

    public void setSpeedWeight(int speedWeight) {
        this.speedWeight = speedWeight;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }


    public double getTicksFromCellZeroX() {
        return ticksFromCellZeroX;
    }

    public void updateTicksFromCellZeroX(double tick) {
        this.ticksFromCellZeroX += tick;
    }

    public double getTicksFromCellZeroY() {
        return ticksFromCellZeroY;
    }

    public void updateTicksFromCellZeroY(double tick) {
        this.ticksFromCellZeroY += tick;
    }

}