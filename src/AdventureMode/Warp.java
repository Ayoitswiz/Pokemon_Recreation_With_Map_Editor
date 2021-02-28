package AdventureMode;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract class Warp extends Collision implements Collisions {
    private String areaToLoad;
    private String collisionsToLoad;
    private String grassAreasToLoad;
    private Map<Integer, Warp> entrances = new HashMap<>();
    private ArrayList<NPC> npcs = new ArrayList<>();
    private String[] pokemonInArea;

    Warp(){ color = new Color(0, 255, 0);
    }

    protected abstract void createEntrances();
    protected abstract void createNPCs();

    void startNpcMovementThreads() {
        for (NPC npc : getNpcs()) {
            if (!npc.isDefeated() && !npc.isMovementThreadOn()) {
                NPCMovementThread npcMovementThread = new NPCMovementThread(npc);
                npc.setMovementThreadOn(true);
            }
        }
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public boolean isWarp() {
        return true;
    }

    protected String getAreaToLoad() {
        return areaToLoad;
    }

    protected void setAreaToLoad(String areaToLoad) {
        this.areaToLoad = areaToLoad;
    }

    protected String getCollisionsToLoad() {
        return collisionsToLoad;
    }

    protected void setCollisionsToLoad(String collisionsToLoad) {
        this.collisionsToLoad = collisionsToLoad;
    }

    protected String getGrassAreasToLoad() {
        return grassAreasToLoad;
    }

    public void setGrassAreasToLoad(String grassAreasToLoad) {
        this.grassAreasToLoad = grassAreasToLoad;
    }

    protected Map<Integer, Warp> getEntrances() {
        return entrances;
    }

    protected ArrayList<NPC> getNpcs() {
        return npcs;
    }

    public String[] getPokemonInArea() {
        return pokemonInArea;
    }

    public void setPokemonInArea(String[] pokemonInArea) {
        this.pokemonInArea = pokemonInArea;
    }
}
