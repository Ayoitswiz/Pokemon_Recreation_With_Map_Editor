package AdventureMode;

import MainMenu.*;

import java.util.ArrayList;
import java.util.HashMap;

public class PokemonList {

    PokemonList() {
    }

    protected ArrayList<Pokemon> create(String...pokemon) {
        ArrayList<Pokemon> pl = new ArrayList<>();
        Moves<String, Move> moves = new Moves<>();
        for(String p: pokemon) {
            switch (p) {
                case "Articuno":
                    pl.add(new Articuno(10, moves.get("Blizzard", "Aerial Ace", "Tackle", "Ice Beam")));
                case "Rayquaza":
                    pl.add(new Rayquaza(12, moves.get("Blizzard", "Aerial Ace", "Tackle", "Ice Beam")));
            }
        }
        return pl;
    }
}
