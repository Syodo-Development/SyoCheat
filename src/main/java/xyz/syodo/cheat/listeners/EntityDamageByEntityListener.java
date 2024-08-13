package xyz.syodo.cheat.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.CheatPlayerManager;
import xyz.syodo.cheat.utils.data.CheatResponse;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player player) {
            CheatPlayer cheatPlayer = CheatPlayerManager.getPlayer(player);
            if(cheatPlayer.getCombatContainer().getHitCooldownData().doCheck().isCheating()) {
                event.setCancelled();
            }
            if(!event.isCancelled()) {
                CheatResponse response = cheatPlayer.getCombatContainer().getReachData().addReachElement(event);
                cheatPlayer.getMovementContainer().getVelocityData().damaged();
                if(response.isCheating()) {
                    event.setCancelled();
                    cheatPlayer.addResponse(response);
                }
            }
        }
    }

}
