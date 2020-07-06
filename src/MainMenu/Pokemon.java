package MainMenu;

import java.util.ArrayList;
import java.util.Random;

abstract public class Pokemon implements Cloneable {
    protected String name;
    protected int lvl;
    protected int hp;
    protected int speed;
    protected int fullHealth;
    private boolean fainted = false;
    protected int attack;
    protected int defense;
    protected Move[] moves;
    protected int Exp;
    protected ArrayList<Type> type = new ArrayList<>();
    protected double ev;
    private boolean isWild = true;


    protected String attack(Pokemon opponent, Move move){
        int power;
        //power = ((2 * lvl)/5 + 2) * move.power * attack/opponent.defense;
        power = move.power;
        power = opponent.type.get(0).Multiplier(opponent.type, move.moveType, power);

        //Decrease the opponents hp
        decreaseHealth(opponent, power);
        move.pp -= 1;
        if(power == move.power * 2 || power == move.power * 4)
            return name +  " used "  + move.name  + " It was super effective";
        return name +  " used "  + move.name;
    }

    protected void fullyHeal() {
        hp = fullHealth;
        for (Move m : moves) {
            m.pp = m.maxpp;
        }
        fainted = false;
    }

    //Decrease the health of the pokemon being attacked. If their hp drops to zero
    //they have fainted and the pokemon who attacked is rewarded with xp.
    private void decreaseHealth(Pokemon opponent, int damage) {
        opponent.hp -= damage;
        if (opponent.hp <= 0) {
            opponent.fainted = true;
            gainExp(opponent.lvl);
        }
    }

    void gainExp(int defeatedPokemonsLvl){
        //Ai pokemon gives exp based on lvl
        Exp += (int) Math.pow(defeatedPokemonsLvl, 2) * 3;
        int x = (int) Math.pow(lvl + 1, 3);
        if(Exp >= x){
            lvl++;
        }
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void changeName(String newName){
        name = newName;
    }

    public boolean isFainted() {
        return fainted;
    }

    public boolean isWild() {
        return isWild;
    }

    protected void setWild(boolean wild) {
        isWild = wild;
    }


    private boolean hasUsedTurn;

    //if the pokemon is wild they need to be able to choose a move because there is no trainer to
    //decide for them.
    protected Move chooseMove() {
        Random rand = new Random();
        // Obtain a number between [0 - 4].
        int n = rand.nextInt(4);
        return moves[n];
    }
    //if the pokemon is wild they need to be able to use a turn because there is no trainer to
    //use it for them.
    public boolean hasUsedTurn() {
        return hasUsedTurn;
    }
    //if the pokemon is wild they need to be able to use a turn because there is no trainer to
    //use it for them.
    public void setHasUsedTurn(boolean hasUsedTurn) {
        this.hasUsedTurn = hasUsedTurn;
    }
}
