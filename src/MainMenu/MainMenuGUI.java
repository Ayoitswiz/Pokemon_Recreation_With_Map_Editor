package MainMenu;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import AdventureMode.AdventureModeMain;
import AdventureMode.MySprite;

public class MainMenuGUI extends JPanel{

    private JList<DefaultListModel<String>>  lstDisplayAllPokemon;
    private DefaultListModel<String> model1;
    private ArrayList<Pokemon> pokemonList = new ArrayList<>();
    public ArrayList<Pokemon> selectedPokemonList = new ArrayList<>();
    private int maxPokemon = 0;
    public static final Image mainMenuBackground = new ImageIcon("src/imagesUI/MainMenuBackground.jpg").getImage();
    //SHARED USAGE BETWEEN PACKAGES
    private boolean isBattleOver = false;
    public JButton btnStartBattle;
    private ItemShop itemShop;
    private AdventureModeMain adventureModeMain;
    public MainMenuGUI() throws IOException {
        MySprite humanTrainer = new MySprite(new BigDecimal("10000.00"), "Ash");
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
                         Pokemon p = (Pokemon) pokemonList.get(lstIndices[i]).clone();

                         selectedPokemonList.add(p);
                     } catch (CloneNotSupportedException ex) {
                         ex.printStackTrace();
                     }
                     Move[] move;
                     for (Pokemon selectedP: selectedPokemonList) {
                         int moveNum = selectedP.moves.length;
                         move = new Move[moveNum];
                         int slot = 0;
                         for (Move m : selectedP.moves) {
                             try {
                                 move[slot] = (Move) m.clone();
                             } catch (CloneNotSupportedException ex) {
                                 ex.printStackTrace();
                             }
                             slot++;
                         }
                         selectedP.moves = move;
                     }
                     humanTrainer.setPokeSlots(selectedPokemonList);
                     humanTrainer.currentPokemon = humanTrainer.getPokeSlots().get(0);
                     btnHeal.setEnabled(true);
                     btnStartJourney.setEnabled(true);
                     model1.setElementAt(model1.getElementAt(lstIndices[i]) + " (Selected)", lstIndices[i]);
                     btnStartBattle.setEnabled(true);
                    maxPokemon++;
                 }
             }
         });

         //Remove Pokemon
         btnRemove.addActionListener(e -> {
             if (maxPokemon > 0) {
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
                         humanTrainer.setPokeSlots(selectedPokemonList);
                         model1.setElementAt(lstValues[i].toString(), lstIndices[i]);
                         if (humanTrainer.getPokeSlots().size() == 0) {
                             btnStartBattle.setEnabled(false);
                             btnHeal.setEnabled(false);
                             btnStartJourney.setEnabled(false);
                         }
                         maxPokemon--;
                     }
                 }
             }
         });

         btnHeal.addActionListener(e -> {
             try {
                 humanTrainer.fullyHealPokemon();
             } catch (NullPointerException ex) {
                 ex.printStackTrace();
             }
             if(humanTrainer.getPokeSlots().size()>0)
                 btnStartBattle.setEnabled(true);
         });

         btnStartBattle.addActionListener(e -> {
             BattleGUI battleGUI = new BattleGUI(humanTrainer, gui);
             gui.setUi(battleGUI.getClass().getSimpleName());
             gui.setBackgroundImage(battleGUI.getClass().getSimpleName());
             battleGUI.displayAIPokemonInfo();
             battleGUI.displayHtPokemonInfo();
             btnStartBattle.setEnabled(false);
         });

         btnItemShop.addActionListener(e -> {
             if(itemShop == null)
             itemShop = new ItemShop(humanTrainer, gui);
             gui.setUi(itemShop.getClass().getSimpleName());
         });

        btnStartJourney.addActionListener(e -> {
            if (adventureModeMain == null)
            adventureModeMain = new AdventureModeMain(humanTrainer, gui);

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
        lstDisplayAllPokemon = new JList(model1);
        Move move = new Move();
        HashMap<String, Move> moves = move.createMove();

        Pokemon articuno = new Articuno(11, new Move[]{moves.get("Ice Beam"), moves.get("Weak Move"), moves.get("Hyper Beam"), moves.get("Blizzard")});
        Pokemon rayquaza = new Rayquaza(10,new Move[]{moves.get("Diamond Storm"), moves.get("Hyper Beam"), moves.get("Fire Blast"), moves.get("Ice Beam")});
        Pokemon gengar = new MegaGengar(9, new Move[]{moves.get("Ice Beam"), moves.get("Weak Move"), moves.get("Shadow Ball"), moves.get("Fire Blast")});

        pokemonList.add(articuno);
        pokemonList.add(rayquaza);
        pokemonList.add(gengar);
        model1.addElement(articuno.name);
        model1.addElement(rayquaza.name);
        model1.addElement(gengar.name);
    }
}
