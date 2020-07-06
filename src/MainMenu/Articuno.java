package MainMenu;

public class Articuno extends Pokemon {

    public Articuno(int lvl, Move[] moves){

        this.name = "Articuno";
        this.lvl = lvl;
        this.hp = 225;
        this.speed = lvl * 3;
/*        this.attack = lvl * 4;
        this.defense = lvl * 4;*/
        this.fullHealth = hp;
        this.moves = moves;
        this.Exp = (int) Math.pow(lvl, 3);
        this.type.add(new IceType());
        this.type.add(new FlyingType());
    }
}
