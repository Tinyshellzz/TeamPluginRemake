//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import tcc.youajing.teamplugin.command.TeamCommand;
import tcc.youajing.teamplugin.config.PluginConfig;
import tcc.youajing.teamplugin.database.HomeBanListMapper;
import tcc.youajing.teamplugin.database.MCPlayerMapper;
import tcc.youajing.teamplugin.database.TeamMapper;
import tcc.youajing.teamplugin.database.VisitBanListMapper;
import tcc.youajing.teamplugin.listener.PlayerJoinListener;
import tcc.youajing.teamplugin.placeholder.TeampluginExpansion;
import tcc.youajing.teamplugin.services.TeamService;
import tcc.youajing.teamplugin.services.TeamVisitService;
import tcc.youajing.teamplugin.tasks.RunTask;

public final class TeamPlugin extends JavaPlugin {
    public TeamPlugin() {
    }

    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + String.valueOf(ChatColor.GREEN) + "######################");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + String.valueOf(ChatColor.GREEN) + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + String.valueOf(ChatColor.GREEN) + "#来自尤精的插件已启动#");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + String.valueOf(ChatColor.GREEN) + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + String.valueOf(ChatColor.GREEN) + "######################");
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
        ObjectPool.visitBanListMapper = new VisitBanListMapper();
        ObjectPool.homeBanListMapper = new HomeBanListMapper();
    }

    public void register() {
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getCommand("team").setExecutor(new TeamCommand(this));


        // 注册 PlaceHolder
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TeampluginExpansion(this).register();
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[TccTools]" + ChatColor.GREEN + "PlaceholderAPI 准备就绪");
        }
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + String.valueOf(ChatColor.DARK_RED) + "######################");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + String.valueOf(ChatColor.DARK_RED) + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + String.valueOf(ChatColor.DARK_RED) + "#来自尤精的插件已关闭#");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + String.valueOf(ChatColor.DARK_RED) + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + String.valueOf(ChatColor.DARK_RED) + "######################");
    }
}
