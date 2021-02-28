package MainMenu;

import java.util.Random;
public class AITrainer extends Trainer {
    protected Moves<String, Move> moves = new Moves<>();
    public static boolean canMove = true;

    public AITrainer() {

    }

    @Override
    protected void swap() {
        super.swap();
        for (Pokemon p: getPokeSlots()) {
            if(p.hp > 0) {
                setCurrentPokemon(p);
                //I don't know why I'm doing this but I'm to lazy to test rn.
                getCurrentPokemon().fullHealth = getCurrentPokemon().hp;
                break;
            }
        }
        setDefeated(getCurrentPokemon().hp <= 0);
    }

    @Override
    public void makeDecision() {
        // Obtain a number between [0 - 4].
        // TODO: 8/1/2020 decrease chance of using item or take out when shipped
        int n = new Random().nextInt(4);
        if (getCurrentPokemon().hp < 80 && n >=0 ) {
            useItem(getCurrentPokemon(), new Item().createItem().get("Max Potion"));
            setIsUsingItem(true);
            setChosenMove(null);
        }
        else {
            setChosenMove(getCurrentPokemon().moves[n]);
        }
    }
}
