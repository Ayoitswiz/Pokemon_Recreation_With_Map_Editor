package gg.Pokemon;

import gg.Battle.Trainers.AITrainer;
import gg.Battle.Battler;
import gg.Pokemon.Moves.Move;
import gg.Pokemon.AllTypes.Types;
import lombok.AllArgsConstructor;
import lombok.Data;

import gg.Pokemon.AllTypes.eTypes;

@Data
@AllArgsConstructor
public class Pokemon implements Cloneable {
  private ePokemon eName;
  private String gender = "Male";
  private int lvl;
  private int HP;
  private int speed;
  private int maxHP;
  private int attack;
  private int defense;
  private double Exp;
  private int ev;
  private Move[] moves;
  private eTypes[] type;
  private Battler battler;


  Pokemon(){}



  public String attack(Pokemon opponent, Move move){
    move.setPp(move.getPp() - 1);
    getBattler().setUsedTurn(true);
    int modifier = Types.type(move.getType())
      .getModifier(opponent.getType());

    opponent.setHP(-(move.getPower() * modifier));

    if (opponent.isFainted())
      gainExp(opponent.getLvl());

    return (battler instanceof AITrainer ? "Foe ":"") + getName() + " used " + move.getName() + ((modifier >= 2) ? " It was super effective!!!" : ".");

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
    return getClass().getSimpleName();
  }

  public String getName() {
    return eName.name();
  }
}
