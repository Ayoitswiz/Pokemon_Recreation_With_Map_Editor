package gg.Battle.Trainers;


import gg.Battle.BattleGUI;
import gg.Battle.Battler;
import gg.GUIManager;
import gg.ViewPokeSlots.PokemonInPartyPanel;
import utilities.Lambda;

import java.math.BigDecimal;

public class HumanTrainer extends Trainer {
  private Battler opponent;
  private boolean isInAdventureMode = false;
  private boolean userRanFromBattle;

  public HumanTrainer() {
    setAlias("You");
  }

  @Override
  public void faintedSwap(Lambda runWhenPickedPoke) {
    new PokemonInPartyPanel()
      .open(getPokeSlots(), getCurrentPokemon(),
        poke -> {
        setCurrentPokemon(poke);
        setUsedTurn(true);
        setTurnText("Go " + getCurrentPokemon().getName() + "!");
        runWhenPickedPoke.foo();
        }).onPartyClose(() -> {
      GUIManager.setUi(BattleGUI.class.getSimpleName());
      GUIManager.setBackgroundImage(BattleGUI.class.getSimpleName());
    });
        //p -> (res, err) -> {
//          try {
//            setCurrentPokemon(p);
//            setTurnText("Go " + getCurrentPokemon().getName() + "!");
//            res.foo();
//          } catch (Exception ex) {
//            err.foo(ex);
//          }
//        }).onClose(runWhenPickedPoke::foo);
  }

  @Override
  public String getCurrentPokemonFaintedText() {
    return getCurrentPokemon().getName() + " fainted!";
  }

  @Override
  public void makeDecision() {
  }



  public Battler getOpponent() {
    return opponent;
  }

  public void setOpponent(Battler opponent) {
    this.opponent = opponent;
  }

  public boolean isInAdventureMode() {
    return isInAdventureMode;
  }

  public void setInAdventureMode(boolean inAdventureMode) {
    isInAdventureMode = inAdventureMode;
  }

  public boolean hasOpponent() {
    return getOpponent() != null;
  }

  public String lostBattleDialog() {
    return isInAdventureMode()
      ?
      "You are all out of pokemon. " +
        "You lose 200 pokedollars. You now have " + getMoney() + " pokedollars." +
        " You'll be returned to the Pokemon Center"
      :
      "You lose 200 pokedollars. You now have " + getMoney() + " pokedollars." +
        " Click Continue to return to the Main Menu...";
  }

  public String wonBattleDialog() {
    return "You win! You earned " + "200 pokedollars. " +
      "You now have " + getMoney() + " pokedollars." +
      (isInAdventureMode() ? "" : " Click Continue to return to the Main Menu...");

  }

  public String ranFromBattleDialog() {
    return getName() + " has decided to run from the battle. You lose 200 pokedollars. You now have " + getMoney() + " pokedollars.";
  }

  public boolean userRanFromBattle() {
    return userRanFromBattle;
  }

  public void setUserRanFromBattle(boolean userRanFromBattle) {
    this.userRanFromBattle = userRanFromBattle;
  }


  @Override
  public BigDecimal getEndBattleMoney() {
    return new BigDecimal(0);
  }

  public void updateMoney(BigDecimal moneyGainedOrlost) {
    setMoney(getMoney().add(moneyGainedOrlost));
  }

  @Override
  public String getLostBattleDialog() {
    BigDecimal moneyStolen = getMoney().divide(new BigDecimal(10));
    updateMoney(moneyStolen.negate());
    return "You have lost the battle! " +
      "Wait!? What!? He's taking your wallet!!! " +
      "You lost " + moneyStolen +
      "You now have " + getMoney();
  }
}


