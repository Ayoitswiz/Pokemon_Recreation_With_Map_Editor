import consoleupgrade.ConsoleUpgrade;
import menus.mainmenu.MainMenuGUI;
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
 * Click through play by play game dialog(much harder than UIs.I thought) :(
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
public class Main {

public static void main(String... args) {
	new ConsoleUpgrade();
	new MainMenuGUI();
}
}




/*
 * TODO: When Windows 10 zoom setting decreases/increases the default size of the application is scaled with it, so as of
 *  right now, UIs.I'm at 125% zoom and was at 100% zoom before and now the app is to big for screen at program start.
 *
 * */
