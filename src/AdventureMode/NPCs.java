package AdventureMode;

import MainMenu.*;

import java.io.IOException;
import java.util.ArrayList;

class RocketGruntMale extends NPC {
        private static final String SPRITE_SHEET_PATH = "src/AdventureMode/imagesUi/TeamRocketMaleGrunt.png";

        RocketGruntMale(RocketGruntMaleDirection direction, int startingCell, int cellCountToMoveThrough) {
            super(0,
                    0,
                    60,
                    64,
                    SPRITE_SHEET_PATH,
                    "Rocket Grunt",
                    1,
                    new String[]{"All the pokemon in Mt. Moon are ours!...", "Don't try to stop us..."},
                    startingCell,
                    cellCountToMoveThrough,
                    13,
                    direction);

            getPokeSlots().add(new Rayquaza(9, new Move[]{moves.get("Fire Blast"), moves.get("Aerial Ace"), moves.get("Tackle"), moves.get("Hyper Beam")}));
            getPokeSlots().add(new Articuno(12, new Move[]{moves.get("Blizzard"), moves.get("Aerial Ace"), moves.get("Tackle"), moves.get("Ice Beam")}));
            this.currentPokemon = getPokeSlots().get(0);
            maxMovingIndex = 3;
            directionRG = direction;

            try {
                createSprites(SPRITE_SHEET_PATH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //If you want the NPC to have movement. Npcs that have been defeated in battle no longer move
            if (!isDefeated()) {
                NPCMovementThread npcMovementThread = new NPCMovementThread(this);
            }
        }
    }


class RocketGruntMale3 extends NPC {
    private static final String SPRITE_SHEET_PATH = "src/AdventureMode/imagesUi/TeamRocketMaleGrunt.png";

    RocketGruntMale3(RocketGruntMaleDirection direction, int startingCell, int cellCountToMoveThrough) {
        super(0,
                0,
                60,
                64,
                SPRITE_SHEET_PATH,
                "Rocket Grunt",
                1,
                new String[]{"All the pokemon in Mt. Moon are ours!...", "Don't try to stop us..."},
                startingCell,
                cellCountToMoveThrough,
                14,
                direction);

        getPokeSlots().add(new Articuno(9, new Move[]{moves.get("Fire Blast"), moves.get("Aerial Ace"), moves.get("Tackle"), moves.get("Hyper Beam")}));
        getPokeSlots().add(new Articuno(12, new Move[]{moves.get("Blizzard"), moves.get("Aerial Ace"), moves.get("Tackle"), moves.get("Ice Beam")}));
        this.currentPokemon = getPokeSlots().get(0);
        maxMovingIndex = 3;
        directionRG = direction;

        try {
            createSprites(SPRITE_SHEET_PATH);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //If you want the NPC to have movement. Npcs that have been defeated in battle no longer move
        if (!isDefeated()) {
            NPCMovementThread npcMovementThread = new NPCMovementThread(this);
        }
    }
}

    //Needs interface
    class RocketGruntMale2 extends NPC {
        private static final String SPRITE_SHEET_PATH = "src/AdventureMode/imagesUi/TeamRocketMaleGrunt.png";

        RocketGruntMale2(RocketGruntMaleDirection direction, int startingCell, int cellCountToMoveThrough) {
            super(0,
                    0,
                    60,
                    64,
                    SPRITE_SHEET_PATH,
                    "Rocket Captain",
                    1,
                    new String[]{"Wait, who are you?! You're not supposed to be here!"},
                    startingCell,
                    cellCountToMoveThrough,
                    14,
                    direction);

            //Need attribute to seperate Npc's from non trainer Npc's.
            //
            getPokeSlots().add(new MegaGengar(8, new Move[]{moves.get("Shadow Ball"), moves.get("Diamond Storm"), moves.get("Ice Beam"), moves.get("Hyper Beam")}));
            getPokeSlots().add(new Articuno(12, new Move[]{moves.get("Blizzard"), moves.get("Aerial Ace"), moves.get("Tackle"), moves.get("Ice Beam")}));
            this.currentPokemon = getPokeSlots().get(0);
            maxMovingIndex = 3;
            directionRG = direction;


            try {
                createSprites(SPRITE_SHEET_PATH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (!isDefeated()) {
                NPCMovementThread npcMovementThread = new NPCMovementThread(this);
            }
        }
    }

//Needs interface
class RocketGruntMale4 extends NPC {
    private static final String SPRITE_SHEET_PATH = "src/AdventureMode/imagesUi/TeamRocketMaleGrunt.png";

    RocketGruntMale4(RocketGruntMaleDirection direction, int startingCell, int cellCountToMoveThrough) {
        super(0,
                0,
                60,
                64,
                SPRITE_SHEET_PATH,
                "Rocket Captain",
                1,
                new String[]{"Wait, who are you?! You're not supposed to be here!"},
                startingCell,
                cellCountToMoveThrough,
                13,
                direction);

        //Need attribute to seperate Npc's from non trainer Npc's.
        //

        getPokeSlots().add(new MegaGengar(8, new Move[]{moves.get("Shadow Ball"), moves.get("Diamond Storm"), moves.get("Ice Beam"), moves.get("Hyper Beam")}));
        getPokeSlots().add(new MegaGengar(12, new Move[]{moves.get("Blizzard"), moves.get("Aerial Ace"), moves.get("Tackle"), moves.get("Ice Beam")}));
        this.currentPokemon = getPokeSlots().get(0);
        maxMovingIndex = 3;
        directionRG = direction;


        try {
            createSprites(SPRITE_SHEET_PATH);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (!isDefeated()) {
            NPCMovementThread npcMovementThread = new NPCMovementThread(this);
        }
    }
}

class RocketGruntMale5 extends NPC {
    private static final String SPRITE_SHEET_PATH = "src/AdventureMode/imagesUi/TeamRocketMaleGrunt.png";

    RocketGruntMale5(RocketGruntMaleDirection direction, int startingCell, int cellCountToMoveThrough) {
        super(0,
                0,
                60,
                64,
                SPRITE_SHEET_PATH,
                "Rocket Captain",
                1,
                new String[]{"Wait, who are you?! You're not supposed to be here!"},
                startingCell,
                cellCountToMoveThrough,
                13,
                direction);

        //Need class to seperate Npc's from non trainer Npc's.
        //

        getPokeSlots().add(new MegaGengar(8, new Move[]{moves.get("Shadow Ball"), moves.get("Diamond Storm"), moves.get("Ice Beam"), moves.get("Hyper Beam")}));
        getPokeSlots().add(new MegaGengar(12, new Move[]{moves.get("Blizzard"), moves.get("Aerial Ace"), moves.get("Tackle"), moves.get("Ice Beam")}));
        this.currentPokemon = getPokeSlots().get(0);
        maxMovingIndex = 3;
        directionRG = direction;

        try {
            createSprites(SPRITE_SHEET_PATH);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (!isDefeated()) {
            NPCMovementThread npcMovementThread = new NPCMovementThread(this);
        }
    }
}
