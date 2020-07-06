package MainMenu;

import java.util.HashMap;
import java.util.Random;
public class AITrainer extends Trainer {
    private Move move = new Move();
    protected HashMap<String, Move> moves = move.createMove();
    public AITrainer() {

    }

    @Override
    protected void swap() {
        super.swap();
        for (Pokemon p: getPokeSlots()) {
            if(p.hp>0) {
                currentPokemon = p;
                currentPokemon.fullHealth = currentPokemon.hp;
                break;
            }
        }
        if(currentPokemon.hp<=0) {
            setDefeated(true);
        }
    }

    @Override
    public void makeDecision() {
        Random rand = new Random();
        // Obtain a number between [0 - 4].
        int n = rand.nextInt(4);
        if (currentPokemon.hp < 80 && n >=0 ) {
            useItem(currentPokemon, new Item().createItem().get("Max Potion"));
            setIsUsingItem(true);
            setChosenMove(null);
        }
        else {
            setChosenMove(currentPokemon.moves[n]);
        }
    }
}
