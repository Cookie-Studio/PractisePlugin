package me.liuli.pra.managers;

import com.alibaba.fastjson.JSONObject;
import me.liuli.pra.PractisePlugin;
import me.liuli.pra.utils.OtherUtil;

import java.io.File;

public class LanguageManager {
    //root
    public static String prefix;
    //command
    public static String not_input_kit,
            illegal_kit,
            player_offline,
            queued,
            already_in,
            not_in,
            left_queue;
    //play
    public static String fight_title,
            fight_against;

    public static void load() {
        if (!new File(PractisePlugin.plugin.getDataFolder().getPath() + "/lang.yml").exists()) {
            OtherUtil.writeFile(PractisePlugin.plugin.getDataFolder().getPath() + "/lang.yml",
                    OtherUtil.getTextFromResource("lang.yml"));
        }

        JSONObject langJSON = JSONObject.parseObject(OtherUtil.y2j(new File(PractisePlugin.plugin.getDataFolder().getPath() + "/lang.yml")));

        prefix = langJSON.getString("prefix");

        JSONObject command = langJSON.getJSONObject("command");
        not_input_kit = prefix + command.getString("not_input_kit");
        illegal_kit = prefix + command.getString("illegal_kit");
        player_offline = prefix + command.getString("player_offline");
        queued = prefix + command.getString("queued");
        already_in = prefix + command.getString("already_in");
        not_in = prefix + command.getString("not_in");
        left_queue = prefix + command.getString("left_queue");

        JSONObject play = langJSON.getJSONObject("play");
        fight_title = play.getString("fight_title");
        fight_against = LanguageManager.prefix + play.getString("fight_against");
    }
}
