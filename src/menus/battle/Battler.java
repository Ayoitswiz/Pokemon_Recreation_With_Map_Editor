package menus.battle;

import menus.pokemon.Pokemon;
import menus.pokemon.moves.Move;
import utilities.Lambda;

import java.math.BigDecimal;
import java.util.LinkedList;

public interface Battler {

boolean isOutOfUsablePokemon();

String getName();
String getTurnText();
String getCurrentPokemonFaintedText();
String getLostBattleDialog();
String toString();

Move getChosenMove();
Pokemon getCurrentPokemon();
BigDecimal getEndBattleMoney();
LinkedList<Pokemon> getPokeSlots();


void setChosenMove(int i);
void makeDecision();
void setTurnText(String s);
boolean isUsedTurn();



// messing around w/ syntax I don't want to forget about.
static Pokemon getPokeWithHigherSpeed(Pokemon p1, Pokemon p2)[] {
	return
	p1.getSpeed() == Math.max(p1.getSpeed(), p2.getSpeed())
	?
	new Pokemon[]{p1, p2}
	: Battler.<BattleGUI>getPokeWithHigherSpeed(p2, p1);
}



default void faintedSwap(Lambda executeWhenPickedPoke) {
	executeWhenPickedPoke.foo();
}


default void setPokemonsBattler(Pokemon pokemon) {
	pokemon.setBattler(this);
}

}
