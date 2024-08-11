package xyz.syodo.cheat.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityMotionEvent;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.CheatPlayerManager;
import xyz.syodo.cheat.utils.data.CheatResponse;

public class PlayerMotionListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityMotion(EntityMotionEvent event) {
        if(!event.isCancelled()) {
            if(event.getEntity() instanceof Player player) {
                CheatPlayer cheatPlayer = CheatPlayerManager.getPlayer(player);
                if(cheatPlayer.getMovementContainer().getVelocityData().setMotion(event)) {
                    Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                        cheatPlayer.addResponse(cheatPlayer.getMovementContainer().getVelocityData().doCheck());
                    }, 5);
                }
            }
        }
    }

}
