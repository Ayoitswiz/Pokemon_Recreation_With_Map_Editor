package AdventureMode;

import gg.Battle.Trainers.AITrainer;
import gg.Pokemon.ePokemon;

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
    private ePokemon[] pokemonInArea;

    Warp(){ color = new Color(0, 255, 0);
    }

    protected abstract void createEntrances();
    protected abstract void createNPCs();

    void startNpcMovementThreads() {
        for (NPC npc : getNpcs()) {
            if (!npc.isOutOfUsablePokemon() && AITrainer.canMove) {
                npc.setNpcMovementThread();
            }
        }
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

    public ePokemon[] getPokemonInArea() {
        return pokemonInArea;
    }

    public void setPokemonInArea(ePokemon... pokemonInArea) {
        this.pokemonInArea = pokemonInArea;
    }
}
