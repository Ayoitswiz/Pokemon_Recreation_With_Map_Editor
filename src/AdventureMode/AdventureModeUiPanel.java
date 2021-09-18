package AdventureMode;

import gg.Battle.Trainers.AITrainer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import static gg.Pokemon.ePokemon.*;
public class AdventureModeUiPanel extends JPanel {
    private GridBagConstraints c = new GridBagConstraints();
    //need getter for jp at some point
    JLayeredPane jp;
    public static Component cellZero;
    public static boolean EDIT_MODE = false; //When set to true, cells are turned to buttons and cells of different types are painted with a unique color
    /**
     * When EDIT_MODE is set to true, and so is OPEN_OUTPUT_STREAMS, any cells clicked will be saved to a txt file
     * When the output streams are open the output file will be overwritten.
     */
    public static final boolean OPEN_OUTPUT_STREAMS = true;
    public static final boolean TESTER_CLASS = false;
    private String pathName;
    public static final int ROWS = 55;
    public static final int COLS = 53;
    private UserSpriteManager userSpriteManager;
    private FileOutputStream out;
    public ArrayList<Integer> CELLS_THAT_CANNOT_BE_ENTERED;
    public ArrayList<Integer> grassCellsInArea = new ArrayList<>();
    private ArrayList<NPC> npcs;

    private PewterCity pewterCity;
    private Route3 route3;
    private PewterCityGym pewterCityGym;

    public JPanel getWallAndWarpLayer() {
        return wallAndWarpLayer;
    }

    public Warp getCurrentArea() {
        return currentArea;
    }

    private Warp currentArea;

    private JPanel wallAndWarpLayer = new JPanel(new GridBagLayout());

    ;
    AdventureModeUiPanel(MySprite humantrainer) {

        pewterCity = new PewterCity(1854);
        route3 = new Route3(583);
        pewterCityGym = new PewterCityGym(1136);
        wallAndWarpLayer.setOpaque(false);
        this.setLayout(new GridBagLayout());
        setBackground(Color.RED);
        c.fill = 1;
        c.weightx = 1;
        c.weighty = 1;
        currentArea = pewterCity;
        createAreaPart1();
        userSpriteManager = new UserSpriteManager(this, humantrainer);
        createAreaPart2();
    }

    private void createAreaPart1() {
        jp = new JLayeredPane();
        jp.setLayout(new GridBagLayout());
        pathName = currentArea.getAreaToLoad();

        ReadInCollisionBoxes(currentArea.getCollisionsToLoad());
        ReadInCollisionBoxes2(currentArea.getGrassAreasToLoad());
        if (OPEN_OUTPUT_STREAMS && EDIT_MODE) {
            OpenOutputStream(currentArea.getCollisionsToLoad());
            System.out.println("OUTPUT STREAM OPEN: Click cells you do not want the user to enter.");
        }
        c.gridwidth = 1;
        c.gridheight = 1;
        createGrid(currentArea);
    }

    private void createAreaPart2() {
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = COLS;
        c.gridheight = ROWS;

        jp.add(userSpriteManager, c);
        if (EDIT_MODE)
            jp.setLayer(userSpriteManager, 0);
        else
            jp.setLayer(userSpriteManager, 3);

        c.fill = 1;
        c.weighty = 1;
        c.weightx = 1;
        jp.add(wallAndWarpLayer, c);
        jp.setLayer(wallAndWarpLayer, 1);

        this.add(jp, c);

        currentArea.createNPCs();
        currentArea.startNpcMovementThreads();

        npcs = currentArea.getNpcs();

        repaint();
        validate();
        for(NPC npc: npcs) {
            //Only ever needs to be set when the npc's first spawn in. Is set in <code> UserSpriteManager.NPCHandler() <code> every time after.
            npc.setSpriteSize();
            npc.setDirection(npc.getStartingDirection());
            npc.setDeltas();
            npc.setStepsFromCellZeroBasedOnComponent(wallAndWarpLayer.getComponent(
                    npc.getStartingCell()).getLocation());
            npc.updateStartingCellPosition(wallAndWarpLayer.getComponent(npc.getStartingCell()).getLocation());
            npc.updateComponentsFromContainerStart();
            npc.setLocation();
        }
        userSpriteManager.createNPCs(npcs);
        repaint();
    }

    void Enter(Warp entranceToArea) {
        //Stop movement threads when leaving an area
        //doesn't cover additional entrances
        if (currentArea != entranceToArea) {
            AITrainer.canMove = true;
        }
        currentArea = entranceToArea;
        userSpriteManager.setCompWeAreIn(currentArea.moveUserToCell);
        jp.removeAll();
        wallAndWarpLayer.removeAll();
        createAreaPart1();
        createAreaPart2();
    }

    //Takes the collision.txt file as a parameter and if count matches any number in that collection make it a instance of CantEnter
    private void createGrid(Warp name) {
        name.createEntrances();
        int count = 0;
        for (int i = 0; i < ROWS; i++) {
            c.gridy = i;
            c.gridx = 0;
            for (int j = 0; j < COLS; j++) {
                c.gridx = j;

                Collision cell;
                if (name.getEntrances().containsKey(count)) {
                    cell = name.getEntrances().get(count);
                } else if (CELLS_THAT_CANNOT_BE_ENTERED.contains(count)) {
                    cell = new CantEnter(count);
                } else if (grassCellsInArea.contains(count)) {
                    cell = new Grass(currentArea.getPokemonInArea());
                } else {
                    cell = new CanEnter(count);
                }

                if (EDIT_MODE && OPEN_OUTPUT_STREAMS) {
                    cell.setOut(out);
                    cell.openOutputStream(count, CELLS_THAT_CANNOT_BE_ENTERED, currentArea.getCollisionsToLoad());
                }
                wallAndWarpLayer.add(cell, c);
                if (count == 0) {
                    cellZero = cell;
                }
                count++;
            }
        }
    }

    //Open the output stream to the txt file where we will store cell numbers if in EDIT_MODE
    private void OpenOutputStream(String name) {
        try {
            out = new FileOutputStream(name);
            for (Integer i: CELLS_THAT_CANNOT_BE_ENTERED) {
                out.write((i + "\n").getBytes());
            }
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Read in all integers from txt file for whatever ui image we are trying to display
    private void ReadInCollisionBoxes(String name) {
        try {
            Scanner scanner = new Scanner(new File(name));
            CELLS_THAT_CANNOT_BE_ENTERED = new ArrayList<>();
            while (scanner.hasNextInt()) {
                CELLS_THAT_CANNOT_BE_ENTERED.add(scanner.nextInt());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void ReadInCollisionBoxes2(String name) {
        try {
            Scanner scanner = new Scanner(new File(name));
            grassCellsInArea = new ArrayList<>();
            while (scanner.hasNextInt()) {
                grassCellsInArea.add(scanner.nextInt());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public String getPathName() {
        return pathName;
    }

    /*WARP ENTRANCES/EXITS & NPC SPAWNING ******************************************************************
     * ****************************************************************************************************** */

    //User will run right through any component with this class.
    static class CanEnter extends Collision {
        CanEnter(int cellNum) {
            i = cellNum;
            color = new Color(0, 0, 255);
        }
    }

    static class CantEnter extends Collision {
        CantEnter(int cellNum) {
            i = cellNum;
            color = new Color(255, 0, 0);
        }

        @Override
        boolean isWall() {
            return true;
        }
    }

    //Just used for collision detection if player collides with a component of this class
    class PewterCityGym extends Warp {
        PewterCityGym(int moveUserToCell) {
            this.moveUserToCell = moveUserToCell;
            setAreaToLoad("src/AdventureMode/img/PewterCityGym.png");
            setCollisionsToLoad("CollisionCells/CellsThatCannotBeEnteredPewterCityGym.txt");
        }

        @Override
        public void createEntrances() {
            getEntrances().put(1136, pewterCity);
        }

        @Override
        protected void createNPCs() {
        }
    }

    /**
     * Warps or locations control where the exits and entrances to other areas of the map are.
     * They are cells on the grid and when in <code>EDIT_COLLISION_MODE</code> the cells will be green.
     * Each location has an inner class. These essentially work as copies of the outer class so they share
     * values but the purpose of the inner class is so that UIs.I can add multiple cells of the same 'Warp" that all
     * share values so when the user steps on a warp it doesn't create new NPC's for that area every time he enters
     * using a different cell.
     */
    class PewterCity extends Warp {
        PewterCity(int moveUserToCell) {
            setAreaToLoad("src/AdventureMode/img/PewterCityMap.jpg");
            setCollisionsToLoad("CollisionCells/CellsThatCannotBeEnteredPewterCity.txt");
            setGrassAreasToLoad("WildPokemonCells/pewterCityGrass.txt");
            this.moveUserToCell = moveUserToCell;
            setPokemonInArea(ARTICUNO, RAYQUAZA);
        }

        @Override
        public void createEntrances() {
            if (getEntrances().size() == 0) {
                getEntrances().put(1136, pewterCityGym);
                getEntrances().put(1801, route3);
                getEntrances().put(1854, route3.new Route3AdditionalEntrances(583));
                getEntrances().put(1907, route3.new Route3AdditionalEntrances(583));
            }
        }

        @Override
        protected void createNPCs() {
        }

        class PewterCityAdditionalEntrances extends Warp {

            PewterCityAdditionalEntrances(int moveUserToCell) {
                setAreaToLoad("src/AdventureMode/img/PewterCityMap.jpg");
                setCollisionsToLoad("CollisionCells/CellsThatCannotBeEnteredPewterCity.txt");
                setGrassAreasToLoad(pewterCity.getGrassAreasToLoad());
                setPokemonInArea(pewterCity.getPokemonInArea());
                this.moveUserToCell = moveUserToCell;
            }
            @Override
            protected void createEntrances() {
                if (pewterCity.getEntrances().size() == 0) {
                    pewterCity.createEntrances();
                }
                if (getEntrances().size() == 0) {
                    getEntrances().putAll(pewterCity.getEntrances());
                }            }

            @Override
            protected void createNPCs() {
                if (getNpcs().size() == 0) {
                    getNpcs().addAll(pewterCity.getNpcs());
                }
            }
        }
    }

    //Just used for collision detection if player collides with a component of this class
    class Route3 extends Warp {
        Route3(int moveUserToCell) {
            setAreaToLoad("src/AdventureMode/img/Route3.png");
            setCollisionsToLoad("CollisionCells/CellsThatCannotBeEnteredRoute3.txt");
            setGrassAreasToLoad("WildPokemonCells/route3Grass.txt");
            i = this.moveUserToCell = moveUserToCell;
            setPokemonInArea(ARTICUNO, RAYQUAZA);
        }

        @Override
        public void createEntrances() {
            if (getEntrances().size() == 0) {
                getEntrances().put(530, pewterCity);
                getEntrances().put(583, pewterCity.new PewterCityAdditionalEntrances(1854));
                getEntrances().put(636, pewterCity.new PewterCityAdditionalEntrances(1854));
                getEntrances().put(689, pewterCity.new PewterCityAdditionalEntrances(1854));
            }
        }

        @Override
        protected void createNPCs() {
            if (getNpcs().size() == 0) {
                getNpcs().add(new RocketGruntMale(RocketGruntMaleDirection.RIGHT, 484, 7));
                getNpcs().add(new RocketGruntMale2(RocketGruntMaleDirection.FORWARD, 998, 14));
                getNpcs().add(new RocketGruntMale2(RocketGruntMaleDirection.FORWARD, 613, 2));
                getNpcs().add(new RocketGruntMale3(RocketGruntMaleDirection.LEFT, 994, 14));
                getNpcs().add(new RocketGruntMale3(RocketGruntMaleDirection.LEFT, 863, 7));
                getNpcs().add(new RocketGruntMale4(RocketGruntMaleDirection.AWAY, 1783, 8));
                getNpcs().add(new RocketGruntMale5(RocketGruntMaleDirection.LEFT, 658, 6));
            }
        }

        class Route3AdditionalEntrances extends Warp {

            Route3AdditionalEntrances(int moveUserToCell) {
                setAreaToLoad(route3.getAreaToLoad());
                setCollisionsToLoad(route3.getCollisionsToLoad());
                setGrassAreasToLoad(route3.getGrassAreasToLoad());
                setPokemonInArea(route3.getPokemonInArea());
                i = this.moveUserToCell = moveUserToCell;
            }
            @Override
            protected void createEntrances() {
                if (route3.getEntrances().size() == 0) {
                    route3.createEntrances();
                }
                if (getEntrances().size() == 0) {
                    getEntrances().putAll(route3.getEntrances());
                }
            }

            @Override
            protected void createNPCs() {
                if (route3.getNpcs().size() == 0) {
                    route3.createNPCs();
                }
                if (getNpcs().size() == 0) {
                    getNpcs().addAll(route3.getNpcs());
                }
            }
        }
    }
}
