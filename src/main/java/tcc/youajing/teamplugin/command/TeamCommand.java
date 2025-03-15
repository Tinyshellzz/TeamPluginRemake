package tcc.youajing.teamplugin.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tcc.youajing.teamplugin.entities.MCPlayer;
import tcc.youajing.teamplugin.entities.Team;
import tcc.youajing.teamplugin.services.TeamManager;
import tcc.youajing.teamplugin.services.TeamService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static tcc.youajing.teamplugin.ObjectPool.*;


public class TeamCommand implements TabExecutor {
    private Plugin plugin;

    public TeamCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            String subcommand = args[0].toLowerCase();


            switch (subcommand) {
                case "get":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.get" + sender.toString());
                    return teamService.get(sender, command, label, args);
                case "reload":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.reload" + sender.toString());
                    return teamService.reload(sender, command, label, args);
            }

            return true;
        }


        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "什么！不会用Team插件？？");
            player.sendMessage(ChatColor.GOLD + "快给我去看《TCC社区游玩手册》！！！");
            return true;
        }


        String subcommand = args[0].toLowerCase();


        switch (subcommand) {
            case "get":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.get" + sender.toString());
                return teamService.get(player, command, label, args);
            case "new":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.new" + sender.toString());
                return teamService.create(player, command, label, args);
            case "color":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.color" + sender.toString());
                return teamService.color(player, command, label, args);
            case "abbr":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.abbr" + sender.toString());
                return teamService.abbr(player, command, label, args);
            case "enabbr":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.enabbr" + sender.toString());
                return teamService.enabbr(player, command, label, args);
            case "del":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.del" + sender.toString());
                return teamService.del(player, command, label, args);
            case "sethome":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.sethome" + sender.toString());
                return teamService.sethome(player, command, label, args);
            case "enforcesethome":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.enforcesethome" + sender.toString());
                return teamService.enforcesethome(player, command, label, args);
            case "enforcesetcolor":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.enforcesetcolor" + sender.toString());
                return teamService.enforcesetcolor(player, command, label, args);
            case "home":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.home" + sender.toString());
                return teamService.home(player, command, label, args);
            case "tp":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.tp" + sender.toString());
                return teamService.tp(player, command, label, args);
            case "invite":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.invite" + sender.toString());
                return teamService.invite(player, command, label, args);
            case "kick":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.kick" + sender.toString());
                return teamService.kick(player, command, label, args);
            case "set副手":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.set副手" + sender.toString());
                return teamService.setVicePresident(player, command, label, args);
            case "unset副手":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.unset副手" + sender.toString());
                return teamService.unsetVicePresident(player, command, label, args);
            case "rename":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.rename" + sender.toString());
                return teamService.rename(player, command, label, args);
            case "list":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.list" + sender.toString());
                return teamService.list(player, command, label, args);
            case "members":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.members" + sender.toString());
                return teamService.members(player, command, label, args);
            case "online":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.online" + sender.toString());
                return teamService.online(player, command, label, args);
            case "accept":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.accept" + sender.toString());
                return teamService.accept(player, command, label, args);
            case "reject":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.reject" + sender.toString());
                return teamService.reject(player, command, label, args);
            case "enforcerename":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.enforcerename" + sender.toString());
                return teamService.enforcerename(player, command, label, args);
            case "quit":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.quit" + sender.toString());
                return teamService.quit(player, command, label, args);
            case "qq":
                Bukkit.getConsoleSender().sendMessage("TeamCommand.qq" + sender.toString());
                return teamService.qq(player, command, label, args);
            case "setvisit":
                return teamVisitService.setVisit(player, command, label, args);
            case "visit":
                return teamVisitService.visit(player, command, label, args);
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // 判断命令发送者是否是玩家
        if (!(sender instanceof Player)) {
            return null;
        }

        Player player = (Player) sender; // 强制转换为玩家对象

        // 判断命令参数的长度
        if (args.length == 1) {
            // 如果只有一个参数，返回所有子命令的列表
            return Arrays.asList("new", "set副手", "unset副手", "del", "sethome", "rename", "invite" , "accept" , "reject" , "kick", "color", "abbr","list", "members" ,"home","online", "quit", "qq", "setvisit", "visit");
        } else if (args.length == 2) {
            // 如果有两个参数，根据第一个参数返回不同的补全列表
            String subcommand = args[0].toLowerCase();

            Team team = null;
            List<String> teamNames = null;
            switch (subcommand) {

                case "new":
                    // 创建团队的子命令，不需要补全
                    return null;

                case "del":
                    // 删除团队的子命令，返回所有团队名，令其选择
                    team = TeamManager.getTeamByPlayer(player);
                    if (team == null || !team.isPresident(player)) {
                        return null;
                    }
                    List<String> teams = new ArrayList<>();
                    for (Team team1 : TeamManager.getTeams()) {
                        teams.add(team1.getName());
                    }
                    return teams;
                case "rename":
                    //无需补全
                    return null;
                case "sethome":
                    // 设定团队传送点的子命令，不需要补全
                    return null;
                case "home":
                    // 传送到团队传送点的子命令，不需要补全
                    return null;

                case "invite":
                    team = TeamManager.getTeamByPlayer(player);
                    // 邀请玩家加入团队的子命令，返回所有在线玩家的名字
                    if (team == null) {
                        return null;
                    }
                    List<String> players = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!TeamManager.isMember(team, p)) {
                            players.add(p.getName());
                        }
                    }
                    return players;

                case "kick":
                    // 从团队中剔除玩家的子命令，返回团队中所有队员的名字（除了自己）
                    team = TeamManager.getTeamByPlayer(player);
                    if (team == null) {
                        return null;
                    }
                    if (!team.isPresident(player) && !team.isVicePresident(player)) {
                        return null;
                    }
                    List<String> members = new ArrayList<>();
                    for (MCPlayer mcPlayer : TeamManager.getMembers(team)) {
                        String playerkicked = Bukkit.getOfflinePlayer(mcPlayer.uuid).getName();
                        if (playerkicked != null && !playerkicked.equals(player.getName())) {
                            members.add(playerkicked);
                        }
                    }
                    return members;

                case "set副手":
                    //返回团队中所有队员的名字（除了自己）
                    team = TeamManager.getTeamByPlayer(player);
                    if (team == null || !team.isPresident(player)) {
                        return null;
                    }
                    members = new ArrayList<>();
                    for (MCPlayer mcPlayer : TeamManager.getMembers(team)) {
                        String playerkicked = Bukkit.getOfflinePlayer(mcPlayer.uuid).getName();
                        if (playerkicked != null && !playerkicked.equals(player.getName())) {
                            members.add(playerkicked);
                        }
                    }
                    return members;

                case "unset副手" :
                    return null;

                case "color":
                    // 选择团队颜色的子命令，返回所有颜色代码的列表
                    team = TeamManager.getTeamByPlayer(player);
                    if (team == null) {
                        return null;
                    }
                    if (!team.isPresident(player) && !team.isVicePresident(player)) {
                        return null;
                    }
                    return new ArrayList<>(colors.keySet());

                case "abbr":
                    // 选择团队缩写的子命令，返回团队名称中的每一个字
                    team = TeamManager.getTeamByPlayer(player);
                    if (team == null) {
                        return null;
                    }
                    if (!team.isPresident(player) && !team.isVicePresident(player)) {
                        return null;
                    }
                    char[] chars = team.getName().toCharArray();
                    List<String> abbrs = new ArrayList<>();
                    for (char c : chars) {
                        abbrs.add(String.valueOf(c));
                    }
                    return abbrs;

                case "list":
                    // 查看团队列表的子命令，不需要补全
                    return null;

                case "quit":
                    //
                    return null;
                case "accept":
                    return null;
                case "reject":
                    return null;

                case "members":
                    // 查看团队成员的子命令，返回所有团队的名称
                    teamNames = new ArrayList<>();
                    for (Team t : TeamManager.getTeams()) {
                        teamNames.add(t.getName());
                    }
                    return teamNames;

                case "tp":
                    // 查看团队成员的子命令，返回所有团队的名称
                    if (!player.hasPermission("teamplugin.op")) {
                        return null;
                    }
                    teamNames = new ArrayList<>();
                    for (Team t : TeamManager.getTeams()) {
                        if(t.getHome() != null) {
                            teamNames.add(t.getName());
                        }
                    } return teamNames;

                case "enabbr":
                    // 查看团队成员的子命令，返回所有团队的名称
                    if (!player.hasPermission("teamplugin.op")) {
                        return null;
                    }
                    ArrayList teamNames1 = new ArrayList<>();
                    for (Team t : TeamManager.getTeams()) {
                        if(t.getHome() != null) {
                            teamNames1.add(t.getName());
                        }
                    } return teamNames1;
                case "setvisit":
                    return null;
                case "visit":
                    return teamMapper.get_visit_teamName_list();
                default:
                    return null;
            }
        } else {
            // 如果有三个或以上的参数，不需要补全
            return null;
        }
    }
}
