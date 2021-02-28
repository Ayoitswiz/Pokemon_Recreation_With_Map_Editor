package MainMenu;

public class Move implements Cloneable {

    private String name;
    private int pp;
    private int maxpp;
    private double accuracy;
    private int power;
    private String moveType;

    Move(String name, int pp, double accuracy, int power, String moveType) {
        this.name = name;
        this.pp = pp;
        this.accuracy = accuracy;
        this.power = power;
        this.maxpp = pp;
        this.moveType = moveType;
    }


    public String getName() {
        return name;
    }

    public int getPp() {
        return pp;
    }

    public int getMaxpp() {
        return maxpp;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getPower() {
        return power;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public void setMaxpp(int maxpp) {
        this.maxpp = maxpp;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
