package me.liuli.pra.core;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.potion.Effect;
import de.theamychan.scoreboard.api.ScoreboardAPI;
import de.theamychan.scoreboard.network.DisplaySlot;
import de.theamychan.scoreboard.network.Scoreboard;
import de.theamychan.scoreboard.network.ScoreboardDisplay;
import me.liuli.pra.managers.Manager;
import me.liuli.pra.managers.PlayerManager;
import me.liuli.pra.managers.RoomManager;
import me.liuli.pra.utils.PlayerUtil;

import java.util.Map;

public class Room {
    public Level level;
    public Player red,blue;
    public Scoreboard redScoreboard,blueScoreboard;
    public Kit kit;
    public GameMap gameMap;

    public Room(Level level,Player red,Player blue,Kit kit,GameMap gameMap){
        this.level=level;
        this.red=red;
        this.blue=blue;
        this.kit=kit;
        this.gameMap=gameMap;
    }

    public void updateRedScoreboard(){
        if(redScoreboard!=null) redScoreboard.hideFor(red);
        redScoreboard = ScoreboardAPI.createScoreboard();
        ScoreboardDisplay rsbd = redScoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", Manager.sbTitle);
        int count=1;
        for(String str:Manager.scoreboard){
            String name=str.replaceAll("%against%",blue.getName())
                    .replaceAll("%map%",gameMap.mapName);
            rsbd.addLine(name,count);
            count++;
        }
        redScoreboard.showFor(red);
    }

    public void updateBlueScoreboard(){
        if(blueScoreboard!=null) blueScoreboard.hideFor(blue);
        blueScoreboard = ScoreboardAPI.createScoreboard();
        ScoreboardDisplay bsbd = blueScoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", Manager.sbTitle);
        int count=1;
        for(String str:Manager.scoreboard){
            String name=str.replaceAll("%against%",red.getName())
                    .replaceAll("%map%",gameMap.mapName);
            bsbd.addLine(name,count);
            count++;
        }
        blueScoreboard.showFor(blue);
    }

    public void close(boolean tp){
        Position spawn= Server.getInstance().getDefaultLevel().getSafeSpawn();
        if(tp){
            red.teleport(spawn);
            blue.teleport(spawn);
            red.getInventory().clearAll();
            blue.getInventory().clearAll();
            red.setHealth(20);
            red.setMaxHealth(20);
            blue.setHealth(20);
            blue.setMaxHealth(20);
            PlayerUtil.removeAllEffect(red);
            PlayerUtil.removeAllEffect(blue);
            redScoreboard.hideFor(red);
            blueScoreboard.hideFor(blue);
        }
        PlayerManager.playingPlayers.remove(blue.getUniqueId());
        PlayerManager.playingPlayers.remove(red.getUniqueId());
        RoomManager.removeRoom(this);
    }
}
