package AdventureMode;

import MainMenu.*;

import java.io.IOException;

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

            getPokeSlots().add(new Rayquaza(9, moves.get("Fire Blast", "Aerial Ace", "Tackle", "Hyper Beam")));
            getPokeSlots().add(new Articuno(12, moves.get("Blizzard", "Aerial Ace", "Tackle", "Ice Beam")));
            setCurrentPokemon(getPokeSlots().get(0));
            setMaxMovingIndex(3);
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

        getPokeSlots().add(new Rayquaza(9, moves.get("Fire Blast", "Aerial Ace", "Tackle", "Hyper Beam")));
        getPokeSlots().add(new Articuno(12, moves.get("Blizzard", "Aerial Ace", "Tackle", "Ice Beam")));
        setCurrentPokemon(getPokeSlots().get(0));
        setMaxMovingIndex(3);
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
            getPokeSlots().add(new Rayquaza(9, moves.get("Fire Blast", "Aerial Ace", "Tackle", "Hyper Beam")));
            getPokeSlots().add(new Articuno(12, moves.get("Blizzard", "Aerial Ace", "Tackle", "Ice Beam")));;
            setCurrentPokemon(getPokeSlots().get(0));
            setMaxMovingIndex(3);
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

        getPokeSlots().add(new Rayquaza(9, moves.get("Fire Blast", "Aerial Ace", "Tackle", "Hyper Beam")));
        getPokeSlots().add(new Articuno(12, moves.get("Blizzard", "Aerial Ace", "Tackle", "Ice Beam")));
        setCurrentPokemon(getPokeSlots().get(0));
        setMaxMovingIndex(3);
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

        getPokeSlots().add(new Rayquaza(9, moves.get("Fire Blast", "Aerial Ace", "Tackle", "Hyper Beam")));
        getPokeSlots().add(new Articuno(12, moves.get("Blizzard", "Aerial Ace", "Tackle", "Ice Beam")));
        setCurrentPokemon(getPokeSlots().get(0));
        setMaxMovingIndex(3);
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
