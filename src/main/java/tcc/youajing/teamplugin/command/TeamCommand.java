//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tcc.youajing.teamplugin.ObjectPool;
import tcc.youajing.teamplugin.entities.MCPlayer;
import tcc.youajing.teamplugin.entities.Team;
import tcc.youajing.teamplugin.services.TeamBanService;
import tcc.youajing.teamplugin.services.TeamInfoService;
import tcc.youajing.teamplugin.services.TeamManager;
import tcc.youajing.teamplugin.utils.MyUtil;

public class TeamCommand implements TabExecutor {
    private Plugin plugin;

    public TeamCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {  // 所有命令发送者都能用的命令
            Matcher _m = Pattern.compile("^.*CraftRemoteConsoleCommandSender.*$").matcher(sender.toString());
            switch (args[0]) {
                case "ban":
                    if (!(sender instanceof ConsoleCommandSender || _m.find() || sender.isOp() || sender.hasPermission("team.ban"))) {
                        sender.sendMessage(ChatColor.RED + "只有控制台, op以及拥有team.ban权限的玩家才能使用该命令");
                        return true;
                    }

                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "参数不足");
                        return true;
                    }

                    switch (args[1]) {
                        case "visit":
                            return TeamBanService.banVisit(sender, command, label, args);
                        case "home":
                            return TeamBanService.banHome(sender, command, label, args);
                    }
                case "unban":
                    if (!(sender instanceof ConsoleCommandSender || _m.find() || sender.isOp() || sender.hasPermission("team.ban"))) {
                        sender.sendMessage(ChatColor.RED + "只有控制台, op以及拥有team.ban权限的玩家才能使用该命令");
                        return true;
                    }

                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "参数不足");
                        return true;
                    }

                    switch (args[1]) {
                        case "visit":
                            return TeamBanService.unbanVisit(sender, command, label, args);
                        case "home":
                            return TeamBanService.unbanHome(sender, command, label, args);
                    }
            }
        }

        if (!(sender instanceof Player player)) {   // 非玩家使用的命令
            switch (args[0].toLowerCase()) {
                case "get":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.get" + sender.toString());
                    return ObjectPool.teamService.get(sender, command, label, args);
                case "reload":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.reload" + sender.toString());
                    return ObjectPool.teamService.reload(sender, command, label, args);
                default:
                    return true;
            }
        } else if (args.length == 0) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "什么！不会用Team插件？？");
            player.sendMessage(String.valueOf(ChatColor.GOLD) + "快给我去看《TCC社区游玩手册》！！！");
            return true;
        } else {    // 玩家使用的命令
            switch (args[0].toLowerCase()) {
                case "get":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.get" + sender.toString());
                    return ObjectPool.teamService.get(player, command, label, args);
                case "new":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.new" + sender.toString());
                    return ObjectPool.teamService.create(player, command, label, args);
                case "color":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.color" + sender.toString());
                    return ObjectPool.teamService.color(player, command, label, args);
                case "abbr":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.abbr" + sender.toString());
                    return ObjectPool.teamService.abbr(player, command, label, args);
                case "enabbr":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.enabbr" + sender.toString());
                    return ObjectPool.teamService.enabbr(player, command, label, args);
                case "del":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.del" + sender.toString());
                    return ObjectPool.teamService.del(player, command, label, args);
                case "sethome":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.sethome" + sender.toString());
                    return ObjectPool.teamService.sethome(player, command, label, args);
                case "enforcesethome":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.enforcesethome" + sender.toString());
                    return ObjectPool.teamService.enforcesethome(player, command, label, args);
                case "enforcesetcolor":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.enforcesetcolor" + sender.toString());
                    return ObjectPool.teamService.enforcesetcolor(player, command, label, args);
                case "home":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.home" + sender.toString());
                    return ObjectPool.teamService.home(player, command, label, args);
                case "tp":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.tp" + sender.toString());
                    return ObjectPool.teamService.tp(player, command, label, args);
                case "invite":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.invite" + sender.toString());
                    return ObjectPool.teamService.invite(player, command, label, args);
                case "kick":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.kick" + sender.toString());
                    return ObjectPool.teamService.kick(player, command, label, args);
                case "set副手":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.set副手" + sender.toString());
                    return ObjectPool.teamService.setVicePresident(player, command, label, args);
                case "unset副手":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.unset副手" + sender.toString());
                    return ObjectPool.teamService.unsetVicePresident(player, command, label, args);
                case "rename":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.rename" + sender.toString());
                    return ObjectPool.teamService.rename(player, command, label, args);
                case "list":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.list" + sender.toString());
                    return ObjectPool.teamService.list(player, command, label, args);
                case "members":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.members" + sender.toString());
                    return ObjectPool.teamService.members(player, command, label, args);
                case "online":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.online" + sender.toString());
                    return ObjectPool.teamService.online(player, command, label, args);
                case "accept":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.accept" + sender.toString());
                    return ObjectPool.teamService.accept(player, command, label, args);
                case "reject":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.reject" + sender.toString());
                    return ObjectPool.teamService.reject(player, command, label, args);
                case "enforcerename":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.enforcerename" + sender.toString());
                    return ObjectPool.teamService.enforcerename(player, command, label, args);
                case "quit":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.quit" + sender.toString());
                    return ObjectPool.teamService.quit(player, command, label, args);
                case "qq":
                    Bukkit.getConsoleSender().sendMessage("TeamCommand.qq" + sender.toString());
                    return ObjectPool.teamService.qq(player, command, label, args);
                case "setvisit":
                    return ObjectPool.teamVisitService.setVisit(player, command, label, args);
                case "visit":
                    return ObjectPool.teamVisitService.visit(player, command, label, args);
                case "info":
                    return TeamInfoService.info(player, command, label, args);
                case "ban":
                    return TeamBanService.banVisit(player, command, label, args);
                case "unban":
                    return TeamBanService.unbanVisit(player, command, label, args);
                default:
                    return false;
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return null;
        } else if (args.length == 1) {
            Matcher _m = Pattern.compile("^.*CraftRemoteConsoleCommandSender.*$").matcher(sender.toString());
            String input = args[0].toLowerCase();
            if (!(sender instanceof ConsoleCommandSender || _m.find() || sender.isOp() || sender.hasPermission("team.use"))) {
                return MyUtil.tabComplete(Arrays.asList("new", "set副手", "unset副手", "del", "sethome", "rename", "invite", "accept", "reject", "kick", "color", "abbr", "list", "members", "home", "online", "quit", "qq", "setvisit", "visit", "info"), input);
            }

            return MyUtil.tabComplete(Arrays.asList("reload", "new", "set副手", "unset副手", "del", "sethome", "rename", "invite", "accept", "reject", "kick", "color", "abbr", "list", "members", "home", "online", "quit", "qq", "setvisit", "visit", "info", "ban", "unban"), input);
        } else if (args.length == 2) {
            String subcommand = args[0].toLowerCase();
            ArrayList<String> res = new ArrayList<>();
            String input = args[1].toLowerCase();
            Team team = null;
            Team t;
            switch (subcommand) {
                case "new":
                    return null;
                case "del":
                    team = TeamManager.getTeamByPlayer(player);
                    if (team != null && team.isPresident(player)) {
                        for (Team t1 : TeamManager.getTeams()) res.add(t1.getName());
                        return MyUtil.tabComplete(res, input);
                    }
                    return null;
                case "info":
                    for (Team t1 : TeamManager.getTeams()) res.add(t1.getName());
                    return MyUtil.tabComplete(res, input);
                case "rename":
                    return null;
                case "sethome":
                    return null;
                case "home":
                    return null;
                case "invite":
                    team = TeamManager.getTeamByPlayer(player);
                    if (team == null) {
                        return null;
                    }

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!TeamManager.isMember(team, p)) {
                            res.add(p.getName());
                        }
                    }

                    return MyUtil.tabComplete(res, input);
                case "kick":
                    team = TeamManager.getTeamByPlayer(player);
                    if (team == null) {
                        return null;
                    } else {
                        if (!team.isPresident(player) && !team.isVicePresident(player)) {
                            return null;
                        }

                        for (MCPlayer p : TeamManager.getMembers(team)) {
                            String playerkicked = ObjectPool.mcPlayerMapper.get_user_by_uuid(p.uuid).getName();
                            if (playerkicked != null && !playerkicked.equals(player.getName())) {
                                res.add(playerkicked);
                            }
                        }

                        return MyUtil.tabComplete(res, input);
                    }
                case "set副手":
                    team = TeamManager.getTeamByPlayer(player);
                    if (team != null && team.isPresident(player)) {
                        for (MCPlayer p : TeamManager.getMembers(team)) {
                            String playerkicked = ObjectPool.mcPlayerMapper.get_user_by_uuid(p.uuid).getName();
                            if (playerkicked != null && !playerkicked.equals(player.getName())) {
                                res.add(playerkicked);
                            }
                        }

                        return MyUtil.tabComplete(res, input);
                    }
                    return null;
                case "unset副手":
                    return null;
                case "color":
                    team = TeamManager.getTeamByPlayer(player);
                    if (team == null) {
                        return null;
                    } else {
                        if (!team.isPresident(player) && !team.isVicePresident(player)) {
                            return null;
                        }

                        return MyUtil.tabComplete(new ArrayList(ObjectPool.colors.keySet()), input);
                    }
                case "abbr":
                    team = TeamManager.getTeamByPlayer(player);
                    if (team == null) {
                        return null;
                    } else {
                        if (!team.isPresident(player) && !team.isVicePresident(player)) {
                            return null;
                        }

                        char[] chars = team.getName().toCharArray();

                        for (int i = 0; i < chars.length; ++i) {
                            char c = chars[i];
                            res.add(String.valueOf(c));
                        }

                        return MyUtil.tabComplete(res, input);
                    }
                case "list":
                    return null;
                case "quit":
                    return null;
                case "accept":
                    return null;
                case "reject":
                    return null;
                case "members":
                    for (Team t1 : TeamManager.getTeams()) {
                        res.add(t1.getName());
                    }

                    return MyUtil.tabComplete(res, input);
                case "tp":
                    if (subcommand.equals("tp") && !player.hasPermission("teamplugin.op")) {
                        return null;
                    }

                    for (Team t1 : TeamManager.getTeams()) {
                        res.add(t1.getName());
                    }

                    return MyUtil.tabComplete(res, input);
                case "enabbr":
                    if (!player.hasPermission("teamplugin.op")) {
                        return null;
                    }

                    for (Team t1 : TeamManager.getTeams()) {
                        res.add(t1.getName());
                    }

                    return MyUtil.tabComplete(res, input);
                case "setvisit":
                    return null;
                case "visit":
                    return MyUtil.tabComplete(ObjectPool.teamMapper.get_visit_teamName_list(), input);
                case "ban":
                case "unban":
                    return MyUtil.tabComplete(Arrays.asList("visit", "home"), input);
                default:
                    return null;
            }
        } else if (args.length == 3) {
            String subcommand = args[0].toLowerCase();
            ArrayList<String> res = new ArrayList<>();

            switch (subcommand) {
                case "ban":
                case "unban":
                    String subcommand2 = args[1].toLowerCase();
                    String input = args[2].toLowerCase();

                    switch (subcommand2) {
                        case "visit": case "home":
                            for (Team t1 : TeamManager.getTeams()) {
                                res.add(t1.getName());
                            }

                            return MyUtil.tabComplete(res, input);
                    }
            }
        }

        return null;
    }
}
