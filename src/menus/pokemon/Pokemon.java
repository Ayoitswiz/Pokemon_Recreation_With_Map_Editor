package menus.pokemon;

import menus.battle.Battler;
import menus.battle.Opponent;
import menus.pokemon.types.Types;
import menus.pokemon.types.eTypes;
import menus.pokemon.moves.Move;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pokemon implements Cloneable {
  private ePokemon eName;
  private String gender = "Male";
  private int lvl;
  private int HP;
	private final String status = "STATUS_EFFECT";
  private int speed;
  private int maxHP;
  private int attack;
  private int defense;
  private double Exp;
  private int ev;
  private Move[] moves;
  private eTypes[] type;
  private Battler battler; //Todo:// store their class name not the whole battler


  Pokemon(){}



  public String attack(Pokemon opponent, Move move){
    move.setPp(move.getPp() - 1);
    int modifier = Types.type(move.getType())
      .getModifier(opponent.getType());

    opponent.setHP(-(move.getPower() * modifier));

    if (opponent.isFainted())
      gainExp(opponent.getLvl());

    return (battler instanceof Opponent ? "Foe ":"") + getName() + " used " + move.getName() + ((modifier >= 2) ? " It was super effective!!!" : ".");

    //power = ((2 * lvl)/5 + 2) * move.power * attack/opponent.defense;
  }

  //Exp gain is based on lvl of defeated pokemon
  void gainExp(int opponentLvl){
    Exp += Math.pow(opponentLvl, 2) * 3;
    // Calculate how much exp is needed at poke's lvl to lvl up
    double x = Math.pow(getLvl()+1, 3);
    if(Exp >= x){
      lvl++;
    }
  }

  public void fullyHeal() {
    HP = maxHP;
    for (Move m : moves) {
      m.setPp(m.getMaxpp());
    }
  }


  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public boolean isFainted() {
    return getHP() <= 0;
  }

  /**
   * Allows hp to be set to negative, but that's okay because
   * <code>getHP()</code> only returns an int greater than 0.
   * Just be aware that this is possible.
   * @param h should be negative for decreasing health
   */
  public void setHP(int h) {
    HP = Math.min(getHP() + h, maxHP);
  }

  public int getHP() {
    return Math.max(HP, 0);
  }


  @Override
  public String toString() {
    return eName.toString();
  }

  public String getName() {
    return eName.name();
  }


  public String getHPRatio() {
    return getHP()+"/"+getMaxHP();
  }

public String pp(int i) {
  Move m = getMoves()[i];
  return m.getPp() + "/" + m.getMaxpp() + " " + m.getType();
}

public Move getMove(int i) {
		return getMoves()[i];
}

public Move getMove0() {
	return getMoves()[0];
}

public Move getMove1() {
	return getMoves()[1];
}

public Move getMove2() {
	return getMoves()[2];
}

public Move getMove3() {
	return getMoves()[3];
}

public String pp0() {
		return pp(0);
}

public String pp1() {
	return pp(1);
}

public String pp2() {
	return pp(2);
}


public String pp3() {
	return pp(3);
}


}
