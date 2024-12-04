package xyz.syodo.cheat.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerToggleFlightEvent;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class PlayerToggleFlightListener implements Listener {

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        CheatPlayerManager.getPlayer(player).getMovementContainer().getFlyData().toggledFlight();
    }
}
