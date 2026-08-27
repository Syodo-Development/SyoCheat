package xyz.syodo.cheat.listeners;

import org.powernukkitx.Player;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.player.PlayerToggleFlightEvent;
import xyz.syodo.cheat.SyoCheat;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class PlayerToggleFlightListener implements Listener {

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        if(!SyoCheat.isENABLED()) return;
        Player player = event.getPlayer();
        CheatPlayerManager.getPlayer(player).getMovementContainer().getFlyData().toggledFlight();
    }
}
