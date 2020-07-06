package MainMenu;

import java.util.HashMap;
import java.util.Map;

public class Move implements Cloneable{
    protected String name;
    protected int pp;
    protected int maxpp;
    protected double accuracy;
    protected int power;
    protected String moveType;


    public Move(){

    }
    Move(String name, int pp, double accuracy, int power, String moveType)
    {
        this.name = name;
        this.pp = pp;
        this.accuracy = accuracy;
        this.power = power;
        this.maxpp = pp;
        this.moveType = moveType;
    }

    public HashMap<String, Move> createMove()
    {
        Map<String, String> type = new MoveType().AllMoveTypes();

        //Fire
        Move FireBlast = new Move("Fire Blast", 5, 100, 120, type.get("Fire"));

        //Ice
        Move IceBeam = new Move("Ice Beam", 10, .95, 100, type.get("Ice"));
        Move Blizzard = new Move("Blizzard", 5, .70, 120, type.get("Ice"));

        //Flying
        Move AerialAce = new Move("Aerial Ace", 10, 100, 90, type.get("Flying"));

        //Normal
        Move HyperBeam = new Move("Hyper Beam", 5, 1, 150, type.get("Normal"));
        Move Tackle = new Move("Tackle", 40, 100, 40, type.get("Normal"));
        Move WeakMove = new Move("Weak Move", 40, 100, 10, type.get("Normal"));

        //Bug

        //Ghost
        Move ShadowBall = new Move("Shadow Ball", 10, 1, 80, type.get("Ghost"));

        //Rock
        Move DiamondStorm = new Move("Diamond Storm", 5, 95, 100, type.get("Rock"));


        HashMap<String, Move> hm = new HashMap<>();

        // Adding object in HashMap
        //could replace the keys with
        //move.name to prevent misspelling/bugs.
        hm.put("Ice Beam", IceBeam);
        hm.put("Blizzard", Blizzard);
        hm.put("Aerial Ace", AerialAce);
        hm.put("Hyper Beam", HyperBeam);
        hm.put("Tackle", Tackle);
        hm.put("Weak Move", WeakMove);
        hm.put("Fire Blast", FireBlast);
        hm.put("Shadow Ball", ShadowBall);
        hm.put("Diamond Storm", DiamondStorm);
        return hm;
        // Invoking HashMap object
        // It might or might not display elements
        // in the insertion order
    }
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
