package me.liuli.pra.managers;

import cn.nukkit.Player;
import me.liuli.pra.core.Kit;
import me.liuli.pra.core.Room;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    public static Map<String, Player> queuedPlayers=new HashMap<>();
    public static Map<UUID, Room> playingPlayers=new HashMap<>();

    public static void addPlayer(Player player, Kit kit){
        if(queuedPlayers.get(kit.id)==null) {
            queuedPlayers.put(kit.id, player);
        }else{
            //create arena
            RoomManager.createRoom(queuedPlayers.remove(kit.id),player,kit);
        }
    }

    public static boolean isPlayerIn(Player player){
        if(queuedPlayers.containsValue(player)){
            return true;
        }
        if(playingPlayers.get(player.getUniqueId())!=null){
            return true;
        }
        //waiting duel invite
        return false;
    }

    public static void removePlayer(Player player) {
        if(queuedPlayers.containsValue(player)){
            for(Map.Entry<String,Player> entry:queuedPlayers.entrySet()){
                if(entry.getValue().equals(player)){
                    queuedPlayers.remove(entry.getKey());
                }
            }
        }
        if(playingPlayers.get(player.getUniqueId())!=null){
            playingPlayers.get(player.getUniqueId()).close(true);
        }
    }
}
