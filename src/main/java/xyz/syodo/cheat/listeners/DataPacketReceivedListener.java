package xyz.syodo.cheat.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.types.inventory.transaction.TransactionData;
import cn.nukkit.network.protocol.types.inventory.transaction.UseItemOnEntityData;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class DataPacketReceivedListener implements Listener {

    @EventHandler
    public void on(DataPacketReceiveEvent event) {
        DataPacket packet = event.getPacket();
        Player player = event.getPlayer();
        if(player == null) return;
        CheatPlayer cheatPlayer = CheatPlayerManager.getPlayer(player);
        if(cheatPlayer == null) return;
        if(packet instanceof PlayerActionPacket actionPacket) {
            if(actionPacket.action == 31) {
                cheatPlayer.getCombatContainer().getCpsData().addClick();
                player.sendMessage("1");
            }
        } else if(packet instanceof InventoryTransactionPacket inventoryTransactionPacket) {
            if(inventoryTransactionPacket.transactionType == 3) {
                if(inventoryTransactionPacket.transactionData instanceof UseItemOnEntityData useItemOnEntityData) {
                    if(useItemOnEntityData.actionType == 1) {
                        cheatPlayer.getCombatContainer().getCpsData().addClick();
                    }
                }
            }
        }
    }

}
