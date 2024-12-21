package xyz.syodo.cheat.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
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
                CheatResponse reachResponse = cheatPlayer.getCombatContainer().getReachData().addReachElement(event);
                cheatPlayer.addResponse(cheatPlayer.getCombatContainer().getAimlockData().doCheck());
                cheatPlayer.getMovementContainer().getVelocityData().damaged();
                if(reachResponse.isCheating()) {
                    event.setCancelled();
                    cheatPlayer.addResponse(reachResponse);
                }
            }
        }
    }

}
