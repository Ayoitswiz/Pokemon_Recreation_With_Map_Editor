package AdventureMode;

import gg.Battle.BattleGUI;
import gg.Battle.Trainers.AITrainer;
import gg.Pokemon.WildPokemon;
import gg.Pokemon.ePokemon;

import java.awt.*;
import java.util.*;

public class Grass extends Collision {

  private ArrayList<WildPokemon> pokemonInArea;
  private PokemonList pl = new PokemonList();

  Grass(ePokemon... pokemon) {

    setPokemonInArea(pokemon);
  }

  @Override
  protected boolean isGrassBlock() {
    return true;
  }

  public void determineIfPokemonIsThere(MySprite user, DialogBox dBox) {
    if (new Random().nextInt(15) == 5) {
      encounterPokemon(user, dBox);
    }
  }

  private void encounterPokemon(MySprite user, DialogBox dBox) {
    WildPokemon wildPokemon =
      pl.get(
        pokemonInArea.get(new Random().nextInt(pokemonInArea.size()))
          .getPokemon().getEName()
      );
    user.setOpponent(wildPokemon);

    user.setMoving(false);

    dBox
      .open()
      .setDialog(wildPokemon.getPreBattleDialog())
      .onDialogEnd(() -> {
        dBox.setVisible(false);
        AITrainer.canMove = false;
        new BattleGUI(user, wildPokemon)
          .onBattleGUIClose(() -> {
            AITrainer.canMove = true;
          });
      });
  }

  public boolean collides(Rectangle r) {
    return getBounds().intersects(r);
  }

  private void setPokemonInArea(ePokemon... pokemon) {
    pl = new PokemonList();
    pokemonInArea = pl.create(pokemon);
    //Pokemon p = new Pokemon();
  }
}
