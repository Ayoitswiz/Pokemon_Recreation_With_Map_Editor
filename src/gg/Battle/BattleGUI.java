package gg.Battle;

import AdventureMode.DefaultNPC;
import AdventureMode.DialogBox;
import AdventureMode.MySprite;
import gg.Backpack.Backpack;
import gg.ExitFunctionException;
import gg.GUIManager;
import gg.InGameItems.Item;
import gg.Pokemon.Moves.Move;
import gg.Pokemon.Moves.Moves;
import gg.Pokemon.CreatePokemon;
import gg.Pokemon.Pokemon;
import gg.ViewPokeSlots.PokemonInPartyPanel;
import gg.Styles.PokemonProgressBar;
import gg.Battle.Trainers.AITrainer;
import gg.Battle.Trainers.HumanTrainer;
import org.jetbrains.annotations.NotNull;
import utilities.Lambda;
import utilities.LambdaWithReturnVal;
import utilities.LambdaWithParamAndRetVal;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;

import static gg.Pokemon.Moves.eMoves.BLIZZARD;
import static gg.Pokemon.Moves.eMoves.DIAMOND_STORM;
import static gg.Pokemon.Moves.eMoves.ICE_BEAM;
import static gg.Pokemon.Moves.eMoves.SHADOW_BALL;
import static gg.Pokemon.Moves.eMoves.STRONG_MOVE;
import static gg.Pokemon.Moves.eMoves.WEAK_MOVE;

public class BattleGUI extends JPanel {
  private Image background = new ImageIcon("src/imagesUI/PokeballFlat.png").getImage();
  private Lambda onClose = () -> {};

    @NotNull
    public static Border getBorder(Border raisedetched, Border loweredetched, Border raisedbevel, Border loweredbevel, Border redLine) {
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

  public Image getBackgroundImage() {
    return background;
  }


  public BattleGUI(HumanTrainer  user, Opponent ai) {
    setLayout(new GridBagLayout());
    setBackground(Color.black);
    createG(user, ai);
    setOpaque(false);
    GUIManager.addNewBackgroundImage(this.getClass().getSimpleName(), this.getBackgroundImage());
    GUIManager.addNewUi(this);
    GUIManager.setBackgroundImage(this.getClass().getSimpleName());
    GUIManager.setUi(this.getClass().getSimpleName());
  }

  private void createG(HumanTrainer  user, Opponent ai) {
    LambdaWithReturnVal<Component> createEmptySpace = () -> new JPanel() {{
      setBackground(new Color(200, 200, 200));
      setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }};

    DialogBox dBox = new DialogBox();


    JLabel currentPokemonName = new JLabel(ai.getCurrentPokemon().getName());
    JLabel currentPokemonlvl = new JLabel("Lvl: " + ai.getCurrentPokemon().getLvl());
    JLabel currentPokemonGender = new JLabel(ai.getCurrentPokemon().getGender());
    JProgressBar hpBar = new PokemonProgressBar(ai.getCurrentPokemon().getHP(), ai.getCurrentPokemon().getMaxHP());
    JLabel statuslbl = new JLabel("STATUS: ");
    JLabel hpNumlbl = new JLabel(ai.getCurrentPokemon().getHP() + "/" + ai.getCurrentPokemon().getMaxHP());

    JPanel top = new JPanel(new GridBagLayout()) {{
      JPanel opponentInfoContainer = new JPanel(new GridBagLayout()) {
        {
          JPanel opponentInfo = new JPanel(new GridBagLayout()) {{
            JPanel topInfo = new JPanel(new GridBagLayout()) {{
              JPanel name = new JPanel(new GridBagLayout()) {{
                GridBagConstraints namec = new GridBagConstraints();
                namec.fill = GridBagConstraints.BOTH;
                namec.weightx = 1;
                namec.weighty = 1;
                namec.insets = new Insets(0, 5, 0, 0);
                add(currentPokemonName, namec);
              }};
              JPanel lvl = new JPanel(new GridBagLayout()) {{
                GridBagConstraints lvlc = new GridBagConstraints();
                lvlc.fill = GridBagConstraints.BOTH;
                lvlc.weightx = 1;
                lvlc.weighty = 1;
                lvlc.insets = new Insets(0, 5, 0, 0);
                add(currentPokemonlvl, lvlc);
              }};
              name.setBackground(Color.RED);
              lvl.setBackground(Color.ORANGE);
              GridBagConstraints userTopInfoc = new GridBagConstraints();
              userTopInfoc.fill = GridBagConstraints.BOTH;
              userTopInfoc.weightx = .75;
              userTopInfoc.weighty = 1;
              add(name, userTopInfoc);
              userTopInfoc.weightx = .25;
              add(lvl, userTopInfoc);
            }};
            JPanel midInfo = new JPanel(new GridBagLayout()) {{
              JPanel gender = new JPanel(new GridBagLayout()) {{
                GridBagConstraints genderc = new GridBagConstraints();
                genderc.fill = GridBagConstraints.BOTH;
                genderc.weightx = 1;
                genderc.weighty = 1;
                genderc.insets = new Insets(0, 5, 0, 0);
                add(currentPokemonGender, genderc);
              }};
              JPanel hpBarContainer = new JPanel(new GridBagLayout()) {{
                JLabel hplbl = new JLabel("HP");
                GridBagConstraints hpc = new GridBagConstraints();
                hpc.fill = GridBagConstraints.HORIZONTAL;
                hpc.weightx = .1;
                hplbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                add(hplbl, hpc);
                hpc.weightx = .9;
                hpc.insets = new Insets(0, 0, 2, 5);
                add(hpBar, hpc);
              }};
              gender.setBackground(Color.yellow);
              hpBarContainer.setBackground(Color.green);
              GridBagConstraints userMidInfoc = new GridBagConstraints();
              userMidInfoc.fill = GridBagConstraints.BOTH;
              userMidInfoc.weightx = .25;
              userMidInfoc.weighty = 1;
              add(gender, userMidInfoc);
              userMidInfoc.weightx = .75;
              add(hpBarContainer, userMidInfoc);
            }};
            JPanel botInfo = new JPanel(new GridBagLayout()) {{
              JPanel status = new JPanel(new GridBagLayout()) {{
                GridBagConstraints statusc = new GridBagConstraints();
                statusc.fill = GridBagConstraints.HORIZONTAL;
                statusc.weightx = 1;
                statusc.insets = new Insets(0, 5, 0, 0);
                add(statuslbl, statusc);
              }};
              JPanel hpNumContainer = new JPanel(new GridBagLayout()) {{
                GridBagConstraints hpNumc = new GridBagConstraints();
                hpNumc.fill = GridBagConstraints.HORIZONTAL;
                hpNumc.weightx = 1;
                hpNumc.insets = new Insets(0, 0, 0, 5);
                hpNumlbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                add(hpNumlbl, hpNumc);
              }};
              status.setBackground(Color.blue);
              hpNumContainer.setBackground(Color.magenta);
              GridBagConstraints userBotInfoc = new GridBagConstraints();
              userBotInfoc.fill = GridBagConstraints.BOTH;
              userBotInfoc.weightx = .75;
              userBotInfoc.weighty = 1;
              add(status, userBotInfoc);
              userBotInfoc.weightx = .25;
              add(hpNumContainer, userBotInfoc);
            }};
            GridBagConstraints opponentInfoc = new GridBagConstraints();
            opponentInfoc.fill = GridBagConstraints.BOTH;
            opponentInfoc.gridx = 0;
            opponentInfoc.weightx = 1;
            opponentInfoc.weighty = .33;
            add(topInfo, opponentInfoc);
            add(midInfo, opponentInfoc);
            add(botInfo, opponentInfoc);
          }};
          opponentInfo.setBackground(new Color(255, 255, 0));
          GridBagConstraints opponentInfoc = new GridBagConstraints();
          opponentInfoc.fill = GridBagConstraints.BOTH;
          opponentInfoc.weightx = .95;
          opponentInfoc.weighty = .60;
          add(opponentInfo, opponentInfoc);
          opponentInfoc.weightx = .05;
          opponentInfoc.weighty = .4;
          add(createEmptySpace.foo(), opponentInfoc);
          opponentInfoc.gridy = 1;
          add(createEmptySpace.foo(), opponentInfoc);
          add(createEmptySpace.foo(), opponentInfoc);
        }

        @Override
        protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          currentPokemonName.setText(ai.getCurrentPokemon().getName());
          currentPokemonlvl.setText("Lvl: " + ai.getCurrentPokemon().getLvl());
          currentPokemonGender.setText(ai.getCurrentPokemon().getGender());
          hpBar.setMaximum(ai.getCurrentPokemon().getMaxHP());
          hpBar.setValue(ai.getCurrentPokemon().getHP());
          statuslbl.setText("STATUS: ");
          hpNumlbl.setText(ai.getCurrentPokemon().getHP() + "/" + ai.getCurrentPokemon().getMaxHP());
        }
      };
      JPanel opponentPokemonGifContainer = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
          super.paintComponent(g);
          if (getWidth() > 0 && getHeight() > 0) {
            int height = getHeight();
            int width = getWidth();
            int x = getWidth() - width;
            int y = getHeight() - height;
            g.drawImage(new ImageIcon(getClass().getResource("../../img/PokeFront/" + ai.getCurrentPokemon().getName() + ".gif")).getImage(), x, y, width, height, this);
          }
        }
      };
      opponentInfoContainer.setBackground(Color.PINK);
      opponentPokemonGifContainer.setBackground(Color.DARK_GRAY);
      GridBagConstraints topc = new GridBagConstraints();
      topc.fill = GridBagConstraints.BOTH;
      topc.weightx = .5;
      topc.weighty = 1;
      add(opponentInfoContainer, topc);
      add(opponentPokemonGifContainer, topc);
    }};
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    JPanel middle = new JPanel(new GridBagLayout()) {{
      JPanel usersPokemonGifContainer = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
          super.paintComponent(g);
          if (getWidth() > 0 && getHeight() > 0) {
            int height = getHeight();
            int width = getWidth();
            int x = getWidth() - width;
            int y = getHeight() / 4;
            g.drawImage(new ImageIcon(getClass().getResource("../../img/PokeBack/" + user.getCurrentPokemon().getName() + ".gif")).getImage(), x, y, width, height, this);
          }
        }
      };
      JLabel currentPokemonName = new JLabel(user.getCurrentPokemon().getName());
      JLabel currentPokemonlvl = new JLabel("Lvl: " + user.getCurrentPokemon().getLvl());
      JLabel currentPokemonGender = new JLabel(user.getCurrentPokemon().getGender());
      JProgressBar hpBar = new PokemonProgressBar(user.getCurrentPokemon().getHP(), user.getCurrentPokemon().getMaxHP());
      JLabel statuslbl = new JLabel("STATUS: ");
      JLabel hpNumlbl = new JLabel(user.getCurrentPokemon().getHP() + "/" + user.getCurrentPokemon().getMaxHP());
      PokemonProgressBar expBar = new PokemonProgressBar(
        (int) (user.getCurrentPokemon().getExp() - (int) Math.pow(user.getCurrentPokemon().getLvl(), 3)), // current exp
        (int) Math.pow(user.getCurrentPokemon().getLvl() + 1, 3) - (int) Math.pow(user.getCurrentPokemon().getLvl(), 3)); //exp needed to lvl up when at a certain lvl


      JPanel userInfoContainer = new JPanel(new GridBagLayout()) {

        {
          JPanel userInfo = new JPanel(new GridBagLayout()) {{
            JPanel topInfo = new JPanel(new GridBagLayout()) {{
              JPanel name = new JPanel(new GridBagLayout()) {{
                GridBagConstraints namec = new GridBagConstraints();
                namec.fill = GridBagConstraints.BOTH;
                namec.weightx = 1;
                namec.weighty = 1;
                namec.insets = new Insets(0, 5, 0, 0);
                add(currentPokemonName, namec);
              }};
              JPanel lvl = new JPanel(new GridBagLayout()) {{
                GridBagConstraints lvlc = new GridBagConstraints();
                lvlc.fill = GridBagConstraints.BOTH;
                lvlc.weightx = 1;
                lvlc.weighty = 1;
                lvlc.insets = new Insets(0, 5, 0, 0);
                add(currentPokemonlvl, lvlc);
              }};
              name.setBackground(Color.RED);
              lvl.setBackground(Color.ORANGE);
              GridBagConstraints userTopInfoc = new GridBagConstraints();
              userTopInfoc.fill = GridBagConstraints.BOTH;
              userTopInfoc.weightx = .75;
              userTopInfoc.weighty = 1;
              add(name, userTopInfoc);
              userTopInfoc.weightx = .25;
              add(lvl, userTopInfoc);
            }};
            JPanel midInfo = new JPanel(new GridBagLayout()) {{
              JPanel gender = new JPanel(new GridBagLayout()) {{
                GridBagConstraints genderc = new GridBagConstraints();
                genderc.fill = GridBagConstraints.BOTH;
                genderc.weightx = 1;
                genderc.weighty = 1;
                genderc.insets = new Insets(0, 5, 0, 0);
                add(currentPokemonGender, genderc);
              }};
              JPanel hpBarContainer = new JPanel(new GridBagLayout()) {{
                JLabel hplbl = new JLabel("HP");
                GridBagConstraints hpc = new GridBagConstraints();
                hpc.fill = GridBagConstraints.HORIZONTAL;
                hpc.weightx = .1;
                hplbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                add(hplbl, hpc);
                hpc.weightx = .9;
                hpc.insets = new Insets(0, 0, 2, 5);
                add(hpBar, hpc);
              }};
              gender.setBackground(Color.yellow);
              hpBarContainer.setBackground(Color.green);
              GridBagConstraints userMidInfoc = new GridBagConstraints();
              userMidInfoc.fill = GridBagConstraints.BOTH;
              userMidInfoc.weightx = .25;
              userMidInfoc.weighty = 1;
              add(gender, userMidInfoc);
              userMidInfoc.weightx = .75;
              add(hpBarContainer, userMidInfoc);
            }};
            JPanel botInfo = new JPanel(new GridBagLayout()) {{
              JPanel status = new JPanel(new GridBagLayout()) {{
                GridBagConstraints statusc = new GridBagConstraints();
                statusc.fill = GridBagConstraints.HORIZONTAL;
                statusc.weightx = 1;
                statusc.insets = new Insets(0, 5, 0, 0);
                add(statuslbl, statusc);
              }};
              JPanel hpNumContainer = new JPanel(new GridBagLayout()) {{
                GridBagConstraints hpNumc = new GridBagConstraints();
                hpNumc.fill = GridBagConstraints.HORIZONTAL;
                hpNumc.weightx = 1;
                hpNumc.insets = new Insets(0, 0, 0, 5);
                hpNumlbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                add(hpNumlbl, hpNumc);
              }};
              status.setBackground(Color.blue);
              hpNumContainer.setBackground(Color.magenta);
              GridBagConstraints userBotInfoc = new GridBagConstraints();
              userBotInfoc.fill = GridBagConstraints.BOTH;
              userBotInfoc.weightx = .75;
              userBotInfoc.weighty = 1;
              add(status, userBotInfoc);
              userBotInfoc.weightx = .25;
              add(hpNumContainer, userBotInfoc);
            }};
            JPanel expBarContainer = new JPanel(new GridBagLayout()) {{
              GridBagConstraints expBarc = new GridBagConstraints();
              expBarc.fill = GridBagConstraints.BOTH;
              expBarc.weightx = 1;
              expBarc.weighty = 1;
              expBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
              add(expBar, expBarc);
            }};
            GridBagConstraints userInfoc = new GridBagConstraints();
            userInfoc.fill = GridBagConstraints.BOTH;
            userInfoc.gridx = 0;
            userInfoc.weightx = 1;
            userInfoc.weighty = .32;
            add(topInfo, userInfoc);
            add(midInfo, userInfoc);
            add(botInfo, userInfoc);
            userInfoc.weighty = .04;
            add(expBarContainer, userInfoc);
          }};
          userInfo.setBackground(new Color(255, 255, 0));
          GridBagConstraints userInfoContainerc = new GridBagConstraints();
          userInfoContainerc.fill = GridBagConstraints.BOTH;
          userInfoContainerc.weightx = .05;
          userInfoContainerc.weighty = .4;
          add(createEmptySpace.foo(), userInfoContainerc);
          add(createEmptySpace.foo(), userInfoContainerc);
          userInfoContainerc.gridy = 1;
          add(createEmptySpace.foo(), userInfoContainerc);
          userInfoContainerc.weightx = .95;
          userInfoContainerc.weighty = .60;
          add(userInfo, userInfoContainerc);
        }

        @Override
        protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          currentPokemonName.setText(user.getCurrentPokemon().getName());
          currentPokemonlvl.setText("Lvl: " + user.getCurrentPokemon().getLvl());
          currentPokemonGender.setText(user.getCurrentPokemon().getGender());
          hpBar.setMaximum(user.getCurrentPokemon().getMaxHP());
          hpBar.setValue(user.getCurrentPokemon().getHP());
          statuslbl.setText("STATUS: ");
          hpNumlbl.setText(user.getCurrentPokemon().getHP() + "/" + user.getCurrentPokemon().getMaxHP());
          expBar.setMaximum((int) Math.pow(user.getCurrentPokemon().getLvl() + 1, 3) - (int) Math.pow(user.getCurrentPokemon().getLvl(), 3));
          expBar.setValue((int) (user.getCurrentPokemon().getExp() - (int) Math.pow(user.getCurrentPokemon().getLvl(), 3)));

        }


      };
      usersPokemonGifContainer.setBackground(Color.CYAN);
      userInfoContainer.setBackground(Color.WHITE);
      GridBagConstraints midc = new GridBagConstraints();
      midc.fill = GridBagConstraints.BOTH;
      midc.weightx = .5;
      midc.weighty = .5;
      add(usersPokemonGifContainer, midc);
      add(userInfoContainer, midc);
    }};
    final JButton closeMoves = new JButton("BACK");
    Lambda[] o = new Lambda[]{() -> {}};
    JPanel bottomMoves = new JPanel(new GridBagLayout()) {{
      JPanel right = new JPanel(new GridBagLayout()) {{
        GridBagConstraints movesBackBtnContainerc = new GridBagConstraints();
        movesBackBtnContainerc.fill = GridBagConstraints.BOTH;
        movesBackBtnContainerc.weightx = 1;
        movesBackBtnContainerc.weighty = 1;
        add(closeMoves, movesBackBtnContainerc);
      }};
      LambdaWithParamAndRetVal<Move, String> text = move -> "<html> "
        + move.getName()
        + " <br> "
        + move.getPp() + "/" + move.getMaxpp() + " " + move.getType()
        + " </html>";
      final ActionListener[] al = new ActionListener[4];
      JButton move1 = new JButton();
      JButton move2 = new JButton();
      JButton move3 = new JButton();
      JButton move4 = new JButton();
      JPanel left = new JPanel(new GridBagLayout()) {
        {
          GridBagConstraints movesc = new GridBagConstraints();
          movesc.fill = GridBagConstraints.BOTH;
          movesc.weightx = .5;
          movesc.weighty = 1;
          add(move1, movesc);
          add(move2, movesc);
          movesc.gridy = 1;
          add(move3, movesc);
          add(move4, movesc);
        }

        @Override
        protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          move1.setText(text.foo(user.getCurrentPokemon().getMoves()[0]));
          move1.removeActionListener(al[0]);
          al[0] = e -> {
            user.setChosenMove(user.getCurrentPokemon().getMoves()[0]);
            o[0].foo();
          };
          move1.addActionListener(al[0]);

          move2.setText(text.foo(user.getCurrentPokemon().getMoves()[1]));
          move2.removeActionListener(al[1]);
          al[1] = e -> {
            user.setChosenMove(user.getCurrentPokemon().getMoves()[1]);
            o[0].foo();
          };
          move2.addActionListener(al[1]);

          move3.setText(text.foo(user.getCurrentPokemon().getMoves()[2]));
          move3.removeActionListener(al[2]);
          al[2] = e -> {
            user.setChosenMove(user.getCurrentPokemon().getMoves()[2]);
            o[0].foo();
          };
          move3.addActionListener(al[2]);

          move4.setText(text.foo(user.getCurrentPokemon().getMoves()[3]));
          move4.removeActionListener(al[3]);
          al[3] = e -> {
            user.setChosenMove(user.getCurrentPokemon().getMoves()[3]);
            o[0].foo();
          };
          move4.addActionListener(al[3]);
        }
      };
      left.setBackground(Color.PINK);
      right.setBackground(Color.DARK_GRAY);
      GridBagConstraints botc = new GridBagConstraints();
      botc.fill = GridBagConstraints.BOTH;
      botc.weightx = .70;
      botc.weighty = 1;
      add(left, botc);
      botc.weightx = .30;
      add(right, botc);
    }};
    final JButton fight = new JButton("FIGHT");
    final JButton run = new JButton("RUN");






    JPanel options = new JPanel(new GridBagLayout()) {{
      JButton bag = new JButton("BAG");
      bag.addActionListener(baga -> {
        new Backpack()
          .open(user)
          .onBackpackClose(() -> {
            GUIManager.setUi(BattleGUI.class.getSimpleName());
            GUIManager.setBackgroundImage(BattleGUI.class.getSimpleName());
            if (user.isUsedTurn()) {// Lazy, if user picked item and used it on poke this will be true
              o[0].foo();
            }
          });
      });


      JButton pokemon = new JButton("POKEMON");
      pokemon.addActionListener(a ->
        new PokemonInPartyPanel()
        .open(
          user.getPokeSlots(),
          user.getCurrentPokemon(),
          poke -> {
            user.setCurrentPokemon(poke);
            user.setUsedTurn(true);
            user.setTurnText("You sent out " + poke.getName() + "!");
            o[0].foo();
          }
          )
        .onPartyClose(() -> {
          GUIManager.setUi(BattleGUI.class.getSimpleName());
          GUIManager.setBackgroundImage(BattleGUI.class.getSimpleName());
        })
      );
          //this::foo));
      GridBagConstraints optionsc = new GridBagConstraints();
      optionsc.fill = GridBagConstraints.BOTH;
      optionsc.weightx = .5;
      optionsc.weighty = 1;
      add(fight, optionsc);
      add(bag, optionsc);
      optionsc.gridy = 1;
      add(pokemon, optionsc);
      add(run, optionsc);
    }};
    JPanel bottom = new JPanel(new GridBagLayout()) {{
      dBox.setDialog("What will you do?");
      dBox.setBackground(Color.PINK);
      options.setBackground(Color.DARK_GRAY);
      GridBagConstraints botc = new GridBagConstraints();
      botc.fill = GridBagConstraints.BOTH;
      botc.weightx = .70;
      botc.weighty = 1;
      add(dBox, botc);
      botc.weightx = .30;
      add(options, botc);
    }};
    top.setBackground(Color.red);
    middle.setBackground(Color.orange);
    bottom.setBackground(Color.yellow);
    GridBagConstraints bgc = new GridBagConstraints();
    bgc.fill = GridBagConstraints.BOTH;
    bgc.gridx = 0;
    bgc.weightx = 1;
    bgc.weighty = .33;
    add(top, bgc);
    add(middle, bgc);
    add(bottom, bgc);


    fight.addActionListener(a -> {
      this.remove(bottom);
      this.add(bottomMoves, bgc);
      this.repaint();
      this.validate();
    });
    closeMoves.addActionListener(a -> {
      this.remove(bottomMoves);
      this.add(bottom, bgc);
      this.repaint();
      this.validate();
    });
    BattleManager battleManagerr = new BattleManager()
      .open(user, dBox.open())
      .onTurnEnd(() -> {
        GridBagConstraints botc = new GridBagConstraints();
        botc.fill = GridBagConstraints.BOTH;
        botc.weighty = 1;
        botc.weightx = .3;
        bottom.add(options, botc);
        dBox.setDialog("What will you do?");
        bottom.repaint();
        bottom.validate();
      })
      .onBattleEnd(() -> {
        onClose.foo();

      });
    o[0] = () -> {
      remove(bottomMoves);
      bottom.remove(options);
      add(bottom, bgc);
      repaint();
      validate();
      battleManagerr.start();

    };

    run.addActionListener(a -> {
      try {
        bottom.remove(options);
        dBox.setDialog(ai.getDialogIfUserTrysToFleeBattle())
          .onDialogEnd(() -> {
            user.setUserRanFromBattle(true);
            GUIManager.setUiOrDefault("AdventureModeMain", "MainMenuGUI");
            AITrainer.canMove = true;
            user.setOpponent(null);
            onClose.foo();
          });
      } catch (ExitFunctionException ex) {
        dBox.setDialog(ex.getMessage())
        .onDialogEnd(() -> {
          GridBagConstraints botc = new GridBagConstraints();
          botc.fill = GridBagConstraints.BOTH;
          botc.weighty = 1;
          botc.weightx = .3;
          bottom.add(options, botc);
          dBox.setDialog("What will you do?");
          bottom.repaint();
          bottom.validate();
        });

      }

    });
  }

  public BattleGUI onBattleGUIClose(Lambda onClose) {
      this.onClose = onClose;
      return this;
  }

  public static void main(String[] args) {

    HumanTrainer ht = new MySprite(new BigDecimal("10000.00"), "Ash");
    Pokemon rayquaza = CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, WEAK_MOVE, ICE_BEAM), ht);
    Pokemon articuno = CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), ht);
    Pokemon gengar = CreatePokemon.MegaGengar(9, Moves.Move(ICE_BEAM, WEAK_MOVE, SHADOW_BALL, STRONG_MOVE), ht);
    ht.getPokeSlots().add(rayquaza);
    ht.getPokeSlots().add(articuno);
    ht.getPokeSlots().add(gengar);


    HashMap<String, Item> items;
    items = new Item().createItem();
    ht.getItems().put("Max Potion", items.get("Max Potion"));
    ht.getItems().get("Max Potion").setQuantity(10);
    ht.getItems().put("Hyper Potion", items.get("Hyper Potion"));
    ht.getItems().get("Hyper Potion").setQuantity(11);
    ht.getItems().put("Super Potion", items.get("Super Potion"));
    ht.getItems().get("Super Potion").setQuantity(10);
    ht.getItems().put("Potion", items.get("Potion"));
    ht.getItems().get("Potion").setQuantity(10);


    AITrainer ai = new DefaultNPC();

    ht.setOpponent(ai);
    new BattleGUI(ht, ai);
  }
}
