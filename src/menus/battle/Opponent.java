package menus.battle;

public interface Opponent extends Battler {


String getDialogIfUserTrysToFleeBattle();
boolean isCaughtUser();
void  setCaughtUser(boolean b);
}
