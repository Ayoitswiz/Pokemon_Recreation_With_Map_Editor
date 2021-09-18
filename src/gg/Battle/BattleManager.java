package gg.Battle;

import AdventureMode.AdventureModeMain;
import AdventureMode.DefaultNPC;
import AdventureMode.DialogBox;
import AdventureMode.MySprite;
import gg.GUIManager;
import gg.InGameItems.Item;
import gg.Pokemon.Moves.Moves;
import gg.Pokemon.CreatePokemon;
import gg.Pokemon.Pokemon;
import gg.Battle.Trainers.Trainer;
import gg.Battle.Trainers.AITrainer;
import gg.Battle.Trainers.HumanTrainer;
import utilities.Lambda;

import javax.swing.*;

import static gg.Pokemon.Moves.eMoves.*;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Predicate;

public class BattleManager {
  private HumanTrainer user;
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
      user.getOpponent().getCurrentPokemon());

    user.getOpponent().makeDecision();

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
          () -> dBox.setDialog(secondb.getCurrentPokemonFaintedText())))
        startTurn(secondb, psecond, pfirst);

      fainted(
        pfirst,
        firstb,
        () -> dBox.onNext(() -> dBox.setDialog(firstb.getCurrentPokemonFaintedText())));
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
      battler.faintedSwap(() -> dBox.setDialog(battler.getTurnText()));
  }

  private void turnEnd() {
    onTurnEnd.foo();
    user.setUsedTurn(false);
    user.getOpponent().setUsedTurn(false);
  }

  private void battleEnd(Battler battler) {
    user.updateMoney(battler.getEndBattleMoney());
    dBox.setDialog(battler.getLostBattleDialog())
    .onDialogEnd(() -> {
      GUIManager.setUiOrDefault(AdventureModeMain.class.getSimpleName(), "MainMenuGUI");
      turnEnd();
      user.setOpponent(null);
      onBattleEnd.foo();
    });
  }




  public BattleManager open(HumanTrainer user, DialogBox dBox) {
    this.user = user;
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
    HumanTrainer ht = new MySprite(new BigDecimal("10000.00"), "Ash");

    Pokemon rayquaza = CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), ht);
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
    ht.setChosenMove(ht.getCurrentPokemon().getMoves()[0]);
    JPanel test = new JPanel(new GridBagLayout());
    DialogBox dBox = new DialogBox().open();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1;
    c.weighty = 1;
    test.add(dBox, c);
    test.setBackground(new Color(255, 0, 0));
    test.setVisible(true);


    GUIManager.addNewUi(test);
    GUIManager.addNewBackgroundImage(test.getClass().getSimpleName(), null);
    GUIManager.setUi(test.getClass().getSimpleName());
    GUIManager.setBackgroundImage(test.getClass().getSimpleName());
    new BattleManager()
    .open(ht, dBox)
    .start();
  }
}
