package MainMenu;

import AdventureMode.DefaultNPC;
import AdventureMode.NPC;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class BattleGUI extends JPanel {
    private JLabel pokemonGif, opponentpokemonGif, lblMiddlePokemonName, lblMiddlePokemonLvl;
    private JProgressBar hp, ExpBar;
    private JPanel pnlBottomLeft, pnlBottomRight, borderSouth, pnlBottomLeftMoves;
    private PokemonInPartyPanel pokemonInPartyPanel;
    private Backpack backpack;
    private GridBagConstraints c;
    private Font f;
    private JProgressBar AIhp;
    private JLabel lblOponnentPokemonName, lblOponnentPokemonLvl;
    private BattleManager battleManager;
    private AITrainer ai;
    private Pokemon currentPokemon;
    private HumanTrainer humanTrainer;
    private Image background = new ImageIcon("src/imagesUI/PokeballFlat.png").getImage();

    private JButton btnCollapseDialogPanel = new JButton();
    private JButton btnExpandDialogPanel = new JButton();
    private JTextArea txtLeftDefaultText = new JTextArea(2, 20);
    private JButton btnContinue;


    public BattleGUI(HumanTrainer pHumanTrainer, GUIManager gui) {
        AITrainer.canMove = false;
        gui.addNewBackgroundImage(getClass().getSimpleName(), background);
        gui.addNewUi(this);

        this.humanTrainer = pHumanTrainer;
        humanTrainer.setDefeated(false);
        for (Pokemon p : humanTrainer.getPokeSlots()) {
            if (p.hp > 0) {
                humanTrainer.setCurrentPokemon(p);
                break;
            }
        }
        createBtnContinue();
        //Create users backpack panel and pokemon panel which are both access by buttons on the ui
        JButton badButton = new JButton() {

            @Override
            public void doClick() {
                battleManager.turnManager();
            }
        };
        pokemonInPartyPanel = new PokemonInPartyPanel(humanTrainer, gui, btnContinue, txtLeftDefaultText, btnCollapseDialogPanel, btnExpandDialogPanel, badButton);
        backpack = new Backpack(humanTrainer, gui);

        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.setOpaque(false);

        createGui(gui);
        if(humanTrainer.isInAdventureMode()) {
            battleManager = new BattleManager(humanTrainer, btnContinue, txtLeftDefaultText, btnCollapseDialogPanel, gui);
            if (humanTrainer.getPokemonOpponent() != null) {
                createBattleGuiNorth();
            }
            createBattleGuiNorth();
        } else {
            createOpponent();
            createBattleGuiNorth();
            battleManager = new BattleManager(humanTrainer, ai, btnContinue, txtLeftDefaultText, btnCollapseDialogPanel, gui);
        }
    }
    //This button has many implementations that each do different things. However the only way I've found to make this
    //work properly is to remove all action listeners from the button after each click. The reason super is called twice
    //is so I can run the following action performed method every time this button is pressed. Without it and the extra
    //call to super. I would have to repeat the following foreach everywhere I want to use the button.
    private void createBtnContinue() {

        btnContinue = new JButton("Continue") {

            private ActionListener al = e -> {
                for (ActionListener al : getActionListeners()) {
                    removeActionListener(al);
                }
            };
            @Override
            public void addActionListener(ActionListener l) {
                super.addActionListener(l);
                super.addActionListener(al);
                displayHtPokemonInfo();
                displayAIPokemonInfo();
            }
        };
    }
    private void createGui(GUIManager gui) {

        Pokemon currentPokemon = humanTrainer.getCurrentPokemon();
        borderSouth = new JPanel(new GridBagLayout());
        pnlBottomRight = new JPanel(new GridBagLayout());
        pnlBottomLeft = new JPanel(new BorderLayout());
        JPanel pnlMiddleRight = new JPanel(new GridBagLayout());
        JPanel pnlMiddleLeft = new JPanel(new GridBagLayout());
        JPanel pnlMiddleRightHp = new JPanel(new GridBagLayout());
        pnlBottomLeftMoves = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();

        JButton btnFight = new SetBtnProperties("Fight");
        JButton btnItems = new SetBtnProperties("Items");
        JButton btnSwap = new SetBtnProperties("Swap");
        JButton btnRun = new SetBtnProperties("Run");

        lblMiddlePokemonName = new JLabel(currentPokemon.name);
        lblMiddlePokemonLvl = new JLabel("Lvl: " + currentPokemon.lvl);
        JLabel lblMiddlePokemonHP = new JLabel("HP");
        JLabel lblMiddlePokemonExp = new JLabel("EXP");
        Color pale = new Color(248, 248, 216);
        Color menu = new Color(200, 55, 55);
        pnlBottomLeft.setBackground(menu);
        pnlMiddleRight.setBackground(pale);
        pnlMiddleRightHp.setBackground(pale);
        pnlMiddleLeft.setOpaque(false);
        borderSouth.setOpaque(false);
        pnlBottomLeftMoves.setVisible(false);
        f = new Font("Times New Roman", Font.PLAIN, 24);
        btnFight.setFont(f);
        btnItems.setFont(f);
        btnSwap.setFont(f);
        btnRun.setFont(f);
        lblMiddlePokemonName.setFont(f);
        lblMiddlePokemonLvl.setFont(f);

        Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border redLine = BorderFactory.createLineBorder(menu, 15);
        pnlBottomLeft.setBorder(getBorder(raisedetched, loweredetched, raisedbevel, loweredbevel, redLine));

        hp = new PokemonProgressBar(currentPokemon.hp, currentPokemon.fullHealth);
        ExpBar = new PokemonProgressBar(
                currentPokemon.Exp - (int) Math.pow(currentPokemon.lvl, 3), // current exp
                (int) Math.pow(currentPokemon.lvl + 1, 3) - (int) Math.pow(currentPokemon.lvl, 3)); //exp needed to lvl up when at a certain lvl
        pokemonGif = new gifLbl("H/" + currentPokemon.name);

        //Bottom Left
        Dimension dd = new Dimension(pnlBottomLeft.getWidth(), 145);
        txtLeftDefaultText.setPreferredSize(dd);
        txtLeftDefaultText.setText("What will " + currentPokemon.name + " do?");
        txtLeftDefaultText.setWrapStyleWord(true);
        txtLeftDefaultText.setLineWrap(true);
        txtLeftDefaultText.setEditable(false);
        txtLeftDefaultText.setFocusable(false);
        txtLeftDefaultText.setBackground(menu);
        txtLeftDefaultText.setFont(f);
        txtLeftDefaultText.setBorder(UIManager.getBorder("Label.border"));

        displayHtPokemonInfo();

        btnContinue.setVisible(false);
        pnlBottomLeft.add(txtLeftDefaultText, BorderLayout.NORTH);
        pnlBottomLeft.add(btnContinue, BorderLayout.PAGE_END);
        c.fill = 1;
        constraintsForPanelBottomLeft();

        c.weightx = 0.5;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 2;
        borderSouth.add(pnlBottomLeftMoves, c);
        //Bottom Right
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridy = 0;
        pnlBottomRight.add(btnFight, c);
        c.gridx = 1;
        c.gridy = 0;
        pnlBottomRight.add(btnItems, c);
        c.gridx = 0;
        c.gridy = 1;
        pnlBottomRight.add(btnSwap, c);
        c.gridx = 1;
        c.gridy = 1;
        pnlBottomRight.add(btnRun, c);
        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.weightx = 0.35;
        borderSouth.add(pnlBottomRight, c);
        c.weightx = 0.5;
        //Middle Left
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        pnlMiddleLeft.add(pokemonGif, c);
        borderSouth.add(pnlMiddleLeft, c);
        //Middle Right
        c.gridx = 1;
        c.gridwidth = 1;
        c.insets = new Insets(0, 10, 2, 10);
        pnlMiddleRight.add(lblMiddlePokemonName, c);
        lblMiddlePokemonLvl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        pnlMiddleRight.add(lblMiddlePokemonLvl, c);

        //Hp
        c.gridx = 0;
        c.ipady = 0;
        c.weightx = 0.35;
        lblMiddlePokemonHP.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        c.insets = new Insets(0, 0, 2, 5);
        pnlMiddleRightHp.add(lblMiddlePokemonHP, c);
        c.gridx = 1;
        c.weightx = .65;
        pnlMiddleRightHp.add(hp, c);
        c.gridy = 1;
        c.gridx = 0;
        c.weightx = 1;
        c.gridwidth = 2;
        pnlMiddleRight.add(pnlMiddleRightHp, c);
        //Exp
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.insets = new Insets(0, 5, 2, 0);
        lblMiddlePokemonExp.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        pnlMiddleRight.add(lblMiddlePokemonExp, c);
        c.gridx = 1;
        c.weightx = 2;
        c.insets = new Insets(0, 5, 2, 5);
        pnlMiddleRight.add(ExpBar, c);

        c.fill = 2;
        c.weightx = 0.5;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridx = 3;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.SOUTH;
        borderSouth.add(pnlMiddleRight, c);
        btnFight.addActionListener(e -> {
            if (pnlBottomLeft.isVisible()) {
                pnlBottomLeft.setVisible(false);
                pnlBottomLeftMoves.setVisible(true);
            } else {
                pnlBottomLeft.setVisible(true);
                pnlBottomLeftMoves.setVisible(false);
            }
        });
        btnCollapseDialogPanel.addActionListener(e -> collapseDialogPanel());

        btnExpandDialogPanel.addActionListener(e -> {
            expandDialogPanel();
            displayHtPokemonInfo();
        });
        btnSwap.addActionListener(e -> {
            pnlBottomLeftMoves.setVisible(false);
            pnlBottomLeft.setVisible(true);
            gui.setBackgroundImage(pokemonInPartyPanel.getClass().getSimpleName());
            gui.setUi(pokemonInPartyPanel.getClass().getSimpleName());
        });
        btnItems.addActionListener(e -> {
            pnlBottomLeftMoves.setVisible(false);
            pnlBottomLeft.setVisible(true);
            gui.setUi(backpack.getClass().getSimpleName());
            backpack.setVisible(true);
        });
        btnRun.addActionListener(e -> {
            //this will only be true if user is in adventure mode
            if (humanTrainer.isInAdventureMode() && humanTrainer.getNpcOpponent() != null) {
                txtLeftDefaultText.setText("You cannot run from a trainer battle!");
            } else {
                expandDialogPanel();
                btnContinue.setVisible(true);
                humanTrainer.setDefeated(true);
                battleManager.battleEnd();
            }
            pnlBottomLeftMoves.setVisible(false);
            pnlBottomLeft.setVisible(true);

        });

        this.add(borderSouth, BorderLayout.SOUTH);
    }

    @NotNull
    static Border getBorder(Border raisedetched, Border loweredetched, Border raisedbevel, Border loweredbevel, Border redLine) {
        Border compound;
        Border thick = (BorderFactory.createStrokeBorder(new BasicStroke(10.0f)));
        compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
        compound = BorderFactory.createCompoundBorder(redLine, compound);
        compound = BorderFactory.createCompoundBorder(thick, compound);
        compound = BorderFactory.createCompoundBorder(loweredetched, compound);
        compound = BorderFactory.createCompoundBorder(raisedetched, compound);
        //empty = BorderFactory.createEmptyBorder();
        return compound;
    }

    private void constraintsForPanelBottomLeft() {
        c.ipady = 50;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.65;
        borderSouth.add(pnlBottomLeft, c);
    }

    void displayHtPokemonInfo() {
        JLabel j = new gifLbl("H/" + humanTrainer.getCurrentPokemon().name);
        pokemonGif.setIcon(j.getIcon());
        lblMiddlePokemonName.setText(humanTrainer.getCurrentPokemon().name);
        lblMiddlePokemonLvl.setText("Lvl: " + humanTrainer.getCurrentPokemon().lvl);
        hp.setMaximum(humanTrainer.getCurrentPokemon().fullHealth);
        hp.setValue(humanTrainer.getCurrentPokemon().hp);
        ExpBar.setMaximum((int) Math.pow(humanTrainer.getCurrentPokemon().lvl + 1, 3) - (int) Math.pow(humanTrainer.getCurrentPokemon().lvl, 3));
        ExpBar.setValue(humanTrainer.getCurrentPokemon().Exp - (int) Math.pow(humanTrainer.getCurrentPokemon().lvl, 3));
        moveButtons();
    }

    private void moveButtons() {
        pnlBottomLeftMoves.removeAll();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.weighty = 0.5;
        c.weightx = 0.5;
        int i = 0;
        for (Move move : humanTrainer.getCurrentPokemon().moves) {
            c.gridy = (i >= 2) ? 1 : 0;
            pnlBottomLeftMoves.add(btn(new JButton("<html> " + move.getName() + " <br> " + move.getPp() + "/" + move.getMaxpp() + " " + move.getMoveType() + " </html>"), move), c);
            i++;
        }
    }

    private JButton btn(JButton Jb, Move move) {
        for (ActionListener al : Jb.getActionListeners()) {
            Jb.removeActionListener(al);
            System.out.println("There's an action listen" + getClass() + " " + getClass().getEnclosingMethod().getName());
        }
        if (move.getPp() > 0)
            Jb.addActionListener(e -> {
                expandDialogPanel();
                humanTrainer.setChosenMove(move);
                battleManager.turnManager();

            });
        Jb.setFont(f);
        return Jb;
    }

    void expandDialogPanel() {
        pnlBottomLeft.setVisible(true);
        borderSouth.remove(pnlBottomLeft);
        borderSouth.remove(pnlBottomRight);
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 1;
        borderSouth.add(pnlBottomLeft, c);
        pnlBottomLeftMoves.setVisible(false);
        btnContinue.setVisible(true);
    }

    void collapseDialogPanel() {
        borderSouth.remove(pnlBottomLeft);
        constraintsForPanelBottomLeft();
        c.fill = 1;
        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.weightx = 0.35;
        c.weighty = 1;
        borderSouth.add(pnlBottomRight, c);
        pnlBottomLeft.setVisible(false);
        pnlBottomLeft.setVisible(true);
        txtLeftDefaultText.setText("What will " + humanTrainer.getCurrentPokemon().name + " do next?");
        btnContinue.setVisible(false);
    }

    private void createOpponent() {
        ai = new DefaultNPC();
        ai.setName("Lebron James");
        humanTrainer.setOpponent((NPC) ai);
    }
    private void createBattleGuiNorth() {

        if (humanTrainer.getNpcOpponent() != null && humanTrainer.getNpcOpponent().getPokeSlots().size() > 0) {
            ai = humanTrainer.getNpcOpponent();
                ai.setCurrentPokemon(ai.getPokeSlots().get(0));
                currentPokemon = ai.getPokeSlots().get(0);
        } else {
            currentPokemon = humanTrainer.getPokemonOpponent();
        }

        JPanel borderNorth = new JPanel(new GridBagLayout());
        JPanel pnlTopLeft = new JPanel(new GridBagLayout());
        JPanel pnlTopLeftHp = new JPanel(new GridBagLayout());
        JPanel pnlTopRight = new JPanel(new GridBagLayout());
        JPanel pnlTopMiddle = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        lblOponnentPokemonName = new JLabel(currentPokemon.name);
        lblOponnentPokemonLvl = new JLabel("Lvl: " + currentPokemon.lvl);
        JLabel lblTopPokemonHP = new JLabel("HP");
        Color green = new Color(248, 248, 216);
        pnlTopLeft.setBackground(green);
        pnlTopRight.setBackground(green);
        pnlTopLeftHp.setBackground(green);
        Font f = new Font("Times New Roman", Font.PLAIN, 24);
        lblOponnentPokemonName.setFont(f);
        lblOponnentPokemonLvl.setFont(f);

        opponentpokemonGif = new gifLbl("Ai/" + currentPokemon.name);

        borderNorth.setBackground(Color.RED);
        pnlTopRight.setOpaque(false);
        borderNorth.setOpaque(false);
        AIhp = new PokemonProgressBar(currentPokemon.hp, currentPokemon.fullHealth);

        displayAIPokemonInfo();

        c.fill = 1;
        c.ipady = 20;
        c.weightx = 0.5;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(0, 10, 2, 10);
        pnlTopLeft.add(lblOponnentPokemonName, c);
        c.gridx = 1;
        lblOponnentPokemonLvl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        pnlTopLeft.add(lblOponnentPokemonLvl, c);
        //Hp
        c.gridy = 0;
        c.gridx = 0;
        c.ipady = 0;
        c.weightx = 0.35;
        lblTopPokemonHP.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        c.insets = new Insets(0, 0, 2, 5);
        pnlTopLeftHp.add(lblTopPokemonHP, c);
        c.gridx = 1;
        c.weightx = .65;
        c.fill = 2;
        pnlTopLeftHp.add(AIhp, c);
        c.gridy = 1;
        c.gridx = 0;
        c.weightx = 1;
        c.gridwidth = 2;
        pnlTopLeft.add(pnlTopLeftHp, c);
        c.gridwidth = 1;
        c.weightx = 0.25;
        c.gridy = 0;
        c.gridx = 0;
        pnlTopRight.add(opponentpokemonGif, c);
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(30, 0, 0, 0);
        borderNorth.add(pnlTopLeft, c);
        c.gridx = 1;
        c.weightx = 0.45;
        borderNorth.add(pnlTopMiddle, c);
        c.fill = 1;
        c.gridx = 2;
        c.weightx = 0.30;
        c.insets = new Insets(0, 0, 0, 0);
        borderNorth.add(pnlTopRight, c);
        this.add(borderNorth, BorderLayout.NORTH);
    }

    public void displayAIPokemonInfo() {
        if (ai != null) {
            currentPokemon = ai.getCurrentPokemon();
        }
        JLabel j = new gifLbl("Ai/" + currentPokemon.name);
        opponentpokemonGif.setIcon(j.getIcon());
        lblOponnentPokemonName.setText(currentPokemon.name);
        lblOponnentPokemonLvl.setText("Lvl: " + currentPokemon.lvl);
        AIhp.setMaximum(currentPokemon.fullHealth);
        AIhp.setValue(currentPokemon.hp);
    }

    public Image getBackgroundImage() {
        return background;
    }
}
