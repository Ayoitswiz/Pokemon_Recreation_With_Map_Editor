package menus.pokemon;

import menus.battle.Opponent;
import menus.pokemon.moves.Move;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Random;

//Will only ever find in adventure mode so maybe it should be in that package. Could remove the public
//modifier.
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class WildPokemon extends Pokemon implements Opponent {
  private Move chosenMove;
  private Pokemon pokemon;
  private boolean isUsedTurn;
  private String turnText;
  private static final BigDecimal money = new BigDecimal(0);
  private String preBattleDialog;
	private boolean caughtUser = false;

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
public void setChosenMove(int i) {
	chosenMove = pokemon.getMove(i);
}

@Override
public Move getChosenMove() {
	Move m = chosenMove;
	chosenMove = null;
	return m;
}
@Override
  public void makeDecision() {
    setChosenMove(new Random().nextInt(4));
  }

  @Override
  public String getLostBattleDialog() {
    return "You defeated a wild " + pokemon.getName();
  }

  @Override
  public BigDecimal getEndBattleMoney() {
    return money;
  }

@Override
public LinkedList<Pokemon> getPokeSlots() {
	return new LinkedList<>(){{addFirst(pokemon);}};
}

public String getPreBattleDialog() {
    return "A wild pokemon appeared!";
  }

  @Override
  public String getDialogIfUserTrysToFleeBattle() {
    return "Got away safely!";
  }
}
