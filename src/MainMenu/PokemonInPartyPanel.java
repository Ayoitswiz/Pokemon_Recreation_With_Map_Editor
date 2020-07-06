package MainMenu;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PokemonInPartyPanel {

    private HumanTrainer humanTrainer;
    private GridBagConstraints c;
    private JButton confirm, btnContinue, btnCollapseDialog, btnExpandDialog;
    private JTextArea gameDialog;
    private JPanel pokemonInPartyPanel, pnlPokemonInParty;
    private JProgressBar healthBar;
    private ArrayList<JProgressBar> healthBarList = new ArrayList<>();
    private GUIManager gui;
    private Pokemon selectedPokemon;
    private ArrayList<JPanel> gifPnlList = new ArrayList<>();
    JButton close;
    Border raisedetched, loweredetched, raisedbevel, loweredbevel;

    public PokemonInPartyPanel(HumanTrainer pHumanTrainer, GUIManager pGui) {
        humanTrainer = pHumanTrainer;
        gui = pGui;
        Image background = new ImageIcon("src/imagesUI/Pokeball.png").getImage();
        close = new JButton("CLOSE");
        pnlPokemonInParty = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        pokemonInPartyPanel = new JPanel(new BorderLayout());
        createEachPokemonsPanel();

        close.addActionListener(e -> {
            humanTrainer.setUseItem(null);
            resetSelectedPokemonOnClose();
            selectedPokemon = null;
                gui.setUi("AdventureModeMain");

        });

        pokemonInPartyPanel.setOpaque(false);
        pnlPokemonInParty.setOpaque(false);
        pokemonInPartyPanel.add(pnlPokemonInParty, BorderLayout.CENTER);
        pokemonInPartyPanel.add(close, BorderLayout.SOUTH);

        gui.addNewBackgroundImage(getClass().getSimpleName(), background);
        gui.addNewUi(getClass().getSimpleName(), pokemonInPartyPanel);
    }

    public PokemonInPartyPanel(HumanTrainer pHumanTrainer, GUIManager pGui, JButton pBtnContinue, JTextArea pGameDialog, JButton pBtnCollapseDialog, JButton pBtnExpandDialog, JButton badButton) {
        humanTrainer = pHumanTrainer;
        gui = pGui;
        Image background = new ImageIcon("src/imagesUI/Pokeball.png").getImage();
        close = new JButton("CLOSE");

        btnContinue = pBtnContinue;
        gameDialog = pGameDialog;
        btnCollapseDialog = pBtnCollapseDialog;
        btnExpandDialog = pBtnExpandDialog;
        pnlPokemonInParty = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        confirm = new JButton("CONFIRM");
        pokemonInPartyPanel = new JPanel(new BorderLayout()) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (humanTrainer.currentPokemon.hp <= 0) {
                    close.setEnabled(false);
                }

                updateHealthBar();
                int i = 0;
                for (JPanel jp : gifPnlList) {
                    if ((humanTrainer.currentPokemon == humanTrainer.getPokeSlots().get(i) || humanTrainer.getPokeSlots().get(i).isFainted()) && humanTrainer.getSelectedItem() == null) {
                        jp.getParent().setEnabled(false);
                    } else {
                        jp.getParent().setEnabled(true);
                    }
                    i++;
                }
            }
        };

        gui.addNewBackgroundImage(getClass().getSimpleName(), background);
        gui.addNewUi(getClass().getSimpleName(), pokemonInPartyPanel);

        createEachPokemonsPanel();

        close.addActionListener(e -> {
            humanTrainer.setUseItem(null);
            resetSelectedPokemonOnClose();
            selectedPokemon = null;
            gui.setBackgroundImage("BattleGUI");
            gui.setUi("BattleGUI");

        });

        confirm.addActionListener(e -> {
            //if the trainers pokemon faints they get to swap out their pokemon without losing a turn.
            //If the trainer swaps when his current pokemon's hp > 0 he loses a turn.
            if (!humanTrainer.currentPokemon.isFainted()) {
                humanTrainer.setLosesTurn(true);
            }
            if (humanTrainer.getSelectedItem() == null) {
                updateCurrentPokemon();
                boolean isUserSwappingOutANonFaintedPokemon = false;
                if (!humanTrainer.currentPokemon.isFainted())
                    isUserSwappingOutANonFaintedPokemon = true;
                humanTrainer.currentPokemon = selectedPokemon;
                if (isUserSwappingOutANonFaintedPokemon)
                    humanTrainer.currentPokemon.setHasUsedTurn(true);
                gameDialog.setText("Go " + humanTrainer.currentPokemon.name + "!");
            } else {
                resetSelectedPokemonOnClose();
                gameDialog.setText(humanTrainer.getName() + " used a " + humanTrainer.getSelectedItem().name);
            }
            btnExpandDialog.doClick();
            gui.setBackgroundImage("BattleGUI");
            gui.setUi("BattleGUI");
            confirm.setEnabled(false);
            selectedPokemon = null;
            close.setEnabled(true);
            btnContinue.addActionListener(ee -> {
                if (humanTrainer.hasLostTurn() || humanTrainer.getSelectedItem() != null) {
                    badButton.doClick();
                    humanTrainer.setUseItem(null);
                } else {
                    btnCollapseDialog.doClick();
                }
                humanTrainer.setLosesTurn(false);
            });
        });
        confirm.setEnabled(false);
        pokemonInPartyPanel.setOpaque(false);
        pnlPokemonInParty.setOpaque(false);
        pokemonInPartyPanel.add(pnlPokemonInParty, BorderLayout.CENTER);
        pokemonInPartyPanel.add(confirm, BorderLayout.NORTH);
        pokemonInPartyPanel.add(close, BorderLayout.SOUTH);
    }

    private void createEachPokemonsPanel() {
        int i = 0;
        for (Pokemon p : humanTrainer.getPokeSlots()) {
            c.gridy = 0;
            c.gridx = i;
            c.fill = 1;
            c.weightx = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weighty = .10;
            c.ipady = 0;
            pnlPokemonInParty.add(new JPanel() {{
                setOpaque(false);
            }}, c);
            c.weighty = .30;
            c.insets = new Insets(0, 5, 0, 5);
            c.anchor = GridBagConstraints.SOUTH;
            c.gridy = 1;
            pnlPokemonInParty.add(new PokemonPanelNameLbl(Color.WHITE, p.name, p.type), c);
            c.weighty = 1;
            c.gridy = 2;
            c.fill = 1;
            c.anchor = GridBagConstraints.CENTER;

            int finalI = i;
            pnlPokemonInParty.add(new gifBtn(p.name, finalI) {
                {
                    this.addMouseListener(new java.awt.event.MouseAdapter() {
                        public synchronized void mouseEntered(MouseEvent evt) {
                            c.ipadx = 50;
                            c.fill = 1;
                            c.weightx = 1;
                            c.insets = new Insets(0, 0, 0, 0);
                            add(gifPnlList.get(finalI), c);
                            //the color change to the background was executing before the previous object resized, occasionally. The wait syncs the two up much more nicely and also
                            try {
                                wait(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            setBackground(Color.RED);
                            pnlPokemonInParty.repaint();
                            pnlPokemonInParty.validate();
                        }

                        public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                            if (selectedPokemon != humanTrainer.getPokeSlots().get(finalI) && humanTrainer.currentPokemon != humanTrainer.getPokeSlots().get(finalI)) {
                                c.ipadx = 0;
                                add(gifPnlList.get(finalI), c);
                                try {
                                    wait(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                setBackground(UIManager.getColor("control"));
                                pnlPokemonInParty.validate();
                            }
                        }
                    });
            }

            }, c);
            c.ipadx = 0;
            c.insets = new Insets(0, 5, 0, 5);
            c.gridy = 3;
            c.ipady = 10;
            c.fill = 2;
            c.anchor = GridBagConstraints.NORTH;
            healthBarList.add(new PokemonProgressBar(p.hp, p.fullHealth));
            pnlPokemonInParty.add(healthBarList.get(i), c);
            i++;
        }
    }

    private void updateHealthBar() {
        int i = 0;
        for (Pokemon p : humanTrainer.getPokeSlots()) {
            healthBarList.get(i).setMaximum(p.fullHealth);
            healthBarList.get(i).setValue(p.hp);
            i++;
        }
    }

    private void resetSelectedPokemonOnClose() {
        int j = 0;
            for(JPanel jp: gifPnlList) {
                if (selectedPokemon == humanTrainer.getPokeSlots().get(j)) {
                    changeToNonSelectedPokemon(j);
                }
                j++;
            }
        }

    private void updateSelectedPokemon(int i) {
        int j = 0;
        if (selectedPokemon != humanTrainer.getPokeSlots().get(i)) {
            for(JPanel jp: gifPnlList) {
                if (selectedPokemon == humanTrainer.getPokeSlots().get(j) && humanTrainer.getPokeSlots().get(j) != humanTrainer.getPokeSlots().get(i)) {
                    changeToNonSelectedPokemon(j);
                }
                j++;
            }
        }
    }

    private void updateCurrentPokemon() {
        int j = 0;
        for(JPanel jp: gifPnlList) {
            if (humanTrainer.currentPokemon == humanTrainer.getPokeSlots().get(j)) {
                changeToNonSelectedPokemon(j);
            }
            j++;
        }
    }

    private void changeToNonSelectedPokemon(int j) {

        c.ipadx = 0;
        c.gridx = j;
        c.gridy = 2;
        c.fill = 1;
        c.weighty = 1;
        c.weightx = 1;
        gifPnlList.get(j).getParent().setBackground(UIManager.getColor("control"));
        c.insets = new Insets(0, 0, 0, 0);
        gifPnlList.get(j).getParent().add(gifPnlList.get(j), c);

        c.insets = new Insets(0, 5, 0, 5);
        pnlPokemonInParty.add(gifPnlList.get(j).getParent(), c);
        pnlPokemonInParty.repaint();
        pnlPokemonInParty.validate();
    }


    class gifBtn extends JButton {
        private Image background;
        private int i;

        gifBtn(String name, int i) {
            background = new ImageIcon("src/imagesAi/" + name + ".gif").getImage();

            JPanel jp = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    //Adds a red box to the gifBtn to indicate this pokemon is the pokemon we currently have out in battle
                    if(humanTrainer.currentPokemon == humanTrainer.getPokeSlots().get(i) && !humanTrainer.currentPokemon.isFainted()) {
                        g.setColor(Color.RED);
                        g.fillRect(0, 0, getWidth(), getHeight());
                        getParent().setBackground(Color.RED);
                    }
                    g.drawImage(background, -gifBtn.this.getInsets().left, 0, getWidth() + (gifBtn.this.getInsets().right * 2), getHeight(), this);
                    g.setColor(Color.RED);
                    Graphics2D g2 = (Graphics2D) g;
                    if(humanTrainer.getPokeSlots().get(i).hp <= 0) {
                        g2.setStroke(new BasicStroke(4));
                        g2.drawLine(0, 0, getWidth(), getHeight());
                        g2.drawLine(getWidth(), 0, 0, getHeight());
                        g2.setColor(new Color(0, 0, 0, 130));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                    }
                    if (selectedPokemon == humanTrainer.getPokeSlots().get(i)) {
                        g.fillArc( -15, getHeight() - 35, 50, 50, 45, 20);
                    }
                }
            };


            jp.setOpaque(false);
            setLayout(new GridBagLayout());
            setOpaque(false);
            GridBagConstraints c = new GridBagConstraints();
            c.fill = 1;
            c.weightx = 1;
            c.weighty = 1;
            c.gridheight = 1;
            c.gridwidth = 1;
            if(humanTrainer.currentPokemon == humanTrainer.getPokeSlots().get(i) && !humanTrainer.currentPokemon.isFainted()) {
                c.ipadx = 50;
                c.insets = new Insets(0, 0, 0, 0);
            }
            setContentAreaFilled(false);
            makeButtonLookNice();
            a();
            add(jp, c);
            this.i = i;
            gifPnlList.add(jp);
        }

        public void a() {
            addActionListener(e -> {
                updateSelectedPokemon(i);
                if (humanTrainer.getSelectedItem() == null ) {
                    if (humanTrainer.hasOpponent()) {
                        confirm.setEnabled(true);
                    }
                    selectedPokemon = humanTrainer.getPokeSlots().get(i);
                } else {
                    humanTrainer.useItem(humanTrainer.getPokeSlots().get(i), humanTrainer.getSelectedItem());
                    if (humanTrainer.hasOpponent() && humanTrainer.isUsingItem()) {
                        close.setEnabled(false);
                        confirm.setEnabled(true);
                        confirm.doClick();
                        confirm.setEnabled(false);
                    }
                }
                updateHealthBar();
            });
        }

        private void makeButtonLookNice() {
            setOpaque(false);
            setBorderPainted(true);
            setContentAreaFilled(false);
            //setRolloverEnabled(true);
            raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
            loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
            raisedbevel = BorderFactory.createRaisedBevelBorder();
            loweredbevel = BorderFactory.createLoweredBevelBorder();
            Border redLine = BorderFactory.createLineBorder(new Color(255, 255, 0), 15);
            setBorder(BattleGUI.getBorder(raisedetched, loweredetched, raisedbevel, loweredbevel, redLine));
        }
    }
}
