package menus.Styles;

import javax.swing.*;
import java.awt.*;

final public class ErrorLabel extends JLabel {

public ErrorLabel() {
	super("Hello World!", SwingConstants.CENTER);
}



public ErrorLabel setDefaultProperties(){
	setVisible(false);
	setBackground(new Color(255, 0, 0));
	setOpaque(true);
	setForeground(new Color(255,255,255));
	return this;
}

public ErrorLabel setToVisible() {
	setVisible(true);
	return this;
}
}
