package xyz.syodo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.types.inventory.transaction.UseItemOnEntityData;
import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import xyz.syodo.SyoCheat;
import xyz.syodo.cloud.CloudAPI;
import xyz.syodo.communication.message.Message;

@Getter
public class CheatPlayer {

	private Player player;
	
	public CheatPlayer(Player player) {
		this.player = player;
	}

}
