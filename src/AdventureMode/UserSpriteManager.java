package AdventureMode;

import MainMenu.GUIManager;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static AdventureMode.AdventureModeUiPanel.*;

@SuppressWarnings("serial")
public class UserSpriteManager extends JPanel {

    private SpriteDirection spriteDirection = SpriteDirection.RIGHT; //Enum to control player direction
    public static final int TIMER_DELAY = 50; //Delay on timer for sprite movement
    private MySprite sprite = null; //The users sprite
    private AdventureModeUiPanel ui;//Manages all of the Ui, collisions, and area switching
    private int compNum = /*1295*/ 1844;//The component number we spawn on top of, and is referenced for collision detection and resizing
    private Timer timer = null; //Timer for
    private boolean stopPlayerMovement = false; //A control to stop the players movement when he gets stopped by an AiTrainer
    private DialogBox dialogBox; //Dialog box for NPC communication
    private ArrayList<NPC> npcsInArea = new ArrayList<>();  //Holds the NPC/ai Trainers in any given area
    private Collision compWeAreIn;
    private StartMenu startMenu;
    private GUIManager gui;
    private boolean setPositionOnProgramStart = true;
    private ArrayList<Collision> colls = new ArrayList<>();

    public UserSpriteManager(AdventureModeUiPanel ui, MySprite sprite, GUIManager gui) {
        //sprite passed in from MainMenuGui just so I can get the pokemon in the scroolpane at the main menu
        this.sprite = sprite;
        this.sprite.setInAdventureMode(true);
        this.ui = ui;
        sprite.setDirection(spriteDirection);
        this.gui = gui;
        compWeAreIn = getCell(compNum);
        sprite.setLocationRelativeTo(getCell(0));
        startMenu = new StartMenu(this.sprite, this.gui, npcsInArea);
        setBackground(Color.GREEN);
        setDoubleBuffered(true);
        //sprite size updates late
        //hitbox location updates late when warping to new area
        setKeyBindings(SpriteDirection.LEFT, KeyEvent.VK_A);
        setKeyBindings(SpriteDirection.RIGHT, KeyEvent.VK_D);
        setKeyBindings(SpriteDirection.FORWARD, KeyEvent.VK_S);
        setKeyBindings(SpriteDirection.AWAY, KeyEvent.VK_W);
        setKeyBindings(KeyEvent.VK_M, 0);
        setKeyBindings(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK);
        setKeyBindings(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK);
        setKeyBindings(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK);
        //Start timer to help control user movement
        timer = new Timer(TIMER_DELAY, new TimerListener());
        timer.start();
    }

    private void setKeyBindings(int keyCode, int modifier) {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        KeyStroke keyPressed = KeyStroke.getKeyStroke(keyCode, modifier, false);

        inputMap.put(keyPressed, keyPressed.toString());
        actionMap.put(keyPressed.toString(), new ControlAction(keyCode));
    }
    private class ControlAction extends AbstractAction {
        private int keyCode;
        public ControlAction(int keyCode) {
            this.keyCode = keyCode;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (keyCode) {
                case KeyEvent.VK_I:
                    sprite.setSpeedWeight(sprite.getSpeedWeight() + 10);
                    System.out.println("SpeedWeight:" + sprite.getSpeedWeight());
                    break;
                case KeyEvent.VK_D:
                    if (sprite.getSpeedWeight() > 10)
                    sprite.setSpeedWeight(sprite.getSpeedWeight() - 10);
                    System.out.println("SpeedWeight:" + sprite.getSpeedWeight());
                    break;
                case KeyEvent.VK_M:
                    sprite.setMoving(false);
                    if (startMenu.isVisible()) {
                        remove(startMenu);
                        startMenu.setVisible(false);
                    } else {
                        add(startMenu, 0);
                        startMenu.setVisible(true);
                    }
                    break;
                case KeyEvent.VK_E:
                    EDIT_MODE = !EDIT_MODE;
                    ui.Enter(ui.getCurrentArea());
            }
            sprite.setDeltas();
            sprite.setTicksFromCellZeroBasedOnOtherSprite(sprite.getLocation());
            sprite.updateComponentsFromContainerStart();
        }
    }
    private void setKeyBindings(SpriteDirection dir, int keyCode) {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        KeyStroke keyPressed = KeyStroke.getKeyStroke(keyCode, 0, false);
        KeyStroke keyReleased = KeyStroke.getKeyStroke(keyCode, 0, true);

        inputMap.put(keyPressed, keyPressed.toString());
        inputMap.put(keyReleased, keyReleased.toString());
        actionMap.put(keyPressed.toString(), new MoveAction(dir, false));
        actionMap.put(keyReleased.toString(), new MoveAction(dir, true));
    }
    //Pulled from class in AdventureModeUiPanel
    public void createNPCs(ArrayList<NPC> npcsInArea) {
        this.npcsInArea.clear();
        this.npcsInArea.addAll(npcsInArea);
    }

    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sprite.setSpriteSize(getCell(0).getSize());
            if (sprite.isMoving()) {
                switch (sprite.getDirection()) {
                    /*NEED TO SWITCH TO USING X, Y TO GET CELLS. IF YOU JUST WARPED AND THERE IS A WARP ON THE OTHER SIDE OF SCREEN YOU CAN HIT THAT WARP!*/
                    //Checking more cells than necessary so I can move at very high speeds without breaking the program while testing
                    //Cells need to be added in this order. From farthest away to closest.
                    case RIGHT:
                        colls.addAll(getCell(
                                4, -49, 57,
                                         3, -50, 56,
                                         2, -51, 55,
                                         1, -52, 54));
                        break;
                    case LEFT:
                        colls.addAll(getCell(
                                -4, 49, -57,
                                         -3, 50, -56,
                                         -2, 51, -55,
                                         -1, 52, -54));
                        break;
                    case FORWARD:
                        colls.addAll(getCell(
                                211, 212, 213,
                                         158, 159, 160,
                                         105, 106, 107,
                                         52, 53, 54));
                        break;
                    case AWAY:
                        colls.addAll(getCell(
                                -211, -212, -213,
                                         -158, -159, -160,
                                         -105, -106, -107,
                                         -52, -53, -54));
                        break;
                }
                colls.add(compWeAreIn);
                sprite.tick(colls, compWeAreIn, npcsInArea, ui);
                //Upon warping the user gets set to not moving. The users compNum should not be updated until everything is in place.
                sprite.setDeltas();
                sprite.updateComponentsFromContainerStart();
                sprite.setLocation();
                if (sprite.isMoving()) {
                    sprite.updateHitBoxCenterPoint();
                    compNum = Arrays.asList(ui.getWallAndWarpLayer().getComponents()).indexOf(ui.getWallAndWarpLayer().getComponentAt(sprite.getHitboxCenterPoint()));
                }
                compWeAreIn = getCell(compNum);
            }
            playerIsInBattle();
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
                ImageIcon img = new ImageIcon(ImageIO.read(new File(ui.getPathName())));
                g.drawImage(img.getImage(), getCell(0).getX(), getCell(0).getY(), getCell(0).getWidth() * COLS, getCell(0).getHeight() * ROWS, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        g.setColor(new Color(255, 0, 0, 100));
        colls.forEach(c -> g.fillRect(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
        colls.clear();
        if (dialogBox != null) {
            dialogBox.setBounds(0, getHeight() - (getHeight() / 4), getWidth(), getHeight() / 4);
            if (!dialogBox.isVisible()) {
                dialogBox.setVisible(true);
            }
        }
        startMenu.setBounds(getWidth() - (getWidth() / 4), getHeight() / 16, getWidth() / 4, getHeight() - (getHeight() / 8));
        g.setColor(new Color(0, 0, 255));
        g.fillRect(compWeAreIn.getX(), compWeAreIn.getY(), compWeAreIn.getWidth(), compWeAreIn.getHeight());
        //This should never occur unless walking off the map. If the user does walk off however this will pull them back in no matter how far into
        //oblivion they are. However best to leave off during testing because it can hide bugs.
        /*        if (!sprite.getUserHitBox().intersects(compWeAreIn.getBounds()) && !sprite.hasOpponent()) {
            setSpriteLocation();
        }*/
        sprite.setLocationRelativeTo(getCell(0));
        sprite.setSpriteSize(getCell(0).getSize());
        if (setPositionOnProgramStart) {
            setSpriteLocation();
            if (sprite.getX() != 0) {
                setPositionOnProgramStart = false;
            }
        }
        sprite.setDeltas();
        sprite.updateComponentsFromContainerStart();
        sprite.setLocation();
        sprite.updateHitBoxCenterPoint();
        compWeAreIn = getCell(compNum);
        sprite.draw(g);
        NpcHandler(g);
    }

    private void playerIsInBattle() {
        if (sprite.hasOpponent()) {
            if (sprite.getNpcOpponent() != null) {
                if (sprite.getNpcOpponent().isDefeated()) {
                    ui.setVisible(true);
                    sprite.setOpponent(null);
                    dialogBox = null;
                    ui.getCurrentArea().startNpcMovementThreads();
                }
            } else {
                if (sprite.getPokemonOpponent().isFainted()) {
                    ui.setVisible(true);
                    sprite.setOpponent(null);
                    dialogBox = null;
                }
            }
        }
    }

    //If an argument is out of the range of possible components in jp return cell 0.
    @NotNull
    private Collision getCell(Point p) {
        if (p.y <= ROWS * getCell(0).getHeight() &&
            p.y >= getCell(0).getY() &&
            p.x >= getCell(0).getX() &&
            p.x <= COLS * getCell(0).getWidth())
            return (Collision) ui.getWallAndWarpLayer().getComponentAt(p.x, p.y);
        else return getCell(0);

    }

    //If an argument is out of the range of possible components in jp return cell 0.
    @NotNull
    private Collision getCell(int x, int y) {
        if (y <= ROWS * getCell(0).getHeight() &&
            y >= getCell(0).getY() &&
            x >= getCell(0).getX() &&
            x <= COLS * getCell(0).getWidth())
            return (Collision) ui.getWallAndWarpLayer().getComponentAt(x, y);
        else return getCell(0);

    }

    //If an argument is out of the range of possible components in jp return cell 0.
    @NotNull
    private Collision getCell(int cellNum) {
        if (cellNum < ui.getWallAndWarpLayer().getComponentCount() && cellNum >= 0)
            return (Collision) ui.getWallAndWarpLayer().getComponent(cellNum);
        else return getCell(0);

    }

    //If an argument is out of the range of possible components in jp return cell 0.
    private ArrayList<Collision> getCell(int...cellNum) {
        var collisions = new ArrayList<Collision>();
        for (int value : cellNum) {
            value += compNum;
            if (value < ui.getWallAndWarpLayer().getComponentCount() && value >= 0)
                collisions.add((Collision) ui.getWallAndWarpLayer().getComponent(value));
        }
        return collisions;
    }

    private void NpcHandler(Graphics g) {
        if (npcsInArea != null) {
            for (NPC npc : npcsInArea) {
                npc.setSpriteSize(getCell(0).getSize());
                npc.setLocationRelativeTo(getCell(0));
                npc.setDeltas();
                npc.updateComponentsFromContainerStart();
                npc.setStartingCellsX(getCell(npc.startingCell).getX());
                npc.setStartingCellsY(getCell(npc.startingCell).getY());
                npc.setLocation();
                npc.updateHitbox();
                //The npc movement thread operates independently of the thread the following code executes on,
                //so the npc's variables need to either be passed in or synchronized so they remain constant
                //throughout the methods execution.
                //NPC MOVING RIGHT
                DetectIfUserIsInNpcPath(npc, RocketGruntMaleDirection.RIGHT, compWeAreIn.getWidth(), sprite.getX(), npc.getMaxX(), npc.getHitboxMaxX(), SpriteDirection.LEFT, g);
                //NPC MOVING LEFT
                DetectIfUserIsInNpcPath(npc, RocketGruntMaleDirection.LEFT, compWeAreIn.getWidth(), npc.getX(), sprite.getMaxX(), npc.getHitbox().x, SpriteDirection.RIGHT, g);
                //NPC MOVING DOWNWARD ON SCREEN
                DetectIfUserIsInNpcPath(npc, RocketGruntMaleDirection.FORWARD, compWeAreIn.getHeight(), sprite.getY(), npc.getMaxY(), npc.getHitboxMaxY(), SpriteDirection.AWAY, g);
                //NPC MOVING UPWARD ON SCREEN
                DetectIfUserIsInNpcPath(npc, RocketGruntMaleDirection.AWAY, compWeAreIn.getHeight(), npc.getY(), sprite.getMaxY(), npc.getHitbox().y, SpriteDirection.FORWARD, g);
                npc.draw(g);
            }
        }
    }

    //should be broken into multiple methods
    private void DetectIfUserIsInNpcPath(NPC npc, RocketGruntMaleDirection npcDir, int cellDimension, int x, int x2, int hbPos, SpriteDirection spriteDir, Graphics g) {
        if (npc.getDirection() == npcDir) {
            npc.distanceCanMove = npc.cellCountToMoveThrough * cellDimension;

            //If the user is within the range to be challenged by an npc looking the users way and if the user does not already have an opponent
            if (npc.getBattleGlareBounds(npc.battleGlareRange * cellDimension).intersects(sprite.getUserHitBox().getBounds()) && !sprite.hasOpponent() && !npc.isDefeated()) {
                //Detects if there is a wall in between the npc and the user. If a wall is an instance of CantEnter than the npc cannot lock on to the user.
                int i = 0;
                Collision cell = null;
                //find way to combine them into a method or shrink code down cause it looks nasty
                //make sure the compNum update works properly when detected
                boolean isInRange = false;
                while (i <= npc.battleGlareRange) {
                    int dist = i * cellDimension;
                    switch (npcDir) {
                        case RIGHT:
                            cell = getCell(hbPos + dist, (int) getCell(npc.startingCell).getBounds().getCenterY());
                            if (sprite.getUserHitBox().getMaxX() - 2 < cell.getMaxX()) {
                                isInRange = true;
                            }
                            break;
                        case LEFT:
                            cell = getCell(hbPos - dist, (int) getCell(npc.startingCell).getBounds().getCenterY());
                            if (sprite.getUserHitBox().getX() + 2 > cell.getBounds().getMaxX()) {
                                isInRange = true;
                            }
                            break;
                        case FORWARD:
                            cell = getCell(npc.getStartingCellsX(), hbPos + dist);
                            if (sprite.getUserHitBox().getMaxY() - 2 < cell.getY()) {
                                isInRange = true;
                            }
                            break;
                        case AWAY:
                            cell = getCell(npc.getStartingCellsX(), hbPos - dist);
                            if (sprite.getUserHitBox().getY() + 2 > cell.getY()) {
                                isInRange = true;
                            }
                    }
                    g.setColor(Color.RED);
                    g.fillRect(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());

                    if (!cell.moveInto() && !isInRange) {
                        break;
                    } else if (isInRange) {
                        npc.caughtPlayerInBattleGlare = true;
                        break;
                    }
                    i++;
                }
                //Should test what happens when running away from npc when detected
                if (npc.caughtPlayerInBattleGlare) {
                    for (NPC npcs : npcsInArea) {
                        if (npcs != npc)
                        npcs.setMovementThreadOn(false);
                    }
                    AiDetectedUser(npc);
                    switch (npc.directionRG) {
                        case LEFT:
                        case RIGHT:
                            sprite.setY(npc.getY());
                            sprite.setTicksFromCellZeroBasedOnOtherSprite(sprite.getLocation());
                            break;
                        case FORWARD:
                        case AWAY:
                            sprite.setX(npc.getX());
                            sprite.setTicksFromCellZeroBasedOnOtherSprite(sprite.getLocation());
                    }
                    sprite.updateComponentsFromContainerStart();
                    sprite.setMoving(false);
                    sprite.setDirection(spriteDir);
                    sprite.updateHitBoxCenterPoint();
                    compWeAreIn = getCell(compNum = Arrays.asList(ui.getWallAndWarpLayer().getComponents()).indexOf(getCell(sprite.getHitboxCenterPoint())));
                }
            }
            //updates NPC's distance from user if the npc has caught the user in their battle glare
            //the distance between npc and user is then subtracted by the npc's relative delta value in
            //the class NPCMovementThread.
            if (npc.caughtPlayerInBattleGlare)
                npc.setDistanceFromUser(x - (x2));
        }
    }

    private void AiDetectedUser(NPC npc) {
        stopPlayerMovement = true;
        sprite.setOpponent(npc);
        openDialogBox();
    }

    private void openDialogBox() {
        dialogBox = new DialogBox(sprite, gui);
        add(dialogBox);
    }

    /**
     * Switches the players default cell based on direction of movement. There is extra protection here than needed.
     * The extra protection is only so I can move at very high speeds like 35px/50ms and still have it work.
     * It gets the bounds of the cells around the player and if the center point of the user enters one of those cells
     * It sets the users cell number to it
     */

    //INNER CLASS for controlling when user can & cannot move.
    private class MoveAction extends AbstractAction {
        private SpriteDirection dir;
        private boolean released;

        public MoveAction(SpriteDirection dir, boolean released) {
            this.dir = dir;
            this.released = released;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (!sprite.hasOpponent()) {
                stopPlayerMovement = false;
            }

            if (released || stopPlayerMovement || startMenu.isVisible() || sprite.hasOpponent()) {
                sprite.setMoving(false);
            } else {
                sprite.setMoving(true);
                sprite.setDirection(dir);
            }
        }
    }

    private void setSpriteLocation() {
        sprite.setDeltas();
        sprite.updateHitBox();
        sprite.setTicksFromCellZeroBasedOnComponent(compWeAreIn.getLocation());
        sprite.updateComponentsFromContainerStart();
        sprite.setLocation();
        sprite.getHitboxCenterPoint().setLocation(compWeAreIn.getBounds().getCenterX(), compWeAreIn.getBounds().getCenterY());
    }

    /**
     * Set component we are in and call <code> setSpriteLocation </code> to update the sprites location
     * @param cellNum The cell number that the user will be sent to.
     */
    protected void setCompWeAreIn(int cellNum) {
        compNum = cellNum;
        compWeAreIn = getCell(cellNum);
        setSpriteLocation();
        //when spam resizing, when warping, if user was moving during that, the user will continue to move even though
        //the movement key is no longer being pressed
        sprite.setMoving(false);
    }
}