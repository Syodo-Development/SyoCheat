package xyz.syodo.cheat;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import xyz.syodo.database.redis.SyoRedis;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class SyoCheat extends PluginBase {

	@Getter
	public static final String prefix = "§r§8» §b§lSyo§cCheat§r§8│§c ";

	private static Plugin plugin;
	@Getter
	private static SyoRedis redis;

	@Override
	public void onLoad() {
		redis = new SyoRedis("jointick");
	}

	@Override
	public void onEnable() {
		
		plugin = this;
		registerListeners();
		getServer().getLogger().info("§aSyoCheat §ev" + this.getDescription().getVersion() + " §aenabled!");
	
	}
	
	public static Plugin get() {
		return plugin;
	}
	
	public void registerListeners() {
		Server.getInstance().getPluginManager().registerEvents(new CheatPlayerManager(), plugin);
	}
	
	
}
