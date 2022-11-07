package menus.pokemon;

import menus.battle.Battler;
import menus.pokemon.types.eTypes;
import menus.pokemon.moves.Move;

import static menus.pokemon.types.eTypes.*;

public class CreatePokemon {
  private Pokemon pokemon;


  public static Pokemon Articuno(int lvl, Move[] moves, Battler battler) {
    return new Pokemon(
      ePokemon.ARTICUNO,
      "Female",
      lvl,
      255,
      lvl * 3,
      255,
      0,
      0,
      Math.pow(lvl, 3),
      0,
      moves,
      new eTypes[]{ICE, FLYING},
      battler
    );
  }

  public static Pokemon MegaGengar(int lvl, Move[] moves, Battler battler) {
    return new Pokemon(ePokemon.MEGA_GENGAR,
      "Female",
      lvl,
      300,
      lvl * 3,
      300,
      0,
      0,
      (int) Math.pow(lvl, 3),
      0,
      moves,
      new eTypes[]{GHOST, POISON},
      battler);

  }

  public static Pokemon Rayquaza(int lvl, Move[] moves, Battler battler) {
    return new Pokemon(ePokemon.RAYQUAZA,
      "Female",
      lvl,
      210,
      lvl * 4,
      210,
      0,
      0,
      (int) Math.pow(lvl, 3),
      0,
      moves,
      new eTypes[]{DRAGON, FLYING},
      battler);
    // E = floor(min(255, floor(sqrt(max(0, stat experience  - 1)) + 1)) / 4)
    //this.attack = floor(floor((2 * 45 + 4 + 0) * 53 / 100 + 5) * 0.9)
  }
}


