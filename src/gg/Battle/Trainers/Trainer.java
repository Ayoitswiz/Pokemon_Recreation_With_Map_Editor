package gg.Battle.Trainers;

import AdventureMode.Sprite;
import gg.Battle.Battler;
import gg.ExitFunctionException;
import gg.InGameItems.Item;
import gg.InGameItems.ItemInterface;
import gg.Pokemon.Moves.Move;
import gg.Pokemon.Pokemon;
import lombok.Data;
import utilities.LambdaWithParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static gg.ExitFunctionException.*;

@Data
abstract public class Trainer extends Sprite implements ItemInterface, Battler {

  private String name;
  private String alias;
  private String turnText;
  private Pokemon currentPokemon;
  private final HashMap<String, Item> items = new HashMap<>();
  private final ArrayList<Pokemon> pokeSlots = new ArrayList<>() {
    @Override
    public boolean add(Pokemon poke) {
      if (this.size() < 1) {
        setCurrentPokemon(poke);
      }
      return super.add(poke);

    }
  };
  private BigDecimal money;
  private Move chosenMove;
  private boolean isUsedTurn;

  @Override
  public boolean isOutOfUsablePokemon() {
    for (Pokemon p : getPokeSlots()) {
      if (p.getHP() > 0) {
        return false;
      }
    }
    return true;
  }


  @Override
  public void useItem(Pokemon p, Item item) {

    //Heal the pokemon if the item type is of healing and the pokemon is not fainted and if
    //the pokemon is not at fullHealth already cause why heal a pokemon who's health can't go any higher.

    switch (item.getType()) {
      case "Healing":
        If(p.isFainted(), "Cannot use " + item.getName() + " on a fainted pokemon")
        .If(p.getHP() == p.getMaxHP(), "Cannot use " + item.getName() + " because " + p.getName() +"'s HP is already full");

        p.setHP(item.getHealthEffect());
        setUsedTurn(true);
        setTurnText(getAlias() + " used " + item.getName() + " to heal " + p.getName());
      case "ATK_BOOST":
        return;
      default:
        throw new ExitFunctionException(item.getName() + " cannot be used on " + p.getName());
                /* Eventual return of why the item could not be used
                *the gui for this feature has not been made yet.
                return "Unable to use Item";*/
    }
  }


  public void fullyHealPokemon() {
    for (Pokemon p : getPokeSlots()) {
      p.fullyHeal();
    }
  }


  public void setPokeSlots(ArrayList<Pokemon> pokeSlots) {
    this.pokeSlots.clear();
    this.pokeSlots.addAll(pokeSlots);
  }

//  private final LambdaWithParam<Pokemon> setCurrentPokemon = poke -> {
//    If(poke.isFainted(), "Cannot set a fainted pokemon to current pokemon")
//    .If(poke == this.getCurrentPokemon(), "Cannot set current pokemon to current pokemon");
//
//    this.setCurrentPokemon(poke);
//  };

  public void setCurrentPokemon(Pokemon poke) {
    If(poke.isFainted(), "Cannot set a fainted pokemon to current pokemon")
      .If(poke == this.getCurrentPokemon(), "Cannot set current pokemon to current pokemon");

    currentPokemon = poke;
  }


//  public LambdaWithParam<Pokemon> getSetCurrentPokemonLambda() {
//    return setCurrentPokemon;
//  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
