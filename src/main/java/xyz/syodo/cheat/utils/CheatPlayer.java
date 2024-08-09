package xyz.syodo.cheat.utils;

import cn.nukkit.Player;
import lombok.Getter;

@Getter
public class CheatPlayer {

	private Player player;
	
	public CheatPlayer(Player player) {
		this.player = player;
	}

}
