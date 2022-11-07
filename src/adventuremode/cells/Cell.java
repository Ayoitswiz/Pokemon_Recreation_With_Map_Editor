package adventuremode.cells;

import adventuremode.components.AdventureModeUiPanel.Wall;
import adventuremode.components.AdventureModeUiPanel.Warp;
import lombok.Getter;
import lombok.Setter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


public class Cell implements Collisions {
@Getter @Setter protected int num;
@Getter @Setter private int colNum;
@Getter @Setter private int rowNum;
@Getter @Setter private int x;
@Getter @Setter private int y;
@Getter @Setter private BufferedImage img;
@Getter private int width;
@Getter private int height;
protected Color color;

@Override
public boolean intersects(Rectangle r) {
	return getBounds().intersects(r);
}

public void draw(Graphics2D g) {
	if (this instanceof Wall) g.setColor(Color.red);
	if (this instanceof Grass) g.setColor(Color.black);
	if (this instanceof Warp) g.setColor(Color.green);
	if (this instanceof Wall || this instanceof Grass || this instanceof Warp) {
		int stroke = 2;
		g.setStroke(new BasicStroke(stroke));
		g.drawLine(getMaxX(), y, x, getMaxY());
		g.drawLine(x, y, getMaxX(), getMaxY());
		g.drawRect(x, y, width - stroke, height - stroke);
	}
	g.setFont(g.getFont().deriveFont(Font.BOLD, (width + height) / 4f));
	g.setColor(this instanceof Wall ? Color.BLACK : Color.WHITE);
	g.drawString(Integer.toString(num), x, getMaxY());
}


public int getMaxY() {
	return y + height;
}

public int getMaxX() {
	return x + width;
}

public void setBounds(int x, int y, int width, int height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height =height;
}

public Rectangle getBounds() {
	return new Rectangle(x, y, width, height);
}


public void changeCellType(List<Integer> walls, String areaToLoad) {
	try {
		var inputFile = new File(areaToLoad);
		if (walls.contains(num)) {
			var tempFile = new File("temp.txt");
			try (var writer = new BufferedWriter(new FileWriter(tempFile))) {
				try (var reader = new BufferedReader(new FileReader(inputFile))) {
					var lineToRemove = Integer.toString(num);
					String currentLine;
					while ((currentLine = reader.readLine()) != null) {
						// trim newline when comparing with lineToRemove
						if (currentLine.trim().equals(lineToRemove)) continue;
						writer.write(currentLine + System.getProperty("line.separator"));
					}
				}
				Files.delete(inputFile.toPath());
			}
			if (tempFile.renameTo(inputFile)) {
				walls.remove(Integer.valueOf(num));
			}
		} else {
			try (var writer = new BufferedWriter(new FileWriter(inputFile.getAbsoluteFile(), true))) {
				writer.write((num + System.getProperty("line.separator")));
				walls.add(num);
			}
		}
	} catch (IOException ex) {
		ex.printStackTrace();
	}
}
}
