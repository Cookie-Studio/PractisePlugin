package me.liuli.pra.managers;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import me.liuli.pra.PractisePlugin;
import me.liuli.pra.core.GameMap;
import me.liuli.pra.core.Kit;
import me.liuli.pra.core.Room;
import me.liuli.pra.utils.OtherUtil;

import java.util.UUID;

public class RoomManager {
    public static void createRoom(Player red, Player blue, Kit kit){
        try {
            String levelName=getRandomLevelName();
            GameMap gameMap=kit.getGameMap();
            OtherUtil.copyDir(PractisePlugin.plugin.getDataFolder().getPath()+"/maps/"+gameMap.folderName+"/","./worlds/"+levelName+"/");
            Server.getInstance().loadLevel(levelName);

            Level level=Server.getInstance().getLevelByName(levelName);
            level.stopTime();
            level.setTime(gameMap.time);
            level.setAutoSave(false);

            Room room=new Room(level,red,blue);
            PlayerManager.playingPlayers.put(red.getUniqueId(),room);
            PlayerManager.playingPlayers.put(blue.getUniqueId(),room);

            red.teleport(Position.fromObject(gameMap.redSpawn,level));
            blue.teleport(Position.fromObject(gameMap.blueSpawn,level));
            kit.addItem(red);
            kit.addItem(blue);

            red.sendTitle(LanguageManager.fight_title);
            blue.sendTitle(LanguageManager.fight_title);
            red.sendMessage(LanguageManager.fight_against.replaceAll("%name%",blue.getName()));
            blue.sendMessage(LanguageManager.fight_against.replaceAll("%name%",red.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getRandomLevelName(){
        return UUID.randomUUID().toString().substring(0,10).replaceAll("-","").toUpperCase();
    }
}
