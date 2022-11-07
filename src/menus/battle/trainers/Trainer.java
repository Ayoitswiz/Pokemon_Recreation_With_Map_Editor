package menus.battle.trainers;

import adventuremode.sprites.Sprite;
import menus.battle.Battler;
import menus.ExitFunctionException;
import menus.pokemon.moves.Move;
import menus.pokemon.Pokemon;
import menus.in_game_items.Item;
import menus.in_game_items.ItemInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static menus.ExitFunctionException.If;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Trainer extends Sprite implements ItemInterface, Battler {

private String name;
private String alias;
private String turnText;
private final Map<String, Item> items = new HashMap<>();
private BigDecimal money;
private Move chosenMove;
private final LinkedList<Pokemon> pokeSlots = new LinkedList<>() {
	@Override
	public boolean add(Pokemon poke) {
		remove(poke);
		if (isEmpty()) {
			addFirst(poke);
			return true;
		}
		return super.add(poke);
	}
	@Override
	public void addFirst(Pokemon poke) {
		If(poke.isFainted(), "Cannot set a fainted pokemon to current pokemon")
		.If(poke == pokeSlots.peek(), "Cannot set current pokemon to current pokemon");
		remove(poke);
		super.addFirst(poke);
	}
};

@Override
public boolean isUsedTurn() {
	return getCurrentPokemon().isFainted() || chosenMove == null;
}

@Override
public Move getChosenMove() {
	Move m = chosenMove;
	chosenMove = null;
	return m;
}

@Override
public void setChosenMove(int i) {
	this.chosenMove = pokeSlots.getFirst().getMove(i);
}

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
public boolean useItem(Pokemon p, Item item) {

//Heal the pokemon if the item type is of healing and the pokemon is not fainted and if
//the pokemon is not at fullHealth already cause why heal a pokemon who's health can't go any higher.

	switch (item.getType()) {
		case "Healing":
			If(p.isFainted(), "Cannot use " + item.getName() + " on a fainted pokemon")
			.If(p.getHP() == p.getMaxHP(), "Cannot use " + item.getName() + " because " + p.getName() +"'s HP is already full");

			p.setHP(item.getHealthEffect());
			setTurnText(getAlias() + " used " + item.getName() + " to heal " + p.getName());
			break;
		case "ATK_BOOST":
			;
		default:
			throw new ExitFunctionException(item.getName() + " cannot be used on " + p.getName());
/* Eventual return of why the item could not be used
*the gui for this feature has not been made yet.
return "Unable to use Item";*/
	}
	return isUsedTurn();
}


public void fullyHealPokemon() {
	getPokeSlots().forEach(Pokemon::fullyHeal);
}


public void setPokeSlots(List<Pokemon> pokeSlots) {
	this.pokeSlots.clear();
	this.pokeSlots.addAll(pokeSlots);
}

@Override
public String toString() {
	return getClass().getSimpleName();
}

@Override
public Pokemon getCurrentPokemon() {
	return pokeSlots.getFirst();
}


}
