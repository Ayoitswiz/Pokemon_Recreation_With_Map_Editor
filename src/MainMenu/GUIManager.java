package MainMenu;
import AdventureMode.AdventureModeMain;
import ConsoleUpgrade.ConsoleUpgrade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class GUIManager {
    private static final int TIMER_DELAY = 50; //Delay on timer for sprite movement
    private Timer timer = null; //Timer for
    private Map<String, Image>  backgroundMap = new HashMap<>();
    private Image background;
    private JFrame frame;
    private GridBagConstraints c = new GridBagConstraints();
    private Map<String, JPanel> uiMap = new HashMap<>();
    private boolean helper = false;

    private JPanel ui = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    };

    GUIManager(){
        ConsoleUpgrade cu = new ConsoleUpgrade();
        frame = new JFrame();
        frame.setSize(1800, 1000);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ui.setVisible(true);
        ui.setBackground(Color.blue);
        ui.setLayout(new GridBagLayout());
        c.fill = 1;
        c.weighty =1;
        c.weightx =1;
        ui.setMinimumSize(new Dimension(frame.getInsets().left + frame.getInsets().right + 530 , frame.getInsets().top + frame.getInsets().bottom + 550));
        frame.add(ui);
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension((frame.getWidth() - frame.getContentPane().getWidth()) + 530 , (frame.getHeight() - frame.getContentPane().getHeight()) + 550));
        timer = new Timer(TIMER_DELAY, new TimerListener());
        timer.setInitialDelay(3000);
        timer.start();
    }

    /**
     * This timer controls which gui is to be displayed at any given time throughout the applications lifespan.
     * When a instance of a ui panel is created use the <code> addNewUi </code> function.
     * Then simply use the <code> setUi </code> function if the desire is to make that instance the current ui.
     */
    private class TimerListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (ui instanceof AdventureModeMain && !ui.getComponent(1).isVisible()) {
            ui.validate();
            ui.repaint();
            ui.getComponent(0).repaint();
            ui.getComponent(0).validate();
        }
    }
}
    public void setBackgroundImage(String key) {
        if(backgroundMap.containsKey(key)){
            this.background = backgroundMap.get(key);
        }
        sout(!backgroundMap.containsKey(key) ? "No key of " + key + " exists with that name!" : "Background set: " + key);
    }

    public void addNewBackgroundImage(String key, Image background) {
        if (!backgroundMap.containsKey(key)) {
            backgroundMap.put(key, background);
        }
        sout(!backgroundMap.containsKey(key) ? "Backgound: " + key + " added" : "Key: " + key + " already added Exception.");
    }

    public void setUi(String key){
        if (uiMap.containsKey(key)) {
            ui.removeAll();
            ui.repaint();
            ui.add(uiMap.get(key), c);
            uiMap.get(key).setVisible(true);
        }
        sout(ui.getComponents().length > 0 ? "Ui set: " + key : "No key of " + key + " exists with that name!");
        ui.validate();
    }

    public void addNewUi(JPanel ui) {
        String key = ui.getClass().getSimpleName();
        addToUiList(key, ui);
    }

    public void addNewUi(String key, JPanel ui) {
        addToUiList(key, ui);
    }

    private void addToUiList(String key, JPanel ui) {
        if(uiMap.containsKey(key)) {
            uiMap.replace(key, ui);
            sout("Key: " + key + " already added Exception. Key replaced");
        } else {
            uiMap.put(key, ui);
            sout("Ui: " + key + " added");
        }
    }

    private void sout(String out) {
        if (helper) {
            System.out.println(out);
        }
    }

    public void shutoffTimer() {
        timer.stop();
    }
}

/*
    public void setBackgroundImage(String key) {
        if(!backgroundMap.containsKey(key)){
            if (helper)System.err.println("No key of " + key + " exists with that name!");
        } else {
            this.background = backgroundMap.get(key);
            if (helper)System.out.println("Background set: " + key);
        }
    }
    public void addNewBackgroundImage(String key, Image background) {
        if(!backgroundMap.containsKey(key)) {
            backgroundMap.put(key, background);
            if (helper)System.out.println("Backgound: " + key + " added");
        } else {
            if (helper)System.err.println("Key: " + key + " already added Exception.");
        }
    }

    public void setUi(String key){
        if(!uiMap.containsKey(key)) {
            if (helper)System.err.println("No key of " + key + " exists with that name!");
        } else {
            if(ui.getComponentCount() > 0)
                ui.getComponent(0).setVisible(false);
            ui.removeAll();
            uiMap.get(key).setVisible(true);
            ui.add(uiMap.get(key), c);
            if (helper)System.out.println("Ui set: " + key);
        }
        ui.validate();
    }

    public void addNewUi(JPanel ui) {
        String key = ui.getClass().getSimpleName();
        addToUiList(key, ui);
    }
    public void addNewUi(String key, JPanel ui) {
        addToUiList(key, ui);
    }

    private void addToUiList(String key, JPanel ui) {
        if(!uiMap.containsKey(key)) {
            uiMap.put(key, ui);
            if (helper)System.out.println("Ui: " + key + " added");
        } else {
            uiMap.replace(key, ui);
            if (helper)System.err.println("Key: " + key + " already added Exception. Key replaced");
        }
    }

    public void shutoffTimer() {
        timer.stop();
    }
}*/
