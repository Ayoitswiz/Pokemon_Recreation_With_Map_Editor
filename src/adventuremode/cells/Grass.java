package adventuremode.cells;

import adventuremode.PokemonList;
import adventuremode.components.DialogBox;
import adventuremode.sprites.MySprite;
import adventuremode.sprites.NPC;
import menus.battle.BattleGUI;
import menus.pokemon.WildPokemon;
import menus.pokemon.ePokemon;

import java.awt.Rectangle;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class Grass extends Cell {

  private List<WildPokemon> pokemonInArea;
  private PokemonList pl = new PokemonList();
  private final Random rand;


{
  try {
    rand = SecureRandom.getInstanceStrong();
  } catch (NoSuchAlgorithmException e) {
    throw new RuntimeException(e);
  }
}

public Grass(ePokemon... pokemon) {
    setPokemonInArea(pokemon);
  }

  public void determineIfPokemonIsThere(MySprite user, DialogBox dBox) {
    if (rand.nextInt(15) == 5) {
      encounterPokemon(user, dBox);
    }
  }

  private void encounterPokemon(MySprite user, DialogBox dBox) {

    WildPokemon wildPokemon =
      pl.get(
        pokemonInArea.get(rand.nextInt(pokemonInArea.size()))
         .getPokemon()
				 .getEName()
      );
		NPC.canMove(false);
    dBox
      .open()
      .setDialog(wildPokemon.getPreBattleDialog())
      .onDialogEnd(() -> {
        dBox.setVisible(false);
        new BattleGUI()
				.onClose(() -> NPC.canMove(true))
				.open(user, wildPokemon);
      });
  }

  public boolean intersects(Rectangle r) {
    return getBounds().intersects(r);
  }

  private void setPokemonInArea(ePokemon... pokemon) {
		pokemonInArea = (pl = new PokemonList()).create(pokemon);
  }
}
