package xyz.syodo.utils;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

public class CheatPlayerManager implements Listener {

	public static HashMap<String, CheatPlayer> players = new HashMap<>();
	
	@EventHandler
	public void on(PlayerJoinEvent e) {

		Player p = e.getPlayer();
		players.put(p.getName(), new CheatPlayer(p));

	}
	
	@EventHandler
	public void on(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(players.containsKey(p.getName())) {
			players.remove(p.getName());
		}
	}
	
	public static CheatPlayer getPlayer(Player p) {
		return players.get(p.getName());
	}
	
	
	@SuppressWarnings("deprecation")
	public CheatPlayerManager() {

		for(Player p : Server.getInstance().getOnlinePlayers().values()) {
			CheatPlayerManager.players.put(p.getName(), new CheatPlayer(p));
		}

		Server.getInstance().getScheduler().scheduleDelayedRepeatingTask(new Runnable() {

			@Override
			public void run() {
				for(CheatPlayer p : players.values()) {
					p.purgePoints();
				}
			}
		}, 20, 20);
	}
	
}
