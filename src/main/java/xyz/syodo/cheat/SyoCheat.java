package xyz.syodo.cheat;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import lombok.Getter;
import xyz.syodo.database.redis.SyoRedis;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class SyoCheat extends PluginBase {

	@Getter public static final String prefix = "§r§8» §b§lSyo§cCheat§r§8│§c ";
	@Getter private static SyoRedis redis;
	private static Plugin plugin;

	public static Plugin get() {
		return plugin;
	}

	@Override
	public void onLoad() {
		plugin = this;
		redis = new SyoRedis("jointick");
	}

	@Override
	public void onEnable() {
		this.registerListeners();

		this.getServer().getLogger().info("§aSyoCheat §ev" + this.getDescription().getVersion() + " §aenabled!");
	}
	
	public void registerListeners() {
		PluginManager pluginManager = Server.getInstance().getPluginManager();

		pluginManager.registerEvents(new CheatPlayerManager(), plugin);
	}
	
}
