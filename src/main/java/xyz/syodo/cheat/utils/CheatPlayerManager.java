package xyz.syodo.cheat.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class CheatPlayerManager implements Listener {

	public static Object2ObjectOpenHashMap<String, CheatPlayer> players = new Object2ObjectOpenHashMap<>();
	
	@EventHandler
	public void on(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		players.put(player.getName(), new CheatPlayer(player));
	}
	
	@EventHandler
	public void on(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		players.remove(player.getName());
	}
	
	public static CheatPlayer getPlayer(Player p) {
		return players.get(p.getName());
	}

	public CheatPlayerManager() {
		Server.getInstance().getOnlinePlayers().values().forEach(player -> players.put(player.getName(), new CheatPlayer(player)));

		//Server.getInstance().getScheduler().scheduleDelayedRepeatingTask(() -> players.values().forEach(CheatPlayer::purgePoints), 20, 20);
	}
	
}
