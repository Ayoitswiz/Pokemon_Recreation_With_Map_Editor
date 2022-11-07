package menus.pokemon.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static menus.pokemon.types.eTypes.*;

public final class Types extends HashMap<eTypes, Type> {
  private static final Types types = new Types();
  private Types(){}

  public static Type type(eTypes type) {
    return Types.types.get(type);
  }

  /**
   * Note: could load in a csv of the official weak/resist charts by nintendo
   * <a href="https://rankedboost.com/pokemon-lets-go/type-chart/">https://rankedboost.com/pokemon-lets-go/type-chart/</a>
   */
  static {
    for (eTypes[][] type: new eTypes[][][]{
      {{NORMAL},
        {FIGHTING},
        {null},
        {GHOST}},
      {{FIGHTING},
        {FLYING, PSYCHIC, FAIRY},
        {ROCK, BUG, DARK},
        {null}},
      {{FLYING},
        {ROCK, ELECTRIC, ICE},
        {FIGHTING, BUG, GRASS},
        {GROUND}},
      {{POISON},
        {GROUND, PSYCHIC},
        {FIGHTING, POISON, GRASS, FAIRY},
        {null}},
      {{GROUND},
        {WATER, GRASS, ICE},
        {POISON, ROCK, },
        {ELECTRIC}},
      {{ROCK},
        {FIGHTING, GROUND, STEEL, WATER, GRASS},
        {NORMAL, FLYING, POISON, FIRE},
        {null}},
      {{BUG},
        {FLYING, ROCK, FIRE},
        {FIGHTING, GROUND, GRASS},
        {null}},
      {{GHOST},
        {GHOST, DARK},
        {POISON, BUG, },
        {NORMAL, FIGHTING}},
      {{STEEL},
        {FIGHTING, GROUND, FIRE},
        {NORMAL, FLYING, ROCK, BUG, STEEL, GRASS, PSYCHIC, ICE, DRAGON, FAIRY},
        {POISON}},
      {{FIRE},
        {GROUND, ROCK, WATER},
        {BUG, STEEL, FIRE, GRASS, ICE, FAIRY},
        {null}},
      {{WATER},
        {GRASS, ELECTRIC},
        {STEEL, FIRE, WATER, ICE},
        {null}},
      {{GRASS},
        {FLYING, POISON, BUG, FIRE, ICE},
        {GROUND, WATER, GRASS, ELECTRIC},
        {null}},
      {{ELECTRIC},
        {GROUND},
        {FLYING, STEEL, ELECTRIC},
        {null}},
      {{PSYCHIC},
        {BUG, GHOST, DARK},
        {FIGHTING, PSYCHIC},
        {null}},
      {{ICE},
        {FIGHTING, ROCK, STEEL, FIRE},
        {ICE},
        {null}},
      {{DRAGON},
        {ICE, DRAGON, FAIRY},
        {FIRE, WATER, GRASS, ELECTRIC},
        {}},
      {{DARK},
        {FIGHTING, BUG, FAIRY},
        {GHOST, DARK},
        {PSYCHIC}},
      {{FAIRY},
        {POISON, STEEL},
        {FIGHTING, BUG, DARK},
        {DRAGON}},
    }) types.put(type[0][0], new Type(
      type[0][0],
      new HashSet<>(Arrays.asList(type[1])),
      new HashSet<>(Arrays.asList(type[2])),
      new HashSet<>(Arrays.asList(type[3]))));
  }
}
