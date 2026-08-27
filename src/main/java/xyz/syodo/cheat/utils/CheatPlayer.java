package xyz.syodo.cheat.utils;

import org.powernukkitx.Player;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import org.powernukkitx.Server;
import xyz.syodo.cheat.SyoCheat;
import xyz.syodo.cheat.utils.data.CheatResponse;
import xyz.syodo.cheat.utils.data.container.combat.CombatContainer;
import xyz.syodo.cheat.utils.data.container.movement.MovementContainer;

@Getter
@Setter
public class CheatPlayer {

	//Static variables
	@Setter private static int KICK_POINTS = 100;
	@Setter private static int KICK_COUNT_SPECIFIC = 4;
	@Setter private static long REMOVAL_TIME = 150000; //2.5 minutes

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
				this.getPlayer().sendMessage(SyoCheat.getPrefix() + "You got kicked because our system detected you cheating.");
				this.getPlayer().kick("", false);
				response.getMetaData().put("kick", cheatpoints >= KICK_POINTS ? "POINTS" : "COUNT_SPECIFIC");
				Server.getInstance().getOnlinePlayers().values().stream().filter(player1 -> player1.hasPermission("syocheat.broadcast")).forEach(player1 -> player1.sendMessage(SyoCheat.getPrefix() + getPlayer().getName() + " §4got kicked for cheating!"));
			} else {
				cheatResponses.clear();
				this.getPlayer().sendMessage(SyoCheat.getPrefix() + "Your CheatPoints were reset.");
			}
		}
	}
}
