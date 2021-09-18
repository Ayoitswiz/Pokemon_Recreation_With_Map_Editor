package AdventureMode;

import gg.Pokemon.Moves.Moves;
import gg.Pokemon.Pokemon;
import gg.Pokemon.WildPokemon;
import gg.Pokemon.ePokemon;

import static gg.Pokemon.Moves.eMoves.*;
import static gg.Pokemon.CreatePokemon.*;
import java.util.ArrayList;

public class PokemonList {

    PokemonList() {
    }

    protected ArrayList<WildPokemon> create(ePokemon... pokemon) {
        ArrayList<WildPokemon> pl = new ArrayList<>();

        for(ePokemon p: pokemon) {
            WildPokemon w = new WildPokemon();
            w.setPokemon(switch (p) {
                case ARTICUNO -> Articuno(10, Moves.Move(BLIZZARD, TACKLE, AERIAL_ACE, ICE_BEAM), w);
                case RAYQUAZA -> Rayquaza(12, Moves.Move(HYPER_BEAM, DIAMOND_STORM, TACKLE, FIRE_BLAST), w);
                case MEGA_GENGAR -> MegaGengar(6,Moves.Move(WEAK_MOVE, STRONG_MOVE, SHADOW_BALL, ICE_BEAM), w);
            });
            pl.add(w);
        }
        return pl;
    }

    public WildPokemon get(ePokemon ePoke) {
        WildPokemon w = new WildPokemon();
        w.setPokemon(
        switch (ePoke) {
            case ARTICUNO -> Articuno(10, Moves.Move(BLIZZARD, TACKLE, AERIAL_ACE, ICE_BEAM), w);
            case RAYQUAZA -> Rayquaza(12, Moves.Move(HYPER_BEAM, DIAMOND_STORM, TACKLE, FIRE_BLAST), w);
            case MEGA_GENGAR -> MegaGengar(6,Moves.Move(WEAK_MOVE, STRONG_MOVE, SHADOW_BALL, ICE_BEAM), w);
        });
        return w;
    }
}
