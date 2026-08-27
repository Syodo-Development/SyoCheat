package xyz.syodo.cheat;

import org.powernukkitx.Server;
import org.powernukkitx.command.Command;
import org.powernukkitx.command.SimpleCommandMap;
import org.powernukkitx.plugin.Plugin;
import org.powernukkitx.plugin.PluginBase;
import org.powernukkitx.plugin.PluginManager;
import org.powernukkitx.scheduler.ServerScheduler;
import lombok.Getter;
import lombok.Setter;
import xyz.syodo.cheat.commands.SyoCheatCommand;
import xyz.syodo.cheat.listeners.*;
import xyz.syodo.cheat.task.AutoClickerTask;
import xyz.syodo.cheat.task.PlayerTimedLocationTask;
import xyz.syodo.cheat.utils.CheatPlayerManager;

public class SyoCheat extends PluginBase {

	@Getter public static final String prefix = "§r§8» §b§lSyo§cCheat§r§8│§c ";
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
