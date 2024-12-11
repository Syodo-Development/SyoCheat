package xyz.syodo.cheat.commands;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import xyz.syodo.cheat.SyoCheat;

public class SyoCheatCommand extends Command {
    public SyoCheatCommand() {
        super("syocheat");
        setPermission("syocheat.moderator");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("toggle")) {
                SyoCheat.setENABLED(!SyoCheat.isENABLED());
                sender.sendMessage("§aSyoCheat got " + (SyoCheat.isENABLED() ? "§2enabled" : "§4disabled") + "!");
            }
        } else sender.sendMessage("§cUse /syocheat toggle");
        return true;
    }
}
