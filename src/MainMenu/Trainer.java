package MainMenu;

import AdventureMode.Sprite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

abstract public class Trainer extends Sprite implements ItemInterface {

    protected String name;
    private ArrayList<Pokemon> pokeSlots = new ArrayList<>() {
        @Override
        public boolean add(Pokemon e) {
            e.setWild(false);
            return super.add(e);
        }
    };
    protected Pokemon currentPokemon;
    protected HashMap<String, Item> items;
    protected BigDecimal money;
    private Move chosenMove;
    private boolean losesTurn;
    private boolean isUsingItem;
    private boolean isUsingMove;
    private boolean hasUsedTurn;

    protected void swap() {
        currentPokemon.setHasUsedTurn(true);
    }
    protected abstract void makeDecision();

    public boolean isOutOfUsablePokemon() {
        for (Pokemon p : getPokeSlots()) {
            if (p.hp > 0) {
                return false;
            }
        }
        return true;
    }

     public void useItem(Pokemon p, Item item) {

        //Heal the pokemon if the item type is of healing and the pokemon is not fainted and if
         //the pokemon is not at fullHealth already cause why heal a pokemon who's health can't go any higher.
        if(item.type.equals("Healing") && !p.isFainted() && p.fullHealth != p.hp) {
            int maxHealthGain = p.fullHealth - p.hp;
            if (p.hp + item.healthEffect > p.fullHealth) {
                p.hp += maxHealthGain;
            } else {
                p.hp += item.healthEffect;
            }
            item.quantity--;
            isUsingItem = true;
        } else {
            /* Eventual return of why the item could not be used
            *the gui for this feature has not been made yet.
            return "Unable to use Item";*/
        }
        currentPokemon.setHasUsedTurn(true);
    }

    protected void resetBoolsForNextTurn() {
         isUsingItem = false;
         isUsingMove = false;
         hasUsedTurn = false;
        setChosenMove(null);
    }

    protected void fullyHealPokemon(){
        for(Pokemon p: getPokeSlots()) {
            p.fullyHeal();
        }
    }

    public boolean hasLostTurn() {
        return losesTurn;
    }

    public void setLosesTurn(boolean losesTurn) {
        this.losesTurn = losesTurn;
    }

    public boolean isUsingItem() {
        return isUsingItem;
    }

    public void setIsUsingItem(boolean usingItem) {
        isUsingItem = usingItem;
        //if the trainer is using an item then set hasUsedTurn to true so they can no longer use a move during battle
        //
        hasUsedTurn = true;
    }
    public Move getChosenMove() {
        return chosenMove;
    }

    public void setChosenMove(Move move) {
        chosenMove = move;
        isUsingMove = chosenMove != null;
    }

    public boolean isUsingMove() {
        return isUsingMove;
    }

    public void setUsingMove(boolean usingMove) {
        isUsingMove = usingMove;
    }

    public boolean hasUsedTurn() {
        return hasUsedTurn;
    }

    public void setHasUsedTurn(boolean hasUsedTurn) {
        this.hasUsedTurn = hasUsedTurn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Pokemon> getPokeSlots() {
        return pokeSlots;
    }

    public void setPokeSlots(ArrayList<Pokemon> pokeSlots) {
        for(Pokemon p: pokeSlots) {
            p.setWild(false);
        }
        this.pokeSlots = pokeSlots;
    }
}
