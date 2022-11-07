package adventuremode.sprites;

import menus.pokemon.moves.Moves;
import menus.pokemon.CreatePokemon;

import static menus.pokemon.moves.eMoves.BLIZZARD;
import static menus.pokemon.moves.eMoves.DIAMOND_STORM;
import static menus.pokemon.moves.eMoves.FIRE_BLAST;
import static menus.pokemon.moves.eMoves.ICE_BEAM;
import static menus.pokemon.moves.eMoves.SHADOW_BALL;
import static menus.pokemon.moves.eMoves.STRONG_MOVE;
import static menus.pokemon.moves.eMoves.WEAK_MOVE;


//At the current time this applies only for sprite sheets that have a standing column in the first column.
//invoke super to force all these classes to help make sure they implement everything they need.
public class DefaultNPC extends NPC {

    public DefaultNPC() {
        getPokeSlots().add(CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), this));
        getPokeSlots().add(CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), this));
        getPokeSlots().add(CreatePokemon.MegaGengar(9, Moves.Move(ICE_BEAM, WEAK_MOVE, SHADOW_BALL, STRONG_MOVE), this));

        preBattleDialog = new String[]{"All the pokemon in Mt. Moon are ours!...", "Don't try to stop us..."};
    }
}
