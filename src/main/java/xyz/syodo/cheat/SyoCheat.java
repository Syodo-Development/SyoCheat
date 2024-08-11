package xyz.syodo.cheat;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.scheduler.ServerScheduler;
import lombok.Getter;
import xyz.syodo.cheat.listeners.DataPacketReceivedListener;
import xyz.syodo.cheat.listeners.EntityDamageByEntityListener;
import xyz.syodo.cheat.listeners.PlayerMotionListener;
import xyz.syodo.cheat.listeners.PlayerTeleportListener;
import xyz.syodo.cheat.task.AutoClickerTask;
import xyz.syodo.cheat.task.PlayerTimedLocationTask;
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
		this.registerTasks();

		this.getServer().getLogger().info("§aSyoCheat §ev" + this.getDescription().getVersion() + " §aenabled!");
	}
	
	public void registerListeners() {

		PluginManager pluginManager = Server.getInstance().getPluginManager();

		pluginManager.registerEvents(new CheatPlayerManager(), plugin);
		pluginManager.registerEvents(new EntityDamageByEntityListener(), plugin);
		pluginManager.registerEvents(new DataPacketReceivedListener(), plugin);
		pluginManager.registerEvents(new PlayerTeleportListener(), plugin);
		pluginManager.registerEvents(new PlayerMotionListener(), plugin);

	}

	public void registerTasks() {

		ServerScheduler serverScheduler = Server.getInstance().getScheduler();

		serverScheduler.scheduleDelayedRepeatingTask(new AutoClickerTask(), 10, 10);
		serverScheduler.scheduleDelayedRepeatingTask(new PlayerTimedLocationTask(), 10, 10);

	}
	
}
