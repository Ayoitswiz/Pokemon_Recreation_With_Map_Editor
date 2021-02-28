package AdventureMode;

import MainMenu.Articuno;
import MainMenu.Rayquaza;


//At the current time this applies only for sprite sheets that have a standing column in the first column.
//invoke super to force all these classes to help make sure they implement everything they need.
public class DefaultNPC extends NPC {

    public DefaultNPC() {
        getPokeSlots().add(new Rayquaza(9, moves.get("Fire Blast", "Aerial Ace", "Tackle", "Hyper Beam")));
        getPokeSlots().add(new Articuno(12, moves.get("Blizzard", "Aerial Ace", "Tackle", "Ice Beam")));
        setCurrentPokemon(getPokeSlots().get(0));
    }
}
