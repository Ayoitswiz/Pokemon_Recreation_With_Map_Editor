package AdventureMode;

import MainMenu.Backpack;
import MainMenu.GUIManager;
import MainMenu.PokemonInPartyPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class StartMenu extends JPanel {
    private Border raisedetched, loweredetched, raisedbevel, loweredbevel;
    private Border compound;
    private Color menu = new Color(200, 55, 55);
    private StartMenuButton pokedex, pokemon, bag, pokenav, user, save, option, mainMenu, exit;
    private MySprite sprite;
    private GUIManager gui;
    private Backpack backpack;
    private PokemonInPartyPanel pokemonInPartyPanel;
    private ArrayList<NPC> npcsInArea;
    StartMenu(MySprite mySprite, GUIManager gui, ArrayList<NPC> npcsInArea) {
        sprite = mySprite;
        this.gui = gui;
        this.npcsInArea = npcsInArea;

        pokemonInPartyPanel = new PokemonInPartyPanel(mySprite, gui);


        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        raisedbevel = BorderFactory.createRaisedBevelBorder();
        loweredbevel = BorderFactory.createLoweredBevelBorder();
        //empty = BorderFactory.createEmptyBorder();
        Border redLine = BorderFactory.createLineBorder(menu, 15);
        Border thick = (BorderFactory.createStrokeBorder(new BasicStroke(10.0f)));
        compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
        compound = BorderFactory.createCompoundBorder(redLine, compound);
        compound = BorderFactory.createCompoundBorder(thick, compound);
        compound = BorderFactory.createCompoundBorder(loweredetched, compound);
        compound = BorderFactory.createCompoundBorder(raisedetched, compound);

        setBorder(compound);

        pokedex = new StartMenuButton("POKéDEX", mySprite);
        pokemon = new StartMenuButton("POKéMON", mySprite);
        bag = new StartMenuButton    ("BAG", mySprite);
        pokenav = new StartMenuButton("POKéNAV", mySprite);
        user = new StartMenuButton   ("NAME", mySprite);
        save = new StartMenuButton   ("SAVE", mySprite);
        option = new StartMenuButton ("OPTION", mySprite);
        mainMenu = new StartMenuButton   ("Main Menu", mySprite);
        exit = new StartMenuButton   ("Exit", mySprite);


        DialogBoxTextArea dialogBoxTextArea = new DialogBoxTextArea();

        c.fill = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 50;
        c.gridheight = 1;
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        add(pokedex, c);
        c.gridy = 1;
        add(pokemon, c);
        c.gridy = 2;
        add(bag, c);
        c.gridy = 3;
        add(pokenav, c);
        c.gridy = 4;
        add(user, c);
        c.gridy = 5;
        add(save, c);
        c.gridy = 6;
        add(option, c);
        c.gridy = 7;
        add(mainMenu, c);
        c.gridy = 8;
        add(exit, c);
        c.gridy = 9;
        add(dialogBoxTextArea, c);
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        setVisible(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * (float) ((getWidth() + getHeight()) / 500));
        g.setFont(newFont);
        g.setColor(Color.BLACK);
        validate();
    }

    class DialogBoxTextArea extends JTextArea implements FocusListener {
        DialogBoxTextArea() {
            Font f = new Font("Times New Roman", Font.PLAIN, 24);
            setRows(2);
            setColumns(20);
            setWrapStyleWord(true);
            setLineWrap(true);
            setFont(f);
            setText("Enter Command");
            setVisible(true);
            setBorder(UIManager.getBorder("Label.border"));
            addFocusListener(this);
            setLayout(new BorderLayout());
            add(new JButton("Enter") {
                {
                    addActionListener(e -> {
                        String[] commandAndValue = DialogBoxTextArea.this.getText().split("\\s");

                        switch (commandAndValue[0]) {
                            case "sw": //Adjust users speed. Lower == faster.
                                sprite.setSpeedWeight(Integer.parseInt(commandAndValue[1]));
                                sprite.setDeltas();
                                sprite.setTicksFromCellZeroBasedOnOtherSprite(sprite.getLocation());
                                sprite.updateComponentsFromContainerStart();

                            case "co": //Clear opponent. Only works prior to battle
                                StartMenu.this.setVisible(false);
                                StartMenu.this.getParent().removeAll();

                                for(NPC npc: npcsInArea) {
                                    if (sprite.getNpcOpponent() == npc) {
                                        System.out.println(npc.getClass());
                                        npc.makeNpcTurnAround();
                                        npc.caughtPlayerInBattleGlare = false;
                                        sprite.getNpcOpponent().caughtPlayerInBattleGlare = false;
                                        sprite.setOpponent(null);
                                        npc.setMoving(true);
                                    }
                                }
                                break;
                        }
                    });
                }
            }, BorderLayout.SOUTH);
        }

        @Override
        public void focusGained(FocusEvent e) {
            setText("");
        }

        @Override
        public void focusLost(FocusEvent e) {
            if(getText().length()<1) {
                setText("Enter Command");
            }
        }
    }

    class StartMenuButton extends JButton {
        int i = 1;
        String text;
        private String name;
        StartMenuButton(String text, MySprite mySprite) {
            setBackground(new Color(59, 89, 182));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setVisible(true);
            this.text = text;
            name = mySprite.getName();
            setHorizontalAlignment(SwingConstants.LEFT);

            addActionListener(e -> {
                switch (text){
                    case "POKéDEX":
                        break;
                    case "POKéMON":
                        pokemonInPartyPanel = new PokemonInPartyPanel(mySprite, gui);
                        gui.setUi(pokemonInPartyPanel.getClass().getSimpleName());
                        gui.setBackgroundImage(pokemonInPartyPanel.getClass().getSimpleName());
                        break;
                    case "BAG":
                        backpack = new Backpack(sprite, gui);
                        gui.setUi(backpack.getClass().getSimpleName());
                        new PokemonInPartyPanel(mySprite, gui);
                        break;
                    case "POKéNAV":
                        break;
                    case "NAME":
                        System.out.println(mySprite.getName());
                        break;
                    case "SAVE":
                        break;
                    case "OPTION":
                        break;
                    case "Main Menu":
                        gui.setUi("MainMenuGUI");
                        gui.setBackgroundImage("MainMenuGUI");
                        break;
                    case "Exit":
                        getParent().getParent().remove(getParent());
                        getParent().setVisible(false);
                        break;
                }

            });
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Font currentFont = g.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * (float) ((getWidth() + getHeight()) / 100));
            g.setFont(newFont);
            g.setColor(Color.BLACK);
            if(text.equals("USER")){
                g.drawString(name, getInsets().left/4, getInsets().top + g.getFontMetrics().getHeight());

            } else {
                g.drawString(text, getInsets().left / 4, getInsets().top + g.getFontMetrics().getHeight());
            }
        }
    }
}