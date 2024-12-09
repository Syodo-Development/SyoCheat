package xyz.syodo.cheat.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.types.inventory.transaction.UseItemOnEntityData;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class DataPacketReceivedListener implements Listener {

    @EventHandler
    public void on(DataPacketReceiveEvent event) {
        DataPacket packet = event.getPacket();
        Player player = event.getPlayer();
        if(player == null) return;
        if(!player.locallyInitialized) return;
        CheatPlayer cheatPlayer = CheatPlayerManager.getPlayer(player);
        if(packet instanceof InventoryTransactionPacket inventoryTransactionPacket) {
            if(inventoryTransactionPacket.transactionType == InventoryTransactionPacket.TYPE_USE_ITEM_ON_ENTITY) {
                if(inventoryTransactionPacket.transactionData instanceof UseItemOnEntityData useItemOnEntityData) {
                    if(useItemOnEntityData.actionType == InventoryTransactionPacket.USE_ITEM_ON_ENTITY_ACTION_ATTACK) {
                        cheatPlayer.getCombatContainer().getCpsData().addClick();
                        cheatPlayer.addResponse(cheatPlayer.getCombatContainer().getAimBotData().addAngle(useItemOnEntityData));
                    }
                }
            }
        } else if(packet instanceof PlayerAuthInputPacket playerAuthInputPacket) {
            cheatPlayer.addResponse(cheatPlayer.getMovementContainer().getPlayerAuthInputData().addPacket(playerAuthInputPacket));
            cheatPlayer.addResponse(cheatPlayer.getMovementContainer().getFlyData().doCheck());
        }
    }

}
