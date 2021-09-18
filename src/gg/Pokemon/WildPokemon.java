package gg.Pokemon;

import gg.Battle.Battler;
import gg.Battle.Opponent;
import gg.Pokemon.Moves.Move;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Random;

//Will only ever find in adventure mode so maybe it should be in that package. Could remove the public
//modifier.
@Data
@NoArgsConstructor
public class WildPokemon extends Pokemon implements Opponent {
  private Move chosenMove;
  private Pokemon pokemon;
  private boolean isUsedTurn;
  private String turnText;
  private static final BigDecimal money = new BigDecimal(0);
  private String preBattleDialog;

  @Override
  public boolean isOutOfUsablePokemon() {
    return pokemon.isFainted();
  }

  @Override
  public Pokemon getCurrentPokemon() {
    return pokemon;
  }

  @Override
  public String getCurrentPokemonFaintedText() {
    return "Wild " + getPokemon().getName() + " fainted!";
  }

  @Override
  public void makeDecision() {
    setChosenMove(pokemon.getMoves()[new Random().nextInt(4)]);
  }

  @Override
  public String getLostBattleDialog() {
    return "You defeated a wild " + pokemon.getName();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  @Override
  public BigDecimal getEndBattleMoney() {
    return money;
  }

  public String getPreBattleDialog() {
    return "A wild pokemon appeared!";
  }

  @Override
  public String getDialogIfUserTrysToFleeBattle() {
    return "Got away safely!";
  }
}
