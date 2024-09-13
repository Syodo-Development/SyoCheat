package xyz.syodo.cheat.task;

import cn.nukkit.scheduler.Task;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class AutoClickerTask extends Task {
    @Override
    public void onRun(int i) {
        CheatPlayerManager.getPlayers().values().forEach(player -> player.addResponse(player.getCombatContainer().getCpsData().saveCPS()));
    }
}
