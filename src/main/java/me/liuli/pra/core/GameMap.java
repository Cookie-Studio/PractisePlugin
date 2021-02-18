package me.liuli.pra.core;

import cn.nukkit.math.Vector3;
import com.alibaba.fastjson.JSONObject;

public class GameMap {
    public String id;
    public String folderName;
    public String mapName;
    public int time;
    public Vector3 redSpawn;
    public Vector3 blueSpawn;

    public GameMap(String id, JSONObject json) {
        this.id = id;
        this.folderName = json.getString("folder");
        this.mapName = json.getString("name");
        this.time = json.getInteger("time");

        JSONObject location = json.getJSONObject("red");
        this.redSpawn = new Vector3(location.getFloat("x"), location.getFloat("y"), location.getFloat("z"));
        location = json.getJSONObject("blue");
        this.blueSpawn = new Vector3(location.getFloat("x"), location.getFloat("y"), location.getFloat("z"));
    }
}
