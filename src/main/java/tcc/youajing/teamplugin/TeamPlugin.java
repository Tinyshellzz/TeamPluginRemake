//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import tcc.youajing.teamplugin.command.TeamCommand;
import tcc.youajing.teamplugin.config.PluginConfig;
import tcc.youajing.teamplugin.database.MCPlayerMapper;
import tcc.youajing.teamplugin.database.TeamMapper;
import tcc.youajing.teamplugin.listener.PlayerJoinListener;
import tcc.youajing.teamplugin.placeholder.TeampluginExpansion;
import tcc.youajing.teamplugin.services.TeamService;
import tcc.youajing.teamplugin.services.TeamVisitService;
import tcc.youajing.teamplugin.tasks.RunTask;

public final class TeamPlugin extends JavaPlugin {
    public TeamPlugin() {
    }

    public void onEnable() {
        ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
        String var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.GREEN) + "######################");
        var10000 = Bukkit.getConsoleSender();
        var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.GREEN) + "#                    #");
        var10000 = Bukkit.getConsoleSender();
        var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.GREEN) + "#来自尤精的插件已启动#");
        var10000 = Bukkit.getConsoleSender();
        var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.GREEN) + "#                    #");
        var10000 = Bukkit.getConsoleSender();
        var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.GREEN) + "######################");
        this.init();
        this.register();
        RunTask.run();
    }

    public void init() {
        ObjectPool.plugin = this;
        PluginConfig.reload();
        ObjectPool.mcPlayerMapper = new MCPlayerMapper();
        ObjectPool.teamMapper = new TeamMapper();
        ObjectPool.teamService = new TeamService();
        ObjectPool.teamVisitService = new TeamVisitService();
    }

    public void register() {
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getCommand("team").setExecutor(new TeamCommand(this));
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            (new TeampluginExpansion(this)).register();
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.DARK_AQUA);
            var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.GREEN) + "PlaceholderAPI 准备就绪");
        }

    }

    public void onDisable() {
        ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
        String var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.DARK_RED) + "######################");
        var10000 = Bukkit.getConsoleSender();
        var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.DARK_RED) + "#                    #");
        var10000 = Bukkit.getConsoleSender();
        var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.DARK_RED) + "#来自尤精的插件已关闭#");
        var10000 = Bukkit.getConsoleSender();
        var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.DARK_RED) + "#                    #");
        var10000 = Bukkit.getConsoleSender();
        var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.DARK_RED) + "######################");
    }
}
