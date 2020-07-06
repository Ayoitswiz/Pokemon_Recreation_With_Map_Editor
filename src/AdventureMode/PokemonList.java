package AdventureMode;

import MainMenu.*;

import java.util.ArrayList;
import java.util.HashMap;

public class PokemonList {

    PokemonList() {
    }

    protected ArrayList<Pokemon> create(String...pokemon) {
        ArrayList<Pokemon> pl = new ArrayList<>();
        Move move = new Move();
        HashMap<String, Move> moves = move.createMove();
        for(String p: pokemon) {
            switch (p) {
                case "Articuno":
                    pl.add(new Articuno(10, new Move[]{moves.get("Blizzard"), moves.get("Aerial Ace"), moves.get("Tackle"), moves.get("Ice Beam")}));
                case "Rayquaza":
                    pl.add(new Rayquaza(12, new Move[]{moves.get("Blizzard"), moves.get("Aerial Ace"), moves.get("Tackle"), moves.get("Ice Beam")}));
            }
        }
        return pl;
    }
}
