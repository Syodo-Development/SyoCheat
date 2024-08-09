package xyz.syodo.cheat.utils;

import cn.nukkit.Player;
import lombok.Getter;
import xyz.syodo.cheat.utils.data.container.combat.CombatContainer;

@Getter
public class CheatPlayer {

	private final Player player;

	private final CombatContainer combatContainer = new CombatContainer();

	public CheatPlayer(Player player) {
		this.player = player;
	}

}
