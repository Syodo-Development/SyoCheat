package xyz.syodo.cheat.listeners;

import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.player.PlayerKickEvent;
import xyz.syodo.cheat.SyoCheat;

public class PlayerKickListener implements Listener {

    @EventHandler
    public void on(PlayerKickEvent event) {
        if(!SyoCheat.isENABLED()) return;
        if(event.getReasonEnum() == PlayerKickEvent.Reason.FLYING_DISABLED) {
            event.setCancelled();
        }
    }

}
