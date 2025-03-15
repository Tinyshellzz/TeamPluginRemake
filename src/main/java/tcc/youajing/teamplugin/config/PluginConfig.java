package tcc.youajing.teamplugin.config;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import tcc.youajing.teamplugin.ObjectPool;

import static tcc.youajing.teamplugin.ObjectPool.plugin;


public class PluginConfig {
    public boolean debug = false;
    public String db_host;
    public int db_port;
    public String db_user;
    public String db_passwd;
    public String db_database;
    public int active_playtime = 25200;
    public int team_qq_requirement = 5;

    private static ConfigWrapper configWrapper = new ConfigWrapper(plugin, "config.yml");

    public static void reload() {
        configWrapper.reloadConfig(); // 重新加载配置文件

        YamlConfiguration yamlconfig = configWrapper.getConfig();
        ObjectPool.pluginConfig = new PluginConfig();
        PluginConfig config = ObjectPool.pluginConfig;
        Boolean debug = yamlconfig.getBoolean("debug");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.GREEN + "Debug: " + debug);
        config.debug = debug;
        config.db_host = yamlconfig.getString("db_host");
        config.db_port = yamlconfig.getInt("db_port");
        config.db_user = yamlconfig.getString("db_user");
        config.db_passwd = yamlconfig.getString("db_passwd");
        config.db_database = yamlconfig.getString("db_database");
        String active_playtime_str = yamlconfig.getString("active_playtime");
        if (active_playtime_str != null) {
            config.active_playtime = Integer.parseInt(active_playtime_str);
        }
        String team_qq_requirement_str = yamlconfig.getString("team_qq_requirement");
        if (team_qq_requirement_str != null) {
            config.team_qq_requirement = Integer.parseInt(team_qq_requirement_str);
        }
    }

    @Override
    public String toString() {
        return "PluginConfig{" +
                ", db_host='" + db_host + '\'' +
                ", db_port=" + db_port +
                ", db_user='" + db_user + '\'' +
                ", db_passwd='" + db_passwd + '\'' +
                ", db_database='" + db_database + '\'' +
                '}';
    }

}
