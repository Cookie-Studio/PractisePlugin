package me.liuli.pra.core;

import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.liuli.pra.managers.Manager;
import me.liuli.pra.utils.ItemUtil;

import java.util.ArrayList;

public class Kit {
    public String id;
    public Item helmet;
    public Item chestplate;
    public Item leggings;
    public Item boots;
    public Item[] items;
    public GameMap[] maps;
    public int hp, ac;
    public float basekb,xzkb_g,ykb_g,xzkb,ykb;
    public boolean damage;

    private int randomMapCount = 0;

    public Kit(String id, JSONObject json) {
        this.id = id;
        this.helmet = ItemUtil.itemPhaser(json.getString("helmet"));
        this.chestplate = ItemUtil.itemPhaser(json.getString("chestplate"));
        this.leggings = ItemUtil.itemPhaser(json.getString("leggings"));
        this.boots = ItemUtil.itemPhaser(json.getString("boots"));

        JSONArray itemArray = json.getJSONArray("items");
        ArrayList<Item> itemCache = new ArrayList<>();
        for (Object data : itemArray) {
            itemCache.add(ItemUtil.itemPhaser((String) data));
        }
        items = itemCache.toArray(new Item[0]);

        ArrayList<GameMap> gameMapCache = new ArrayList<>();
        for (Object data : json.getJSONArray("maps")) {
            gameMapCache.add(Manager.gameMaps.get((String) data));
        }
        maps = gameMapCache.toArray(new GameMap[0]);

        hp = json.getInteger("hp");
        ac = json.getInteger("ac");
        basekb = json.getFloat("basekb");
        xzkb_g = json.getFloat("xzkb-g");
        xzkb = json.getFloat("xzkb");
        ykb_g = json.getFloat("ykb-g");
        ykb = json.getFloat("ykb");
        damage = json.getBoolean("damage");
    }

    public GameMap getGameMap() {
        if (randomMapCount >= maps.length) {
            randomMapCount = 0;
        }
        GameMap gameMap = maps[randomMapCount];
        randomMapCount++;
        return gameMap;
    }

    public void addItem(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clearAll();

        inventory.setHelmet(this.helmet);
        inventory.setChestplate(this.chestplate);
        inventory.setLeggings(this.leggings);
        inventory.setBoots(this.boots);
        inventory.addItem(items);
    }
}
