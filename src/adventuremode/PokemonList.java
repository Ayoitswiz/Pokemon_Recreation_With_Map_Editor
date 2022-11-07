package adventuremode;

import menus.pokemon.moves.Moves;
import menus.pokemon.WildPokemon;
import menus.pokemon.ePokemon;

import java.util.ArrayList;
import java.util.List;

import static menus.pokemon.CreatePokemon.Articuno;
import static menus.pokemon.CreatePokemon.MegaGengar;
import static menus.pokemon.CreatePokemon.Rayquaza;
import static menus.pokemon.moves.eMoves.*;

public class PokemonList {

public List<WildPokemon> create(ePokemon... pokemon) {
	List<WildPokemon> pl = new ArrayList<>();

	for(ePokemon p: pokemon) {
		WildPokemon w = new WildPokemon();
		w.setPokemon(switch (p) {
			case ARTICUNO -> 		Articuno(10, Moves.Move(BLIZZARD, TACKLE, AERIAL_ACE, ICE_BEAM), w);
			case RAYQUAZA -> 		Rayquaza(12, Moves.Move(HYPER_BEAM, DIAMOND_STORM, TACKLE, FIRE_BLAST), w);
			case MEGA_GENGAR -> MegaGengar(6,Moves.Move(WEAK_MOVE, STRONG_MOVE, SHADOW_BALL, ICE_BEAM), w);
		});
		pl.add(w);
	}
	return pl;
}

public WildPokemon get(ePokemon ePoke) {
	WildPokemon w = new WildPokemon();
	w.setPokemon(switch (ePoke) {
		case ARTICUNO -> 		Articuno(10, Moves.Move(BLIZZARD, TACKLE, AERIAL_ACE, ICE_BEAM), w);
		case RAYQUAZA -> 		Rayquaza(12, Moves.Move(HYPER_BEAM, DIAMOND_STORM, TACKLE, FIRE_BLAST), w);
		case MEGA_GENGAR -> MegaGengar(6,Moves.Move(WEAK_MOVE, STRONG_MOVE, SHADOW_BALL, ICE_BEAM), w);
	});
	return w;
}
}
