import MainMenu.GUIManager;
import MainMenu.MainMenuGUI;

import java.awt.*;
import java.io.IOException;

public class Main {

    Main(){

    }
        public static void main(String[] args) throws IOException {
            MainMenuGUI manager = new MainMenuGUI();
            Toolkit.getDefaultToolkit().setDynamicLayout(false);
        }
}

