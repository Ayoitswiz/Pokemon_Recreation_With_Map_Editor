package MainMenu;


import java.util.Random;

//Will only ever find in adventure mode so maybe it should be in that package. Could remove the public
//modifier.
public class WildPokemon extends Pokemon {

    private boolean hasUsedTurn;
    public WildPokemon() {
        setWild(true);
    }

    protected Move chooseMove() {
        Random rand = new Random();
        // Obtain a number between [0 - 4].
        int n = rand.nextInt(4);
        return moves[n];
    }

    public boolean hasUsedTurn() {
        return hasUsedTurn;
    }

    public void setHasUsedTurn(boolean hasUsedTurn) {
        this.hasUsedTurn = hasUsedTurn;
    }
}
