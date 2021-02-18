package me.liuli.pra.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import me.liuli.pra.core.Kit;
import me.liuli.pra.managers.LanguageManager;
import me.liuli.pra.managers.Manager;
import me.liuli.pra.managers.PlayerManager;

public class CommandListener extends Command {
    private String message = "PractisePlugin By Liuli!";
    private String help = "/prapl join <kitId>\n"
            + "/prapl leave\n"
            + "/prapl <name>"
            + "/prapl version";

    public CommandListener() {
        super("prapl", "PractisePlugin By Liuli!");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.isPlayer()) {
            sender.sendMessage(message);
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(help);
            return false;
        }
        Player player = Server.getInstance().getPlayer(sender.getName());
        switch (args[0]) {
            case "join": {
                if (args.length <= 1) {
                    sender.sendMessage(LanguageManager.not_input_kit);
                    return false;
                }
                Kit kit = Manager.kits.get(args[1]);
                if (kit == null) {
                    sender.sendMessage(LanguageManager.illegal_kit);
                    return false;
                }
                if (PlayerManager.isPlayerIn(player)) {
                    sender.sendMessage(LanguageManager.already_in);
                    return false;
                }
                PlayerManager.addPlayer(player, kit);
                sender.sendMessage(LanguageManager.queued);
                break;
            }
            case "leave": {
                if (!PlayerManager.isPlayerIn(player)) {
                    sender.sendMessage(LanguageManager.not_in);
                }
                PlayerManager.removePlayer(player);
                sender.sendMessage(LanguageManager.left_queue);
                break;
            }
            case "help": {
                sender.sendMessage(help);
                break;
            }
            case "version": {
                sender.sendMessage(message);
                break;
            }
            default: {
                //duel invite
                sender.sendMessage(LanguageManager.prefix + "Sorry to invite players not yet supported");
            }
        }
        return false;
    }
}

