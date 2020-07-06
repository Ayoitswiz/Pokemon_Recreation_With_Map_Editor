/*
package MainMenu;

import javax.swing.*;

public class BattleManagerUpdated {

    private Pokemon pokeOpponent;
    private HumanTrainer user;
    private AITrainer ai;
    private JButton driver = new JButton();
    String nxtCheck = "";
    BattleManagerUpdated() {


    }


    private void battle() {

        int checkNum = 0;
        boolean startNextTurnSequence = true;

        if (startNextTurnSequence && driver.getActionListeners().length == 0) {

            driver.addActionListener(e -> {
                switch (check) {

                    case "Check who user is battleing":
                        checkWhoUserIsBattleing();
                    case "Check if either trainer used item":
                        checkForUsedItems();
                        break;
                    case "Check if either trainer swapped pokemon":
                        checkIfEitherTainerSwapped();
                        break;

                }
            });
        }
    }

    private void checkWhoUserIsBattleing() {
        if (user.getOpponent().equals("npc")) {
            nxtCheck = ""
        }
    }
    private void checkForUsedItems() {
        String whoUsed;
        if (user.isUsingItem() && ai.isUsingItem()) {
            whoUsed = "both";
        } else if (user.isUsingItem()) {
            whoUsed = "user";
        } else if (ai.isUsingItem()) {
            whoUsed = "ai";
        }
    }
    private void checkIfEitherTainerSwapped() {
        String whoSwapped;
        if (user.isSwapping() && ai.isSwapping()) {
            whoSwapped = "both";
        } else if (user.isSwapping()) {
            whoSwapped = "user";
        } else if (ai.isSwapping()) {
            whoSwapped = "ai";
        }

    }
}
*/
