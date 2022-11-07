package menus.battle;

import menus.pokemon.moves.Move;
import menus.pokemon.Pokemon;
import utilities.Lambda;

import java.math.BigDecimal;
import java.util.LinkedList;

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

void setChosenMove(int i);

void makeDecision();

// messing around w/ syntax I don't want to forget about.
static Pokemon getPokeWithHigherSpeed(Pokemon p1, Pokemon p2)[] {
	return
	p1.getSpeed() == Math.max(p1.getSpeed(), p2.getSpeed())
	?
	new Pokemon[]{p1, p2}
	: Battler.<BattleGUI>getPokeWithHigherSpeed(p2, p1);
}

boolean isUsedTurn();



void setTurnText(String s);
String getTurnText();

String getLostBattleDialog();
BigDecimal getEndBattleMoney();


String toString();

LinkedList<Pokemon> getPokeSlots();

}
