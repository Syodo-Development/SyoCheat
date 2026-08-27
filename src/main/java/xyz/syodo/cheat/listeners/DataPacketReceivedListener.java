package xyz.syodo.cheat.listeners;

import org.powernukkitx.Player;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.server.PacketReceiveEvent;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUseOnActorActionType;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.data.ItemUseOnActorInventoryTransaction;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import lombok.SneakyThrows;
import xyz.syodo.cheat.SyoCheat;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class DataPacketReceivedListener implements Listener {

    @SneakyThrows
    @EventHandler
    public void on(PacketReceiveEvent event) {
        if(!SyoCheat.isENABLED()) return;
        BedrockPacket packet = event.getPacket();
        Player player = event.getPlayer();
        if(player == null) return;
        if(!player.locallyInitialized) return;
        CheatPlayer cheatPlayer = CheatPlayerManager.getPlayer(player);
        if(packet instanceof InventoryTransactionPacket inventoryTransactionPacket) {
            if(inventoryTransactionPacket.getTransaction() instanceof ItemUseOnActorInventoryTransaction useItemOnEntityData) {
                    if(useItemOnEntityData.getActionType() == ItemUseOnActorActionType.ATTACK) {
                        cheatPlayer.getCombatContainer().getCpsData().addClick();
                        cheatPlayer.getCombatContainer().getAimlockData().addAngle(useItemOnEntityData);
                    }
            }
        } else if(packet instanceof PlayerAuthInputPacket playerAuthInputPacket) {
            cheatPlayer.addResponse(cheatPlayer.getMovementContainer().getPlayerAuthInputData().addPacket(playerAuthInputPacket));
            cheatPlayer.addResponse(cheatPlayer.getMovementContainer().getFlyData().doCheck());
        }
    }

}
