package menus.battle.trainers;

import menus.battle.Opponent;
import menus.ExitFunctionException;
import menus.pokemon.Pokemon;
import menus.in_game_items.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;
import utilities.Lambda;

import java.math.BigDecimal;
import java.util.Random;
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AITrainer extends Trainer implements Opponent {

    public AITrainer() {
        setMoney(new BigDecimal(300 * new Random().nextInt(4)));
        setAlias("Foe");
    }

    @Override
    public void faintedSwap(Lambda swapText) {
        for (Pokemon p: getPokeSlots()) {
            if(p.getHP() > 0) {
							getPokeSlots().addFirst(p);
                break;
            }
        }
        setTurnText(getClass().getSimpleName() + " sent out " +  getCurrentPokemon().getName());
        swapText.foo();
    }

    @Override
    public String getCurrentPokemonFaintedText() {
        return "Foe " + getCurrentPokemon().getName() + " fainted!";
    }

    @Override
    public void makeDecision() {
        // Obtain a number between [0 - 4].
        // TODO: 8/1/2020 decrease chance of using item or take out when shipped
        int n = new Random().nextInt(4);
        if (getCurrentPokemon().getHP() < 80 && n >=3 ) {
            useItem(getCurrentPokemon(), new Item().createItem().get("Max Potion"));
        }
        else {
            setChosenMove(n);
        }
    }


    @Override
    public String getLostBattleDialog() {
        return "You defeated " + getName() + "! You earn " + getMoney() + " PokeDollars";
    }

    @Override
    public BigDecimal getEndBattleMoney() {
        return getMoney();
    }

    @Override
    public String getDialogIfUserTrysToFleeBattle() {
        ExitFunctionException
          .If(true, "You cannot flee from a trainer battle! " +
            "Have some pride!!!");
        return null;

    }
}
