package MainMenu;


import AdventureMode.NPC;

import java.math.BigDecimal;

public class HumanTrainer extends Trainer {

    private Item selectedItem;
    private NPC opponent;
    private Pokemon pokemonOpponent;
    private boolean hasOpponent;
    private boolean isInAdventureMode = false;
    public HumanTrainer() {
    }

    @Override
    protected void swap() {
        setDefeated(isOutOfUsablePokemon());
    }

    void updatePlayerMoney(BigDecimal pMoney){
        money = money.add(pMoney);
    }

    @Override
    protected void resetBoolsForNextTurn() {
        super.resetBoolsForNextTurn();
        selectedItem = null;
    }

    @Override
    protected void makeDecision() {
    }

    protected void setUseItem(Item item) {
        selectedItem = item;
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    public NPC getNpcOpponent() {
        return opponent;
    }

    public void setOpponent(NPC opponent) {
        this.opponent = opponent;
        hasOpponent = opponent != null;
    }

    public boolean isInAdventureMode() {
        return isInAdventureMode;
    }

    public void setInAdventureMode(boolean inAdventureMode) {
        isInAdventureMode = inAdventureMode;
    }

    public boolean hasOpponent() {
        return hasOpponent;
    }

    public Pokemon getPokemonOpponent() {
        return pokemonOpponent;
    }

    public void setPokemonOpponent(Pokemon pokemonOpponent) {
        this.pokemonOpponent = pokemonOpponent;
        hasOpponent = pokemonOpponent != null;
    }

}


