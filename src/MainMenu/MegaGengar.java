package MainMenu;

public class MegaGengar extends Pokemon {

    public MegaGengar(int lvl, Move[] moves) {

        this.name = "Mega Gengar";
        this.lvl = lvl;
        this.hp = 250;
        this.speed = lvl * 3;
/*        this.attack = lvl * 3;
        this.defense = lvl * 5;*/
        this.fullHealth = hp;
        this.moves = moves;
        this.Exp = (int) Math.pow(lvl, 3);
        this.type.add(new GhostType());
        this.type.add(new PoisonType());
    }
}
