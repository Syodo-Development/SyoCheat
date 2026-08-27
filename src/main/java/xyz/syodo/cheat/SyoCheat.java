package xyz.syodo.cheat;

import org.powernukkitx.Server;
import org.powernukkitx.command.Command;
import org.powernukkitx.command.SimpleCommandMap;
import org.powernukkitx.plugin.Plugin;
import org.powernukkitx.plugin.PluginBase;
import org.powernukkitx.plugin.PluginManager;
import org.powernukkitx.scheduler.ServerScheduler;
import org.powernukkitx.utils.Config;
import lombok.Getter;
import lombok.Setter;
import xyz.syodo.cheat.commands.SyoCheatCommand;
import xyz.syodo.cheat.listeners.*;
import xyz.syodo.cheat.task.AutoClickerTask;
import xyz.syodo.cheat.task.PlayerTimedLocationTask;
import xyz.syodo.cheat.utils.CheatPlayerManager;
import xyz.syodo.cheat.utils.CheatPlayer;
import xyz.syodo.cheat.utils.data.CheatCheck;
import xyz.syodo.cheat.utils.data.container.combat.elements.*;
import xyz.syodo.cheat.utils.data.container.movement.elements.*;

public class SyoCheat extends PluginBase {

	@Getter private static String prefix = "§r§8» §b§lSyo§cCheat§r§8│§c ";
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
		this.saveDefaultConfig();
		this.loadConfiguration();
		this.registerListeners();
		this.registerCommands();
		this.registerTasks();

		this.getServer().getLogger().info("§aSyoCheat §ev" + this.getDescription().getVersion() + " §aenabled!");
	}

	private void loadConfiguration() {
		Config config = this.getConfig();
		ENABLED = config.getBoolean("general.enabled", true);
		prefix = config.getString("general.prefix", prefix).replace('&', '§');

		CheatPlayer.setKICK_POINTS(config.getInt("violations.maximum-points", 100));
		CheatPlayer.setKICK_COUNT_SPECIFIC(config.getInt("violations.maximum-same-check-count", 4));
		CheatPlayer.setREMOVAL_TIME(config.getLong("violations.history-retention-ms", 150000L));

		for (CheatCheck check : CheatCheck.values()) {
			String path = "checks." + check.name().toLowerCase().replace('_', '-') + ".";
			check.setCheatpoints(config.getInt(path + "points", check.getCheatpoints()));
			check.setBroadcastRequirement(config.getInt(path + "broadcast-requirement", check.getBroadcastRequirement()));
		}

		CPSData.setREMEMBER_CPS(config.getInt("checks.autoclicker.remember-samples", 10));
		CPSData.setMINIMUM_AVERAGE_CPS(config.getDouble("checks.autoclicker.minimum-average-cps", 6.0));
		CPSData.setMINIMUM_SAMPLES(config.getInt("checks.autoclicker.minimum-samples", 8));
		CPSData.setMINIMUM_LOWEST_CPS(config.getDouble("checks.autoclicker.minimum-lowest-cps", 4.0));
		CPSData.setCONSISTENCY_DIVISOR(config.getDouble("checks.autoclicker.consistency-divisor", 5.0));
		HitCooldownData.setMINIMUM_HIT_INTERVAL_MS(config.getLong("checks.hit-cooldown.minimum-hit-interval-ms", 333L));

		ReachData.setREMEMBER_HITS(config.getInt("checks.reach.remember-hits", 10));
		ReachData.setFLAG_REACH_AT(config.getDouble("checks.reach.maximum-single-distance", 4.6));
		ReachData.setFLAG_REACH_AT_AVERAGE(config.getDouble("checks.reach.maximum-average-distance", 4.5));
		AimlockData.setREMEMBER_HITS(config.getInt("checks.aimlock.remember-hits", 10));
		AimlockData.setMIN_HITS_TO_TRIGGER(config.getInt("checks.aimlock.minimum-hits", 5));
		AimlockData.setANGLE_TRIGGER(config.getDouble("checks.aimlock.maximum-average-angle", 1.0));

		FlyData.setREQUIRED_MISSMATCH(config.getInt("checks.fly.required-mismatches", 5));
		FlyData.setREQUIRED_MISSMATCH_0(config.getInt("checks.fly.required-zero-height-mismatches", 10));
		FlyData.setMINIMUM_AIRTIME(config.getInt("checks.fly.minimum-air-ticks", 10));
		FlyData.setINTERUPT_VANILLA_FLY(config.getBoolean("checks.fly.resend-vanilla-abilities", true));
		FlyData.setMOTION_GRACE_PERIOD_MS(config.getLong("checks.fly.motion-grace-period-ms", 3000L));

		PlayerAuthInputData.setREMEMBER_PACKETS(config.getInt("checks.speed-auth.remember-packets", 20));
		PlayerAuthInputData.setREMEMBER_AVERAGE(config.getInt("checks.speed-auth.remember-averages", 5));
		PlayerAuthInputData.setALLOWED_DISTANCE(config.getDouble("checks.speed-auth.maximum-single-distance", 0.7));
		PlayerAuthInputData.setALLOWED_AVERAGE(config.getDouble("checks.speed-auth.maximum-average-distance", 0.65));
		PlayerAuthInputData.setTELEPORT_IF_EXCEED(config.getBoolean("checks.speed-auth.teleport-on-exceed", true));

		PlayerTimedLocationData.setREMEMBER_LOCATIONS(config.getInt("checks.speed-timed.remember-locations", 20));
		PlayerTimedLocationData.setALLOWED_SINGLE_DISTANCE(config.getDouble("checks.speed-timed.maximum-single-distance", 4.4));
		PlayerTimedLocationData.setALLOWED_AVERAGE_DISTANCE(config.getDouble("checks.speed-timed.maximum-average-distance", 4.3));
		PlayerTimedLocationData.setTELEPORT_IF_EXCEED(config.getBoolean("checks.speed-timed.teleport-on-exceed", true));
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

		int delay = this.getConfig().getInt("tasks.initial-delay-ticks", 10);
		serverScheduler.scheduleDelayedRepeatingTask(new AutoClickerTask(), delay,
				this.getConfig().getInt("tasks.autoclicker-period-ticks", 10));
		serverScheduler.scheduleDelayedRepeatingTask(new PlayerTimedLocationTask(), delay,
				this.getConfig().getInt("tasks.location-period-ticks", 10));

	}
	
}
