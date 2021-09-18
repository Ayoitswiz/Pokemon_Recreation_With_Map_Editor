package AdventureMode;

import gg.Battle.BattleGUI;
import gg.Battle.Trainers.AITrainer;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


import static AdventureMode.AdventureModeUiPanel.*;

@SuppressWarnings("serial")
public class UserSpriteManager extends JPanel {

    public static final int TIMER_DELAY = 50; //Delay on timer for sprite movement
    private MySprite sprite = null; //The users sprite
    private AdventureModeUiPanel ui;//Manages all of the Ui, collisions, and area switching
    private int compNum = /*1295*/ 1844;//The component number we spawn on top of, and is referenced for collision detection and resizing
    private DialogBox dialogBox  = new DialogBox(); //Dialog box for NPC communication
    private ArrayList<NPC> npcsInArea = new ArrayList<>();  //Holds the NPC/ai Trainers in any given area
    private Collision compWeAreIn;
    private StartMenu startMenu;
    ;
    private boolean setPositionOnProgramStart = true;
    private ArrayList<Collision> colls = new ArrayList<>();


    public UserSpriteManager(AdventureModeUiPanel ui, MySprite sprite) {
        //sprite passed in from MainMenuGui just so UIs.I can get the pokemon in the scroolpane at the main menu
        this.sprite = sprite;
        this.sprite.setInAdventureMode(true);
        this.ui = ui;
        sprite.setDirection(SpriteDirection.FORWARD);

        compWeAreIn = getCell(compNum);
        startMenu = new StartMenu(this.sprite, npcsInArea);
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
        Timer timer = new Timer(TIMER_DELAY, new TimerListener());
        timer.start();
        add(dialogBox);

    }

    private void setKeyBindings(SpriteDirection dir, int keyCode) {
        setKeyBindings(keyCode,  0, new MoveAction(dir, false), new MoveAction(dir, true));
    }

    private void setKeyBindings(int keyCode, int modifier) {
        setKeyBindings(keyCode,  modifier, new ControlAction(keyCode));
    }

    private void setKeyBindings(int keyCode, int modifier, AbstractAction... action) {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        KeyStroke keyPressed = KeyStroke.getKeyStroke(keyCode, modifier, false);
        inputMap.put(keyPressed, keyPressed.toString());
        actionMap.put(keyPressed.toString(), action[0]);
        if (action.length > 1) {
            KeyStroke keyReleased = KeyStroke.getKeyStroke(keyCode, 0, true);
            inputMap.put(keyReleased, keyReleased.toString());
            actionMap.put(keyReleased.toString(), action[1]);
        }
    }

    private class ControlAction extends AbstractAction {

        private final int keyCode;

        ControlAction(int keyCode) {
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
                    } else {
                        add(startMenu, 0);
                    }
                    startMenu.setVisible(!startMenu.isVisible());
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

    //Pulled from class in AdventureModeUiPanel
    public void createNPCs(ArrayList<NPC> npcsInArea) {
        this.npcsInArea.clear();
        this.npcsInArea.addAll(npcsInArea);
    }

    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sprite.setSpriteSize();
            if (sprite.isMoving()) {
                //Checking more cells than necessary so UIs.I can move at very high speeds without breaking the program while testing
                //Cells need to be added in this order. From farthest away from the user to closest.
                /*
                 * Switches the players default cell based on direction of movement. There is extra protection here than needed.
                 * The extra protection is only so UIs.I can move at very high speeds like 35px/50ms and still have it work.
                 * It gets the bounds of the cells around the player and if the center point of the user enters one of those cells
                 * It sets the users cell number to it
                 */
                switch (sprite.getDirection()) {
                    case RIGHT -> colls.addAll(getCell(
                            4, -49, 57,
                            3, -50, 56,
                            2, -51, 55,
                            1, -52, 54));
                    case LEFT -> colls.addAll(getCell(
                            -4, 49, -57,
                            -3, 50, -56,
                            -2, 51, -55,
                            -1, 52, -54));
                    case FORWARD -> colls.addAll(getCell(
                            211, 212, 213,
                            158, 159, 160,
                            105, 106, 107,
                            52, 53, 54));
                    case AWAY -> colls.addAll(getCell(
                            -211, -212, -213,
                            -158, -159, -160,
                            -105, -106, -107,
                            -52, -53, -54));
                }
                colls.add(compWeAreIn);
                sprite.tick(colls, compWeAreIn, npcsInArea, ui, dialogBox);
                //Upon warping the user gets set to not moving. The users compNum should not be updated until everything is in place.
                sprite.setDeltas();
                sprite.updateComponentsFromContainerStart();
                sprite.setLocation();
                if (sprite.isMoving()) {
                    sprite.updateHitBoxCenterPoint();
                    compNum = Arrays.asList(ui.getWallAndWarpLayer().getComponents())
                            .indexOf(ui.getWallAndWarpLayer().getComponentAt(sprite.getHitboxCenterPoint()));
                }
                compWeAreIn = getCell(compNum);
            }
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //ImageIcon img = new ImageIcon(ImageIO.read(new File(ui.getPathName())));
        BufferedImage inputFile = null;
        try {
            inputFile = ImageIO.read(new File(ui.getPathName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
/*        for (int x = 0; x < inputFile.getWidth(); x++) {
            for (int y = 0; y < inputFile.getHeight(); y++) {
                int rgba = inputFile.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        Math.max(col.getGreen() - 40, 0),
                        255 - col.getBlue());
                inputFile.setRGB(x, y, col.getRGB());
            }
        }*/
        g.drawImage(new ImageIcon(inputFile).getImage(),
                cellZero.getX(),
                cellZero.getY(),
                cellZero.getWidth() * COLS,
                cellZero.getHeight() * ROWS,
                this);

        g.setColor(new Color(255, 0, 0, 100));
        colls.forEach(c -> g.fillRect(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
        colls.clear();

        if (dialogBox.isVisible()) {
            dialogBox.setBounds(
                    0,
                    getHeight() - (getHeight() / 4),
                    getWidth(),
                    getHeight() / 4);
            if (!dialogBox.isVisible()) {
                dialogBox.setVisible(true);
            }
        }

        startMenu.setBounds(this.getSize());

        g.setColor(new Color(0, 0, 255));
        g.fillRect(compWeAreIn.getX(), compWeAreIn.getY(), compWeAreIn.getWidth(), compWeAreIn.getHeight());
        //This should never occur unless walking off the map. If the user does walk off however this will pull them back in no matter how far into
        //oblivion they are. However best to leave off during testing because it can hide bugs.
        /*        if (!sprite.getUserHitBox().intersects(compWeAreIn.getBounds()) && !sprite.hasOpponent()) {
            setSpriteLocation();
        }*/
        sprite.setSpriteSize();
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

    //If an argument is out of the range of possible components in jp return cell 0.
    @NotNull
    private Collision getCell(Point p) {
        return getCell(p.x, p.y);
    }

    //If an argument is out of the range of possible components in jp return cell 0.
    @NotNull
    private Collision getCell(int x, int y) {
        if (y <= ROWS * cellZero.getHeight() &&
                y >= cellZero.getY() &&
                x >= cellZero.getX() &&
                x <= COLS * cellZero.getWidth())
            return (Collision) ui.getWallAndWarpLayer().getComponentAt(x, y);
        else return (Collision) cellZero;
    }

    //If an argument is out of the range of possible components in jp return cell 0.
    @NotNull
    private Collision getCell(int cellNum) {
        if (cellNum < ui.getWallAndWarpLayer().getComponentCount() && cellNum >= 0)
            return (Collision) ui.getWallAndWarpLayer().getComponent(cellNum);
        else return (Collision) cellZero;
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

    // TODO: 7/31/2020 Move to a NpcHandler class.
    private void NpcHandler(Graphics g) {
        if (npcsInArea != null)
        {
            for (var npc : npcsInArea) {
                npc.setSpriteSize();
                npc.setDeltas();
                npc.updateComponentsFromContainerStart();
                npc.setStartingCellsX(getCell(npc.getStartingCell()).getX());
                npc.setStartingCellsY(getCell(npc.getStartingCell()).getY());
                npc.setLocation();
                npc.updateHitbox();
                //The npc movement thread operates independently of the thread the following code executes on,
                //so the npc's variables need to either be passed in or synchronized so they remain constant
                //throughout the methods execution.
                //NPC MOVING RIGHT
                DetectIfUserIsInNpcPath(npc, RocketGruntMaleDirection.RIGHT,    sprite.getX(),  npc.getMaxX(),      npc.getHitboxMaxX(),    SpriteDirection.LEFT, g);
                //NPC MOVING DOWNWARD ON SCREEN
                DetectIfUserIsInNpcPath(npc, RocketGruntMaleDirection.FORWARD,  sprite.getY(),  npc.getMaxY(),      npc.getHitboxMaxY(),    SpriteDirection.AWAY, g);
                //NPC MOVING LEFT
                DetectIfUserIsInNpcPath(npc, RocketGruntMaleDirection.LEFT,     npc.getX(),     sprite.getMaxX(),   npc.getHitbox().x,      SpriteDirection.RIGHT, g);
                //NPC MOVING UPWARD ON SCREEN
                DetectIfUserIsInNpcPath(npc, RocketGruntMaleDirection.AWAY,     npc.getY(),     sprite.getMaxY(),   npc.getHitbox().y,      SpriteDirection.FORWARD, g);
                npc.draw(g);
            }
        }
    }


    // TODO: 7/31/2020 Move to a NpcHandler class.
    //should be broken into multiple methods
    private void DetectIfUserIsInNpcPath(NPC npc, RocketGruntMaleDirection npcDir, int x, int x2, int hbPos, SpriteDirection spriteDir, Graphics g) {
        if (npc.getDirection() == npcDir) {
            //If the user is within the range to be challenged by an npc looking the users way and if the user does not already have an opponent
            if (npc.getBattleGlareBounds().intersects(sprite.getUserHitBox().getBounds()) && !sprite.hasOpponent() && !npc.isOutOfUsablePokemon()) {

                int i = 0;
                Collision cell = null;
                boolean isInRange = false, stop = false;

                //Detects if there is a wall in between the npc and the user. If a wall is an instance of CantEnter than the npc cannot lock on to the user.
                while (i <= npc.battleGlareRange && !npc.caughtPlayerInBattleGlare) {
                    int dist = switch (npcDir) {
                        case LEFT, AWAY -> -i;
                        default -> i;
                    };

                    cell = switch (npcDir) {
                        case RIGHT, LEFT -> getCell(dist * npc.getWidth() / 2 + hbPos, npc.getStartingCellsY());
                        case FORWARD, AWAY -> getCell(npc.getStartingCellsX(), dist * npc.getHeight() / 2 + hbPos);
                    };

                    isInRange = switch (npcDir) {
                        case RIGHT -> sprite.getHitboxMaxX() - 2 < cell.getMaxX();
                        case LEFT -> sprite.getUserHitBox().getX() + 2 > cell.getMaxX();
                        case FORWARD -> sprite.getHitboxMaxY() - 2 < cell.getY();
                        case AWAY -> sprite.getUserHitBox().getY() + 2 > cell.getY();
                    };
                    g.setColor(Color.RED);
                    g.fillRect(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                    if ((cell.isWall() || cell.isWarp()) && !isInRange) break;
                    npc.caughtPlayerInBattleGlare = isInRange;
                    //npc.caughtPlayerInBattleGlare = !stop && isInRange;
                    i++;
                }
                //Should test what happens when running away from npc when detected
                if (npc.caughtPlayerInBattleGlare) {
                    sprite.setOpponent(npc);
                    switch (npc.directionRG) {
                        case LEFT, RIGHT -> {
                            sprite.setY(npc.getY());
                            sprite.setTicksFromCellZeroBasedOnOtherSprite(sprite.getLocation());
                        }
                        case FORWARD, AWAY -> {
                            sprite.setX(npc.getX());
                            sprite.setTicksFromCellZeroBasedOnOtherSprite(sprite.getLocation());
                        }
                    }

                    sprite.updateComponentsFromContainerStart();
                    sprite.setMoving(false);
                    sprite.setDirection(spriteDir);
                    sprite.updateHitBoxCenterPoint();
                    compWeAreIn = getCell(compNum = Arrays.asList(ui.getWallAndWarpLayer().getComponents()).indexOf(getCell(sprite.getHitboxCenterPoint())));

                    dialogBox
                      .open()
                      .setDialog(npc.getName() + " wants to battle!")
                      .cycleDialog(npc.preBattleDialog)
                      .onDialogEnd(() -> {
                          AITrainer.canMove = false;
                          new BattleGUI(sprite, npc)
                            .onBattleGUIClose(() -> {
                                AITrainer.canMove = true;
                                remove(dialogBox);
                                dialogBox = new DialogBox();
                                add(dialogBox);
                            });
                      });
                }
            }
            //updates NPC's distance from user if the npc has caught the user in their battle glare
            //the distance between npc and user is then subtracted by the npc's relative delta value in
            //the class NPCMovementThread.
            if (npc.caughtPlayerInBattleGlare)
                npc.setDistanceFromUser(x - (x2));
        }
    }

    //INNER CLASS for controlling when user can & cannot move.
    private class MoveAction extends AbstractAction {
        private final SpriteDirection dir;
        private final boolean released;
        private boolean pressed = false;
        public MoveAction(SpriteDirection dir, boolean released) {
            this.dir = dir;
            this.released = released;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("released == " + released);

            if (!released)
                pressed = true;

            boolean keepgoing = !released;
            /*
            If user holds a key, then holds another key (so 2 keys are held at same time)
            This makes it so when the user releases the 1st key, the user automatically starts moving in the
            2nd keys direction with no delay. Without this code, you would have to release each key before pressing
            another. That does not make for smooth movement!!! The user would stop moving after releasing the
            first key. They would then wonder why the second key their holding isn't making them move!
             */
            if (released) {
                sprite.setMoving(false);
                for (KeyStroke ks : getInputMap(WHEN_IN_FOCUSED_WINDOW).keys()) {
                    //Eliminate the key bindings with the modifiers: ctrl, alt, shift.
                    if (ks.getModifiers() == 0) {
                        //Looking only for the keycodes that control the users movement
                        switch (ks.getKeyCode()) {
                            case KeyEvent.VK_W:
                            case KeyEvent.VK_A:
                            case KeyEvent.VK_S:
                            case KeyEvent.VK_D:
                                //if doesn't work can do getActionForKeyStroke.dir == dir && !ks.isOnKeyRelease
                                //When a key is released set that key's <class> MoveAction </class> <attribute> pressed </attribute> to false because it is no longer being pressed.
                                if (!ks.isOnKeyRelease()) {
                                    if (/*getActionForKeyStroke(ks) == this*/ ((MoveAction) getActionForKeyStroke(ks)).dir == dir) {
                                        ((MoveAction) getActionForKeyStroke(ks)).pressed = false;
                                        System.out.println(ks + ": no longer pressed");
                                    }
                                    if (((MoveAction) getActionForKeyStroke(ks)).pressed && ((MoveAction) getActionForKeyStroke(ks)).dir != dir) {
                                        if (!sprite.hasOpponent() && !startMenu.isVisible()) {
                                            sprite.setMoving(true);
                                            sprite.setDirection(((MoveAction) getActionForKeyStroke(ks)).dir);
                                            System.out.println(ks + ": is still pressed! Setting direction to " + ((MoveAction) getActionForKeyStroke(ks)).dir);
                                            keepgoing = true;
                                        }
                                    }
                                }
                        }
                    }
                }
            } else if (!released) {
                if (sprite.hasOpponent()) {
                    sprite.setMoving(false);
                } else if (startMenu.isVisible()) {
                    sprite.setMoving(false);
                } else {
                    sprite.setMoving(true);
                    sprite.setDirection(dir);
                }
            }
        }
    }

    private void setSpriteLocation() {
        sprite.setDeltas();
        sprite.updateHitBox();
        sprite.setStepsFromCellZeroBasedOnComponent(compWeAreIn.getLocation());
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
        //the movement key is no longer being pressed so set moving to false when warping
        sprite.setMoving(false);
    }

}
