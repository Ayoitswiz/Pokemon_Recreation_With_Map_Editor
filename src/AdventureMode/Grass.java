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
        Random r = new Random();
        if (r.nextInt(10) == 5) {
            encounterPokemon(user);
        }
    }
    private void encounterPokemon(MySprite user) {
        Random r = new Random();
        int i = r.nextInt(pokemonInArea.size());
        user.setPokemonOpponent(pokemonInArea.get(i));
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
    boolean moveInto() {
        return true;
    }

    private void setPokemonInArea(String...pokemon) {
        PokemonList pl = new PokemonList();
        pokemonInArea = pl.create(pokemon);

        System.out.println(Arrays.toString(pokemon));
        //Pokemon p = new Pokemon();


    }
}
