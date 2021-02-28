package MainMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.function.Predicate;

/*
 * Brandon Wisniewski
 * Pokemon Battle Simulator
 * 4/7/2020
 *
 * */
/*Additional features:
 * Ui for swapping pokemon
 * Item shop
 * Backpack
 * Responsive Ui's
 * Actual pokemon types, with damage multipliers
 * Click through play by play game dialog(much harder than I thought) :(
 * Healing items
 * Not having to exit program to start another battle properly
 * Works very similarly to how pokemon battles actually are
 *
 * Things to note:
 * Using an item counts as a turn and as such, you don't get to use a move.
 * If all your pokemon faint you cannot start a new battle until you heal
 * You can have up to 6 pokemon
 * If ai pokemon's health is below 80 hp they have a 75% chance of using a healing item.
 * You need to purchase items in the item shop
 */
public class BattleManager {
    private AITrainer ai;
    private HumanTrainer user;
    private JButton driver, collapseDialogPanel;
    private JTextArea gameDialog;
    private GUIManager gui;
    private Predicate<Trainer> isUser = isUser -> isUser == user;

    //START FROM BATTLEGUI
    public BattleManager(HumanTrainer user, AITrainer ai, JButton btnContinue, JTextArea txtLeftDefaultText, JButton btnCollapseDialogPanel, GUIManager gui) {
        this(user, btnContinue, txtLeftDefaultText, btnCollapseDialogPanel, gui);
        this.ai = ai;
    }

    public BattleManager(HumanTrainer user, JButton btnContinue, JTextArea txtLeftDefaultText, JButton btnCollapseDialogPanel, GUIManager gui) {
        this.user = user;
        driver = btnContinue;
        gameDialog = txtLeftDefaultText;
        collapseDialogPanel = btnCollapseDialogPanel;
        ai = user.getNpcOpponent();
        this.gui = gui;
    }

    public void battleEnd() {

        user.updatePlayerMoney(user.isDefeated() ? new BigDecimal(-200) : new BigDecimal(200));
        String wonOrNot = "200 pokedollars. You now have " + user.getMoney() + " pokedollars.";
        String losesMoney = "You lose " + wonOrNot;
        String goBackToMainMenu = " Click Continue to return to the Main Menu...";
        //I know this looks kinda stupid but I had just figured out how to use ternary so I couldn't help myself.
        // now im to lazy to fix
        setGameDialog(
                user.isDefeated() ?
                    user.isOutOfUsablePokemon() ?
                        user.isInAdventureMode() ? "You are all out of pokemon. " + losesMoney + " You'll be returned to the Pokemon Center"
                        : losesMoney + goBackToMainMenu
                    : user.getName() + " has decided to run from the battle. " + losesMoney
                : "You win! You earned " + wonOrNot + (user.isInAdventureMode() ? "" : goBackToMainMenu)
        );

        driver.addActionListener(this::actionPerformed);
        // TODO: 8/7/2020
        //  need to add method: if(user has no pokemon with hp && getInAdventureMode == true) send to pokemon center
    }

    public void turnManager() {
        //if ai does not use an item
        Pokemon pOne = user.getCurrentPokemon();
        if (user.getNpcOpponent() != null) {
            ai.makeDecision();
            if (ai.isUsingMove()) {
                determineWhoHasHigherSpeed(pOne, ai.getCurrentPokemon());
            } else {
                //if ai and ht use an item
                setGameDialog(ai.getName() + " used a Max Potion!");
                //Ai has used its turn to use an item, so
                startSecondTrainersTurn(ai.getCurrentPokemon(), pOne);
            }
        } else {
            determineWhoHasHigherSpeed(pOne, user.getPokemonOpponent());
        }
    }

    private void determineWhoHasHigherSpeed(Pokemon pOne, Pokemon pTwo) {
        if (pOne.speed >= pTwo.speed && !user.hasLostTurn()) {
            attack(pOne, pTwo);
        } else {
            attack(pTwo, pOne);
        }
    }

    private void attack(Pokemon pOne, Pokemon pTwo) {
        //foe always chooses move even if an the ai is using an item. Still works but..
        setGameDialog(
                pOne == user.getCurrentPokemon()
                ? pOne.attack(pTwo, user.getChosenMove())
                : "Foe " + pOne.attack(pTwo, pOne.chooseMove()));
        pOne.setHasUsedTurn(true);
        startSecondTrainersTurn(pOne, pTwo);
    }

    //see if the pokemon that was attacked fainted. Check if the pokemon that was attacked has used their turn already.
    private void startSecondTrainersTurn(Pokemon pOne, Pokemon pTwo) {
        if (pTwo.isFainted()) {
            fainted(pTwo);
        } else if (pTwo.hasUsedTurn()) {
            collapseDialogPanel();
        } else {
            driver.addActionListener(e -> attack(pTwo, pOne));
        }
    }

    //If either of the pokemon faint
    //Trainer equals whichever trainer's pokemon fainted
    private void fainted(Pokemon poke) {
        boolean up = poke == user.getCurrentPokemon();
        Trainer t = up ? user : ai;

        driver.addActionListener(e -> {
            setGameDialog((up ? "" : "Foe ") + poke.name + " fainted");
            driver.addActionListener(ee -> {
                //(user.getNpcOpponent() == null && !up) -> essentially means if true, pokemon that fainted is a wild pokemon
                if ((user.getNpcOpponent() == null && !up) || t.isOutOfUsablePokemon()) {// USER BATTLEING WILD POKEMON THE POKEMON THAT FAINTED IS NOT USERS,
                    battleEnd();
                } else if (!up) { // It might never have to get here
                    t.swap();
                    setGameDialog(ai.getName() + " sent out " + ai.getCurrentPokemon().name + "!");
                    } else {
                        poke.setHasUsedTurn(false);
                        gui.setUi("PokemonInPartyPanel");
                        gui.setBackgroundImage("PokemonInPartyPanel");
                    }
                collapseDialogPanel(); //could be moved to turn manager along with the other call in startSecondTrainersTurn
            });
        });
    }

    public void setGameDialog(String gameDialog){
        this.gameDialog.setText(gameDialog);
    }

    //Collapse Dialog Panel
    private void collapseDialogPanel(){
        driver.addActionListener(e -> {
            collapseDialogPanel.doClick();
            turnEnd();
        });
    }

    private void turnEnd() {

        if (ai != null) {
            ai.resetBoolsForNextTurn();
            ai.getCurrentPokemon().setHasUsedTurn(false);
        } else {
            user.getPokemonOpponent().setHasUsedTurn(false);
        }
        user.getCurrentPokemon().setHasUsedTurn(false);
        user.resetBoolsForNextTurn();
    }

    private void actionPerformed(ActionEvent e) {
        if (user.isInAdventureMode()) {
            gui.setUi("AdventureModeMain");
            gui.setBackgroundImage(null);
        } else {
            ai.fullyHealPokemon();
            gui.setBackgroundImage("MainMenuGUI");
            gui.setUi("MainMenuGUI");
        }
        user.setOpponent(null);
        collapseDialogPanel();
        // Reset battle dialog for next battle
        AITrainer.canMove = true;
        setGameDialog("What will " + user.getCurrentPokemon().name + " do?");
    }
}
