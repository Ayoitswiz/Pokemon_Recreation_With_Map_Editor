package AdventureMode;

import MainMenu.HumanTrainer;
import MainMenu.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.List;

public class MySprite extends HumanTrainer {
    private static final String SPRITE_SHEET_PATH = "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/0f4bb20b-5f4e-4e5d-b500-9e4ba283a4e6/d3479l2-6d1e2d2c-b52e-4c08-9bd0-b8b901019da1.gif?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwic3ViIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsImF1ZCI6WyJ1cm46c2VydmljZTpmaWxlLmRvd25sb2FkIl0sIm9iaiI6W1t7InBhdGgiOiIvZi8wZjRiYjIwYi01ZjRlLTRlNWQtYjUwMC05ZTRiYTI4M2E0ZTYvZDM0NzlsMi02ZDFlMmQyYy1iNTJlLTRjMDgtOWJkMC1iOGI5MDEwMTlkYTEuZ2lmIn1dXX0.AB1sz7QxFx_-cEeZ_4FW_vizMxHugTLoVyM1brnM-NU";
    private Map<SpriteDirection, Image> standingImgMap = new EnumMap<>(SpriteDirection.class);
    private Map<SpriteDirection, List<Image>> movingImgMap = new EnumMap<>(SpriteDirection.class);
    private Rectangle userHitBox = new Rectangle(); //User Hit Box
    private Point hitboxCenterPoint = new Point(); // Human Trainer Center point of hit box
    private SpriteDirection directionWhenWarping;


    public MySprite(BigDecimal money, String name) throws IOException {
        this.items = new HashMap<>();
        this.money = money;
        setName(name);
        maxMovingIndex = 4;
        setDeltaX(25);
        setDeltaY(15);
        createSprites();
        updateHitBox();
    }

    public void draw(Graphics g) {
        Image img = null;
        if (!this.moving) {
            img = standingImgMap.get(getDirection());
        } else {
            img = movingImgMap.get(getDirection()).get(movingIndex);
        }
        g.setColor(new Color(255, 0, 0, 100));
        g.fillRect(x, y, getWidth(), getHeight());
        g.drawImage(img, x, y, getWidth(), getHeight(), null);
        g.setColor(new Color(0, 255, 0, 160));
        g.fillRect(userHitBox.x, userHitBox.y, userHitBox.width, userHitBox.height);
        g.setColor(new Color(0, 0, 0));
        g.drawLine(hitboxCenterPoint.x, hitboxCenterPoint.y, hitboxCenterPoint.x + getWidth(), hitboxCenterPoint.y);
        updateHitBox();
    }

    private void createSprites() throws IOException {
        URL spriteSheetUrl = new URL(SPRITE_SHEET_PATH);
        BufferedImage img = ImageIO.read(spriteSheetUrl);

        // get sub-images (sprites) from the sprite sheet
        int x0 = 0;
        int y0 = 64;
        int rW = 32;
        int rH = 32;
        for (int row = 0; row < 4; row++) {
            SpriteDirection dir = SpriteDirection.values()[row];
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

    public void tick(ArrayList<Collision> cols, Collision compWeAreIn, ArrayList<NPC> npcsInArea, AdventureModeUiPanel ui){
        Rectangle npc;
        Collision collision;
        double subFromDelta = 0, subFromDelta2 = 0;
        //problem is the x, y pos are set in two different ways. when setting with the normal x+ x- way it presumably works great
        //however the x position changes if the ticksFromCellZero changes.
        double[] changeInPos;
        if (moving) {
            switch (direction) {
                case RIGHT:
                    //adding an additional one pixel because at very low speeds of less than a pixel a tick, the delta value is so low
                    //the extension onto the hitbox isn't enough to actually make it expend by even a pixel so the user will actually need to collide
                    //with an object in order for a collision to be detected. The goal is to stop the user before he collides with an object
                    //not after.
                    //The + 1 of in the direction of movement stops user from intersecting with npc's, and is then subtracted for
                    //collisions
                    userHitBox.width += (getDeltaX() + 1);
                    npc = collisionDetectionNpc(npcsInArea);
                    if (npc != null)
                        subFromDelta = (getHitboxMaxX()) - npc.x;
                    //userHitBox.width -= 1;
                    //The change in size to the hitboxes height/y when we are moving horizontally is so that the
                    //The same changes are made to the hitboxes width when moving vertically, only we change the width/x instead
                    //user can more easily maneuver tight spaces
                    userHitBox.y += 1;
                    userHitBox.height -= 2;
                    collision = collisionDetection(cols, compWeAreIn, ui);
                    if (collision != null) {
                        subFromDelta2 = getHitboxMaxX() - collision.getX();
                    }
                    changeInPos = subFromDelta(subFromDelta, subFromDelta2, collision, compWeAreIn, getDeltaX());
                    if (changeInPos[1] > 0)
                        updateTicksFromCellZeroX(changeInPos[1]);
                    break;
                case LEFT:
                    userHitBox.x -= (getDeltaX() + 1);
                    userHitBox.width += getDeltaX();
                    npc = collisionDetectionNpc(npcsInArea);
                    if (npc != null) {
                        subFromDelta = npc.getMaxX() - (userHitBox.x);
                    }
                    //userHitBox.x += 1;
                    userHitBox.y += 1;
                    userHitBox.height -= 2;
                    collision = collisionDetection(cols, compWeAreIn, ui);
                    if (collision != null)
                        subFromDelta2 = collision.getMaxX() - (userHitBox.x);
                    changeInPos = subFromDelta(subFromDelta, subFromDelta2, collision, compWeAreIn, getDeltaX());
                    if (changeInPos[1] > 0)
                        updateTicksFromCellZeroX(-changeInPos[1]);
                    break;
                case FORWARD:
                    userHitBox.height += (getDeltaY() + 1);
                    npc = collisionDetectionNpc(npcsInArea);
                    if(npc != null) {
                        subFromDelta = (getHitboxMaxY()) - npc.y;
                    }
                    //userHitBox.height -= 1;
                    userHitBox.x += 1;
                    userHitBox.width -= 2;
                    collision = collisionDetection(cols, compWeAreIn, ui);
                    if (collision != null) {
                        subFromDelta2 = (getHitboxMaxY()) - collision.getY();
                    }
                    changeInPos = subFromDelta(subFromDelta, subFromDelta2, collision, compWeAreIn, getDeltaY());
                    if (changeInPos[1] > 0) {
                        updateTicksFromCellZeroY(changeInPos[1]);
                    }
                    break;
                case AWAY:
                    userHitBox.y -= (getDeltaY() + 1);
                    userHitBox.height += getDeltaY();
                    npc = collisionDetectionNpc(npcsInArea);
                    if (npc != null) {
                        subFromDelta = npc.getMaxY() - (userHitBox.y);
                    }
                    //userHitBox.y += 1;
                    userHitBox.x += 1;
                    userHitBox.width -= 2;
                    collision = collisionDetection(cols, compWeAreIn, ui);
                    if (collision != null)
                        subFromDelta2 = collision.getMaxY() - (userHitBox.y);

                    changeInPos = subFromDelta(subFromDelta, subFromDelta2, collision, compWeAreIn, getDeltaY());
                    if (changeInPos[1] > 0)
                        updateTicksFromCellZeroY(-changeInPos[1]);
                    break;
                default: throw new IllegalStateException("Unexpected value: " + direction);
            }
        }
        movingIndex++;
        movingIndex %= maxMovingIndex;
    }

    /*
    When the user warps their direction will equal directionWhenWarping and the
    direction when warping check is to make sure our Pos and ticks from cell zero do not change because the users position
    needs to be set based on their new destinations cell and cannot be effected by the movement through the cell they were in when they warped,
    this is strictly for a scenario for when the compWeAreIn is a warp
    Everything else is handled as a collision except for again, when the compWeAreIn is a warp we are allowed to move through other connected
    warp cells
    */
    /*collision == null is for situations when the user does not collide with a cell that's an instance of CantEnter, or warp when  */
    private double[] subFromDelta(double subFromDelta, double subFromDelta2, Collision collision, Collision compWeAreIn, double delta) {
        subFromDelta = Math.max(subFromDelta, subFromDelta2);
        if (collision == null || compWeAreIn.isWarp() && directionWhenWarping != direction) {
            return a(subFromDelta, delta);
        } else {
            if (!collision.isWarp()) {
                return a(subFromDelta, delta);
            }
        }
        return new double[]{0, 0};
    }
    private double[] a(double subFromDelta, double delta) {
        return new double[]{(delta - subFromDelta), 1 - subFromDelta / delta};
    }

    private Rectangle collisionDetectionNpc(ArrayList<NPC> npcs) {
        for (NPC npc: npcs){
            if (npc.collides(userHitBox.getBounds())) {
                return npc.getHitbox().getBounds();
            }
        }
        return null;
    }

    private Collision collisionDetection(ArrayList<Collision> cols, Collision compWeAreIn, AdventureModeUiPanel ui) {
        //When the user warps, he is placed inside another warp cell. This block allows the user to move
        //in any direction on that warp without activating it. The warp will only be activated when the user moves in the
        //opposite direction of the direction he was moving when he warped.
        int i = 0; int j = -1; int h = -1;
        if (compWeAreIn.isWarp()) {
            for(SpriteDirection dir: SpriteDirection.values()) {
                if (directionWhenWarping == dir) {
                    h = i;
                }
                if (h == 0) { j = 2; }
                else if(h == 1) { j = 3; }
                else if(h == 2) { j = 0; }
                else if(h == 3) { j = 1; }

                if (j != -1) {
                    if (SpriteDirection.values()[j].equals(direction)) {
                        ui.Enter((Warp) compWeAreIn);
                        directionWhenWarping = direction;
                        return compWeAreIn;
                    }
                }
                i++;
            }
        }

        if (compWeAreIn.isGrassBlock()) {
            Grass g = (Grass) compWeAreIn;
            g.determineIfPokemonIsThere(this);
        }

        //Handles whether the component we are intersecting is a cell we can enter, can't enter, or warp.
        //When moving at very high speeds the user can jump over entire component lengths without checking for collisions
        //to stop that from happening I'm checking two component lengths in front of the user.
        //The array always gets executed all the way through. The order of the array goes from cells farthest away to
        //closest, ending with compWeAreIn. This is because when the user collides with an non-enterable cell their position
        //gets set right outside the edge of that cell. Because of this if the cell farthest away was returned the
        //users position would be based on that, and cause the user to jump forward a cell even if theirs a cant enter cell there


        Collision colToQuery = null;
        for(Collision col: cols) {
            if (!col.moveInto() && col.collides(userHitBox.getBounds())) {
                //There is a col is compWeAreIn. compWeAreIn is passed in individually and along with the arrayList
                if (col.isWarp() && !compWeAreIn.isWarp()) {
                    directionWhenWarping = direction;
                    ui.Enter((Warp) col);
                    return col;
                }
                //Collisions with cells the user cannot enter (and are not warps) takes priority over being queried.
                //Meaning if the user collides with a warp and a wall at the same time, the wall will be the one to be queried.
                //That is why this if statement is last in the loop.
                if (!col.isWarp() && !col.moveInto()) { //if comp is a wall essentially
                    colToQuery = col;
                }
            }
        }
        return colToQuery;
    }

    public SpriteDirection getDirection() {
        return direction;
    }

    /**Updates usersHitBox location & size,
     * The users hitbox always starts halfway down the sprites height
     */
    public void updateHitBox() {
        userHitBox.setBounds(getX() + (getWidth()/4), getY() + (getHeight()/2), getWidth()/2, getHeight()/2);
    }
    protected int getHitboxMaxX() {
        return userHitBox.x + userHitBox.width;
    }

    protected int getHitboxMaxY() {
        return userHitBox.y + userHitBox.height;
    }

    public void updateHitBoxCenterPoint() {
        hitboxCenterPoint.setLocation(getX() + (getWidth()/2),getY() + (getHeight() - (getHeight()/4)));
    }

    public Point getHitboxCenterPoint() {
        return hitboxCenterPoint;
    }

    protected Rectangle getUserHitBox() {
        return userHitBox;
    }

    public void setDirection(SpriteDirection direction) {
        this.direction = direction;
    }

}