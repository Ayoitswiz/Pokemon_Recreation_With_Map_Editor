package menus.battle.trainers;

import adventuremode.sprites.MySprite;
import menus.battle.BattleGUI;
import menus.ViewPokeSlots.PokemonInPartyPanel;
import menus.gui;
import utilities.Lambda;

import java.math.BigDecimal;

public class HumanTrainer extends Trainer {

public HumanTrainer() {
	setAlias("You");
}

@Override
public void faintedSwap(Lambda onPokemonPanelClose) {
	new PokemonInPartyPanel()
	.open(getPokeSlots(),
	poke -> {
		getPokeSlots().addFirst(poke);
		setTurnText("Go " + getCurrentPokemon().getName() + "!");
		onPokemonPanelClose.foo();
	}).onPartyClose(() -> gui.setUIAndBackgroundImg(BattleGUI.class.getSimpleName()));
}

@Override
public String getCurrentPokemonFaintedText() {
	return getCurrentPokemon().getName() + " fainted!";
}

@Override
public void makeDecision() {
}


@Override
public BigDecimal getEndBattleMoney() {
	return new BigDecimal(0);
}

public void updateMoney(BigDecimal moneyGainedOrlost) {
	setMoney(getMoney().add(moneyGainedOrlost));
}

@Override
public String getLostBattleDialog() {
	BigDecimal moneyStolen = getMoney().divide(new BigDecimal(10));
	updateMoney(moneyStolen.negate());
	return "You have lost the battle! " +
				 "Wait!? What!? He's taking your wallet!!! " +
				 "You lost " + moneyStolen +
				 "You now have " + getMoney();
}

public String lostBattleDialog() {
	return this instanceof MySprite
				 ?
				 "You are all out of pokemon. " +
				 "You lose 200 pokedollars. You now have " + getMoney() + " pokedollars." +
				 " You'll be returned to the Pokemon Center"
				 :
				 "You lose 200 pokedollars. You now have " + getMoney() + " pokedollars." +
				 " Click Continue to return to the Main Menu...";
}

public String wonBattleDialog() {
	return "You win! You earned " + "200 pokedollars. " +
				 "You now have " + getMoney() + " pokedollars." +
				 (this instanceof MySprite ? "" : " Click Continue to return to the Main Menu...");

}

}


