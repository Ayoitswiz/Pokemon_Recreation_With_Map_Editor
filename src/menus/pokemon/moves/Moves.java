package menus.pokemon.moves;

import menus.pokemon.types.eTypes;
import utilities.LambdaWithReturnVal;

import java.util.Arrays;
import java.util.HashMap;

import static menus.pokemon.types.eTypes.*;
import static menus.pokemon.moves.eMoves.*;

/**
 * @param <K> The key is the eMove name
 * @param <T> The value is a lambda so the get() method creates a new move everytime.
 */

public final class Moves<K extends eMoves, T extends LambdaWithReturnVal<Move>> extends HashMap<eMoves, LambdaWithReturnVal<Move>> {

private static final Moves<eMoves, LambdaWithReturnVal<Move>> MOVES = new Moves<>();

private Moves() {}

static {
	for (var move : new Object[][]{
	/*{NAME                     PP    ACCURACY  Power       Type        }*/
	{FIRE_BLAST,              5,      100,    120,        FIRE},
	{ICE_BEAM,                10,     95,     100,        ICE},
	{BLIZZARD,                5,      70,     120,        ICE},
	{AERIAL_ACE,              10,     100,    90,         FLYING},
	{HYPER_BEAM,              5,      100,    150,        NORMAL},
	{TACKLE,                  40,     100,    40,         NORMAL},
	{WEAK_MOVE,               40,     100,    10,         NORMAL},
	{SHADOW_BALL,             10,     100,    80,         GHOST},
	{DIAMOND_STORM,           5,      95,     100,        ROCK},
	{STRONG_MOVE,             99,     100,    999,        NORMAL}
	}) {

		MOVES.put(
		(eMoves) move[0],
		() -> new Move(
		((eMoves) move[0]),
		(int) move[1],
		(int) move[1],
		(int) move[2],
		(int) move[3],
		(eTypes) move[4]
		)
		);
	}
}

/**
 * Takes multiple strings or just one string as a parameter and queries the contents of this class
 * and returns a <code> Move[] </code>
 *
 * @param keys to query the HashMap with. Acceptable arguments: <code> get(" ") || get(" ", " ",...) </code>
 * @return returns the value(s) of the HashMap as a Move[]
 */
@SafeVarargs
public static <K extends eMoves> Move[] Move(K... keys) {
	Move[] moveArray = new Move[keys.length];
	int i = 0;
	for (K k : keys) {
		if (MOVES.containsKey(k))
			moveArray[i++] = Moves.MOVES.get(k).foo();
	}
	return moveArray;
}

public static void main(String... args) {
	for (LambdaWithReturnVal<Move> m : new Moves<>().values()) {
		System.out.println(Arrays.toString(
		new Object[]{
		m.foo().getPp() + "\t",
		m.foo().getAccuracy() + "\t",
		m.foo().getPower() + "\t",
		m.foo().getType() + "\t\t",
		m.foo().getName()
		}));
	}
}
}
