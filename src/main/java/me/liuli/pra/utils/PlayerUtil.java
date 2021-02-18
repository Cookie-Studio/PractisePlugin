package me.liuli.pra.utils;

import cn.nukkit.Player;
import cn.nukkit.potion.Effect;

import java.util.Map;

public class PlayerUtil {
    public static void removeAllEffect(Player player){
        for(Map.Entry<Integer, Effect> entry:player.getEffects().entrySet()){
            player.removeEffect(entry.getValue().getId());
        }
    }
}
