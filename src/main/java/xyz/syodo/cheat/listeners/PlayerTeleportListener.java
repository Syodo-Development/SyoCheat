package xyz.syodo.cheat.listeners;

import org.powernukkitx.Player;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.entity.EntityDamageByEntityEvent;
import org.powernukkitx.event.player.PlayerTeleportEvent;
import xyz.syodo.cheat.SyoCheat;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class PlayerTeleportListener implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if(!SyoCheat.isENABLED()) return;
        Player player = event.getPlayer();
        CheatPlayer cheatPlayer = CheatPlayerManager.getPlayer(player);
        cheatPlayer.getMovementContainer().getPlayerAuthInputData().teleported();
        cheatPlayer.getMovementContainer().getPlayerTimedLocationData().teleported();
        cheatPlayer.getMovementContainer().getFlyData().teleported();
    }

}
