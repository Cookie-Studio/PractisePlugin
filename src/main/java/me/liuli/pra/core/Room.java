package me.liuli.pra.core;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import de.theamychan.scoreboard.network.Scoreboard;

public class Room {
    public Level level;
    public Player red,blue;
    public Scoreboard redScoreboard,blueScoreboard;

    public Room(Level level,Player red,Player blue){
        this.level=level;
        this.red=red;
        this.blue=blue;
    }
}
