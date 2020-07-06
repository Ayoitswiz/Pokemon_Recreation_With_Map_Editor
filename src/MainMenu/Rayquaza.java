package MainMenu;


public class Rayquaza extends Pokemon {

    public Rayquaza(int lvl, Move[] moves) {

        this.name = "Rayquaza";
        this.lvl = lvl;
        this.hp = 210;
        this.speed = lvl * 4;
/*        this.attack = lvl * 4;
        this.defense = lvl * 2;*/
        this.fullHealth = hp;
        this.moves = moves;
        this.Exp = (int) Math.pow(lvl, 3);
        this.type.add(new DragonType());
        this.type.add(new FlyingType());

        // E = floor(min(255, floor(sqrt(max(0, stat experience  - 1)) + 1)) / 4)
        //this.attack = floor(floor((2 * 45 + 4 + 0) * 53 / 100 + 5) * 0.9)
    }
}
