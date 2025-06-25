package xyz.syodo.cheat;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.scheduler.ServerScheduler;
import lombok.Getter;
import lombok.Setter;
import xyz.syodo.cheat.commands.SyoCheatCommand;
import xyz.syodo.cheat.listeners.*;
import xyz.syodo.cheat.task.AutoClickerTask;
import xyz.syodo.cheat.task.PlayerTimedLocationTask;
import xyz.syodo.database.redis.SyoRedis;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class SyoCheat extends PluginBase {

	@Getter public static final String prefix = "§r§8» §b§lSyo§cCheat§r§8│§c ";
	@Getter private static SyoRedis redis;
	private static Plugin plugin;

	@Getter
	@Setter
	private static boolean ENABLED = true;

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
		this.registerCommands();
		this.registerTasks();

		this.getServer().getLogger().info("§aSyoCheat §ev" + this.getDescription().getVersion() + " §aenabled!");
	}
	
	private void registerListeners() {

		PluginManager pluginManager = Server.getInstance().getPluginManager();

		pluginManager.registerEvents(new CheatPlayerManager(), plugin);
		pluginManager.registerEvents(new EntityDamageByEntityListener(), plugin);
		pluginManager.registerEvents(new DataPacketReceivedListener(), plugin);
		pluginManager.registerEvents(new PlayerTeleportListener(), plugin);
		pluginManager.registerEvents(new PlayerMotionListener(), plugin);
		pluginManager.registerEvents(new PlayerToggleFlightListener(), plugin);
		pluginManager.registerEvents(new PlayerKickListener(), plugin);
	}

	private void registerCommands() {

		SimpleCommandMap map = Server.getInstance().getCommandMap();

		map.register("syocheat", new SyoCheatCommand());
	}

	private void registerTasks() {

		ServerScheduler serverScheduler = Server.getInstance().getScheduler();

		serverScheduler.scheduleDelayedRepeatingTask(new AutoClickerTask(), 10, 10);
		serverScheduler.scheduleDelayedRepeatingTask(new PlayerTimedLocationTask(), 10, 10);

	}
	
}
