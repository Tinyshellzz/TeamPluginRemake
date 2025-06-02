package tcc.youajing.teamplugin.services;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import tcc.youajing.teamplugin.database.VisitBanListMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tcc.youajing.teamplugin.ObjectPool.visitBanListMapper;

public class TeamBanService {
    public static boolean banVisit(CommandSender sender, Command command, String label, String[] args) {
        Matcher _m = Pattern.compile("^.*CraftRemoteConsoleCommandSender.*$").matcher(sender.toString());
        if(!(sender instanceof ConsoleCommandSender || _m.find() || sender.isOp() || sender.hasPermission("team.use"))){
            sender.sendMessage(ChatColor.RED + "只有控制台, op以及拥有team.use权限的玩家才能使用该命令");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "用法: /team ban visit <名称>");
            return true;
        }

        String target = args[2];
        visitBanListMapper.insert(target);
        sender.sendMessage(ChatColor.GOLD + "已成功禁用team " + target + "的visit功能");
        return true;
    }

    public static boolean unbanVisit(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "用法: /team unban visit <名称>");
            return true;
        }

        String target = args[2];
        visitBanListMapper.remove(target);
        sender.sendMessage(ChatColor.GOLD + "已成功解禁team " + target + "的visit功能");
        return true;
    }
}
