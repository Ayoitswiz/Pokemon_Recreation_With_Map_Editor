package AdventureMode;

import MainMenu.BattleGUI;
import MainMenu.GUIManager;
import MainMenu.Pokemon;
import java.awt.*;
import java.util.*;

public class Grass extends Collision {

    private ArrayList<Pokemon> pokemonInArea;
    private GUIManager gui;
    Grass(GUIManager gui, String... pokemon) {
        this.gui = gui;
        setPokemonInArea(pokemon);
    }

    @Override
    protected boolean isGrassBlock() {
        return true;
    }

    public void determineIfPokemonIsThere(MySprite user) {
        if (new Random().nextInt(15) == 5) {
            encounterPokemon(user);
        }
    }
    private void encounterPokemon(MySprite user) {
        user.setPokemonOpponent(pokemonInArea.get(new Random().nextInt(pokemonInArea.size())));
        user.setMoving(false);
        BattleGUI battleGUI = new BattleGUI(user, gui);
        gui.addNewUi(battleGUI);
        gui.setUi(battleGUI.getClass().getSimpleName());
        gui.addNewBackgroundImage(battleGUI.getClass().getSimpleName(), battleGUI.getBackgroundImage());
        gui.setBackgroundImage(battleGUI.getClass().getSimpleName());
    }

    public boolean collides(Rectangle r) {
        return getBounds().intersects(r);
    }

    @Override
    boolean isAccessible() {
        return true;
    }

    private void setPokemonInArea(String...pokemon) {
        PokemonList pl = new PokemonList();
        pokemonInArea = pl.create(pokemon);
        //Pokemon p = new Pokemon();
    }
}
