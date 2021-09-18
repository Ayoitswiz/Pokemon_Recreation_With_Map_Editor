package gg.ViewPokeSlots;

import gg.Battle.BattleGUI;
import gg.ExitFunctionException;
import gg.GUIManager;
import gg.Pokemon.Pokemon;
import gg.Styles.PokemonProgressBar;
import lombok.Getter;
import lombok.NoArgsConstructor;
import utilities.Lambda;
import utilities.LambdaWithReturnVal;
import utilities.LambdaWithParam;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

@NoArgsConstructor
public class PokemonInPartyPanel extends JPanel {

  private GridBagConstraints c;
  private JPanel pnlPokemonInParty;
  Border raisedetched, loweredetched, raisedbevel, loweredbevel;
  protected LambdaWithReturnVal<Pokemon> getSelectedPoke;
  private Lambda onPartyClose = () -> {};

  @Getter
  private LambdaWithParam<Lambda> setOnClose = lam -> onPartyClose = lam;



  public PokemonInPartyPanel open(ArrayList<Pokemon> pokeslots, Pokemon currentPokemon, LambdaWithParam<Pokemon> poke){//, LambdaWithParamAndRetVal<Pokemon, LambdaWithTwoParam<Lambda, LambdaWithParam<Exception>>> executeThisOnPoke) {
    pnlPokemonInParty = new JPanel(new GridBagLayout());
    c = new GridBagConstraints();
    JPanel northContainer = new JPanel(new GridBagLayout());

    setLayout(new BorderLayout());
    JButton confirm = new JButton("Confirm");
    JLabel errorLbl = new JLabel(){{setVisible(false);}};
    createEachPokemonsPanel(pokeslots, currentPokemon);
    setOpaque(false);
    pnlPokemonInParty.setOpaque(false);


    confirm.addActionListener(a -> {
      try {
        poke.foo(getSelectedPoke.foo());

        onPartyClose.foo();
      } catch (ExitFunctionException ex) {
        errorLbl.setText(ex.getMessage());
        errorLbl.setVisible(true);
      }
    });


//    confirm.addActionListener(a -> {
//      // executeThisOnPoke takes a lambda, and returns a lambda that takes 2 lambdas.
//      executeThisOnPoke.foo(getSelectedPoke.foo()).foo(
//        () -> {
//          // These next two lines only execute if user opened this
//          // panel from the BattleGUI-options panel and pressed confirm to swap pokemon
//          if (!currentPokemon.isFainted()) {
//            getSelectedPoke.foo().getBattler().setUsedTurn(true);
//          }
//          if (onClose != null) {
//            onClose.foo();
//          }
//          GUIManager.setBackgroundImage("BattleGUI");
//          GUIManager.setUi("BattleGUI");
//        },
//        err -> {
//          err.printStackTrace();
//          errorLbl.setText(err.getMessage());
//          errorLbl.setVisible(true);
//        });
//    });


    if (!currentPokemon.isFainted()) {
      JButton close = new JButton("CLOSE");
      close.addActionListener(a -> {
        onPartyClose.foo();
      });
      add(close, BorderLayout.SOUTH);
    }
    GridBagConstraints nc = new GridBagConstraints();
    nc.fill = GridBagConstraints.BOTH;
    nc.weighty = .5;
    nc.weightx = 1;
    northContainer.add(confirm,nc);
    nc.gridy = 1;
    northContainer.add(errorLbl, nc);
    add(northContainer, BorderLayout.NORTH);
    add(pnlPokemonInParty, BorderLayout.CENTER);

    GUIManager.addNewBackgroundImage(getClass().getSimpleName(), new ImageIcon("src/img/UI/Pokeball.png").getImage());
    GUIManager.addNewUi(getClass().getSimpleName(), this);
    GUIManager.setBackgroundImage(getClass().getSimpleName());
    GUIManager.setUi(getClass().getSimpleName());
    return this;
  }

  public PokemonInPartyPanel onPartyClose(Lambda l) {
    this.onPartyClose = l;
    return this;
  }

  private void createEachPokemonsPanel(ArrayList<Pokemon> pokeslots, Pokemon currentPokemon) {
    int i = 0;
    for (Pokemon p : pokeslots) {
      c.gridy = 0;
      c.gridx = i++;
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
      pnlPokemonInParty.add(new PokemonPanelNameLbl(Color.WHITE, p.getName(), p.getType()), c);
      c.weighty = 1;
      c.gridy = 2;
      c.fill = 1;
      c.anchor = GridBagConstraints.CENTER;
      gifBtn gBtn = new gifBtn(p, p == currentPokemon);

      pnlPokemonInParty.add(gBtn, c);
      c.ipadx = 0;
      c.insets = new Insets(0, 5, 0, 5);
      c.gridy = 3;
      c.ipady = 10;
      c.fill = 2;
      c.anchor = GridBagConstraints.NORTH;
      pnlPokemonInParty.add(new PokemonProgressBar(p.getHP(), p.getMaxHP()), c);
    }
  }


  private static int i = 0;
  private static final HashMap<Integer, Boolean> isSelectedPokemon = new HashMap<>();
  private class gifBtn extends JButton {
    private Image background;
    private final int id = i;
    private GridBagConstraints gifBtnC;
    private boolean isCurrentPokemonPanel = false;
    private boolean mouseEnteredInsetsIncreased = false;
    private GridBagLayout gbl = new GridBagLayout();

    // Pokemon fainted
    gifBtn(Pokemon p, boolean isCurrentPokemon) {
      isSelectedPokemon.put(i++, isCurrentPokemon);
      isCurrentPokemonPanel = isCurrentPokemon;
      if (isCurrentPokemon) getSelectedPoke = () -> p;
      createBtns(p);
    }

    private void createBtns(Pokemon p) {
      background = new ImageIcon("src/img/PokeFront/" + p.getName() + ".gif").getImage();

      JPanel jp = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          //Adds a box to the gifBtn indicating this is the poke currently in battle
          if (isCurrentPokemonPanel) {
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, getWidth(), getHeight());
            getParent().setBackground(Color.BLUE);
          }
          g.drawImage(background, -gifBtn.this.getInsets().left, 0, getWidth() + (gifBtn.this.getInsets().right * 2), getHeight(), this);
          g.setColor(Color.RED);
          Graphics2D g2 = (Graphics2D) g;
          // Draw X over fainted poke
          if (p.isFainted()) {
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(0, 0, getWidth(), getHeight());
            g2.drawLine(getWidth(), 0, 0, getHeight());
            g2.setColor(new Color(0, 0, 0, 130));
            g2.fillRect(0, 0, getWidth(), getHeight());
          }
          if (isSelectedPokemon.get(id)) {
            gifBtn.this.setBackground(new Color(255, 0, 0));
            g.fillArc(-15, getHeight() - 35, 50, 50, 45, 20);
          } else if (mouseEnteredInsetsIncreased) {
            gifBtnC.ipadx = 0;
            gifBtn.this.gbl.setConstraints(this, gifBtnC);
            gifBtn.this.revalidate();
            gifBtn.this.setBackground(UIManager.getColor("control"));
            pnlPokemonInParty.validate();
            mouseEnteredInsetsIncreased = false;
        }
        }
      };
      addActionListener(a -> {
        isSelectedPokemon.replaceAll((k, v) -> id == k);
        mouseEnteredInsetsIncreased = true;
        getSelectedPoke = () -> p;
      });
      jp.setOpaque(false);
      setLayout(gbl);
      setOpaque(false);
      gifBtnC = new GridBagConstraints();
      gifBtnC.fill = GridBagConstraints.BOTH;
      gifBtnC.weightx = 1;
      gifBtnC.weighty = 1;
      if (isCurrentPokemonPanel) {
        gifBtnC.ipadx = 50;
      }
      setContentAreaFilled(false);
      makeButtonLookNice();
      add(jp, gifBtnC);
      setMouseListeners(jp, p);
    }



    private void setMouseListeners(JPanel jp, Pokemon p) {
      this.addMouseListener(new java.awt.event.MouseAdapter() {

        public synchronized void mouseEntered(MouseEvent evt) {
          gifBtnC.ipadx = 50;
          gifBtn.this.gbl.setConstraints(jp, gifBtnC);
          gifBtn.this.revalidate();

          try {
            //the color change to the background was executing before the previous object resized, occasionally. The wait syncs the two up much more nicely and also
            wait(10);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          setBackground(new Color(246, 173, 35));
          pnlPokemonInParty.repaint();
          pnlPokemonInParty.validate();
        }

        public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
          if (!isSelectedPokemon.get(id) && !isCurrentPokemonPanel) {
            gifBtnC.ipadx = 0;
            gifBtn.this.gbl.setConstraints(jp, gifBtnC);
            gifBtn.this.revalidate();
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

    private void makeButtonLookNice() {
      setOpaque(false);
      setBorderPainted(true);
      setContentAreaFilled(false);
      //setRolloverEnabled(true);
      raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
      loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      raisedbevel = BorderFactory.createRaisedBevelBorder();
      loweredbevel = BorderFactory.createLoweredBevelBorder();
      Border blueborder = BorderFactory.createLineBorder(new Color(0, 0, 255), 15);
      setBorder(BattleGUI.getBorder(raisedetched, loweredetched, raisedbevel, loweredbevel, blueborder));
    }
  }
}
