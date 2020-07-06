package MainMenu;

import java.util.HashMap;
import java.util.Map;

class MoveType{
    protected String name;
    protected Map<String, String> map;
    MoveType(){
        AllMoveTypes();
    }
    protected Map<String, String> AllMoveTypes(){
        map = new HashMap<>();
        map.put("Fire", "Fire");
        map.put("Water", "Water");
        map.put("Ice", "Ice");
        map.put("Flying", "Flying");
        map.put("Dragon","Dragon");
        map.put("Normal", "Normal");
        map.put("Bug", "Bug");
        map.put("Ghost", "Ghost");
        map.put("Rock", "Rock");
        return map;
    }
}
