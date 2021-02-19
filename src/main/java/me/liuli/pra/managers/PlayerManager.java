package me.liuli.pra.managers;

import cn.nukkit.Player;
import me.liuli.pra.core.Kit;
import me.liuli.pra.core.Room;
import me.liuli.pra.core.cache.DuelInvite;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    public static Map<String, Player> queuedPlayers = new HashMap<>();
    public static Map<UUID, Room> playingPlayers = new HashMap<>();
    public static Map<UUID, DuelInvite> duelInvites = new HashMap<>();

    public static void addPlayer(Player player, Kit kit) {
        if (queuedPlayers.get(kit.id) == null) {
            queuedPlayers.put(kit.id, player);
        } else {
            //create arena
            duelInvites.remove(player.getUniqueId());
            RoomManager.createRoom(queuedPlayers.remove(kit.id), player, kit);
        }
    }

    public static boolean isPlayerIn(Player player) {
        if (queuedPlayers.containsValue(player)) {
            return true;
        }
        return playingPlayers.get(player.getUniqueId()) != null;
    }

    public static void removePlayer(Player player) {
        if (queuedPlayers.containsValue(player)) {
            for (Map.Entry<String, Player> entry : queuedPlayers.entrySet()) {
                if (entry.getValue().equals(player)) {
                    queuedPlayers.remove(entry.getKey());
                }
            }
        }
        if (playingPlayers.get(player.getUniqueId()) != null) {
            playingPlayers.get(player.getUniqueId()).close(true);
        }
    }

    public static void invitePlayer(Player player, Player target, Kit kit) {
        duelInvites.put(player.getUniqueId(), new DuelInvite(player, target, kit));
        player.sendMessage(LanguageManager.invited);
        target.sendMessage(LanguageManager.invite.replaceAll("%name%", player.getName()));
    }

    public static void acceptInvite(Player invite, Player player) {
        DuelInvite inviteData = duelInvites.get(invite.getUniqueId());
        if (inviteData != null) {
            removePlayer(invite);
            removePlayer(player);
            RoomManager.createRoom(invite, player, inviteData.kit);
            duelInvites.remove(player.getUniqueId());
            duelInvites.remove(invite.getUniqueId());
        }
    }
}
