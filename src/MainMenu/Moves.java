package MainMenu;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Extends HashMap and solely supports <class> Move </class> as the only allowable values.
 * When instantiated will create all moves contained in <method> getAllMoves() </method> and add them to
 * this classes HashMap. When instantiated the contents of the class can be accessed via the overloaded <method> get() </method>
 * @param <K> The key should be the name of the Move which can be accessed via <code> Move.getName() </code>
 * @param <T> The type will be of <class> Move </class> which consists of {String: name, int: pp, double: accuracy,
 *           int: power, and String: <class> Type.get("move-type") </class>
 */

public final class Moves<K extends String, T extends Move> extends HashMap<String, Move> {

    public Moves(){
        getAllMoves();
    }

    private void getAllMoves() {
        Map<String, String> type = new MoveType().AllMoveTypes();

        // TODO: 8/4/2020 Boilerplate code needs removed. type.get( ); -> replace with switch case in <code> Move constructor.
        //  Current HashMap returns every move there is instead of only the needed moves. -> implement new
        //  system using switch case. Use move name as parameter.
        // pp, accuracy, and power accept Integer, Double, double, and int. Name and type accept String
        toHashMap(createMoves(new Object[][]{
                /*{NAME                     PP    ACCURACY  Power       Type        }*/
                {"Fire Blast",              5,      100,    120,   type.get("Fire")},
                {"Ice Beam",                10,     .95,    100,   type.get("Ice")},
                {"Blizzard",                5,      .70,    120,   type.get("Ice")},
                {"Aerial Ace",              10,     100,    90,    type.get("Flying")},
                {"Hyper Beam",              5,      100,    150,   type.get("Normal")},
                {"Tackle",                  40,     100,    40,    type.get("Normal")},
                {"Weak Move",               40,     100,    10,    type.get("Normal")},
                {"Shadow Ball",             10,     100,    80,    type.get("Ghost")},
                {"Diamond Storm",           5,      95,     100,   type.get("Rock")}
        }));
    }

    @SafeVarargs
    private void toHashMap(T... move) {
        for (T m: move) {
            this.put(m.getName(), m);
        }
    }

    @SafeVarargs
    private void toHashMap(K[] key, T... move) {
        for (T m: move) {
            this.put(key[this.size()] , m);
        }
    }

    /**
     * Takes multiple strings or just one string as a parameter and queries the contents of this class
     * and returns a <code> Move[] </code>
     * @param key key(s) to query the HashMap with. Acceptable arguments: <code> get(" ") || get(" ", " ",...) </code>
     * @return returns the value(s) of the HashMap as a Move[]
     */
    @SafeVarargs
    public final Move[] get(K... key) {
        Move[] moveArray = new Move[key.length];
        int i = 0;
        for (K k: key) {
            if (containsKey(k))
            moveArray[i++] = super.get(k);
        }
        return moveArray;
    }

    private T[] createMoves(@NotNull Object[][] _2DMoveArray) {
        int _2DLength = _2DMoveArray.length,
                moveLength = _2DMoveArray[0].length;
        Move[] moveArray = new Move[_2DLength];
        Object[] move = new Object[moveLength];
        for (int iMove = 0; iMove < _2DLength; iMove++) {
            System.arraycopy(_2DMoveArray[iMove], 0, move, 0, moveLength);
            moveArray[iMove] =  new Move(
                    move[0].toString(),
                    move[1] instanceof Double ? (int) (double) move[1] : (int) move[1],
                    move[2] instanceof Double ?       (double) move[2] : (int) move[2],
                    move[3] instanceof Double ? (int) (double) move[3] : (int) move[3],
                    move[4].toString());
        }
        return (T[]) moveArray;
    }

    public static void main(String... args) {
        for (Move m : new Moves<>().values()) {
            System.out.println(Arrays.toString(
                    new Object[]{
                            m.getPp() + "\t",
                            m.getAccuracy() + "\t",
                            m.getPower() + "\t",
                            m.getMoveType() + "\t\t",
                            m.getName()
                    }));
        }
    }
}
