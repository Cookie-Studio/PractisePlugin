package me.liuli.pra.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Position;
import me.liuli.pra.core.Room;
import me.liuli.pra.managers.PlayerManager;
import me.liuli.pra.utils.PlayerUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Room room = PlayerManager.playingPlayers.get(event.getPlayer().getUniqueId());
        if (room != null) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Position spawn = Server.getInstance().getDefaultLevel().getSafeSpawn();
                    if (event.getPlayer().equals(room.red)) {
                        room.blue.sendMessage(room.red.getName() + " Quit while playing!");
                        room.blue.teleport(spawn);
                        room.blue.getInventory().clearAll();
                        room.blueScoreboard.hideFor(room.blue);
                        room.blue.setHealth(20);
                        room.blue.setMaxHealth(20);
                        PlayerUtil.removeAllEffect(room.blue);
                    } else {
                        room.red.sendMessage(room.blue.getName() + " Quit while playing!");
                        room.red.teleport(spawn);
                        room.red.getInventory().clearAll();
                        room.redScoreboard.hideFor(room.red);
                        PlayerUtil.removeAllEffect(room.red);
                    }
                    room.close(false);
                }
            }, 1000);
        }
        if (PlayerManager.queuedPlayers.containsValue(event.getPlayer())) {
            for (Map.Entry<String, Player> entry : PlayerManager.queuedPlayers.entrySet()) {
                if (entry.getValue().equals(event.getPlayer())) {
                    PlayerManager.queuedPlayers.remove(entry.getKey());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Room room = PlayerManager.playingPlayers.get(event.getPlayer().getUniqueId());
        if (room != null) {
            event.setCancelled();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDMG(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = ((Player) event.getEntity());
        Room room = PlayerManager.playingPlayers.get(player.getUniqueId());
        if (room != null) {
            event.setAttackCooldown(room.kit.ac);
            event.setKnockBack(room.kit.kb);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = ((Player) event.getEntity());
        Room room = PlayerManager.playingPlayers.get(player.getUniqueId());
        if (room != null) {
            if ((player.getHealth() - event.getDamage()) <= 0) {
                event.setCancelled();

                ArrayList<String> message = new ArrayList<>();
                message.add("--------------------");
                if (player.equals(room.red)) {
                    message.add("WINNER: " + room.blue.getName() + " (" + ((int) player.getHealth()) + " HP)");
                } else {
                    message.add("WINNER: " + room.red.getName() + " (" + ((int) player.getHealth()) + " HP)");
                }
                message.add("LOSER: " + player.getName());
                message.add("--------------------");

                for (String msg : message) {
                    room.red.sendMessage(msg);
                    room.blue.sendMessage(msg);
                }

                room.close(true);
            }
        }
    }
}
