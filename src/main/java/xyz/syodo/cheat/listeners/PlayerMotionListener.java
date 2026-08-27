package xyz.syodo.cheat.listeners;

import org.powernukkitx.Player;
import org.powernukkitx.Server;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.EventPriority;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.entity.EntityMotionEvent;
import xyz.syodo.cheat.SyoCheat;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.CheatPlayerManager;
import xyz.syodo.cheat.utils.data.CheatResponse;

public class PlayerMotionListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityMotion(EntityMotionEvent event) {
        if(!SyoCheat.isENABLED()) return;
        if(!event.isCancelled()) {
            if(event.getEntity() instanceof Player player) {
                CheatPlayer cheatPlayer = CheatPlayerManager.getPlayer(player);
                if(System.currentTimeMillis() - cheatPlayer.getMovementContainer().getPlayerAuthInputData().getTeleported() < 20) return;
                cheatPlayer.getMovementContainer().getFlyData().setLastMotion(event);
            }
        }
    }

}
