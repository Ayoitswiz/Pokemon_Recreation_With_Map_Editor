package gg.Battle;

import gg.Pokemon.Moves.Move;
import gg.Pokemon.Pokemon;
import utilities.Lambda;

import java.math.BigDecimal;


public interface Battler {

  default void faintedSwap(Lambda executeWhenPickedPoke) {
    executeWhenPickedPoke.foo();
  }

  boolean isOutOfUsablePokemon();

  String getName();


  Pokemon getCurrentPokemon();

  String getCurrentPokemonFaintedText();

  default void setPokemonsBattler(Pokemon pokemon) {
    pokemon.setBattler(this);
  }

  Move getChosenMove();

  void setChosenMove(Move move);

  void makeDecision();

  static <P extends Pokemon> Pokemon[] getPokeWithHigherSpeed(P p1, P p2) {
    return
      Math.max(p1.getSpeed(), p2.getSpeed()) == p1.getSpeed()
      ?
      new Pokemon[]{p1, p2}
      : getPokeWithHigherSpeed(p2, p1);


  }

  boolean isUsedTurn();

  void setUsedTurn(boolean bool);


  void setTurnText(String s);
  String getTurnText();

  String getLostBattleDialog();
  BigDecimal getEndBattleMoney();


  String toString();




}
