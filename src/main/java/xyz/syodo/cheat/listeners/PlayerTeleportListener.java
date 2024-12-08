package xyz.syodo.cheat.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class PlayerTeleportListener implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        CheatPlayer cheatPlayer = CheatPlayerManager.getPlayer(player);
        cheatPlayer.getMovementContainer().getPlayerAuthInputData().teleported();
        cheatPlayer.getMovementContainer().getPlayerTimedLocationData().teleported();
        cheatPlayer.getMovementContainer().getFlyData().teleported();
    }

}
