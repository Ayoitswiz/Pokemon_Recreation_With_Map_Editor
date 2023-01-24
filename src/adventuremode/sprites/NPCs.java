package adventuremode.sprites;

import menus.pokemon.moves.Moves;
import menus.pokemon.CreatePokemon;

import static menus.pokemon.moves.eMoves.BLIZZARD;
import static menus.pokemon.moves.eMoves.DIAMOND_STORM;
import static menus.pokemon.moves.eMoves.FIRE_BLAST;
import static menus.pokemon.moves.eMoves.ICE_BEAM;
import static menus.pokemon.moves.eMoves.STRONG_MOVE;
import static menus.pokemon.moves.eMoves.WEAK_MOVE;

public class NPCs extends NPC {

public enum Type {
	ROCKET_GRUNT_MALE, ROCKET_GRUNT_MALE2, ROCKET_GRUNT_MALE3, ROCKET_GRUNT_MALE4, ROCKET_GRUNT_MALE5
}

public NPCs(Type type, Direction direction, int startingCell, int cellCountToMoveThrough) {

	switch (type) {
		case ROCKET_GRUNT_MALE -> {
			preBattleDialog = new String[]{"All the pokemon in Mt. Moon are ours!...", "Don't try to stop us..."};
			getPokeSlots().add(CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), this));
			getPokeSlots().add(CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), this));
		}
		case ROCKET_GRUNT_MALE2 -> {
			preBattleDialog =      new String[]{"Wait, who are you?! You're not supposed to be here!"};
			getPokeSlots().add(CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), this));
			getPokeSlots().add(CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), this));

		}
		case ROCKET_GRUNT_MALE3 -> {
			preBattleDialog =      new String[]{"All the pokemon in Mt. Moon are ours!...", "Don't try to stop us..."};
			getPokeSlots().add(CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), this));
			getPokeSlots().add(CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), this));
			setMaxMovingIndex(3);
		}
		case ROCKET_GRUNT_MALE4 -> {
			preBattleDialog = new String[]{"Wait, who are you?! You're not supposed to be here!"};
			getPokeSlots().add(CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), this));
			getPokeSlots().add(CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), this));
		}
		case ROCKET_GRUNT_MALE5 -> {
			preBattleDialog =      new String[]{"Wait, who are you?! You're not supposed to be here!"};
			getPokeSlots().add(CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), this));
			getPokeSlots().add(CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), this));
		}
	}

	this.battleGlareRange = 6;
	setName(type.name());
	startingCellNum = startingCell;
	this.cellCountToMoveThrough = cellCountToMoveThrough;
	setMaxMovingIndex(3);
	setStartingDirection(direction);
	this.dir = direction;
	createSpriteMaps("src/adventuremode/img/TeamRocketMaleGrunt.png",
	4,
	0,
	60,
	64,
	1,
	2,
	3,
	0);
}
}
