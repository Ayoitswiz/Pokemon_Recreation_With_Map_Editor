package menus.pokemon.types;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;

@AllArgsConstructor
public class Type {
private eTypes type;
private HashSet<eTypes> weaknesses;
private HashSet<eTypes> resistances;
private HashSet<eTypes> immuneTo;


public int getModifier(eTypes[] opponentType) {
	HashSet<eTypes> temp = weaknesses;
	temp.retainAll(Arrays.asList(opponentType));
	return Math.max(temp.size() * 2, 1);
}
}




