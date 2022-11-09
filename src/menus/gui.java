package menus;

import lombok.NoArgsConstructor;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
// Doesn't really need to exist anymore. Adventuremode should really be the only persistently
// stored UI.
@NoArgsConstructor
public final class gui {
public static final JFrame frame = new JFrame();
private static final HashMap<String, Image> backgroundMap = new HashMap<>();
private static final HashMap<String, JComponent> uiMap = new HashMap<>();
private static Image background;

public static final JComponent ui = new JPanel(new BorderLayout()) {
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
};

static {
	javax.swing.SwingUtilities.invokeLater(() -> {
		frame.getContentPane().setPreferredSize(new Dimension(750, 750));
		frame.pack();
		frame.setVisible(true);
		ui.setMinimumSize(new Dimension(frame.getInsets().left + frame.getInsets().right + 530,
																		frame.getInsets().top + frame.getInsets().bottom + 550)
																	);
		frame.add(ui);
		ui.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	});
}

public static void setBackgroundImage(String key) {
	backgroundMap.computeIfPresent(key, (keyy, img) -> background = img);
}

public static void addNewBackgroundImage(String key, Image background) {
	backgroundMap.computeIfAbsent(key, k -> background);
}

public static void setUI(String key) {
	if (uiMap.containsKey(key)) {
		ui.removeAll();
		ui.repaint();
		ui.add(uiMap.get(key));
		uiMap.get(key).setVisible(true);
	}
	ui.validate();
}

public static void setUiOrDefault(String key, String key2) {
	ui.removeAll();
	ui.repaint();
	ui.add(uiMap.getOrDefault(key, uiMap.get(key2)));
	background = backgroundMap.getOrDefault(
																					key, backgroundMap.getOrDefault(key2, null)
																				);
	uiMap.getOrDefault(key, uiMap.get(key2)).setVisible(true);
	ui.validate();
}

public static void addNewUI(JComponent ui) {
	uiMap.put(ui.getClass().getSimpleName(), ui);
}

public static void addNewUI(String key, JComponent ui) {
	uiMap.put(key, ui);
}

public static void setUIAndBackgroundImg(String key) {
	setUI(key);
	setBackgroundImage(key);
}

public static void addNewUIAndBackgroundImg(JComponent ui, Image background) {
	addNewUI(ui);
	addNewBackgroundImage(ui.getClass().getSimpleName(), background);
}


public static <T extends Component> void addArtifact(String uiName, T component) {
	uiMap.get(uiName).add(component);
}

public static int getWidth() {
	return frame.getContentPane().getWidth();
}

public static int getHeight() {
	return frame.getContentPane().getHeight();
}

public static Integer x(Object i) {
	return switch (i) {
		case Double s -> (int) (getWidth() * s);
		case Integer s -> getWidth() * s;
		default -> throw new IllegalStateException("Unexpected value: " + i);
	};
}

public static Integer y(Object i) {
	return switch (i) {
		case Double d -> (int) (getHeight() * d);
		case Integer d -> getHeight() * d;
		default -> throw new IllegalStateException("Unexpected value: " + i);
	};
}

}
