package xyz.syodo.cheat.listeners;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerKickEvent;
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
