package me.liuli.pra.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.entity.*;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import me.liuli.pra.core.Kit;
import me.liuli.pra.core.Room;
import me.liuli.pra.managers.LanguageManager;
import me.liuli.pra.managers.PlayerManager;
import me.liuli.pra.utils.OtherUtil;
import me.liuli.pra.utils.PlayerUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        PlayerManager.duelInvites.remove(event.getPlayer().getUniqueId());

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
            event.setKnockBack(room.kit.basekb);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityMotion(EntityMotionEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = ((Player) event.getEntity());
        Room room = PlayerManager.playingPlayers.get(player.getUniqueId());
        if (room != null) {
            Kit kit=room.kit;
            if(player.onGround) {
                event.getMotion().x *= kit.xzkb_g;
                event.getMotion().y *= kit.ykb_g;
                event.getMotion().z *= kit.xzkb_g;
            }else{
                event.getMotion().x *= kit.xzkb;
                event.getMotion().y *= kit.ykb;
                event.getMotion().z *= kit.xzkb;
            }
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
            if (!room.damageAble) {
                event.setCancelled();
                return;
            }
            if(!room.kit.damage){
                event.setDamage(-1);
            }
            if ((player.getHealth() - event.getFinalDamage()) <= 1) {
                event.setCancelled();

                ArrayList<String> message = new ArrayList<>();
                message.add("--------------------");
                if (player.equals(room.red)) {
                    room.blue.sendTitle(LanguageManager.vict_title);
                    message.add("WINNER: " + room.blue.getName() + " (" + ((int) room.blue.getHealth()) + " HP)");
                } else {
                    room.red.sendTitle(LanguageManager.vict_title);
                    message.add("WINNER: " + room.red.getName() + " (" + ((int) room.red.getHealth()) + " HP)");
                }
                message.add("LOSER: " + player.getName());
                player.sendTitle(LanguageManager.lose_title);
                message.add("--------------------");

                for (String msg : message) {
                    room.red.sendMessage(msg);
                    room.blue.sendMessage(msg);
                }

                room.damageAble = false;

                PlayerInventory inventory=player.getInventory();
                for(Map.Entry<Integer, Item> entry:inventory.getContents().entrySet()){
                    player.level.dropItem(player,entry.getValue(), new Vector3(OtherUtil.randDouble(0.5,-0.5),0.3,OtherUtil.randDouble(0.5,-0.5)),true,3000);
                }
                inventory.clearAll();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        room.close(true);
                    }
                }, 5000);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(EntityTeleportEvent event){
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = ((Player) event.getEntity());
        if(!event.getTo().getLevel().equals(event.getFrom().getLevel())){
            PlayerManager.removePlayer(player);
            PlayerManager.duelInvites.remove(player.getUniqueId());
        }
    }

//    打pot不回血了
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onRegain(EntityRegainHealthEvent event){
//        if (!(event.getEntity() instanceof Player)) {
//            return;
//        }
//        Player player = ((Player) event.getEntity());
//        Room room = PlayerManager.playingPlayers.get(player.getUniqueId());
//        if (room != null) {
//            event.setCancelled();
//        }
//    }
}
