//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tcc.youajing.teamplugin.ObjectPool;
import tcc.youajing.teamplugin.entities.MCPlayer;
import tcc.youajing.teamplugin.entities.Team;
import tcc.youajing.teamplugin.services.TeamInfoService;
import tcc.youajing.teamplugin.services.TeamManager;

public class TeamCommand implements TabExecutor {
    private Plugin plugin;

    public TeamCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
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
        } else {
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
                default:
                    return false;
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return null;
        } else if (args.length == 1) {
            return Arrays.asList("new", "set副手", "unset副手", "del", "sethome", "rename", "invite", "accept", "reject", "kick", "color", "abbr", "list", "members", "home", "online", "quit", "qq", "setvisit", "visit", "info");
        } else if (args.length == 2) {
            String subcommand = args[0].toLowerCase();
            Team team = null;
            List<String> teamNames = null;
            ArrayList teams;
            ArrayList members;
            Iterator var20;
            Team team1;
            Iterator var23;
            MCPlayer mcPlayer;
            Iterator var26;
            String playerkicked;
            Team t;
            switch (subcommand) {
                case "new":
                    return null;
                case "del":
                    team = TeamManager.getTeamByPlayer(player);
                    if (team != null && team.isPresident(player)) {
                        teams = new ArrayList();
                        var20 = TeamManager.getTeams().iterator();

                        while(var20.hasNext()) {
                            team1 = (Team)var20.next();
                            teams.add(team1.getName());
                        }

                        return teams;
                    } else {
                        return null;
                    }
                case "info":
                    teams = new ArrayList();
                    var20 = TeamManager.getTeams().iterator();

                    while(var20.hasNext()) {
                        team1 = (Team)var20.next();
                        teams.add(team1.getName());
                    }

                    return teams;
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

                    List<String> players = new ArrayList();
                    Iterator var21 = Bukkit.getOnlinePlayers().iterator();

                    while(var21.hasNext()) {
                        Player p = (Player)var21.next();
                        if (!TeamManager.isMember(team, p)) {
                            players.add(p.getName());
                        }
                    }

                    return players;
                case "kick":
                    team = TeamManager.getTeamByPlayer(player);
                    if (team == null) {
                        return null;
                    } else {
                        if (!team.isPresident(player) && !team.isVicePresident(player)) {
                            return null;
                        }

                        members = new ArrayList();
                        var23 = TeamManager.getMembers(team).iterator();

                        while(var23.hasNext()) {
                            mcPlayer = (MCPlayer)var23.next();
                            playerkicked = Bukkit.getOfflinePlayer(mcPlayer.uuid).getName();
                            if (playerkicked != null && !playerkicked.equals(player.getName())) {
                                members.add(playerkicked);
                            }
                        }

                        return members;
                    }
                case "set副手":
                    team = TeamManager.getTeamByPlayer(player);
                    if (team != null && team.isPresident(player)) {
                        members = new ArrayList();
                        var23 = TeamManager.getMembers(team).iterator();

                        while(var23.hasNext()) {
                            mcPlayer = (MCPlayer)var23.next();
                            playerkicked = Bukkit.getOfflinePlayer(mcPlayer.uuid).getName();
                            if (playerkicked != null && !playerkicked.equals(player.getName())) {
                                members.add(playerkicked);
                            }
                        }

                        return members;
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

                        return new ArrayList(ObjectPool.colors.keySet());
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
                        List<String> abbrs = new ArrayList();
                        char[] var27 = chars;
                        int var30 = chars.length;

                        for(int var31 = 0; var31 < var30; ++var31) {
                            char c = var27[var31];
                            abbrs.add(String.valueOf(c));
                        }

                        return abbrs;
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
                    teamNames = new ArrayList();
                    var26 = TeamManager.getTeams().iterator();

                    while(var26.hasNext()) {
                        t = (Team)var26.next();
                        teamNames.add(t.getName());
                    }

                    return teamNames;
                case "tp":
                    if (subcommand.equals("tp") && !player.hasPermission("teamplugin.op")) {
                        return null;
                    }

                    teamNames = new ArrayList();
                    var26 = TeamManager.getTeams().iterator();

                    while(var26.hasNext()) {
                        t = (Team)var26.next();
                        if (t.getHome() != null) {
                            teamNames.add(t.getName());
                        }
                    }

                    return teamNames;
                case "enabbr":
                    if (!player.hasPermission("teamplugin.op")) {
                        return null;
                    }

                    ArrayList teamNames1 = new ArrayList();
                    Iterator var17 = TeamManager.getTeams().iterator();

                    while(var17.hasNext()) {
                        t = (Team)var17.next();
                        if (t.getHome() != null) {
                            teamNames1.add(t.getName());
                        }
                    }

                    return teamNames1;
                case "setvisit":
                    return null;
                case "visit":
                    return ObjectPool.teamMapper.get_visit_teamName_list();
                default:
                    return null;
            }
        } else {
            return null;
        }
    }
}
