package tcc.youajing.teamplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import tcc.youajing.teamplugin.command.TeamCommand;
import tcc.youajing.teamplugin.config.PluginConfig;
import tcc.youajing.teamplugin.database.MCPlayerMapper;
import tcc.youajing.teamplugin.database.TeamMapper;
import tcc.youajing.teamplugin.listener.PlayerJoinListener;
import tcc.youajing.teamplugin.placeholder.TeampluginExpansion;
import tcc.youajing.teamplugin.services.TeamService;
import tcc.youajing.teamplugin.tasks.RunTask;


public final class TeamPlugin extends JavaPlugin  {
    @Override
    public void onEnable() {
        // team,启动！
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.GREEN + "######################");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.GREEN + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.GREEN + "#来自尤精的插件已启动#");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.GREEN + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.GREEN + "######################");

        // 初始化
        init();

        // 注册组件
        register();

        // 运行定时任务
        RunTask.run();
    }

    public void init(){
        ObjectPool.plugin = this;
        PluginConfig.reload();

        ObjectPool.mcPlayerMapper = new MCPlayerMapper();
        ObjectPool.teamMapper = new TeamMapper();
        ObjectPool.teamService = new TeamService();
    }

    public void register(){
        // 注册 PlayerLoginListener
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        // 注册 team 命令
        this.getCommand("team").setExecutor(new TeamCommand(this));

        // 注册 PlaceHolder
        if(this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            new TeampluginExpansion(this).register();
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.GREEN + "PlaceholderAPI 准备就绪");
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.DARK_RED + "######################");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.DARK_RED + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.DARK_RED + "#来自尤精的插件已关闭#");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.DARK_RED + "#                    #");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Team]" + ChatColor.DARK_RED + "######################");
    }
}