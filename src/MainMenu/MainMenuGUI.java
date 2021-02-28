package MainMenu;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import AdventureMode.AdventureModeMain;
import AdventureMode.MySprite;
import AdventureMode.Sprite;

public class MainMenuGUI extends JPanel{

    private static final Image mainMenuBackground = new ImageIcon("src/imagesUI/MainMenuBackground.jpg").getImage();
    private static JList<? extends String> lstDisplayAllPokemon;
    private static JButton btnStartBattle;
    private static ItemShop itemShop;
    private static AdventureModeMain adventureModeMain;
    private DefaultListModel<String> model1;
    private ArrayList<Pokemon> pokemonList = new ArrayList<>();
    private ArrayList<Pokemon> selectedPokemonList = new ArrayList<>();


    public MainMenuGUI() throws IOException {
        MySprite user = new MySprite(new BigDecimal("10000.00"), "Ash");
        GUIManager gui = new GUIManager();

        btnStartBattle = new SetBtnProperties("    Start Battle!    ");
        createPokemon();
        JPanel gbcStartBattle = new JPanel(new GridBagLayout());
        JPanel gbcpnlDisplayAllPokemon = new JPanel(new GridBagLayout());
        JPanel pnlNorth = new JPanel();
        JLabel lblTitle = new JLabel("Pokemon Main Menu");
        JPanel pane = new JPanel(new GridBagLayout());
        JButton btnItemShop =    new SetBtnProperties("      Item Shop      ");
        JButton btnHeal = new SetBtnProperties("Heal");
        JButton btnSelect = new SetBtnProperties("Select Pokemon!");
        JButton btnRemove = new SetBtnProperties("Remove Pokemon");
        JButton btnStartJourney = new SetBtnProperties("Start Journey");
        JScrollPane spaneDisplayAllPokemon = new JScrollPane(lstDisplayAllPokemon);
        Font f = new Font("Times New Roman", Font.PLAIN, 32);
        Color cPnlNorth = new Color(160, 44, 44, 255);
        //Color background = new Color(30, 30, 30);
        Color MenuBg = new Color(181, 181, 181, 150);
        Dimension[] dd = {new Dimension(getWidth(), 50)};
        GridBagConstraints c = new GridBagConstraints();

        btnHeal.setEnabled(false);
        btnStartJourney.setEnabled(false);
        lblTitle.setFont(f);
        pnlNorth.setBackground(cPnlNorth);
        lblTitle.setForeground(Color.WHITE);
        pnlNorth.setPreferredSize(dd[0]);
        pnlNorth.add(lblTitle);
        gbcStartBattle.setOpaque(false);
        gbcpnlDisplayAllPokemon.setOpaque(false);
        btnRemove.setBackground(new Color(244, 20, 20));
        spaneDisplayAllPokemon.setMinimumSize(new Dimension(200, 125));
        gbcStartBattle.setPreferredSize(new Dimension(350, 200));
        gbcpnlDisplayAllPokemon.setPreferredSize(new Dimension(350, 200));
        btnStartBattle.setFont(f);
        btnStartBattle.setEnabled(false);

        //titlebar
        c.fill = 2;
        c.ipady = 20;
        c.gridheight = 1;
        c.weightx = 1;
        c.gridwidth = 2;
        pane.add(pnlNorth, c);

        //list of pokemon
        c.insets = new Insets(10, 10, 10, 10);
        c.gridy = 1;
        pane.add(spaneDisplayAllPokemon, c);
        //select
        c.gridy = 2;
        c.gridwidth = 1;
        pane.add(btnSelect, c);
        //remove
        c.gridx = 1;
        pane.add(btnRemove, c);
        c.gridy = 3;
        c.gridx = 0;
        pane.add(btnItemShop, c);
        c.gridx = 1;
        pane.add(btnHeal, c);
        //start
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        pane.add(btnStartBattle, c);

        pane.setBackground(MenuBg);
        add(pane, BorderLayout.CENTER);
        add(btnStartJourney, BorderLayout.SOUTH);


        btnSelect.addActionListener(e -> {
            //allows you select and remove multiple pokemon at the same time by returning arrays
            //Can only select 6 pokemon
            Object[] lstValues = lstDisplayAllPokemon.getSelectedValuesList().toArray();
            int[] lstIndices = lstDisplayAllPokemon.getSelectedIndices();

            if (selectedPokemonList.size() + lstIndices.length <= 6) {

                //get the selected element's index and value. Then Add (Selected) to value where index.
                for (int i = 0; i < lstIndices.length; i++) {
                    String a = (String) lstValues[i];
                    int count = a.length() - a.replaceAll("\\)","").length();
                    lstValues[i] = lstValues[i].toString().substring(0, lstValues[i].toString().length() - 11 * count);

                    //clone the pokemon and their moves so we don't repeat code
                    try {
                        selectedPokemonList.add((Pokemon) pokemonList.get(lstIndices[i]).clone());
                        Move[] move;
                        for (Pokemon selectedP: selectedPokemonList) {
                            move = new Move[selectedP.moves.length];
                            int slot = 0;
                            for (Move m : selectedP.moves) {
                                move[slot++] = (Move) m.clone();
                            }
                            selectedP.moves = move;
                        }
                    } catch (CloneNotSupportedException ex) {
                        ex.printStackTrace();
                    }
                    user.setPokeSlots(selectedPokemonList);
                    user.setCurrentPokemon(user.getPokeSlots().get(0));
                    btnHeal.setEnabled(true);
                    btnStartJourney.setEnabled(true);
                    model1.setElementAt(model1.getElementAt(lstIndices[i]) + " (Selected)", lstIndices[i]);
                    btnStartBattle.setEnabled(true);
                }
            }
        });

        //Remove Pokemon
        btnRemove.addActionListener(e -> {
            if (user.getPokeSlots().size() > 0) {
                Object[] lstValues = lstDisplayAllPokemon.getSelectedValuesList().toArray();
                int[] lstIndices = lstDisplayAllPokemon.getSelectedIndices();
                for (int i = 0; i < lstIndices.length; i++) {
                    String a = (String) lstValues[i];
                    //make sure the pokemon we've selected to remove was even selected
                    if (a.indexOf(")") > 0) {
                        lstValues[i] = lstValues[i].toString().substring(0, lstValues[i].toString().length() - 11);
                        for (Pokemon p: selectedPokemonList) {
                            if(p.name.equals(pokemonList.get(lstIndices[i]).name)) {
                                selectedPokemonList.remove(p);
                                break;
                            }
                        }
                        user.setPokeSlots(selectedPokemonList);
                        model1.setElementAt(lstValues[i].toString(), lstIndices[i]);
                        if (user.getPokeSlots().size() == 0) {
                            btnStartBattle.setEnabled(false);
                            btnHeal.setEnabled(false);
                            btnStartJourney.setEnabled(false);
                        }
                    }
                }
            }
        });

        btnHeal.addActionListener(e -> {
            try {
                user.fullyHealPokemon();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
            btnStartBattle.setEnabled(user.getPokeSlots().size()>0);
        });

        btnStartBattle.addActionListener(e -> {
            BattleGUI battleGUI = new BattleGUI(user, gui);
            gui.setUi(battleGUI.getClass().getSimpleName());
            gui.setBackgroundImage(battleGUI.getClass().getSimpleName());
            battleGUI.displayAIPokemonInfo();
            battleGUI.displayHtPokemonInfo();
            btnStartBattle.setEnabled(false);
        });

        btnItemShop.addActionListener(e -> {
            if(itemShop == null)
                itemShop = new ItemShop(user, gui);
            gui.setUi(itemShop.getClass().getSimpleName());
        });

        btnStartJourney.addActionListener(e -> {
            if (adventureModeMain == null)
                adventureModeMain = new AdventureModeMain(user, gui);

            gui.setUi(adventureModeMain.getClass().getSimpleName());
            gui.setBackgroundImage(null);
            btnStartJourney.setText("Resume Journey");
            //If in adventure mode guiManager is only needed for battleGUI at the moment. Shutdown thread??
            //gui.shutoffTimer();
        });

        setOpaque(false);
        gui.addNewBackgroundImage(getClass().getSimpleName(), mainMenuBackground);
        gui.setBackgroundImage(getClass().getSimpleName());
        gui.addNewUi(this);
        gui.setUi(getClass().getSimpleName());
    }

    private void createPokemon() {
        model1 = new DefaultListModel<>();
        lstDisplayAllPokemon = new JList<>(model1);
        Moves<String, Move> moves = new Moves<>();

        Pokemon articuno = new Articuno(11, moves.get("Ice Beam", "Weak Move", "Hyper Beam", "Blizzard"));
        Pokemon rayquaza = new Rayquaza(10, moves.get("Diamond Storm", "Hyper Beam", "Fire Blast", "Ice Beam"));
        Pokemon gengar = new MegaGengar(9, moves.get("Ice Beam", "Weak Move", "Shadow Ball", "Fire Blast"));

        pokemonList.add(articuno);
        pokemonList.add(rayquaza);
        pokemonList.add(gengar);
        model1.addElement(articuno.name);
        model1.addElement(rayquaza.name);
        model1.addElement(gengar.name);
    }
}
