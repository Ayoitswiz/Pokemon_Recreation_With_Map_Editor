package menus.battle;

import adventuremode.components.AdventureModeMain;
import adventuremode.components.DialogBox;
import adventuremode.sprites.DefaultNPC;
import adventuremode.sprites.MySprite;
import menus.battle.trainers.HumanTrainer;
import menus.battle.trainers.Trainer;
import menus.gui;
import menus.in_game_items.Item;
import menus.pokemon.CreatePokemon;
import menus.pokemon.Pokemon;
import menus.pokemon.moves.Moves;
import utilities.Lambda;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

import static menus.pokemon.moves.eMoves.*;

public class BattleManager {
  private Battler user;
	private Opponent ai;
  private Predicate<Trainer> isUser = isUser -> isUser == user;
  private DialogBox dBox;
  private Lambda onTurnEnd;
  private Lambda onBattleEnd;
  // TODO: 8/7/2020
    //  need to add method: if(user has no pokemon with hp && getInAdventureMode == true) send to pokemon center

  void start() {
    Pokemon[] p =
      Battler.getPokeWithHigherSpeed(
      user              .getCurrentPokemon(),
      ai.getCurrentPokemon());

    ai.makeDecision();

    if (p[1].getBattler().isUsedTurn()) Collections.reverse(Arrays.asList(p));

    var pfirst = p[0];
    var psecond = p[1];

    var firstb = pfirst.getBattler();
    var secondb = psecond.getBattler();


    startTurn(firstb, pfirst, psecond);
    dBox.onNext(() ->{

      if (!fainted(
          psecond,
          secondb,
          () -> dBox.setDialog(secondb::getCurrentPokemonFaintedText)))
        startTurn(secondb, psecond, pfirst);

      fainted(
        pfirst,
        firstb,
        () -> dBox.onNext(() -> dBox.setDialog(firstb::getCurrentPokemonFaintedText)));
    })
    .onDialogEnd(this::turnEnd);
  }

  private void startTurn(Battler b, Pokemon attacker, Pokemon atackee) {
    dBox.setDialog(
      b.isUsedTurn()
        ? b.getTurnText()
        : attacker.attack(atackee, b.getChosenMove()));
  }

  private boolean fainted(Pokemon poke, Battler battler, Lambda dialog) {
    if (poke.isFainted()) {
      dialog.foo();
      dBox.onNext(() -> swapPokemon(battler));
    }
    return poke.isFainted();
  }


  private void swapPokemon(Battler battler) {
    if (battler.isOutOfUsablePokemon())
      battleEnd(battler);
    else
      battler.faintedSwap(() -> dBox.setDialog(battler::getTurnText));
  }

  private void turnEnd() {
    onTurnEnd.foo();
  }

  private void battleEnd(Battler battler) {
		((HumanTrainer) user).updateMoney(battler.getEndBattleMoney());
    dBox.setDialog(battler::getLostBattleDialog)
    .onDialogEnd(() -> {
			turnEnd();
			onBattleEnd.foo();
			gui.setUiOrDefault(AdventureModeMain.class.getSimpleName(), "MainMenuGUI");
    });
  }


  public BattleManager open(Battler user, Opponent ai, DialogBox dBox) {
    this.user = user;
		this.ai = ai;
    this.dBox = dBox;
    return this;
  }
  public BattleManager onTurnEnd(Lambda l) {
    onTurnEnd = l;
    return this;
  }
  public BattleManager onBattleEnd(Lambda onBattleEnd) {
    this.onBattleEnd = onBattleEnd;
    return this;
  }
  public static void main(String[] args) {
    Trainer ht = new MySprite(new BigDecimal("10000.00"), "Ash");

    Pokemon rayquaza = CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), ht);
    Pokemon articuno = CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), ht);
    Pokemon gengar = CreatePokemon.MegaGengar(9, Moves.Move(ICE_BEAM, WEAK_MOVE, SHADOW_BALL, STRONG_MOVE), ht);

    ht.getPokeSlots().add(rayquaza);
    ht.getPokeSlots().add(articuno);
    ht.getPokeSlots().add(gengar);


    Map<String, Item> items;
    items = new Item().createItem();
    ht.getItems().put("Max Potion", items.get("Max Potion"));
    ht.getItems().get("Max Potion").setQuantity(10);
    ht.getItems().put("Hyper Potion", items.get("Hyper Potion"));
    ht.getItems().get("Hyper Potion").setQuantity(11);
    ht.getItems().put("Super Potion", items.get("Super Potion"));
    ht.getItems().get("Super Potion").setQuantity(10);
    ht.getItems().put("Potion", items.get("Potion"));
    ht.getItems().get("Potion").setQuantity(10);


    Opponent ai = new DefaultNPC();

    ht.setChosenMove(0);
    JPanel test = new JPanel(new GridBagLayout());
    DialogBox dBox = new DialogBox().open();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1;
    c.weighty = 1;
    test.add(dBox, c);
    test.setBackground(new Color(255, 0, 0));
    test.setVisible(true);


    gui.addNewUI(test);
    gui.addNewBackgroundImage(test.getClass().getSimpleName(), null);
    gui.setUI(test.getClass().getSimpleName());
    gui.setBackgroundImage(test.getClass().getSimpleName());
    new BattleManager()
    .open(ht, ai, dBox)
    .start();
  }
}
