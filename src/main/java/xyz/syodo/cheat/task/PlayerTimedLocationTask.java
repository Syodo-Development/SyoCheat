package xyz.syodo.cheat.task;

import org.powernukkitx.scheduler.Task;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class PlayerTimedLocationTask extends Task {
    @Override
    public void onRun(int i) {
        CheatPlayerManager.getPlayers().values().forEach(player -> player.addResponse(player.getMovementContainer().getPlayerTimedLocationData().addLocation()));
    }
}
