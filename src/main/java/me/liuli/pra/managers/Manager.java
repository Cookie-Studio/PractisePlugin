package me.liuli.pra.managers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.liuli.pra.PractisePlugin;
import me.liuli.pra.core.GameMap;
import me.liuli.pra.core.Kit;
import me.liuli.pra.utils.OtherUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    public static Map<String, GameMap> gameMaps=new HashMap<>();
    public static Map<String, Kit> kits=new HashMap<>();

    public static String[] scoreboard;
    public static String sbTitle;

    public static void load(PractisePlugin plugin){
        new File(plugin.getDataFolder().getPath()+"/maps/").mkdirs();
        if(!new File(PractisePlugin.plugin.getDataFolder().getPath()+"/config.yml").exists()){
            OtherUtil.writeFile(PractisePlugin.plugin.getDataFolder().getPath()+"/config.yml",
                    OtherUtil.getTextFromResource("config.yml"));
        }

        JSONObject configJSON=JSONObject.parseObject(OtherUtil.y2j(new File(PractisePlugin.plugin.getDataFolder().getPath()+"/config.yml")));

        for(Map.Entry<String, Object> entry:configJSON.getJSONObject("maps").entrySet()){
            String id=entry.getKey();
            plugin.getLogger().info("Loading map \""+id+"\"...");
            gameMaps.put(id, new GameMap(id, (JSONObject) entry.getValue()));
        }

        for(Map.Entry<String, Object> entry:configJSON.getJSONObject("kits").entrySet()){
            String id=entry.getKey();
            plugin.getLogger().info("Loading kit \""+id+"\"...");
            kits.put(id, new Kit(id, (JSONObject) entry.getValue()));
        }

        JSONArray scoreboardJArr=configJSON.getJSONArray("scoreboard");
        ArrayList<String> scoreboardArr=new ArrayList<>();
        for(Object obj:scoreboardJArr){
            if(obj instanceof String){
                scoreboardArr.add((String) obj);
            }
        }
        scoreboard=scoreboardArr.toArray(new String[0]);
        sbTitle=configJSON.getString("title");
    }
}
