package AdventureMode;

import gg.Pokemon.Moves.Moves;
import gg.Pokemon.CreatePokemon;

import java.io.IOException;

import static gg.Pokemon.Moves.eMoves.BLIZZARD;
import static gg.Pokemon.Moves.eMoves.DIAMOND_STORM;
import static gg.Pokemon.Moves.eMoves.FIRE_BLAST;
import static gg.Pokemon.Moves.eMoves.ICE_BEAM;
import static gg.Pokemon.Moves.eMoves.STRONG_MOVE;
import static gg.Pokemon.Moves.eMoves.WEAK_MOVE;

class RocketGruntMale extends NPC {
        private static final String SPRITE_SHEET_PATH = "src/AdventureMode/img/TeamRocketMaleGrunt.png";

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

            getPokeSlots().add(CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), this));
            getPokeSlots().add(CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), this));
            setMaxMovingIndex(3);
            directionRG = direction;

            try {
                createSprites(SPRITE_SHEET_PATH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //If you want the NPC to have movement.
            // Npcs that have been defeated in battle no longer move
            setNpcMovementThread();
        }
    }


class RocketGruntMale3 extends NPC {
    private static final String SPRITE_SHEET_PATH = "src/AdventureMode/img/TeamRocketMaleGrunt.png";

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

        getPokeSlots().add(CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), this));
        getPokeSlots().add(CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), this));
        setMaxMovingIndex(3);
        directionRG = direction;

        try {
            createSprites(SPRITE_SHEET_PATH);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //If you want the NPC to have movement.
        // Npcs that have been defeated in battle no longer move
        setNpcMovementThread();
    }
}

    //Needs interface
    class RocketGruntMale2 extends NPC {
        private static final String SPRITE_SHEET_PATH = "src/AdventureMode/img/TeamRocketMaleGrunt.png";

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
            getPokeSlots().add(CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), this));
            getPokeSlots().add(CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), this));
            setMaxMovingIndex(3);
            directionRG = direction;


            try {
                createSprites(SPRITE_SHEET_PATH);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            setNpcMovementThread();
        }
    }

//Needs interface
class RocketGruntMale4 extends NPC {
    private static final String SPRITE_SHEET_PATH = "src/AdventureMode/img/TeamRocketMaleGrunt.png";

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

        getPokeSlots().add(CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), this));
        getPokeSlots().add(CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), this));
        setMaxMovingIndex(3);
        directionRG = direction;


        try {
            createSprites(SPRITE_SHEET_PATH);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        setNpcMovementThread();
    }
}

class RocketGruntMale5 extends NPC {
    private static final String SPRITE_SHEET_PATH = "src/AdventureMode/img/TeamRocketMaleGrunt.png";

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

        getPokeSlots().add(CreatePokemon.Articuno(11, Moves.Move(ICE_BEAM, WEAK_MOVE, STRONG_MOVE, BLIZZARD), this));
        getPokeSlots().add(CreatePokemon.Rayquaza(10, Moves.Move(DIAMOND_STORM, STRONG_MOVE, FIRE_BLAST, ICE_BEAM), this));
        setMaxMovingIndex(3);
        directionRG = direction;

        try {
            createSprites(SPRITE_SHEET_PATH);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        setNpcMovementThread();
    }
}
