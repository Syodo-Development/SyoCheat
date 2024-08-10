package xyz.syodo.cheat.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import lombok.Getter;
import org.json.JSONObject;
import xyz.syodo.cheat.SyoCheat;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.container.combat.CombatContainer;
import xyz.syodo.cheat.utils.data.container.movement.MovementContainer;
import xyz.syodo.cloud.CloudAPI;
import xyz.syodo.communication.message.Message;

import java.util.HashMap;

@Getter
public class CheatPlayer {

	//Static variables
	private static final int KICK_POINTS = 100;
	private static final int KICK_COUNT_SPECIFIC = 3;
	private static final long REMOVAL_TIME = 300000; //5 minutes


	private final Player player;

	//Containers
	private final CombatContainer combatContainer = new CombatContainer(this);
	private final MovementContainer movementContainer = new MovementContainer(this);

	//Cheat History
	private final HashMap<Long, CheatResponse> cheatResponses = new HashMap<>();

	public CheatPlayer(Player player) {
		this.player = player;
	}

	public void addResponse(CheatResponse response) {
		if(!response.isCheating()) return;
		getPlayer().sendMessage(response.toString());
		cheatResponses.put(System.currentTimeMillis(), response);
		Long time = System.currentTimeMillis();
		int cheatpoints = 0;
		int countSpecific = 0;
		for(Long l : cheatResponses.keySet().stream().toList()) {
			if(time - l > REMOVAL_TIME) {
				cheatResponses.remove(l);
			} else{
				CheatResponse cheatResponse = cheatResponses.get(l);
				cheatpoints += cheatResponses.get(l).getCheck().getCheatpoints();
				if(cheatResponse.getCheck() == response.getCheck()) countSpecific++;
			}
		}

		if(cheatpoints >= KICK_POINTS) {
			this.getPlayer().sendMessage(SyoCheat.getPrefix() + "You got kicked because our system detected you cheating.");
			this.getPlayer().close();
			Server.getInstance().getOnlinePlayers().values().stream().filter(player1 -> player1.hasPermission("syocheat.broadcast")).forEach(player1 -> player1.sendMessage(SyoCheat.getPrefix() + getPlayer().getName() + " §4got kicked for cheating!"));
		}
	}

	public void sendProxyMessage(CheatResponse response) {
		Message.Builder message = new Message.Builder();
		message.setTo("proxy");
		message.appendMany("SyoCheatMessage",
				getPlayer().getName(),
				CloudAPI.get().getCurrentService().name(),
				response.getCheck().name().toUpperCase(),
				new JSONObject(response.getMetaData()).toString());
		message.build().send();
	}

}
