package AdventureMode;

import java.awt.*;

import static AdventureMode.AdventureModeUiPanel.COLS;
import static AdventureMode.AdventureModeUiPanel.ROWS;
import static AdventureMode.AdventureModeUiPanel.cellZero;

abstract public class Sprite {

    private int maxMovingIndex; //how many subimages there are for its directions
    private double deltaX; //How many pixels the sprite will move on screen horizontally
    private double deltaY; //How many pixels the sprite will move on screen vertically
    private int x;
    private int y;
    private int height;
    private int width;
    private boolean moving = false;
    private int movingIndex;
    private int startingCell;
    private int startingCellsX = -1;
    private int startingCellsY = -1;
    private boolean isDefeated = false;
    private double componentsAwayFromcellX = -1;
    private double componentsAwayFromcellY = -1;
    private int speedWeight = 200;
    private double stepsFromCellZeroX = 0;
    private double stepsFromCellZeroY = 0;
    public Sprite(){

    }

    protected void setSpriteSize() {
        setWidth(cellZero.getWidth() * 2);
        setHeight(cellZero.getHeight() * 2);
    }

    /**
     * Takes the dimensions of any cell on ui and multiplies it by
     * how many columns/rows the ui is made up of. This method changes the
     * horizontal and vertical movement speeds of sprites based on the size
     * of the playable area and the sprites speedWeights. This method is meant for
     * resizability.
     */
    protected void setDeltas() {
        setDeltaX((double) (cellZero.getWidth() * COLS) / getSpeedWeight());
        setDeltaY((double) (cellZero.getHeight() * ROWS) / getSpeedWeight());
    }

    protected void setTicksFromCellZeroBasedOnOtherSprite(Point spritesNewLocation) {
        setStepsFromCellZeroX((spritesNewLocation.x - cellZero.getX()) / getDeltaX());
        setStepsFromCellZeroY((spritesNewLocation.y - cellZero.getY()) / getDeltaY());
    }

    protected void setStepsFromCellZeroBasedOnComponent(Point spritesNewLocation) {
        setStepsFromCellZeroX(((spritesNewLocation.getX() - cellZero.getX()) - (double) getWidth() / 4) / getDeltaX());
        setStepsFromCellZeroY(((spritesNewLocation.getY() - cellZero.getY()) - (double) getHeight() / 2) / getDeltaY());
    }

    protected void updateComponentsFromContainerStart() {
        setComponentsAwayFromcellX((getStepsFromCellZeroX() * getDeltaX()) / cellZero.getWidth());
        setComponentsAwayFromcellY((getStepsFromCellZeroY() * getDeltaY()) / cellZero.getHeight());
    }

    protected void setLocation() {
        setX((int) ((getComponentsAwayFromcellX() * cellZero.getWidth()) + cellZero.getX()));
        setY((int) ((getComponentsAwayFromcellY() * cellZero.getHeight()) + cellZero.getY()));
    }

    void setLocation(int x, int y){
        this.setX(x);
        this.setY(y);
    }
    void setLocation(Point p){
        setLocation(p.x, p.y);
    }

    Rectangle getBounds(){
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    protected Point getLocation() {
        return new Point(getX(), getY());
    }


    protected int getX() {
        return x;
    }

    protected void setX(int x) {
        this.x = x;
    }

    protected int getY() {
        return y;
    }

    protected void setY(int y) {
        this.y = y;
    }

    protected int getHeight() {
        return height;
    }

    void setHeight(int height) {
        this.height = height;
    }

    protected int getWidth() {
        return width;
    }

    protected int getMaxX() {
        return getX() + getWidth();
    }

    protected int getMaxY() {
        return getY() + getHeight();
    }

    void setWidth(int width) {
        this.width = width;
    }

    protected boolean isMoving() {
        return moving;
    }

    protected void setMoving(boolean moving) {
        this.moving = moving;
        if (!moving) {
            setMovingIndex(0);
        }
    }

    protected int getMovingIndex() {
        return movingIndex;
    }

    protected void setMovingIndex(int movingIndex) {
        this.movingIndex = movingIndex;
    }

    protected void setMaxMovingIndex(int maxMovingIndex) {
        this.maxMovingIndex = maxMovingIndex;
    }

    protected void setDeltaX(double delta) {
        this.deltaX = delta;
    }

    protected void setDeltaY(double delta) {
        this.deltaY = delta;
    }

    double getComponentsAwayFromcellX() {
        return componentsAwayFromcellX;
    }

    void setComponentsAwayFromcellX(double componentsAwayFromcellX) {
        this.componentsAwayFromcellX = componentsAwayFromcellX;
    }

    double getComponentsAwayFromcellY() {
        return componentsAwayFromcellY;
    }

    void setComponentsAwayFromcellY(double componentsAwayFromcellY) {
        this.componentsAwayFromcellY = componentsAwayFromcellY;
    }

    protected double getDeltaX() {
        return deltaX;
    }

    protected int getStartingCellsY() {
        return startingCellsY;
    }

    protected void setStartingCellsY(int startingCellsY) {
        this.startingCellsY = startingCellsY;
    }

    protected int getStartingCellsX() {
        return startingCellsX;
    }

    protected void setStartingCellsX(int startingCellsX) {
        this.startingCellsX = startingCellsX;
    }

    protected double getDeltaY() {
        return deltaY;
    }

    protected int getSpeedWeight() {
        return speedWeight;
    }

    protected void setSpeedWeight(int speedWeight) {
        this.speedWeight = speedWeight;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }

    double getStepsFromCellZeroX() {
        return stepsFromCellZeroX;
    }

    protected void updateTicksFromCellZeroX(double tick) {
        this.setStepsFromCellZeroX(this.getStepsFromCellZeroX() + tick);
    }

    double getStepsFromCellZeroY() {
        return stepsFromCellZeroY;
    }

    protected void updateTicksFromCellZeroY(double tick) {
        this.setStepsFromCellZeroY(this.getStepsFromCellZeroY() + tick);
    }

    protected int getMaxMovingIndex() {
        return maxMovingIndex;
    }

    protected int getStartingCell() {
        return startingCell;
    }

    protected void setStartingCell(int startingCell) {
        this.startingCell = startingCell;
    }

    void setStepsFromCellZeroX(double stepsFromCellZeroX) {
        this.stepsFromCellZeroX = stepsFromCellZeroX;
    }

    void setStepsFromCellZeroY(double stepsFromCellZeroY) {
        this.stepsFromCellZeroY = stepsFromCellZeroY;
    }
}
