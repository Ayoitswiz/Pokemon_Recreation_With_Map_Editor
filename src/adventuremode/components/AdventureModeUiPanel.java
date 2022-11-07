package adventuremode.components;

import adventuremode.cells.Cell;
import adventuremode.cells.Grass;
import adventuremode.sprites.Direction;
import adventuremode.sprites.MySprite;
import adventuremode.sprites.NPC;
import adventuremode.sprites.NPCs;
import adventuremode.sprites.UserSpriteManager;
import menus.pokemon.ePokemon;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import utilities.LambdaWithParam;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static menus.pokemon.ePokemon.ARTICUNO;
import static menus.pokemon.ePokemon.RAYQUAZA;
public class AdventureModeUiPanel extends JPanel {
/**
 * When EDIT_MODE is set to true, and so is OPEN_OUTPUT_STREAMS, any cells clicked will be saved to a txt file
 * When the output streams are open the output file will be overwritten.
 */
public static boolean EDIT_MODE = false; //When set to true, cells are turned to buttons and cells of different types are painted with a unique color
public static final boolean OPEN_OUTPUT_STREAMS = true;
public static final int ROWS = 55;
public static final int COLS = 53;
private final Cell[] cells = new Cell[2915];
private final List<Integer> walls = new ArrayList<>();
private final List<Integer> grassCellsInArea = new ArrayList<>();
private transient FileOutputStream out;
private transient Warp currentArea;
private transient BufferedImage uiImg;
UserSpriteManager userSpriteManager;

public void createUI(MySprite humantrainer) {
	setLayout(new BorderLayout());
	currentArea = new PewterCity();
	loadArea();
	createNPCs();
	humantrainer.setToCellPos(1791);
	userSpriteManager = new UserSpriteManager(new CellArray(), humantrainer, currentArea);
	add(userSpriteManager);
	drawUI(getGraphics());
	userSpriteManager.start();
	addMouseListener(new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			if (EDIT_MODE && OPEN_OUTPUT_STREAMS) {
				for (var c : cells) {
					if (c.getBounds().contains(e.getPoint())) {
						c.changeCellType(walls, currentArea.getWalls());
						loadArea();
					}
				}
			}
		}
	});
}

private void loadArea() {
	try {
		uiImg = ImageIO.read(new File(currentArea.getAreaImg()));
	} catch (IOException e) {
		e.printStackTrace();
	}
	readInCellFile(currentArea.getWalls(), walls);
	readInCellFile(currentArea.getGrass(), grassCellsInArea);
	if (EDIT_MODE && OPEN_OUTPUT_STREAMS) {
		openOutputStream(currentArea.getWalls());
	}
	createGrid(currentArea);
}
private void createNPCs() {
	for (var npc: currentArea.getNpcs()) {
		npc.setToCellPos(npc.startingCellNum);
		npc.start();
	}
}

void enter(Warp entranceToArea) {
	for (var npc: currentArea.getNpcs()) npc.stop();
	currentArea = entranceToArea;
	userSpriteManager.currentarea = currentArea;
	loadArea();
	drawUI(getGraphics());

	createNPCs();
}

private void createGrid(Warp name) {
	int count = 0;
	Cell c;
	for (int row= 0; row < ROWS; row++) {
		for (int col = 0; col < COLS; col++) {
			if (name.getWarps().containsKey(count)) {
				c = name.getWarps().get(count);
				Warp.enter = AdventureModeUiPanel.this::enter;
			} else if (walls.contains(count)) {
				c = new Wall();
			} else if (grassCellsInArea.contains(count)) {
				c = new Grass(currentArea.getPokemon());
			} else {
				c = new Cell();
			}
			c.setNum(count);
			c.setRowNum(row);
			c.setColNum(col);
			c.setImg(uiImg.getSubimage(col * 16, row * 16, 16, 16));
			cells[row * COLS + col] = c;
			count++;
		}
	}
}

private Cell cell;
private BufferedImage newImage;
private Graphics2D g2;

private void drawUI(Graphics g) {
	newImage = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_ARGB);
	g2 = newImage.createGraphics();
	int row;
	int y, h;
	int col;
	int x;
	int w;
	int remainderH = getHremainder();
	int remainderW;
	int wc = getWcell();
	int hc = getHcell();
	for (y = 0, row = -1; ++row < ROWS; y += h) {
		h = remainderH-- > 0 ? hc + 1 : hc;
		remainderW = getWremainder();
		for (x = 0, col = -1; ++col < COLS; x += w) {
			w = remainderW-- > 0 ? wc + 1 : wc;
			cell = cells[row * COLS + col];
			cell.setBounds(x, y, w, h);
			g2.drawImage(cell.getImg(), x, y, w, h, null);
			if (EDIT_MODE && OPEN_OUTPUT_STREAMS) {
				cell.draw(g2);
			}
		}
	}
	g.drawImage(newImage, 0, 0, getWidth(), getHeight(), null);
}

@Override
protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	drawUI(g);
}



//Open the output stream to the txt file where the cell numbers will be stored if in EDIT_MODE
private void openOutputStream(String name) {
	try {
		out = new FileOutputStream(name);
		for (int i: walls) {
			out.write((i + "\n").getBytes());
		}
		out.close();
	} catch (IOException ex) {
		ex.printStackTrace();
	}
}


private void readInCellFile(String name, List<Integer> cellFile) {
	try (Scanner scanner = new Scanner(new File(name))) {
		cellFile.clear();
		while (scanner.hasNextInt()) {
			cellFile.add(scanner.nextInt());
		}
	} catch (IOException ex) {
		ex.printStackTrace();
	}
}


public class CellArray {
	@NotNull
	public Cell getCell(Point p) {
		return getCell(p.x, p.y);
	}

	@NotNull
	Cell getCell(int x, int y) {
		for (var c: cells) {
			if (c.getBounds().contains(x,y))
				return c;
		}
		return cells[0];
	}

	public List<Cell> getCell(MySprite sprite, int... cellNum) {
		return new ArrayList<>() {{
			for (int val : cellNum) {
				if ((val += getCell(sprite.getHitboxCenterPoint()).getNum()) < cells.length && val >= 0)
					add(cells[val]);
			}
		}};
	}

	public Cell getCell(int cellNum) {
		return cells[cellNum];
	}
}

/*WARP ENTRANCES/EXITS & NPC creation ******************************************************************
 * ****************************************************************************************************** */
// could zoom in screen and load areas once user gets close enough to them

public class Wall extends Cell {}

@Getter
public abstract static class Warp extends Cell {
	public static LambdaWithParam<Warp> enter;
	private final String areaImg;
	private final String walls;
	private final String grass;
	private final ePokemon[] pokemon;
	private final int moveUserToCell;

	Warp(String areaImg, String walls, String grass, ePokemon[] pokemon, int moveUserToCell) {
		this.areaImg = areaImg;
		this.walls = walls;
		this.grass = grass;
		this.pokemon = pokemon;
		this.moveUserToCell = moveUserToCell;
	}
	public abstract Map<Integer, Warp> getWarps();
	public abstract NPC[] getNpcs();
}


static class PewterCityGym extends Warp {
	PewterCityGym() {
		super("src/adventuremode/img/PewterCityGym.png",
		"src/adventuremode/img/PewterCityGym.png",
		"Cells/GrassCells/pewterCityGrass.txt",
		new ePokemon[]{ARTICUNO, RAYQUAZA},
		1136
		);
	}
	private static final	Map<Integer, Warp> warps = new HashMap<>();
	private static final NPC[] npcs = {};

	@Override public 			Map<Integer, Warp> getWarps() { return warps;}
	@Override public NPC[] getNpcs()  { return npcs; }
}


/**
 * Warps or locations control where the exits and entrances to other areas of the map are.
 * They are cells on the grid and when in <code>EDIT_MODE</code> the cells will be green.
 */
static class PewterCity extends Warp {
	PewterCity() {
		super(
		"src/adventuremode/img/PewterCityMap.jpg",
		"Cells/CollisionCells/CellsThatCannotBeEnteredPewterCity.txt",
		"Cells/GrassCells/pewterCityGrass.txt",
		new ePokemon[]{ARTICUNO, RAYQUAZA},
		1854
		);
	}
	private static final NPC[] npcs = {};
	private static final Map<Integer, Warp> warps = new HashMap<>() {{
		put(1136, new PewterCityGym());
		put(1801, new Route3());
		put(1854, new Route3());
		put(1907, new Route3());}};

	@Override public Map<Integer, Warp> getWarps() { return warps; }
	@Override public NPC[] getNpcs()  { return npcs; }
}


static class Route3 extends Warp {
	Route3() {
		super(
		"src/adventuremode/img/Route3.png",
		"Cells/CollisionCells/CellsThatCannotBeEnteredRoute3.txt",
		"Cells/GrassCells/route3Grass.txt",
		new ePokemon[]{ARTICUNO, RAYQUAZA},
		583
		);
	}
	private static final NPC[] npcs = {
	new NPCs(NPCs.Type.ROCKET_GRUNT_MALE, Direction.R, 431, 7),
	new NPCs(NPCs.Type.ROCKET_GRUNT_MALE2, Direction.D, 997, 14),
	new NPCs(NPCs.Type.ROCKET_GRUNT_MALE2, Direction.D, 559, 2),
	new NPCs(NPCs.Type.ROCKET_GRUNT_MALE3, Direction.L, 941, 14),
	new NPCs(NPCs.Type.ROCKET_GRUNT_MALE3, Direction.L, 810, 7),
	new NPCs(NPCs.Type.ROCKET_GRUNT_MALE4, Direction.U, 1782, 8),
	new NPCs(NPCs.Type.ROCKET_GRUNT_MALE5, Direction.L, 605, 6),};
	private static final Map<Integer, Warp> warps = new HashMap<>() {{
		put(530, new PewterCity());
		put(583, new PewterCity());
		put(636, new PewterCity());
		put(689, new PewterCity());}};

	@Override public Map<Integer, Warp> getWarps() { return warps;}
	@Override public NPC[] getNpcs()  { return npcs; }
}




public int getWcell() {
	return getWidth() / COLS; // will be rounded down
}
public int getWremainder() {
	return getWidth() - (COLS * getWcell());
}
public int getHcell() {
	return getHeight() / ROWS;
}
public int getHremainder() {
	return getHeight() - (ROWS * getHcell());
}

}
