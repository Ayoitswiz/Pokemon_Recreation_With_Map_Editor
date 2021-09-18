package gg.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import AdventureMode.AdventureModeMain;
import AdventureMode.DefaultNPC;
import AdventureMode.MySprite;
import AdventureMode.NPC;
import gg.Battle.BattleGUI;
import gg.ExitFunctionException;
import gg.GUIManager;
import gg.InGameItems.ItemShop;
import gg.Pokemon.Moves.Move;
import gg.Pokemon.Moves.Moves;
import gg.Pokemon.CreatePokemon;
import gg.Pokemon.Pokemon;
import gg.Styles.ErrorLabel;
import gg.Styles.SetBtnProperties;
import gg.Battle.Trainers.AITrainer;

import static gg.Pokemon.Moves.eMoves.BLIZZARD;
import static gg.Pokemon.Moves.eMoves.DIAMOND_STORM;
import static gg.Pokemon.Moves.eMoves.FIRE_BLAST;
import static gg.Pokemon.Moves.eMoves.ICE_BEAM;
import static gg.Pokemon.Moves.eMoves.SHADOW_BALL;
import static gg.Pokemon.Moves.eMoves.STRONG_MOVE;
import static gg.Pokemon.Moves.eMoves.WEAK_MOVE;

public class MainMenuGUI extends JPanel{

    public static final String name = MainMenuGUI.class.getSimpleName();

    private static final Image mainMenuBackground = new ImageIcon("src/img/UI/MainMenuBackground.jpg").getImage();
    private static JList<? extends String> lstDisplayAllPokemon;
    private static JButton btnStartBattle;
    private static AdventureModeMain adventureModeMain;
    private DefaultListModel<String> model1;
    private ArrayList<Pokemon> pokemonList = new ArrayList<>();
    private ArrayList<Pokemon> selectedPokemonList = new ArrayList<>();
    private MySprite ht;


    public MainMenuGUI() {
        ht = new MySprite(new BigDecimal("10000.00"), "Ash");
        ErrorLabel errorLabel = new ErrorLabel().setDefaultProperties();
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
        c.gridy = 5;
        pane.add(errorLabel, c);

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
                            move = new Move[selectedP.getMoves().length];
                            int slot = 0;
                            for (Move m : selectedP.getMoves()) {
                                move[slot++] = (Move) m.clone();
                            }
                            selectedP.setMoves(move);
                        }
                    } catch (CloneNotSupportedException ex) {
                        ex.printStackTrace();
                    }
                    selectedPokemonList.forEach(poke -> ht.getPokeSlots().add(poke));
                    model1.setElementAt(model1.getElementAt(lstIndices[i]) + " (Selected)", lstIndices[i]);
                }
            }
        });

        //Remove Pokemon
        btnRemove.addActionListener(e -> {
            if (ht.getPokeSlots().size() > 0) {
                Object[] lstValues = lstDisplayAllPokemon.getSelectedValuesList().toArray();
                int[] lstIndices = lstDisplayAllPokemon.getSelectedIndices();
                for (int i = 0; i < lstIndices.length; i++) {
                    String a = (String) lstValues[i];
                    //make sure the pokemon we've selected to remove was even selected
                    if (a.indexOf(")") > 0) {
                        lstValues[i] = lstValues[i].toString().substring(0, lstValues[i].toString().length() - 11);
                        for (Pokemon p: selectedPokemonList) {
                            if(p.getName().equals(pokemonList.get(lstIndices[i]).getName())) {
                                selectedPokemonList.remove(p);
                                break;
                            }
                        }
                        selectedPokemonList.forEach(poke -> ht.getPokeSlots().remove(poke));
                        model1.setElementAt(lstValues[i].toString(), lstIndices[i]);
                    }
                }
            }
        });

        btnHeal.addActionListener(e -> {
            if (ht.getPokeSlots().size() < 1) throw new ExitFunctionException("You cannot heal your pokemon if you don't have any!", errorLabel);
                ht.fullyHealPokemon();
        });

        btnStartBattle.addActionListener(e -> {
            if (ht.getPokeSlots().size() < 1) throw new ExitFunctionException("You cannot start a battle with 0 pokemon!", errorLabel);
            if (ht.isOutOfUsablePokemon()) throw new ExitFunctionException("You cannot start a battle if all your pokemon are fainted!", errorLabel);
            AITrainer ai = new DefaultNPC();
            ht.setOpponent(ai);
            new BattleGUI(ht, ai);
        });

        btnItemShop.addActionListener(e -> new ItemShop(ht));

        btnStartJourney.addActionListener(e -> {
            if (ht.getPokeSlots().size() < 1) throw new ExitFunctionException("You cannot start a battle with 0 pokemon!", errorLabel);
            if (ht.isOutOfUsablePokemon()) throw new ExitFunctionException("You cannot start a battle if all your pokemon are fainted!", errorLabel);
            if (adventureModeMain == null)
                adventureModeMain = new AdventureModeMain(ht);

            GUIManager.setUi(adventureModeMain.getClass().getSimpleName());
            GUIManager.setBackgroundImage(null);
            btnStartJourney.setText("Resume Journey");
        });

        setOpaque(false);
        GUIManager.addNewBackgroundImage(getClass().getSimpleName(), mainMenuBackground);
        GUIManager.setBackgroundImage(getClass().getSimpleName());
        GUIManager.addNewUi(this);
        GUIManager.setUi(getClass().getSimpleName());
    }

    private void createPokemon() {
        model1 = new DefaultListModel<>();
        lstDisplayAllPokemon = new JList<>(model1);
        Pokemon articuno = CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), ht);
        Pokemon rayquaza = CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), ht);
        Pokemon gengar = CreatePokemon.MegaGengar(9, Moves.Move(ICE_BEAM, WEAK_MOVE, SHADOW_BALL, STRONG_MOVE), ht);

        Pokemon articuno2 = CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), ht);
        Pokemon rayquaza2 = CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), ht);
        Pokemon gengar2 = CreatePokemon.MegaGengar(9, Moves.Move(ICE_BEAM, WEAK_MOVE, SHADOW_BALL, STRONG_MOVE), ht);


        ht.getPokeSlots().add(articuno);
        ht.getPokeSlots().add(rayquaza);
        ht.getPokeSlots().add(gengar);

        ht.getPokeSlots().add(articuno2);
        ht.getPokeSlots().add(rayquaza2);
        ht.getPokeSlots().add(gengar2);

        pokemonList.add(articuno);
        pokemonList.add(rayquaza);
        pokemonList.add(gengar);
        model1.addElement(articuno.getName());
        model1.addElement(rayquaza.getName());
        model1.addElement(gengar.getName());
    }
}
