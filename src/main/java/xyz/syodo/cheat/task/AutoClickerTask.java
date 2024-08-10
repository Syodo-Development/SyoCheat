package xyz.syodo.cheat.task;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.CheatPlayerManager;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.container.combat.elements.CPSData;

public class AutoClickerTask extends Task {
    @Override
    public void onRun(int i) {
        CheatPlayerManager.players.values().forEach(player -> player.addResponse(player.getCombatContainer().getCpsData().saveCPS()));
    }
}
