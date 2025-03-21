package tcc.youajing.teamplugin.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tcc.youajing.teamplugin.TeamPlugin;
import tcc.youajing.teamplugin.entities.Team;
import tcc.youajing.teamplugin.services.TeamManager;
import tcc.youajing.teamplugin.utils.MyUtil;

import java.util.List;

import static tcc.youajing.teamplugin.ObjectPool.teamMapper;

// 创建一个继承自PlaceholderExpansion的类
public class TeampluginExpansion extends PlaceholderExpansion {

    // 获取插件的主类对象
    private final TeamPlugin plugin;

    // 构造方法，传入插件的主类对象
    public TeampluginExpansion(TeamPlugin plugin) {
        this.plugin = plugin;
    }

    // 重写getIdentifier方法，返回一个唯一的标识符
    @Override
    public @NotNull String getIdentifier() {
        return "teamPlugin";
    }

    // 重写getAuthor方法，返回插件的作者
    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    // 重写getVersion方法，返回插件的版本
    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    // 重写onPlaceholderRequest方法，处理占位符请求
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {

        // 判断玩家是否为空，如果为空，返回null
        if (player == null) {
            return null;
        }

        // 获取玩家所在的团队对象
        Team team = TeamManager.getTeamByPlayer2(player);

        // 判断团队是否为空，如果为空，返回null


        // 根据占位符名返回不同的值
        switch (identifier) {
            case "name":
                if (team == null) {
                    return "<#FFFFFF>";
                }
                // 返回团队的名称
                return team.getName();
            case "leader":
                // 返回团队的队长名字
                if (team == null) {
                    return "队伍不存在";
                }
                return Bukkit.getOfflinePlayer(team.getPresident()).getName();
            case "color":
                if (team == null) {
                    return "<#FFFFFF>";
                } else if (team.getColor() == null) {
                    return "<#FFFFFF>";
                }
                // 返回团队的颜色
                return "<" + team.getColor() + ">";
            case "color2":
                if (team == null) {
                    return "<#FFFFFF>";
                } else if (team.getColor() == null) {
                    return "<#FFFFFF>";
                }
                // 返回团队的颜色
                return MyUtil.msgColor(team.getColor());
            case "playtime":
                return String.valueOf(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000);
            case "elytra":
                return String.valueOf(player.getStatistic(Statistic.AVIATE_ONE_CM) / 100000);
            case "name4tab":
                if (team == null) {
                    return "<#FFFFFF>";
                }
                return "&3&l&o·<" + team.getColor() + ">" +  team.getName();
            case "name4chat":
                if (team == null) {
                    return "<#FFFFFF>";
                }
                return "『" + team.getName() + "』";
            case "abbr":
                if (team == null) {
                    return  "<#FFFFFF>";
                }else if (team.getAbbr() == null) {
                    return "<#FFFFFF>";
                }
                return "『"+ team.getAbbr() + "』";
            default:
                // 如果占位符名不匹配，返回null
                List<Team> teams = teamMapper.get_teams();
                for(Team t: teams) {
                    if(identifier.equalsIgnoreCase(t.getName() + "_leaders")) {
                        StringBuilder msg = new StringBuilder("队长: " + Bukkit.getOfflinePlayer(t.getPresident()).getName());
                        if(t.getVicePresident() != null) msg.append("副队: ").append(Bukkit.getOfflinePlayer(t.getVicePresident()).getName());
                        return msg.toString();
                    } else if(identifier.equalsIgnoreCase(t.getName() + "_leader")) {
                        return Bukkit.getOfflinePlayer(t.getPresident()).getName();
                    }
                }
                return null;
        }
    }
}

