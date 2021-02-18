package me.liuli.pra;

import cn.nukkit.plugin.PluginBase;
import me.liuli.pra.listeners.CommandListener;
import me.liuli.pra.listeners.PlayerListener;
import me.liuli.pra.managers.LanguageManager;
import me.liuli.pra.managers.Manager;
import me.liuli.pra.utils.OtherUtil;

import java.io.File;
import java.io.IOException;

public class PractisePlugin extends PluginBase {
    public static PractisePlugin plugin;

    @Override
    public void onLoad() {
        getServer().getProperties().set("spawn-protection", 0);
    }

    @Override
    public void onEnable() {
        plugin = this;

        if (!this.getServer().getPluginManager().getPlugins().containsKey("FastJSONLib")) {
            //download plugin
            try {
                String pluginPath = this.getServer().getPluginPath();
                OtherUtil.downloadFile("https://github.com/liulihaocai/FJL/releases/download/1.0/FastJSONLib-1.0.jar",
                        pluginPath, "FastJSONLib-1.0.jar");
                //then load it
                this.getServer().getPluginManager()
                        .loadPlugin(new File(pluginPath, "FastJSONLib-1.0.jar").getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Manager.load(this);
        LanguageManager.load();
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getCommandMap().register("prapl", new CommandListener());
    }
}
