//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import tcc.youajing.teamplugin.ObjectPool;

public class PluginConfig {
    public boolean debug = false;
    public String db_host;
    public int db_port;
    public String db_user;
    public String db_passwd;
    public String db_database;
    public int team_create_level_requirement;
    public double team_create_play_time_requirement;
    public double visit_play_time_requirement;
    public int team_visit_level_requirement;
    public int team_visit_level_requirement_for_new_player;
    public int active_playtime = 25200;
    public int team_qq_requirement = 5;
    private static ConfigWrapper configWrapper;

    public PluginConfig() {
    }

    public static void reload() {
        configWrapper.reloadConfig();
        YamlConfiguration yamlconfig = configWrapper.getConfig();
        ObjectPool.pluginConfig = new PluginConfig();
        PluginConfig config = ObjectPool.pluginConfig;
        Boolean debug = yamlconfig.getBoolean("debug");
        ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
        String var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.GREEN) + "Debug: " + debug);
        config.debug = debug;
        config.db_host = yamlconfig.getString("db_host");
        config.db_port = yamlconfig.getInt("db_port");
        config.db_user = yamlconfig.getString("db_user");
        config.db_passwd = yamlconfig.getString("db_passwd");
        config.db_database = yamlconfig.getString("db_database");
        config.team_create_level_requirement = yamlconfig.getInt("team_create_level_requirement");
        config.team_create_play_time_requirement = yamlconfig.getDouble("team_create_play_time_requirement");
        config.visit_play_time_requirement = yamlconfig.getDouble("visit_play_time_requirement");
        config.team_visit_level_requirement = yamlconfig.getInt("team_visit_level_requirement");
        config.team_visit_level_requirement_for_new_player = yamlconfig.getInt("team_visit_level_requirement_for_new_player");
        String active_playtime_str = yamlconfig.getString("active_playtime");
        if (active_playtime_str != null) {
            config.active_playtime = Integer.parseInt(active_playtime_str);
        }

        String team_qq_requirement_str = yamlconfig.getString("team_qq_requirement");
        if (team_qq_requirement_str != null) {
            config.team_qq_requirement = Integer.parseInt(team_qq_requirement_str);
        }

    }

    public String toString() {
        return "PluginConfig{, db_host='" + this.db_host + "', db_port=" + this.db_port + ", db_user='" + this.db_user + "', db_passwd='" + this.db_passwd + "', db_database='" + this.db_database + "'}";
    }

    static {
        configWrapper = new ConfigWrapper(ObjectPool.plugin, "config.yml");
    }
}
