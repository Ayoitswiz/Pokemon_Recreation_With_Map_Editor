package AdventureMode;

import gg.Battle.Trainers.AITrainer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public abstract class NPC extends AITrainer implements Collisions
{
    //protected RocketGruntMaleDirection direction;
    private Map<RocketGruntMaleDirection, Image> standingImgMap = new EnumMap<>(RocketGruntMaleDirection.class);
    private Map<RocketGruntMaleDirection, java.util.List<BufferedImage>> movingImgMap = new EnumMap<>(RocketGruntMaleDirection.class);
    private double distanceFromUser = 0;
    protected int cellCountToMoveThrough;
    protected RocketGruntMaleDirection directionRG; //Direction Rocket Grunt-Male is facing
    private RocketGruntMaleDirection startingDirection;
    public boolean caughtPlayerInBattleGlare = false;
    protected int battleGlareRange = 0;
    public String[] preBattleDialog;
    protected int spriteSheetx;
    protected int spriteSheety;
    protected int spriteSheetColWidth;
    protected int spriteSheetRowHeight;
    private Rectangle npcHitbox;
    private NPCMovementThread npcMovementThread;
    public final String SPRITE_SHEET_PATH;


    NPC() {
        SPRITE_SHEET_PATH = "";
    }

    NPC(int spriteSheetx, int spriteSheety, int spriteSheetColWidth, int spriteSheetRowHeight, final String SPRITE_SHEET_PATH, String name, int delta, String[] preBattleDialog, int startingCell, int cellCountToMoveThrough, int battleGlareRangeInCells, RocketGruntMaleDirection startingDirection){
        this.spriteSheetx = spriteSheetx;
        this.spriteSheety = spriteSheety;
        this.spriteSheetColWidth = spriteSheetColWidth;
        this.spriteSheetRowHeight = spriteSheetRowHeight;
        this.SPRITE_SHEET_PATH = SPRITE_SHEET_PATH;
        setName(name);
        this.setDeltaX(delta);
        this.setDeltaY(delta);
        this.preBattleDialog = preBattleDialog;
        this.setStartingCell(startingCell);

        this.cellCountToMoveThrough = cellCountToMoveThrough;
        this.battleGlareRange = battleGlareRangeInCells;
        this.startingDirection = startingDirection;
        directionRG = startingDirection;
        npcHitbox = new Rectangle();
        updateHitbox();
    }

    public void draw(Graphics g) {
        Image img = null;

        img = !isMoving() ? standingImgMap.get(getDirection()) : movingImgMap.get(getDirection()).get(getMovingIndex());
        g.drawImage(img, getX(), getY(), getWidth(), getHeight(), null);
        g.drawRect(getX(), getY(), getWidth(), getHeight());
        g.setColor(new Color(255, 255, 0, 100));
        g.fillRect(npcHitbox.x, npcHitbox.y, npcHitbox.width, npcHitbox.height);
    }

    //Returns the size and position of the AdventureMode.NPC
    public Rectangle getBounds(){
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }


    //The distance an AdventureMode.NPC can see the player from
    public Rectangle getBattleGlareBounds(){

        int d2 = 2,
        width2 = getHitbox().width - d2,
        height2 = getHitbox().height - d2,
        distanceX = battleGlareRange * (width2 / 2),
        distanceY = battleGlareRange * (height2 / 2);


        return new Rectangle(npcHitbox) {{
            switch (directionRG) {
                case RIGHT -> width += distanceX;
                case LEFT -> {
                    x -= distanceX;
                    width += distanceX;
                }
                case FORWARD -> height += distanceY;
                case AWAY -> {
                    y -= distanceY;
                    height += distanceY;
                }
            }
        }};
    }

    void createSprites(String spriteSheetPath) throws IOException {
        BufferedImage img;
        img = ImageIO.read(new File(spriteSheetPath));

        //get sub-images (sprites) from the sprite sheet
        for (int row = 0; row < 4; row++) {
            RocketGruntMaleDirection dir = RocketGruntMaleDirection.values()[row];
            List<BufferedImage> imgList = new ArrayList<>();

            movingImgMap.put(dir, imgList);
            int rY = spriteSheety + row * spriteSheetRowHeight;
            for (int col = 0; col < 4; col++) {
                int rX = spriteSheetx + col * spriteSheetColWidth;
                BufferedImage subImg = img.getSubimage(rX, rY, spriteSheetColWidth, spriteSheetRowHeight);
                if (col == 0) {
                    //subimages in 1st column are standing
                    standingImgMap.put(dir, subImg);
                }
                else {
                    //all others are moving
                    imgList.add(subImg);
                }
            }
        }
    }

    public void tick() {
        if ((canMove || this.caughtPlayerInBattleGlare) && isMoving()) {
            switch (directionRG) {
                case RIGHT:
                    setX((int) (getX() + getDeltaX()));
                    updateTicksFromCellZeroX(1);
                    break;
                case LEFT:
                    setX((int) (getX() - getDeltaX()));
                    updateTicksFromCellZeroX(-1);
                    break;
                case FORWARD:
                    setY((int) (getY() + getDeltaY()));
                    updateTicksFromCellZeroY(1);
                    break;
                case AWAY:
                    setY((int) (getY() - getDeltaY()));
                    updateTicksFromCellZeroY(-1);
            }
            setMovingIndex(getMovingIndex() + 1);
            setMovingIndex(getMovingIndex() % getMaxMovingIndex());
        }
    }
    protected void updateHitbox() {
        //Stop the user being able to jump right in front of the npc while the npc is moving, and then the npc movement
        //thread moves the npc into the user
        npcHitbox.setBounds(getX() + (getWidth()/4), getY() + (getHeight()/2), getWidth()/2, getHeight() - (getHeight()/2));
        if ((canMove || this.caughtPlayerInBattleGlare) && isMoving())
            switch (directionRG) {
                case LEFT:
                    npcHitbox.x = getX();
                    npcHitbox.width = getWidth() - getWidth()/4;
                    break;
                case RIGHT:
                    npcHitbox.width = getWidth() - getWidth()/4;
                    break;
                case FORWARD:
                    npcHitbox.height = getHeight() - getHeight()/4;
                    break;
                case AWAY:
                    npcHitbox.y = getY() + getHeight()/4;
                    npcHitbox.height = getHeight() - getHeight()/4;
            }
        /*else
            npcHitbox.setBounds(getX() + (getWidth()/6), getY() + (getHeight()/4), getWidth()/2, getHeight() - (getHeight()/3));*/
    }

    public void updateStartingCellPosition(Point location) {
        setStartingCellsX(location.x);
        setStartingCellsY(location.y);

    }
    public RocketGruntMaleDirection getDirection() {
        return directionRG;
    }


    @Override
    public boolean collides(Rectangle r) {
       return npcHitbox.intersects(r);
    }


    //Hitbox should be inner class
    protected Rectangle getHitbox() {
        return npcHitbox;
    }
    protected int getHitboxMaxX() {
        return npcHitbox.x + npcHitbox.width;
    }
    protected int getHitboxMaxY() {
        return npcHitbox.y + npcHitbox.height;
    }
    public void setDirection(RocketGruntMaleDirection direction) {
        this.directionRG = direction;
    }

    public void setDistanceFromUser(double distanceFromUser){
        this.distanceFromUser = distanceFromUser;
    }

    public double getDistanceFromUser(){
        return distanceFromUser;
    }


    public void makeNpcTurnAround() {
        int i = 0; int j = -1; int h = -1;
            for(RocketGruntMaleDirection dir: RocketGruntMaleDirection.values()) {
                if (directionRG == dir) {
                    h = i;
                }
                if (h == 0) { j = 3; }
                else if(h == 1) { j = 2; }
                else if(h == 2) { j = 1; }
                else if(h == 3) { j = 0; }

                if (j != -1) {
                    setDirection(RocketGruntMaleDirection.values()[j]);
                    break;
                }
                i++;
            }
    }

    public RocketGruntMaleDirection getStartingDirection() {
        return startingDirection;
    }

    public int getDistanceCanMove() {
        return switch (startingDirection) {
            case LEFT, RIGHT -> cellCountToMoveThrough * getWidth() / 2;
            default -> cellCountToMoveThrough * getHeight() / 2;
        };
    }

    public void setNpcMovementThread() {
        if (npcMovementThread == null || this.npcMovementThread.isTerminated())
            this.npcMovementThread = new NPCMovementThread(this);
    }
}
