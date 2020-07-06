package AdventureMode;

import MainMenu.Articuno;
import MainMenu.Move;
import MainMenu.Rayquaza;

import java.util.ArrayList;

//At the current time this applies only for sprite sheets that have a standing column in the first column.
//invoke super to force all these classes to help make sure they implement everything they need.
public class DefaultNPC extends NPC {

    public DefaultNPC() {
        setPokeSlots(new ArrayList<>());
        getPokeSlots().add(new Rayquaza(9, new Move[]{moves.get("Fire Blast"), moves.get("Aerial Ace"), moves.get("Tackle"), moves.get("Hyper Beam")}));
        getPokeSlots().add(new Articuno(12, new Move[]{moves.get("Blizzard"), moves.get("Aerial Ace"), moves.get("Tackle"), moves.get("Ice Beam")}));
        this.currentPokemon = getPokeSlots().get(0);
    }
}
