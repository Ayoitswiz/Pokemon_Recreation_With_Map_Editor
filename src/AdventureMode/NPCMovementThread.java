package AdventureMode;

import MainMenu.AITrainer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static AdventureMode.RocketGruntMaleDirection.*;

class NPCMovementThread {
    private static final int TIMER_DELAY = 125;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private NPC npc;
    private final AtomicInteger count = new AtomicInteger(0);

    NPCMovementThread(NPC pNpc) {
        this.npc = pNpc;
        npc.setMovementThreadOn(true);
        npc.setMoving(true);
        beep();
    }

    private void changeDirection(int npcPos, int turnAroundPos, RocketGruntMaleDirection newDir) {
        if (npcPos >= turnAroundPos) {
            npc.setDirection(newDir);
        }
    }
    public void beep(){
        final Runnable NpcStartMoving = () -> {
        //If the NPC is defeated in battle stop their thread
        /*//STOP THERE IF THEY CAUGHT PLAYER IN BATTLE GLARE AND THEY ARE RIGHT NEXT TO HIM*/
            if (npc.isDefeated() || !npc.isMovementThreadOn()) {
                scheduler.shutdown();
                //Still gets set because if npc is defeated this should be set to false
                npc.setMovementThreadOn(false);
            } else {
                npc.tick();
                npc.updateHitbox();
                if (npc.isMoving())
                //If the NPC caught the the user in it's battle glare, set NPC to walk towards user until they
                    // are right next to each other
                if (npc.caughtPlayerInBattleGlare) {
                    if (npc.getDistanceFromUser() > 0 && npc.isMoving()) {
                        switch (npc.getDirection()) {
                            case LEFT, RIGHT -> npc.setDistanceFromUser((npc.getDistanceFromUser() - npc.getDeltaX()));
                            case FORWARD, AWAY -> npc.setDistanceFromUser((npc.getDistanceFromUser() - npc.getDeltaY()));
                        }
                    } else {
                        npc.setMoving(false);
                    }
                } else {
                    switch (npc.getStartingDirection()) {
                        case RIGHT:
                            changeDirection(npc.getStartingCellsX(), npc.getX(), RIGHT);
                            changeDirection(npc.getX(), npc.getStartingCellsX() + npc.getDistanceCanMove(), LEFT);
                            break;
                        case FORWARD:
                            changeDirection(npc.getStartingCellsY(), npc.getY(), FORWARD);
                            changeDirection(npc.getY(), npc.getStartingCellsY() + npc.getDistanceCanMove(), AWAY);
                            break;
                        case LEFT:
                            changeDirection(npc.getX(), npc.getStartingCellsX(), LEFT);
                            changeDirection(npc.getStartingCellsX() - npc.getDistanceCanMove(), npc.getX(), RIGHT);
                            break;
                        case AWAY:
                            changeDirection(npc.getY(), npc.getStartingCellsY(), AWAY);
                            changeDirection(npc.getStartingCellsY() - npc.getDistanceCanMove(), npc.getY(), FORWARD);
                    }
                }
            }
        };
        scheduler.scheduleAtFixedRate(NpcStartMoving, 10, TIMER_DELAY, TimeUnit.MILLISECONDS);
    }
}
