package tcc.youajing.teamplugin.services;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tcc.youajing.teamplugin.database.MCPlayerMapper;
import tcc.youajing.teamplugin.database.TeamMapper;
import tcc.youajing.teamplugin.entities.MCPlayer;
import tcc.youajing.teamplugin.entities.Team;
import tcc.youajing.teamplugin.utils.MyUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tcc.youajing.teamplugin.ObjectPool.*;

public class TeamService {
    private HashMap<Player, Team> invitations = new HashMap<>();

    public boolean get(CommandSender player, Command command, String label, String[] args){
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team get <玩家ID>");
            return true;
        }

        String targetName = args[1];
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);
        if (targetPlayer == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "玩家不存在！");
            return true;
        }

        MCPlayer mc_player = mcPlayerMapper.get_user_by_uuid(targetPlayer.getUniqueId());
        if (mc_player == null) {
            player.sendMessage(String.format("{\"team_color\":\"%s\",\"team_name\":\"%s\"}", "#495057", "该玩家还没有加入组织捏"));
            return true;
        }
        String teamName = mc_player.team;
        if (teamName == null) {
            player.sendMessage(String.format("{\"team_color\":\"%s\",\"team_name\":\"%s\"}", "#495057", "该玩家还没有加入组织捏"));
            return true;
        }

        Team team = teamMapper.get_team_by_name(teamName);

        player.sendMessage(String.format("{\"team_color\":\"%s\",\"team_name\":\"%s\"}", team.color, teamName));
        return true;
    }

    public boolean create(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team new <名称>");
            return true;
        }

        int playTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000; // 获取玩家的总游玩时长，单位为小时

        MCPlayer mc_player = mcPlayerMapper.get(player);
        if (mc_player != null && mc_player.team != null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你已经在一个团队中了！请先退出你所在的团队才能使用此指令！");
            return true;
        }
        String name = args[1];
        if (!name.matches("[a-zA-Z0-9_\u4e00-\u9fa5]+")) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "团队名称只能包含字母、数字、下划线和中文！");
            return true;
        }
        int length = 0;
        for (char c : name.toCharArray()) {
            if (c >= '\u4e00' && c <= '\u9fa5') {
                length += 2;
            } else {
                length += 1;
            }
        }
        if (length > 12) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "团队名称不得超过六个汉字（12个字符）！");
            return true;
        }

        if (teamMapper.exists(name)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队名称已经被注册过了！");
            return true;
        }

        if (player.getLevel() < 100) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你需要至少100级经验才能创建团队！谢谢");
            return true;
        }

        // #测试注释
//        if (playTime < 48) {
//            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你的总游玩时长不足48小时，不能创建团队！");
//            return true;
//        }

        player.setLevel(player.getLevel() - 100);
        TeamManager.createTeam(name, player);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GOLD + "``经验等级-100");
        player.sendMessage(ChatColor.GOLD + "恭喜，你创建了一个名为" + name + "的团队");
        player.sendMessage(ChatColor.GOLD + "当团队规模达到" + ChatColor.YELLOW + ChatColor.BOLD + "5" + ChatColor.RESET + ChatColor.GOLD + "人，你可以使用 /team sethome 为团队设置传送点并且可以使用 /team color 选择团队名称颜色");

        return true;
    }

    public boolean color(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team color <颜色别称>");
            return true;
        }
        MCPlayer mc_player = mcPlayerMapper.get(player);
        if (mc_player == null || mc_player.team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }

        Team team = teamMapper.get_team_by_name(mc_player.team);
        if (!team.isPresident(player) & !team.isVicePresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "只有队长、副手才能选择团队颜色！");
            return true;
        }

        int teamSize = TeamManager.getSize(mc_player.team);

        // #测试注释
//        if (teamSize < 5) {
//            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "团队规模小于5人无法选择颜色！");
//            return true;
//        }

        String colorName = args[1];

        if (!colors.containsKey(colorName)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "请选择列表中的颜色");
            return true;
        }
        if (player.getLevel() < 3) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你需要至少3级经验才能更改团队颜色！");
            return true;
        }
        String colorCode = colors.get(colorName);

        player.setLevel(player.getLevel() - 3);
        TeamManager.setColor(team, colorCode);
        player.sendMessage(ChatColor.GOLD + "``经验等级-3");
        String msgColor = MyUtil.msgColor(colorCode);
        player.sendMessage(ChatColor.GOLD + "你成功选择了团队" + msgColor + team.name + ChatColor.GOLD + "的颜色为" + msgColor + colorName + ChatColor.GOLD + "！");

        return true;
    }

    public boolean abbr(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team abbr <简称>");
            return true;
        }

        MCPlayer mc_player = mcPlayerMapper.get(player);
        if (mc_player == null || mc_player.team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }

        Team team = teamMapper.get_team_by_name(mc_player.team);
        if (!team.isPresident(player) & !team.isVicePresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "只有队长、副手才能设定团队简称！");
            return true;
        }

        int teamSize = TeamManager.getSize(mc_player.team);

        // #测试注释
//        if (teamSize < 5) {
//            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "团队规模小于5人无法设定团队简称！");
//            return true;
//        }
        String abbr = args[1];

        if (abbr.length() != 1 || !team.name.contains(abbr)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "请选择列表中的文字");
            return true;
        }
        if (player.getLevel() < 3) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你需要至少3级经验才能改变团队简称！");
            return true;
        }

        player.setLevel(player.getLevel() - 3);
        TeamManager.setColor(team, abbr);
        player.sendMessage(ChatColor.GOLD + "``经验等级-3");
        player.sendMessage(ChatColor.GOLD + "你成功为团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "设定了简称" + MyUtil.msgColor(team.color) + " [" + abbr + "]" + ChatColor.GOLD + "！");

        return true;
    }

    public boolean enabbr(Player player, Command command, String label, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.YELLOW + "参数过少，用法: /team enabbr <组织名> <简称>");
            return true;
        }

        // 判断玩家是否有teamplugin.op权限
        if (!player.hasPermission("teamplugin.op")) {
            player.sendMessage(ChatColor.DARK_RED + "你没有权限执行这个命令！");
            return true;
        }
        // 获取第二个参数，作为团队名
        String teamName = args[1];

        // 判断团队是否存在
        if (!TeamManager.hasTeam(teamName)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队不存在！");
            return true;
        }

        // 获取团队对象
        Team team = TeamManager.getTeam(teamName);

        int teamSize = TeamManager.getSize(teamName);
        // #测试注释
//        if (teamSize < 5) {
//            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "团队规模小于5人无法设定团队简称！");
//            return true;
//        }
        String abbr = args[2];

        TeamManager.setColor(team, abbr);
        player.sendMessage(ChatColor.GOLD + "你成功为团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "设定了简称" + MyUtil.msgColor(team.color) + " [" + abbr + "]" + ChatColor.GOLD + "！");

        return true;
    }

    public boolean del(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team del <名称>");
            return true;
        }
        String name = args[1];
        // 获取团队对象
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null || !Objects.equals(team.getName(), name)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "请输入正确的你的团队名！");
            return true;
        }

        // 判断玩家是否是队长
        if (!team.isPresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "啊？？只有队长才能删除团队！");
            return true;
        }


        // 判断玩家是否有足够的经验等级
        if (player.getLevel() < 30) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你需要至少30级经验才能删除团队！");
            return true;
        }

        player.setLevel(player.getLevel() - 30);
        TeamManager.deleteTeam(team.getName());
        player.sendMessage(ChatColor.GREEN + "你成功删除了团队" + name + "!");
        return true;
    }

    public boolean sethome(Player player, Command command, String label, String[] args) {
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }

        // 判断玩家是否是队长
        if (!team.isPresident(player) & !team.isVicePresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "只有队长、副手才能设定团队传送点！");
            return true;
        }

        int teamSize = TeamManager.getSize(team.getName());

        // #测试注释
//        if (teamSize < 5) {
//            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "团队规模小于5人无法设置传送点！");
//            return true;
//        }

        // 获取玩家的脚下坐标
        Location location = player.getLocation();

        // 设置团队传送点
        TeamManager.setHome(team, location);

        // 发送成功消息
        player.sendMessage(ChatColor.GOLD + "你成功为团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "设定了传送点！");
        return true;
    }

    public boolean enforcesethome(Player player, Command command, String label, String[] args) {
        // 判断玩家是否有teamplugin.op权限
        if (!player.hasPermission("teamplugin.op")) {
            player.sendMessage(ChatColor.DARK_RED + "你没有权限执行这个命令！");
            return true;
        }
        // 获取第二个参数，作为团队名
        String teamName1 = args[1];
        // 判断团队是否存在
        if (!TeamManager.hasTeam(teamName1)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队不存在！");
            return true;
        }
        // 获取团队对象
        Team team = TeamManager.getTeam(teamName1);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }

        // 获取玩家的脚下坐标
        Location location = player.getLocation();

        // 设置团队传送点
        TeamManager.setHome(team, location);

        // 发送成功消息
        player.sendMessage(ChatColor.GOLD + "你成功为团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "设定了传送点！");
        return true;
    }

    public boolean enforcesetcolor(Player player, Command command, String label, String[] args) {
        // 判断玩家是否有teamplugin.op权限
        if (!player.hasPermission("teamplugin.op")) {
            player.sendMessage(ChatColor.DARK_RED + "你没有权限执行这个命令！");
            return true;
        }
        // 获取第二个参数，作为团队名
        String teamName1 = args[1];
        // 判断团队是否存在
        if (!TeamManager.hasTeam(teamName1)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队不存在！");
            return true;
        }
        // 获取团队对象
        Team team = TeamManager.getTeam(teamName1);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }
        String colorCode = args[2];

        TeamManager.setColor(team, colorCode);
        player.sendMessage(ChatColor.GOLD + "``经验等级-3");
        player.sendMessage(ChatColor.GOLD + "你成功选择了团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "的颜色为" + MyUtil.msgColor(team.color) + colorCode + ChatColor.GOLD + "！");
        return true;
    }

    public boolean home(Player player, Command command, String label, String[] args) {
        // 传送到团队传送点的子命令
        // 判断玩家是否在一个团队中
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }

        // 判断玩家是否是队员或队长
        if (!TeamManager.isMember(team, player) && !team.isPresident(player) && !team.isVicePresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "只有团队成员才能传送到团队传送点！");
            return true;
        }

        // 获取团队传送点
        Location location = team.getHome();

        // 判断团队是否有设定传送点
        if (location == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你的团队还没有设定传送点！请让团队的队长使用 /team sethome 来设定传送点");
            return true;
        }
        MyUtil.teleport(player, location);
        player.sendMessage(ChatColor.GOLD + "你成功传送到了团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "的传送点！");

        return true;
    }

    public boolean tp(Player player, Command command, String label, String[] args) {
        // 传送到团队传送点的子命令
        // 判断参数是否足够
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team tp <团队名>");
            return true;
        }
        // 判断玩家是否有teamplugin.op权限
        if (!player.hasPermission("teamplugin.op")) {
            player.sendMessage(ChatColor.DARK_RED + "你没有权限执行这个命令！");
            return true;
        }
        // 获取第二个参数，作为团队名
        String teamName = args[1];
        // 判断团队是否存在
        if (!TeamManager.hasTeam(teamName)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队不存在！");
            return true;
        }

        // 获取团队对象
        Team team = TeamManager.getTeam(teamName);

        // 获取团队传送点
        Location location = team.getHome();

        // 判断团队是否有设定传送点
        if (location == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队还没有设定传送点！请让团队的队长使用 /team sethome 来设定传送点");
            return true;
        }

        // 传送玩家到团队传送点
        MyUtil.teleport(player, location);
        player.sendMessage(ChatColor.GOLD + "你成功传送到了团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "的传送点！");

        return true;
    }

    public boolean invite(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team invite <玩家>");
            return true;
        }
        // 获取第二个参数，作为目标玩家的名字
        String targetName = args[1];

        // 获取目标玩家对象
        Player target = Bukkit.getPlayer(targetName);

        // 判断目标玩家是否在线
        if (target == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你邀请的玩家不在线！");
            return true;
        }

        // 判断目标玩家游玩时长是否满足24h
        int playTime = target.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000;
        if (playTime < 24) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你邀请的玩家的总游玩时长不足24小时，无法邀请他加入团队！");
            return true;
        }
        // 判断玩家是否在团队中
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你还没有拥有团队，请先使用/team new <名称>创建一个团队！");
            return true;
        }
        // 判断玩家是否是队长
        if (!team.isPresident(player) & !team.isVicePresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "只有队长、副手才能邀请玩家！");
            return true;
        }

        // 判断目标玩家是否是自己
        if (target.equals(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "NONONONO不能邀请自己！");
            return true;
        }

        // 判断目标玩家是否已经在一个团队中
        if (TeamManager.getTeamByPlayer(target) != null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个玩家已经在一个团队中了！");
            return true;
        }

        //目标玩家提示音
        player.playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        //发起人提示音
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        // 发送邀请消息给目标玩家
        target.sendMessage(ChatColor.GOLD + "你收到了来自" + player.getName() + "的团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "的邀请，接受请输入/team accept，拒绝请输入/team reject。");

        //发送成功消息给玩家
        player.sendMessage(ChatColor.GOLD + "你成功邀请了" + target.getName() + "加入你的团队！请等待" + target.getName() + "的回复!");
        //将玩家与队伍添加到等待列表
        invitations.put(target, team);


        return true;
    }

    public boolean kick(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team kick <玩家>");
            return true;
        }

        // 判断玩家是否在一个团队中
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }

        // 判断玩家是否是队长
        if (!team.isPresident(player) & !team.isVicePresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "只有队长、副手才能剔除玩家！");
            return true;
        }
        // 获取第二个参数，作为目标玩家的名字
        String targetName = args[1];
        MCPlayer userByName = mcPlayerMapper.get_user_by_name(targetName);

        if (userByName == null || !userByName.inTeam(team.name)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个玩家不在你的团队中！");
            return true;
        }

        // 判断目标玩家是否是自己
        if (targetName.equals(player.getName().toLowerCase())) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不能剔除自己！");
            return true;
        }

        // 从团队中移除目标玩家
        TeamManager.kickMember(userByName.uuid);

        // 发送被剔除消息给目标玩家
        if (Bukkit.getPlayer(userByName.name) != null) {
            Bukkit.getPlayer(userByName.name).sendMessage(ChatColor.DARK_RED + "你被" + player.getName() + "从团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.DARK_RED + "中剔除了！");
        }

        // 发送成功消息给玩家
        player.sendMessage(ChatColor.GOLD + "你成功剔除了" + userByName.name + "从你的团队！");

        return true;
    }

    public boolean setVicePresident(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team setdeputy <玩家>");
            return true;
        }

        // 判断玩家是否在一个团队中
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }

        // 判断玩家是否是队长
        if (!team.isPresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "只有队长才能设置团队副手！");
            return true;
        }
        // 获取第二个参数，作为目标玩家的名字
        String targetName = args[1];

        MCPlayer userByName = mcPlayerMapper.get_user_by_name(targetName);

        if (userByName == null || userByName.inTeam(team.name)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个玩家不在你的团队中！");
            return true;
        }
        // 判断目标玩家是否是自己
        if (targetName.equals(player.getName().toLowerCase())) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不能将自己设置为团队副手！");
            return true;
        }

        // 将目标玩家设为团队副手
        TeamManager.setVicePresident(team, userByName.uuid);

        if (Bukkit.getPlayer(userByName.name) != null) {
            Bukkit.getPlayer(userByName.name).sendMessage(ChatColor.GOLD + "你被" + player.getName() + "设置为团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "的副手！");
        }

        // 发送成功消息给玩家
        player.sendMessage(ChatColor.GOLD + "你成功将" + userByName.name + "设置为你的团队副手！");

        return true;
    }

    public boolean unsetVicePresident(Player player, Command command, String label, String[] args) {
        // 判断玩家是否在一个团队中
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }
        // 判断玩家是否是队长
        if (!team.isPresident(player) && !team.isVicePresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "只有队长以及副手本人才能取消团队副手！");
            return true;
        }


        String fushouName = mcPlayerMapper.get_user_by_uuid(team.vice_president).name;
        TeamManager.unsetVicePresident(team);

        if (player.getName().toLowerCase().equals(fushouName)) {
            player.sendMessage(ChatColor.GOLD + "你不再担任团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "的副手！");
        } else {
            player.sendMessage(fushouName + ChatColor.GOLD + "不再担任你的团队副手");
        }
        return true;
    }

    public boolean rename(Player player, Command command, String label, String[] args) {
        // 更改团队名的子命令
        // 判断参数是否足够
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team rename <新名称>");
            return true;
        }
        // 判断玩家是否在一个团队中
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }

        // 判断玩家是否是队长
        if (!team.isPresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "只有队长才能更改团队名！");
            return true;
        }
        // 获取第二个参数，作为新的团队名
        String newName = args[1];

        // 判断新的团队名是否合法
        if (!newName.matches("[a-zA-Z0-9_\u4e00-\u9fa5]+")) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "团队名称只能包含字母、数字、下划线和中文！");
            return true;
        }

        // 判断新的团队名的长度是否超过六个汉字（12个字符）
        int length = 0;
        for (char c : newName.toCharArray()) {
            if (c >= '\u4e00' && c <= '\u9fa5') {
                length += 2; // 每个汉字占两个字符
            } else {
                length += 1; // 其他字符占一个字符
            }
        }
        if (length > 12) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "团队名称不得超过六个汉字（12个字符）！");
            return true;
        }

        // 判断新的团队名是否已存在
        if (TeamManager.hasTeam(newName)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队名称已经被注册过了！");
            return true;
        }

        // 判断玩家是否有足够的经验等级
        if (player.getLevel() < 100) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你需要至少100级经验才能更改团队名！");
            return true;
        }

        // 扣除玩家100级经验
        player.setLevel(player.getLevel() - 100);

        // 获取原来的团队名
        String oldName = team.getName();

        // 更改团队名
        TeamManager.rename(team, newName);

        // 发送成功消息给玩家
        player.sendMessage(ChatColor.GOLD + "``经验等级-100");
        player.sendMessage("你成功将你的团队名从" + MyUtil.msgColor(team.color) + oldName + ChatColor.RESET + "更改为" + MyUtil.msgColor(team.color) + newName + ChatColor.RESET + "!");

        // 发送通知消息给团队中的其他玩家和队长
        for (MCPlayer mcPlayer : TeamManager.getMembers(team)) {
            Player p = Bukkit.getPlayer(mcPlayer.uuid);
            if (p != null) {
                p.sendMessage(ChatColor.GOLD + "你们的团队名已经被" + player.getName() + "更改为" + newName + "!");
            }
        }

        return true;
    }

    public boolean list(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "如果要翻页的话: /team list <页码>");
        }
        // 查看团队列表的子命令
        // 获取所有团队对象的列表
        List<Team> teams = TeamManager.getTeams();

        // 判断是否有团队存在
        if (teams.isEmpty()) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "目前没有任何团队！");
            return true;
        }

        // 按照团队规模排序，人数大的排在最上面
        teams.sort(Comparator.comparingInt(t -> TeamManager.getSize(t))); // 加一是为了包括队长
        Collections.reverse(teams); // 反转顺序

        // 获取第2个参数，作为页码，默认为1
        int page = 1;

        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "页码必须是一个整数！");
                return true;
            }
        }

        // 定义每页显示的团队数
        int pageSize = 10;

        // 计算总页数
        int totalPages = (teams.size() - 1) / pageSize + 1;

        // 判断页码是否合法
        if (page < 1 || page > totalPages) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "页码必须在1到" + totalPages + "之间！");
            return true;
        }

        // 发送团队列表的标题
        player.sendMessage(ChatColor.GOLD + "已注册的团队列表（第" + page + "页/共" + totalPages + "页）：");

        // 发送团队列表的内容，每行显示一个团队的名称、颜色、规模和队长
        for (int i = (page - 1) * pageSize; i < page * pageSize && i < teams.size(); i++) {
            Team t = teams.get(i);
            String listName = t.getName();
            int size = TeamManager.getSize(t);
            String leaderName;
            if (t.hasVicePresident()) {
                String fushouName = mcPlayerMapper.get_user_by_uuid(t.vice_president).getName();
                leaderName = mcPlayerMapper.get_user_by_uuid(t.president).getName();
                player.sendMessage(ChatColor.GRAY + "- " + ChatColor.AQUA + ChatColor.BOLD + listName);
                player.sendMessage(ChatColor.GRAY + "      [" + size + "人] 队长: " + leaderName + "  副手：" + fushouName);
            } else {
                if (t.hasPresident()) {
                    leaderName = mcPlayerMapper.get_user_by_uuid(t.president).getName();
                    player.sendMessage(ChatColor.GRAY + "- " + ChatColor.AQUA + ChatColor.BOLD + listName);
                    player.sendMessage(ChatColor.GRAY + "      [" + size + "人] 队长: " + leaderName);
                } else {
                    player.sendMessage(ChatColor.GRAY + "- " + ChatColor.AQUA + ChatColor.BOLD + listName);
                }
            }
        }

        return true;
    }

    public boolean members(Player player, Command command, String label, String[] args) {
        // 查看团队成员的子命令
        // 判断参数是否足够
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team members <队伍名>");
            return true;
        }
        // 获取第二个参数，作为团队名
        String teamName = args[1];
        // 判断团队是否存在
        if (!TeamManager.hasTeam(teamName)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队不存在！");
            return true;
        }

        // 获取团队对象
        Team team = TeamManager.getTeam(teamName);

        // 创建一个字符串缓冲区，用于存储团队成员的名字
        StringBuilder members = new StringBuilder();

        // 获取团队的队长的名字，将其添加到字符串缓冲区的最前面，用[队长]的标签标识
        String leaderName = null;
        if (team.hasPresident()) {
            leaderName = mcPlayerMapper.get_user_by_uuid(team.president).getName();
            members.append(ChatColor.AQUA).append(leaderName).append("[队长], ").append(ChatColor.GRAY);
        }
        if (team.hasVicePresident()) {
            String fushouName = mcPlayerMapper.get_user_by_uuid(team.vice_president).getName();
            members.append(ChatColor.AQUA).append(fushouName).append("[副手], ").append(ChatColor.GRAY);
        }

        // 遍历团队中的所有成员，将他们的名字添加到字符串缓冲区中，用逗号分隔
        for (MCPlayer mcPlayer : TeamManager.getMembers(team)) {
            String memberName = Bukkit.getOfflinePlayer(mcPlayer.uuid).getName();
            if (memberName != null) {
                members.append(memberName).append(", ");
            }
        }

        // 发送团队成员的标题
        player.sendMessage(ChatColor.GOLD + "团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "的成员列表：");

        // 发送团队成员的内容
        player.sendMessage(ChatColor.GRAY + "- " + members);
        return true;
    }

    public boolean online(Player player, Command command, String label, String[] args) {
        // 查看在线团队成员的子命令
        // 获取团队对象
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }
        // 创建一个字符串缓冲区，用于存储团队成员的名字
        StringBuilder onlineMembers = new StringBuilder();

        // 获取团队的队长的名字，将其添加到字符串缓冲区的最前面，用[队长]的标签标识
        if (team.hasPresident()) {
            String leaderName = mcPlayerMapper.get_user_by_uuid(team.president).getName();
            if (leaderName != null && Bukkit.getPlayer(leaderName) != null) {
                onlineMembers.append(ChatColor.AQUA).append(leaderName).append("[队长], ").append(ChatColor.GRAY);
            }
        }
        if (team.hasVicePresident()) {
            String fushouName = mcPlayerMapper.get_user_by_uuid(team.vice_president).getName();
            if (fushouName != null && Bukkit.getPlayer(fushouName) != null) {
                onlineMembers.append(ChatColor.AQUA).append(fushouName).append("[副手], ").append(ChatColor.GRAY);
            }
        }

        // 遍历团队中的所有成员，将他们的名字添加到字符串缓冲区中，用逗号分隔
        for (MCPlayer mcPlayer : TeamManager.getMembers(team)) {
            String memberName = Bukkit.getOfflinePlayer(mcPlayer.uuid).getName();
            if (memberName != null && Bukkit.getPlayer(memberName) != null) {
                onlineMembers.append(memberName).append(", ");
            }
        }

        // 发送团队成员的标题
        player.sendMessage(ChatColor.GOLD + "团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "在线的成员列表：");

        // 发送团队成员的内容
        player.sendMessage(ChatColor.GRAY + "- " + onlineMembers.toString());

        return true;
    }

    public boolean accept(Player player, Command command, String label, String[] args) {
        // 接受团队邀请的子命令


        // 判断玩家是否已经在一个团队中
        if (TeamManager.getTeamByPlayer(player) != null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "哥，你已经有团队了！");
            invitations.remove(player);
            return true;
        }
        // 判断玩家是否收到了邀请
        if (!invitations.containsKey(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "没有任何团队向你发出邀请！");
            return true;
        }

        // 获取邀请的团队对象
        Team team = invitations.get(player);

        // 判断团队是否还存在
        if (!TeamManager.hasTeam(team.getName())) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队已经不存在了！");
            invitations.remove(player);
            return true;
        }

        // 判断目标玩家游玩时长是否满足24h
        int playTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000;
        if (playTime < 24) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你的总游玩时长不足24小时，无法加入团队！");
            return true;
        }
        // 将玩家加入团队
        TeamManager.addMember(team, player.getUniqueId());
        // 从邀请列表中移除玩家
        invitations.remove(player, team);
        // 被邀请者成功提示音
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        // 发送成功消息给玩家
        player.sendMessage(ChatColor.GOLD + "你成功加入了团队" + MyUtil.msgColor(team.color) + team.getName());
        // 发送通知消息给团队中的其他玩家和队长
        for (MCPlayer mcPlayer : TeamManager.getMembers(team)) {
            Player p = Bukkit.getPlayer(mcPlayer.uuid);
            if (p != null && !p.equals(player)) {
                player.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                p.sendMessage(ChatColor.GOLD + player.getName() + "加入了你们的团队！");
            }
        }
        if (team.hasPresident()) {
            Player leader = Bukkit.getPlayer(team.getPresident());
            if (leader != null) {
                player.playSound(leader, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                leader.sendMessage(ChatColor.GOLD + player.getName() + "加入了你的团队！");
            }
        }
        if (team.hasVicePresident()) {
            Player fushou1 = Bukkit.getPlayer(team.getVicePresident());
            if (fushou1 != null) {
                player.playSound(fushou1, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                fushou1.sendMessage(ChatColor.GOLD + player.getName() + "加入了你的团队！");
            }
        }

        return true;
    }

    public boolean reject(Player player, Command command, String label, String[] args) {
        // 判断玩家是否已经在一个团队中
        if (TeamManager.getTeamByPlayer(player) != null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "哥，你已经有团队了！");
            invitations.remove(player);
            return true;
        }
        if (!invitations.containsKey(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你没有收到任何团队邀请!");
            return true;
        }
        // 获取邀请的团队对象
        Team team = invitations.get(player);

        if (!TeamManager.hasTeam(team.getName())) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队已经不存在了！");
            invitations.remove(player);
            return true;
        }
        // 从邀请列表中移除玩家
        invitations.remove(player);

        // 发送成功消息给玩家
        player.sendMessage("你拒绝了来自" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "的团队邀请！");

        // 发送通知消息给团队的队长

        if (team.hasPresident()) {
            Player leader = Bukkit.getPlayer(team.getPresident());
            if (leader != null) {
                leader.sendMessage(ChatColor.GOLD + player.getName() + "加入了你的团队！");
            }
        }
        if (team.hasVicePresident()) {
            Player fushou1 = Bukkit.getPlayer(team.getVicePresident());
            if (fushou1 != null) {
                fushou1.sendMessage(ChatColor.GOLD + player.getName() + "加入了你的团队！");
            }
        }
        return true;
    }

    public boolean enforcerename(Player player, Command command, String label, String[] args) {
        // 判断玩家是否有teamplugin.op权限
        if (!player.hasPermission("teamplugin.op")) {
            player.sendMessage(ChatColor.DARK_RED + "你没有权限执行这个命令！");
            return true;
        }
        // 获取第二个参数，作为团队名
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team rename <新名称>");
            return true;
        }
        String teamName1 = args[1];
        // 判断团队是否存在
        if (!TeamManager.hasTeam(teamName1)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队不存在！");
            return true;
        }
        // 获取团队对象
        Team team = TeamManager.getTeam(teamName1);
        // 获取第二个参数，作为新的团队名
        String newName = args[2];

        // 判断新的团队名是否合法
        if (!newName.matches("[a-zA-Z0-9_\u4e00-\u9fa5]+")) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "团队名称只能包含字母、数字、下划线和中文！");
            return true;
        }

        // 判断新的团队名的长度是否超过六个汉字（12个字符）
        int length = 0;
        for (char c : newName.toCharArray()) {
            if (c >= '\u4e00' && c <= '\u9fa5') {
                length += 2; // 每个汉字占两个字符
            } else {
                length += 1; // 其他字符占一个字符
            }
        }
        if (length > 12) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "团队名称不得超过六个汉字（12个字符）！");
            return true;
        }

        // 判断新的团队名是否已存在
        if (TeamManager.hasTeam(newName)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "这个团队名称已经被注册过了！");
            return true;
        }


        // 获取原来的团队名
        String oldName = team.getName();

        // 更改团队名
        TeamManager.rename(team, newName);


        // 发送成功消息给玩家
        player.sendMessage("你成功将你的团队名从" + MyUtil.msgColor(team.color) + oldName + ChatColor.RESET + "更改为" + MyUtil.msgColor(team.color) + newName + ChatColor.RESET + "!");

        // 发送通知消息给团队中的其他玩家和队长
        for (MCPlayer mcPlayer : TeamManager.getMembers(team)) {
            Player p = Bukkit.getPlayer(mcPlayer.uuid);
            if (p != null) {
                p.sendMessage(ChatColor.GOLD + "你们的团队名已经被" + player.getName() + "更改为" + newName + "!");
            }
        }

        return true;
    }

    public boolean quit(Player player, Command command, String label, String[] args) {
        // 判断玩家是否在一个团队中
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！大哥你没有团队咋退出啊！？");
            return true;
        }
        // 判断玩家是否是队长
        if (team.isPresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "哼！想逃？如果你想解散团队，请使用/team del <名称>命令！");
            return true;
        }
        // 从团队中移除玩家
        if (team.isVicePresident(player)) {
            TeamManager.unsetVicePresident(team);
        }
        TeamManager.kickMember(player.getUniqueId());

        // 发送成功消息给玩家
        player.sendMessage(ChatColor.GOLD + "你成功退出了团队" + MyUtil.msgColor(team.color) + team.getName() + ChatColor.GOLD + "！");

        // 发送通知消息给团队中的其他玩家和队长
        for (MCPlayer mcPlayer : TeamManager.getMembers(team)) {
            Player p = Bukkit.getPlayer(mcPlayer.uuid);
            if (p != null) {
                p.sendMessage(ChatColor.DARK_RED + "很遗憾" + player.getName() + "退出了你们的团队！");
            }
        }
        if (team.hasPresident()) {
            Player leader = Bukkit.getPlayer(team.getPresident());
            if (leader != null) {
                leader.sendMessage(ChatColor.DARK_RED + "很遗憾" + player.getName() + "退出了你们的团队！");
            }
        }
        if (team.hasVicePresident()) {
            Player fushou1 = Bukkit.getPlayer(team.getVicePresident());
            if (fushou1 != null) {
                fushou1.sendMessage(ChatColor.DARK_RED + "很遗憾" + player.getName() + "退出了你们的团队！");
            }
        }
        return true;
    }

    public boolean qq(Player player, Command command, String label, String[] args) {
        // 更改团队qq的子命令
        // 判断参数是否足够
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team qq <qq号>");
            return true;
        }
        // 判断玩家是否在一个团队中
        Team team = TeamManager.getTeamByPlayer(player);
        if (team == null) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "你不在一个团队中！");
            return true;
        }

        // 判断玩家是否是队长或副手
        if (!team.isPresident(player) && !team.isVicePresident(player)) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "只有队长和副手才能设置团队qq！");
            return true;
        }

        int teamSize = TeamManager.getSize(team);

        // #测试注释
//        if (teamSize < 5) {
//            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "团队规模小于5人无法选择颜色！");
//            return true;
//        }

        // 获取第二个参数，作为团队的qq
        String qq = args[1];


        // 判断qq号是否合法
        Pattern qqPattern = Pattern.compile("^[0-9]{5,11}$");
        Matcher matcher = qqPattern.matcher(qq);

        if (!matcher.find()) {
            player.sendMessage(ChatColor.DARK_RED + "错误：" + ChatColor.GOLD + "qq格式不正确！");
            return true;
        }
        // 更改团队名
        TeamManager.setQQ(team, qq);

        // 发送成功消息给玩家
        player.sendMessage("你成功将你的团队的qq设置为" + MyUtil.msgColor(team.color) + qq + ChatColor.RESET + "!");

        return true;
    }
}
