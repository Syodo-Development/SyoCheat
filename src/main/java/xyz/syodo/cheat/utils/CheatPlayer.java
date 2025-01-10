package xyz.syodo.cheat.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import xyz.syodo.cheat.SyoCheat;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.container.combat.CombatContainer;
import xyz.syodo.cheat.utils.data.container.movement.MovementContainer;
import xyz.syodo.cloud.CloudAPI;
import xyz.syodo.communication.message.Message;
import xyz.syodo.database.sql.SyoSQL;

@Getter
@Setter
public class CheatPlayer {

	//Static variables
	private static int KICK_POINTS = 100;
	private static int KICK_COUNT_SPECIFIC = 4;
	private static long REMOVAL_TIME = 150000; //2.5 minutes

	private final Player player;

	//Containers
	private final CombatContainer combatContainer;
	private final MovementContainer movementContainer;


	//Cheat History
	private final Long2ObjectOpenHashMap<CheatResponse> cheatResponses = new Long2ObjectOpenHashMap<>();

	public CheatPlayer(Player player) {
		this.player = player;
		this.combatContainer = new CombatContainer(this);
		this.movementContainer = new MovementContainer(this);
	}

	public void addResponse(CheatResponse response) {
		if(!response.isCheating() || !SyoCheat.isENABLED() || !player.locallyInitialized) return;
		cheatResponses.put(System.currentTimeMillis(), response);
		Long time = System.currentTimeMillis();
		int cheatpoints = 0;
		int countSpecific = 0;
		for(Long l : cheatResponses.keySet().stream().toList()) {
			if(time - l > REMOVAL_TIME) {
				cheatResponses.remove(l);
			} else {
				CheatResponse cheatResponse = cheatResponses.get(l);
				cheatpoints += cheatResponses.get(l).getCheck().getCheatpoints();
				if(cheatResponse.getCheck() == response.getCheck()) countSpecific++;
			}
		}
		if(cheatpoints >= KICK_POINTS || countSpecific >= KICK_COUNT_SPECIFIC) {
			if(!this.getPlayer().hasPermission("syocheat.bypass")) {
				//AUTO KICK DISABLED FOR RELEASE
//				this.getPlayer().sendMessage(SyoCheat.getPrefix() + "You got kicked because our system detected you cheating.");
//				this.getPlayer().kick("", false);
//				response.getMetaData().put("kick", cheatpoints >= KICK_POINTS ? "POINTS" : "COUNT_SPECIFIC");
//				Server.getInstance().getOnlinePlayers().values().stream().filter(player1 -> player1.hasPermission("syocheat.broadcast")).forEach(player1 -> player1.sendMessage(SyoCheat.getPrefix() + getPlayer().getName() + " §4got kicked for cheating!"));
			} else {
				cheatResponses.clear();
				this.getPlayer().sendMessage(SyoCheat.getPrefix() + "Your CheatPoints were reset.");
			}
		}
		logCheatResponse(response);
		if(countSpecific >= response.getCheck().getBroadcastRequirement()) {
			sendProxyMessage(response);
		}
	}

	private void sendProxyMessage(CheatResponse response) {
		Message.Builder message = new Message.Builder();
		message.setTo("proxy");
		message.appendMany("SyoCheatMessage",
				getPlayer().getName(),
				CloudAPI.get().getCurrentService().name(),
				response.getCheck().name().toUpperCase(),
				new JSONObject(response.getMetaData()).toString());
		message.build().send();
		message.setTo("discord");
		message.build().send();
	}

	private void logCheatResponse(CheatResponse response) {
		Object2ObjectOpenHashMap<String, Object> values = new Object2ObjectOpenHashMap<>();
		values.put("userid", getPlayer().getUniqueId().toString());
		values.put("type", response.getCheck().name());
		values.put("server", CloudAPI.get().getCurrentService().name());
		values.put("metadata", new JSONObject(response.getMetaData()).toString());
		SyoSQL.insertRow("Moderation.cheatlogs", values);
	}
}
