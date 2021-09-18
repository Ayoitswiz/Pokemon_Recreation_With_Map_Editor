package gg.Pokemon.Moves;

import gg.Pokemon.AllTypes.eTypes;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Move implements Cloneable {

    private eMoves name;
    private int pp;
    private int maxpp;
    private int accuracy;
    private int power;
    private eTypes type;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
