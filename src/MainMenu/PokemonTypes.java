package MainMenu;

import java.util.ArrayList;

class Type {
    protected String name;
    protected String[] Weaknesses;
    protected String[] Resistances;
    protected String[] ImmuneTo;


    public int Multiplier(ArrayList<Type> opponentType, String move, int power) {

        for (Type type : opponentType) {
            for (String s : type.Weaknesses) {
                if (s.equals(move)) {
                    power *= 2;
                }
            }
        }
            return power;
    }
}

class NormalType extends Type {

    NormalType(){
        this.name = "Normal";
        this.Weaknesses = new String[]{"Fighting"};
        Resistances = new String[]{};
    }
}

class FightingType extends Type {

    FightingType(){
        this.name = "Fighting";
        this.Weaknesses = new String[]{"Flying", "Psychic", "Fairy"};
        Resistances = new String[]{"Rock", "Bug", "Dark"};

    }
}

class FlyingType extends Type {

    FlyingType() {
        this.name = "Flying";
        this.Weaknesses = new String[]{"Rock", "Electric", "Ice"};
        Resistances = new String[]{"Fighting", "Ground", "Bug", "Grass"};
    }
}

class PoisonType extends Type {

    PoisonType(){
        this.name = "Poison";
        this.Weaknesses = new String[]{"Ground", "Psychic"};
    }
}

class GroundType extends Type {

    GroundType(){
        this.name = "Poison";
        this.Weaknesses = new String[]{"Ground", "Psychic"};
    }
}

class RockType extends Type {

    RockType(){
        this.name = "Poison";
        this.Weaknesses = new String[]{"Ground", "Psychic"};
    }
}

class BugType extends Type {

    BugType(){
        this.name = "Bug";
        this.Weaknesses = new String[]{"Flying", "Rock", "Fire"};
    }
}

class GhostType extends Type {

    GhostType(){
        this.name = "Ghost";
        this.Weaknesses = new String[]{"Ghost", "Dark"};
    }
}

class SteelType extends Type {

    SteelType(){
        this.name = "Ghost";
        this.Weaknesses = new String[]{"Ghost", "Dark"};
    }
}

class FireType extends Type {

    FireType(){
        this.name = "Fire";
        this.Weaknesses = new String[]{"Water", "Ground", "Rock"};
        Resistances = new String[]{""};
    }
}

class WaterType extends Type {

    WaterType(){
        this.name = "Water";
        this.Weaknesses = new String[]{"Grass", "Electric"};
    }
}

class GrassType extends Type {

    GrassType(){
        this.name = "Ghost";
        this.Weaknesses = new String[]{"Ghost", "Dark"};
    }
}

class ElectricType extends Type {

    ElectricType(){
        this.name = "Ghost";
        this.Weaknesses = new String[]{"Ghost", "Dark"};
    }
}

class PsychicType extends Type {

    PsychicType(){
        this.name = "Ghost";
        this.Weaknesses = new String[]{"Ghost", "Dark"};
    }
}

class IceType extends Type {

    IceType(){
        this.name = "Ice";
        this.Weaknesses = new String[]{"Fighting", "Rock", "Steel", "Fire"};
    }
}

class DragonType extends Type {

    DragonType(){
        this.name = "Dragon";
        this.Weaknesses = new String[]{"Ice", "Dragon", "Fairy"};
    }
}

class DarkType extends Type {

    DarkType(){
        this.name = "Ghost";
        this.Weaknesses = new String[]{"Ghost", "Dark"};
    }
}

class FairyType extends Type {

    FairyType(){
        this.name = "Ghost";
        this.Weaknesses = new String[]{"Ghost", "Dark"};
    }
}




