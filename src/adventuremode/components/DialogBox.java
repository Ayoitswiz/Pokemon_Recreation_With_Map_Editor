package adventuremode.components;

import menus.ExitFunctionException;
import menus.gui;
import utilities.Lambda;
import utilities.LambdaWithReturnVal;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class DialogBox extends JPanel {
private final JTextArea textarea;
private final JButton next;
private final Queue<Lambda> onNext = new LinkedList<>();
private transient Lambda onDialogEnd = () -> {};
private transient Lambda onException = null;
private transient LambdaWithReturnVal<Rectangle> bounds = () -> new Rectangle(
gui.x(0.0),
gui.y(.75),
gui.x(1.0),
gui.y(.253));

public DialogBox() {
	textarea = new JTextArea() {{
		setWrapStyleWord(true);
		setLineWrap(true);
		setEditable(false);
		setFont(new Font("Times New Roman", Font.PLAIN, 22));
	}};

	next = new JButton() {{
			setVisible(false);
			setText("Next");
			setBackground(new Color(59, 89, 182));
			setForeground(Color.WHITE);
			setFocusPainted(false);
			setFont(new Font("Tahoma", Font.BOLD, 14));

			addActionListener(e -> {
				if (!onNext.isEmpty()) {
					onNext.remove().foo();
					// added because clicking next would make the whole dialogbox invisible for very short but noticable time &
					// the next button would be at the top of the dialogbox instead of the bottom, but this resolves that.
					DialogBox.this.repaint();
					DialogBox.this.validate();
				} else {
					setNextNotVisible();
					onDialogEnd.foo();
					onDialogEnd = () -> {};
				}
			});
		}};

	setLayout(new BorderLayout());
	setBounds(bounds.foo());

	add(new JScrollPane(textarea), BorderLayout.CENTER);
	add(next, BorderLayout.SOUTH);
	setVisible(false);
	setBackground(new Color(255, 225, 255));
}



@Override
public int getX() {
	setBounds(getBounds());
	return super.getX();
}

@Override
public int getY() {
	setBounds(getBounds());
	return super.getY();
}

@Override
public int getWidth() {
	setBounds(getBounds());
	return super.getWidth();
}

@Override
public int getHeight() {
	setBounds(getBounds());
	return super.getHeight();
}

public void setbounds(int x, int y, int width, int height) {
	bounds = null;
	setBounds(x, y, width, height);
	validate();
}

@Override
public Rectangle getBounds() {
	if (bounds != null)
		setBounds(bounds.foo());

	return super.getBounds();
}


public DialogBox open() {
	this.setVisible(true);
	return this;
}

public DialogBox introDialog() {
	setDialog("Welcome to Pokemon Recreation with an additional map editor!!!");
	cycleDialog(
	"The game is fully resizable so make the window whatever size feels most comfortable! It should still be usable" +
	" even at almost Apple watch sizes. Give it a try, see how small you can make it while still being functional XD",
	"You can enter & exit EDIT_MODE via: ctrl+e",
	"While in EDIT_MODE you can click on cells to change their type",
	"You can visit other locations by walking over a Warp cell! To see some of the warp cells," +
	" enter Edit mode by pressing: ctrl+e. Then, look for the green cells.",
	"You can move around using the keys: w, a, s, d",
	"You can change your speed by pressing: ctrl+i to decrease, ctrl+d increase",
	"You can open the Start menu by pressing: m",
	"If you want to view these messages again open the start menu by pressing: m. Then, select \"Options\"",
	"That's Everything!");
	onDialogEnd(this::reset);
	return this;
}

/**
 * Used to set text without displaying the <attribute>dialogBoxButton</attribute>
 */
public DialogBox setDialog(String dialog) {
	textarea.setText(dialog);
	return this;
}

public DialogBox setDialog(Lambda onNext) {
	next.setVisible(true);
	this.onNext.add(onNext);
	return this;
}

public DialogBox setDefaultBattleDialog() {
	textarea.setText("What will you do?");
	return this;
}

public String getDefaultBattleDialog() {
	return "What will you do?";
}

public DialogBox setDialog(LambdaWithReturnVal<String> dialog) {
	try {
		setDialog(dialog.foo());
	} catch (ExitFunctionException ex) {
		if (onException != null) {
			onException.foo();
			onException = null;
		}
		else {
			setDialog(ex::getMessage)
			.onDialogEnd(this::setDefaultBattleDialog);
		}
		throw ex;
	}
	return this;
}

public DialogBox cycleDialog(Queue<String> cycleDialog) {
	cycleDialog.forEach(dialog -> onNext.add(() -> setDialog(dialog)));
	return this;
}

public DialogBox cycleDialog(String... cycleDialog) {
	setVisible(true);
	next.setVisible(true);
	Arrays.asList(cycleDialog).forEach(dialog -> onNext.add(() -> setDialog(dialog)));
	return this;
}


public DialogBox onNext(Lambda onNext) {
	next.setVisible(true);
	this.onNext.add(onNext);
	return this;
}

public boolean hasNext() {
	return next.isVisible();
}


public DialogBox setNextNotVisible() {
	next.setVisible(false);
	return this;
}


public DialogBox onException(Lambda onException) {
	this.onException = onException;
	return this;
}


public DialogBox onDialogEnd(Lambda funcToExecuteOnDialogEnd) {
	next.setVisible(true);
	this.onDialogEnd = funcToExecuteOnDialogEnd;
	return this;
}

public void reset() {
	setVisible(false);
}
}
