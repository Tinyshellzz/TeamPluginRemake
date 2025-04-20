//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import tcc.youajing.teamplugin.ObjectPool;
import tcc.youajing.teamplugin.config.PluginConfig;
import tcc.youajing.teamplugin.entities.MCPlayer;
import tcc.youajing.teamplugin.entities.Team;
import tcc.youajing.teamplugin.placeholder.TeampluginExpansion;
import tcc.youajing.teamplugin.utils.MyUtil;

public class TeamService {
    private HashMap<Player, Team> invitations = new HashMap();

    public TeamService() {
    }

    public boolean get(CommandSender player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team get <玩家ID>");
            return true;
        } else {
            String targetName = args[1];
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);
            if (targetPlayer == null) {
                String var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "玩家不存在！");
                return true;
            } else {
                Bukkit.getConsoleSender().sendMessage("TeamService.get get_user_by_uuid" + player.toString());
                MCPlayer mc_player = ObjectPool.mcPlayerMapper.get_user_by_uuid(targetPlayer.getUniqueId());
                if (mc_player == null) {
                    player.sendMessage(String.format("{\"team_color\":\"%s\",\"team_name\":\"%s\"}", "#495057", "该玩家还没有加入组织捏"));
                    return true;
                } else {
                    String teamName = mc_player.team;
                    if (teamName == null) {
                        player.sendMessage(String.format("{\"team_color\":\"%s\",\"team_name\":\"%s\"}", "#495057", "该玩家还没有加入组织捏"));
                        return true;
                    } else {
                        Team team = ObjectPool.teamMapper.get_team_by_name(teamName);
                        TeamManager.teams.put(mc_player.getUuid(), team);
                        TeamManager.teams.put(mc_player.getUuid(), team);
                        player.sendMessage(String.format("{\"team_color\":\"%s\",\"team_name\":\"%s\"}", team.color, teamName));
                        return true;
                    }
                }
            }
        }
    }

    public boolean create(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team new <名称>");
            return true;
        } else {
            int playTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000;
            MCPlayer mc_player = ObjectPool.mcPlayerMapper.get(player);
            String var10001;
            if (mc_player != null && mc_player.team != null) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你已经在一个团队中了！请先退出你所在的团队才能使用此指令！");
                return true;
            } else {
                String name = args[1];
                if (!name.matches("[a-zA-Z0-9_一-龥]+")) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "团队名称只能包含字母、数字、下划线和中文！");
                    return true;
                } else {
                    int length = 0;
                    char[] var9 = name.toCharArray();
                    int var10 = var9.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                        char c = var9[var11];
                        if (c >= 19968 && c <= '龥') {
                            length += 2;
                        } else {
                            ++length;
                        }
                    }

                    if (length > 12) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "团队名称不得超过六个汉字（12个字符）！");
                        return true;
                    } else if (ObjectPool.teamMapper.exists(name)) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队名称已经被注册过了！");
                        return true;
                    } else if (playTime < 48 && !ObjectPool.pluginConfig.debug) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你的总游玩时长不足48小时，不能创建团队！");
                        return true;
                    } else {
                        if (!ObjectPool.pluginConfig.debug) {
                            if (player.getLevel() < 100) {
                                var10001 = String.valueOf(ChatColor.DARK_RED);
                                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你需要至少100级经验才能创建团队！谢谢");
                                return true;
                            }

                            player.setLevel(player.getLevel() - 100);
                        }

                        TeamManager.createTeam(name, player);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                        player.sendMessage(String.valueOf(ChatColor.GOLD) + "``经验等级-100");
                        var10001 = String.valueOf(ChatColor.GOLD);
                        player.sendMessage(var10001 + "恭喜，你创建了一个名为" + name + "的团队");
                        player.sendMessage(String.valueOf(ChatColor.GOLD) + "你可以用 /team setvisit 设置团队公开传送点");
                        var10001 = String.valueOf(ChatColor.GOLD);
                        player.sendMessage(var10001 + "当团队规模达到" + String.valueOf(ChatColor.YELLOW) + String.valueOf(ChatColor.BOLD) + "5" + String.valueOf(ChatColor.RESET) + String.valueOf(ChatColor.GOLD) + "人，你可以使用 /team sethome 为团队设置传送点并且可以使用 /team color 选择团队名称颜色");
                        return true;
                    }
                }
            }
        }
    }

    public boolean color(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team color <颜色别称>");
            return true;
        } else {
            MCPlayer mc_player = ObjectPool.mcPlayerMapper.get(player);
            String var10001;
            if (mc_player != null && mc_player.team != null) {
                Team team = ObjectPool.teamMapper.get_team_by_name(mc_player.team);
                TeamManager.teams.put(mc_player.getUuid(), team);
                TeamManager.teams.put(mc_player.getUuid(), team);
                if (!team.isPresident(player) & !team.isVicePresident(player)) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "只有队长、副手才能选择团队颜色！");
                    return true;
                } else {
                    int teamSize = TeamManager.getSize(mc_player.team);
                    if (teamSize < 5 && !ObjectPool.pluginConfig.debug) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "团队规模小于5人无法选择颜色！");
                        return true;
                    } else {
                        String colorName = args[1];
                        if (!ObjectPool.colors.containsKey(colorName)) {
                            var10001 = String.valueOf(ChatColor.DARK_RED);
                            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "请选择列表中的颜色");
                            return true;
                        } else if (player.getLevel() < 3) {
                            var10001 = String.valueOf(ChatColor.DARK_RED);
                            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你需要至少3级经验才能更改团队颜色！");
                            return true;
                        } else {
                            String colorCode = (String)ObjectPool.colors.get(colorName);
                            player.setLevel(player.getLevel() - 3);
                            TeamManager.setColor(team, colorCode);
                            player.sendMessage(String.valueOf(ChatColor.GOLD) + "``经验等级-3");
                            String msgColor = MyUtil.msgColor(colorCode);
                            var10001 = String.valueOf(ChatColor.GOLD);
                            player.sendMessage(var10001 + "你成功选择了团队" + msgColor + team.name + String.valueOf(ChatColor.GOLD) + "的颜色为" + msgColor + colorName + String.valueOf(ChatColor.GOLD) + "！");
                            return true;
                        }
                    }
                }
            } else {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
                return true;
            }
        }
    }

    public boolean abbr(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team abbr <简称>");
            return true;
        } else {
            MCPlayer mc_player = ObjectPool.mcPlayerMapper.get(player);
            String var10001;
            if (mc_player != null && mc_player.team != null) {
                Team team = ObjectPool.teamMapper.get_team_by_name(mc_player.team);
                TeamManager.teams.put(mc_player.getUuid(), team);
                TeamManager.teams.put(mc_player.getUuid(), team);
                if (!team.isPresident(player) & !team.isVicePresident(player)) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "只有队长、副手才能设定团队简称！");
                    return true;
                } else {
                    int teamSize = TeamManager.getSize(mc_player.team);
                    if (teamSize < 5 && !ObjectPool.pluginConfig.debug) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "团队规模小于5人无法设定团队简称！");
                        return true;
                    } else {
                        String abbr = args[1];
                        if (abbr.length() == 1 && team.name.contains(abbr)) {
                            if (player.getLevel() < 3) {
                                var10001 = String.valueOf(ChatColor.DARK_RED);
                                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你需要至少3级经验才能改变团队简称！");
                                return true;
                            } else {
                                player.setLevel(player.getLevel() - 3);
                                TeamManager.setAbbr(team, abbr);
                                player.sendMessage(String.valueOf(ChatColor.GOLD) + "``经验等级-3");
                                var10001 = String.valueOf(ChatColor.GOLD);
                                player.sendMessage(var10001 + "你成功为团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "设定了简称" + MyUtil.msgColor(team.color) + " [" + abbr + "]" + String.valueOf(ChatColor.GOLD) + "！");
                                return true;
                            }
                        } else {
                            var10001 = String.valueOf(ChatColor.DARK_RED);
                            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "请选择列表中的文字");
                            return true;
                        }
                    }
                }
            } else {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
                return true;
            }
        }
    }

    public boolean enabbr(Player player, Command command, String label, String[] args) {
        if (args.length < 3) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "参数过少，用法: /team enabbr <组织名> <简称>");
            return true;
        } else if (!player.hasPermission("teamplugin.op")) {
            player.sendMessage(String.valueOf(ChatColor.DARK_RED) + "你没有权限执行这个命令！");
            return true;
        } else {
            String teamName = args[1];
            String var10001;
            if (!TeamManager.hasTeam(teamName)) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队不存在！");
                return true;
            } else {
                Team team = TeamManager.getTeam(teamName);
                int teamSize = TeamManager.getSize(teamName);
                if (teamSize < 5 && !ObjectPool.pluginConfig.debug) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "团队规模小于5人无法设定团队简称！");
                    return true;
                } else {
                    String abbr = args[2];
                    TeamManager.setAbbr(team, abbr);
                    var10001 = String.valueOf(ChatColor.GOLD);
                    player.sendMessage(var10001 + "你成功为团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "设定了简称" + MyUtil.msgColor(team.color) + " [" + abbr + "]" + String.valueOf(ChatColor.GOLD) + "！");
                    return true;
                }
            }
        }
    }

    public boolean del(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team del <名称>");
            return true;
        } else {
            String name = args[1];
            Team team = TeamManager.getTeamByPlayer(player);
            String var10001;
            if (team != null && Objects.equals(team.getName(), name)) {
                if (!team.isPresident(player)) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "啊？？只有队长才能删除团队！");
                    return true;
                } else if (player.getLevel() < 30) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你需要至少30级经验才能删除团队！");
                    return true;
                } else {
                    player.setLevel(player.getLevel() - 30);
                    TeamManager.deleteTeam(team.getName());
                    var10001 = String.valueOf(ChatColor.GREEN);
                    player.sendMessage(var10001 + "你成功删除了团队" + name + "!");
                    return true;
                }
            } else {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "请输入正确的你的团队名！");
                return true;
            }
        }
    }

    public boolean sethome(Player player, Command command, String label, String[] args) {
        Team team = TeamManager.getTeamByPlayer(player);
        String var10001;
        if (team == null) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
            return true;
        } else if (!team.isPresident(player) & !team.isVicePresident(player)) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "只有队长、副手才能设定团队传送点！");
            return true;
        } else {
            int teamSize = TeamManager.getSize(team.getName());
            if (teamSize < 5 && !ObjectPool.pluginConfig.debug) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "团队规模小于5人无法设置传送点！");
                return true;
            } else {
                Location location = player.getLocation();
                TeamManager.setHome(team, location);
                var10001 = String.valueOf(ChatColor.GOLD);
                player.sendMessage(var10001 + "你成功为团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "设定了传送点！");
                return true;
            }
        }
    }

    public boolean enforcesethome(Player player, Command command, String label, String[] args) {
        if (!player.hasPermission("teamplugin.op")) {
            player.sendMessage(String.valueOf(ChatColor.DARK_RED) + "你没有权限执行这个命令！");
            return true;
        } else {
            String teamName1 = args[1];
            String var10001;
            if (!TeamManager.hasTeam(teamName1)) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队不存在！");
                return true;
            } else {
                Team team = TeamManager.getTeam(teamName1);
                if (team == null) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
                    return true;
                } else {
                    Location location = player.getLocation();
                    TeamManager.setHome(team, location);
                    var10001 = String.valueOf(ChatColor.GOLD);
                    player.sendMessage(var10001 + "你成功为团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "设定了传送点！");
                    return true;
                }
            }
        }
    }

    public boolean enforcesetcolor(Player player, Command command, String label, String[] args) {
        if (!player.hasPermission("teamplugin.op")) {
            player.sendMessage(String.valueOf(ChatColor.DARK_RED) + "你没有权限执行这个命令！");
            return true;
        } else {
            String teamName1 = args[1];
            String var10001;
            if (!TeamManager.hasTeam(teamName1)) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队不存在！");
                return true;
            } else {
                Team team = TeamManager.getTeam(teamName1);
                if (team == null) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
                    return true;
                } else {
                    String colorCode = args[2];
                    TeamManager.setColor(team, colorCode);
                    player.sendMessage(String.valueOf(ChatColor.GOLD) + "``经验等级-3");
                    var10001 = String.valueOf(ChatColor.GOLD);
                    player.sendMessage(var10001 + "你成功选择了团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "的颜色为" + MyUtil.msgColor(team.color) + colorCode + String.valueOf(ChatColor.GOLD) + "！");
                    return true;
                }
            }
        }
    }

    public boolean home(Player player, Command command, String label, String[] args) {
        Team team = TeamManager.getTeamByPlayer(player);
        String var10001;
        if (team == null) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
            return true;
        } else if (!TeamManager.isMember(team, player) && !team.isPresident(player) && !team.isVicePresident(player)) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "只有团队成员才能传送到团队传送点！");
            return true;
        } else {
            Location location = team.getHome();
            if (location == null) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你的团队还没有设定传送点！请让团队的队长使用 /team sethome 来设定传送点");
                return true;
            } else {
                MyUtil.teleport(player, location);
                var10001 = String.valueOf(ChatColor.GOLD);
                player.sendMessage(var10001 + "你成功传送到了团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "的传送点！");
                return true;
            }
        }
    }

    public boolean tp(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team tp <团队名>");
            return true;
        } else if (!player.hasPermission("teamplugin.op")) {
            player.sendMessage(String.valueOf(ChatColor.DARK_RED) + "你没有权限执行这个命令！");
            return true;
        } else {
            String teamName = args[1];
            String var10001;
            if (!TeamManager.hasTeam(teamName)) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队不存在！");
                return true;
            } else {
                Team team = TeamManager.getTeam(teamName);
                Location location = team.getHome();
                if (location == null) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队还没有设定传送点！请让团队的队长使用 /team sethome 来设定传送点");
                    return true;
                } else {
                    MyUtil.teleport(player, location);
                    var10001 = String.valueOf(ChatColor.GOLD);
                    player.sendMessage(var10001 + "你成功传送到了团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "的传送点！");
                    return true;
                }
            }
        }
    }

    public boolean invite(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team invite <玩家>");
            return true;
        } else {
            String targetName = args[1];
            Player target = Bukkit.getPlayer(targetName);
            String var10001;
            if (target == null) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你邀请的玩家不在线！");
                return true;
            } else {
                int playTime = target.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000;
                if (playTime < 24) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你邀请的玩家的总游玩时长不足24小时，无法邀请他加入团队！");
                    return true;
                } else {
                    Team team = TeamManager.getTeamByPlayer(player);
                    if (team == null) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你还没有拥有团队，请先使用/team new <名称>创建一个团队！");
                        return true;
                    } else if (!team.isPresident(player) & !team.isVicePresident(player)) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "只有队长、副手才能邀请玩家！");
                        return true;
                    } else if (target.equals(player)) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "NONONONO不能邀请自己！");
                        return true;
                    } else if (TeamManager.getTeamByPlayer(target) != null) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个玩家已经在一个团队中了！");
                        return true;
                    } else {
                        player.playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                        var10001 = String.valueOf(ChatColor.GOLD);
                        target.sendMessage(var10001 + "你收到了来自" + player.getName() + "的团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "的邀请，接受请输入/team accept，拒绝请输入/team reject。");
                        var10001 = String.valueOf(ChatColor.GOLD);
                        player.sendMessage(var10001 + "你成功邀请了" + target.getName() + "加入你的团队！请等待" + target.getName() + "的回复!");
                        this.invitations.put(target, team);
                        return true;
                    }
                }
            }
        }
    }

    public boolean kick(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team kick <玩家>");
            return true;
        } else {
            Team team = TeamManager.getTeamByPlayer(player);
            String var10001;
            if (team == null) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
                return true;
            } else if (!team.isPresident(player) & !team.isVicePresident(player)) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "只有队长、副手才能剔除玩家！");
                return true;
            } else {
                String targetName = args[1];
                MCPlayer userByName = ObjectPool.mcPlayerMapper.get_user_by_name(targetName);
                if (userByName != null && userByName.inTeam(team)) {
                    if (targetName.equals(player.getName().toLowerCase())) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不能剔除自己！");
                        return true;
                    } else {
                        TeamManager.kickMember(userByName.uuid);
                        if (Bukkit.getPlayer(userByName.name) != null) {
                            Player var10000 = Bukkit.getPlayer(userByName.name);
                            var10001 = String.valueOf(ChatColor.DARK_RED);
                            var10000.sendMessage(var10001 + "你被" + player.getName() + "从团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.DARK_RED) + "中剔除了！");
                        }

                        var10001 = String.valueOf(ChatColor.GOLD);
                        player.sendMessage(var10001 + "你成功剔除了" + userByName.name + "从你的团队！");
                        return true;
                    }
                } else {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个玩家不在你的团队中！");
                    return true;
                }
            }
        }
    }

    public boolean setVicePresident(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team setdeputy <玩家>");
            return true;
        } else {
            Team team = TeamManager.getTeamByPlayer(player);
            String var10001;
            if (team == null) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
                return true;
            } else if (!team.isPresident(player)) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "只有队长才能设置团队副手！");
                return true;
            } else {
                String targetName = args[1];
                MCPlayer userByName = ObjectPool.mcPlayerMapper.get_user_by_name(targetName);
                if (userByName != null && userByName.inTeam(team)) {
                    if (targetName.equals(player.getName().toLowerCase())) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不能将自己设置为团队副手！");
                        return true;
                    } else {
                        TeamManager.setVicePresident(team, userByName.uuid);
                        if (Bukkit.getPlayer(userByName.name) != null) {
                            Player var10000 = Bukkit.getPlayer(userByName.name);
                            var10001 = String.valueOf(ChatColor.GOLD);
                            var10000.sendMessage(var10001 + "你被" + player.getName() + "设置为团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "的副手！");
                        }

                        var10001 = String.valueOf(ChatColor.GOLD);
                        player.sendMessage(var10001 + "你成功将" + userByName.name + "设置为你的团队副手！");
                        return true;
                    }
                } else {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个玩家不在你的团队中！");
                    return true;
                }
            }
        }
    }

    public boolean unsetVicePresident(Player player, Command command, String label, String[] args) {
        Team team = TeamManager.getTeamByPlayer(player);
        String var10001;
        if (team == null) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
            return true;
        } else if (!team.isPresident(player) && !team.isVicePresident(player)) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "只有队长以及副手本人才能取消团队副手！");
            return true;
        } else {
            String fushouName = ObjectPool.mcPlayerMapper.get_user_by_uuid(team.vice_president).name;
            TeamManager.unsetVicePresident(team);
            if (player.getName().toLowerCase().equals(fushouName)) {
                var10001 = String.valueOf(ChatColor.GOLD);
                player.sendMessage(var10001 + "你不再担任团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "的副手！");
            } else {
                player.sendMessage(fushouName + String.valueOf(ChatColor.GOLD) + "不再担任你的团队副手");
            }

            return true;
        }
    }

    public boolean rename(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team rename <新名称>");
            return true;
        } else {
            Team team = TeamManager.getTeamByPlayer(player);
            String var10001;
            if (team == null) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
                return true;
            } else if (!team.isPresident(player)) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "只有队长才能更改团队名！");
                return true;
            } else {
                String newName = args[1];
                if (!newName.matches("[a-zA-Z0-9_一-龥]+")) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "团队名称只能包含字母、数字、下划线和中文！");
                    return true;
                } else {
                    int length = 0;
                    char[] var8 = newName.toCharArray();
                    int var9 = var8.length;

                    for(int var10 = 0; var10 < var9; ++var10) {
                        char c = var8[var10];
                        if (c >= 19968 && c <= '龥') {
                            length += 2;
                        } else {
                            ++length;
                        }
                    }

                    if (length > 12) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "团队名称不得超过六个汉字（12个字符）！");
                        return true;
                    } else if (TeamManager.hasTeam(newName)) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队名称已经被注册过了！");
                        return true;
                    } else if (player.getLevel() < 100) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你需要至少100级经验才能更改团队名！");
                        return true;
                    } else {
                        player.setLevel(player.getLevel() - 100);
                        String oldName = team.getName();
                        TeamManager.rename(team, newName);
                        player.sendMessage(String.valueOf(ChatColor.GOLD) + "``经验等级-100");
                        var10001 = MyUtil.msgColor(team.color);
                        player.sendMessage("你成功将你的团队名从" + var10001 + oldName + String.valueOf(ChatColor.RESET) + "更改为" + MyUtil.msgColor(team.color) + newName + String.valueOf(ChatColor.RESET) + "!");
                        Iterator var13 = TeamManager.getMembers(team).iterator();

                        while(var13.hasNext()) {
                            MCPlayer mcPlayer = (MCPlayer)var13.next();
                            Player p = Bukkit.getPlayer(mcPlayer.uuid);
                            if (p != null) {
                                var10001 = String.valueOf(ChatColor.GOLD);
                                p.sendMessage(var10001 + "你们的团队名已经被" + player.getName() + "更改为" + newName + "!");
                            }
                        }

                        return true;
                    }
                }
            }
        }
    }

    public boolean list(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "如果要翻页的话: /team list <页码>");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Team> teams = TeamManager.getTeams();
                String var10001;
                if (teams.isEmpty()) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "目前没有任何团队！");
                } else {
                    teams.sort(Comparator.comparingInt((tx) -> {
                        return TeamManager.getSize(tx);
                    }));
                    Collections.reverse(teams);
                    int page = 1;
                    if (args.length > 1) {
                        try {
                            page = Integer.parseInt(args[1]);
                        } catch (NumberFormatException var15) {
                            var10001 = String.valueOf(ChatColor.DARK_RED);
                            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "页码必须是一个整数！");
                        }
                    }

                    int pageSize = 10;
                    int totalPages = (teams.size() - 1) / pageSize + 1;
                    if (page >= 1 && page <= totalPages) {
                        player.sendMessage(String.valueOf(ChatColor.GOLD) + "已注册的团队列表（第" + page + "页/共" + totalPages + "页）：");

                        for (int i = (page - 1) * pageSize; i < page * pageSize && i < teams.size(); ++i) {
                            Team t = (Team) teams.get(i);
                            String listName = t.getName();
                            int size = TeamManager.getSize(t);
                            String leaderName;
                            if (t.hasVicePresident()) {
                                String fushouName = ObjectPool.mcPlayerMapper.get_user_by_uuid(t.vice_president).getName();
                                leaderName = ObjectPool.mcPlayerMapper.get_user_by_uuid(t.president).getName();
                                var10001 = String.valueOf(ChatColor.GRAY);
                                player.sendMessage(var10001 + "- " + String.valueOf(ChatColor.AQUA) + String.valueOf(ChatColor.BOLD) + listName);
                                player.sendMessage(String.valueOf(ChatColor.GRAY) + "      [" + size + "人] 队长: " + leaderName + "  副手：" + fushouName);
                            } else if (t.hasPresident()) {
                                leaderName = ObjectPool.mcPlayerMapper.get_user_by_uuid(t.president).getName();
                                var10001 = String.valueOf(ChatColor.GRAY);
                                player.sendMessage(var10001 + "- " + String.valueOf(ChatColor.AQUA) + String.valueOf(ChatColor.BOLD) + listName);
                                player.sendMessage(String.valueOf(ChatColor.GRAY) + "      [" + size + "人] 队长: " + leaderName);
                            } else {
                                var10001 = String.valueOf(ChatColor.GRAY);
                                player.sendMessage(var10001 + "- " + String.valueOf(ChatColor.AQUA) + String.valueOf(ChatColor.BOLD) + listName);
                            }
                        }
                    } else {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "页码必须在1到" + totalPages + "之间！");

                    }
                }
            }
        }).start();
        return true;
    }

    public boolean members(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team members <队伍名>");
            return true;
        } else {
            String teamName = args[1];
            String var10001;
            if (!TeamManager.hasTeam(teamName)) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队不存在！");
                return true;
            } else {
                Team team = TeamManager.getTeam(teamName);
                StringBuilder members = new StringBuilder();
                String leaderName = null;
                if (team.hasPresident()) {
                    leaderName = ObjectPool.mcPlayerMapper.get_user_by_uuid(team.president).getName();
                    members.append(ChatColor.AQUA).append(leaderName).append("[队长], ").append(ChatColor.GRAY);
                }

                if (team.hasVicePresident()) {
                    String fushouName = ObjectPool.mcPlayerMapper.get_user_by_uuid(team.vice_president).getName();
                    members.append(ChatColor.AQUA).append(fushouName).append("[副手], ").append(ChatColor.GRAY);
                }

                Iterator var12 = TeamManager.getMembers(team).iterator();

                while(var12.hasNext()) {
                    MCPlayer mcPlayer = (MCPlayer)var12.next();
                    String memberName = Bukkit.getOfflinePlayer(mcPlayer.uuid).getName();
                    if (memberName != null) {
                        members.append(memberName).append(", ");
                    }
                }

                var10001 = String.valueOf(ChatColor.GOLD);
                player.sendMessage(var10001 + "团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "的成员列表：");
                var10001 = String.valueOf(ChatColor.GRAY);
                player.sendMessage(var10001 + "- " + String.valueOf(members));
                return true;
            }
        }
    }

    public boolean online(Player player, Command command, String label, String[] args) {
        Team team = TeamManager.getTeamByPlayer(player);
        String var10001;
        if (team == null) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
            return true;
        } else {
            StringBuilder onlineMembers = new StringBuilder();
            String fushouName;
            if (team.hasPresident()) {
                fushouName = ObjectPool.mcPlayerMapper.get_user_by_uuid(team.president).getName();
                if (fushouName != null && Bukkit.getPlayer(fushouName) != null) {
                    onlineMembers.append(ChatColor.AQUA).append(fushouName).append("[队长], ").append(ChatColor.GRAY);
                }
            }

            if (team.hasVicePresident()) {
                fushouName = ObjectPool.mcPlayerMapper.get_user_by_uuid(team.vice_president).getName();
                if (fushouName != null && Bukkit.getPlayer(fushouName) != null) {
                    onlineMembers.append(ChatColor.AQUA).append(fushouName).append("[副手], ").append(ChatColor.GRAY);
                }
            }

            Iterator var10 = TeamManager.getMembers(team).iterator();

            while(var10.hasNext()) {
                MCPlayer mcPlayer = (MCPlayer)var10.next();
                String memberName = Bukkit.getOfflinePlayer(mcPlayer.uuid).getName();
                if (memberName != null && Bukkit.getPlayer(memberName) != null) {
                    onlineMembers.append(memberName).append(", ");
                }
            }

            var10001 = String.valueOf(ChatColor.GOLD);
            player.sendMessage(var10001 + "团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "在线的成员列表：");
            var10001 = String.valueOf(ChatColor.GRAY);
            player.sendMessage(var10001 + "- " + onlineMembers.toString());
            return true;
        }
    }

    public boolean accept(Player player, Command command, String label, String[] args) {
        String var10001;
        if (TeamManager.getTeamByPlayer(player) != null) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "哥，你已经有团队了！");
            this.invitations.remove(player);
            return true;
        } else if (!this.invitations.containsKey(player)) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "没有任何团队向你发出邀请！");
            return true;
        } else {
            Team team = (Team)this.invitations.get(player);
            if (!TeamManager.hasTeam(team.getName())) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队已经不存在了！");
                this.invitations.remove(player);
                return true;
            } else {
                int playTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000;
                if (playTime < 24) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你的总游玩时长不足24小时，无法加入团队！");
                    return true;
                } else {
                    TeamManager.addMember(team, player.getUniqueId());
                    this.invitations.remove(player, team);
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    var10001 = String.valueOf(ChatColor.GOLD);
                    player.sendMessage(var10001 + "你成功加入了团队" + MyUtil.msgColor(team.color) + team.getName());
                    Iterator var7 = TeamManager.getMembers(team).iterator();

                    while(var7.hasNext()) {
                        MCPlayer mcPlayer = (MCPlayer)var7.next();
                        Player p = Bukkit.getPlayer(mcPlayer.uuid);
                        if (p != null && !p.equals(player)) {
                            player.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                            var10001 = String.valueOf(ChatColor.GOLD);
                            p.sendMessage(var10001 + player.getName() + "加入了你们的团队！");
                        }
                    }

                    Player fushou1;
                    if (team.hasPresident()) {
                        fushou1 = Bukkit.getPlayer(team.getPresident());
                        if (fushou1 != null) {
                            player.playSound(fushou1, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                            var10001 = String.valueOf(ChatColor.GOLD);
                            fushou1.sendMessage(var10001 + player.getName() + "加入了你的团队！");
                        }
                    }

                    if (team.hasVicePresident()) {
                        fushou1 = Bukkit.getPlayer(team.getVicePresident());
                        if (fushou1 != null) {
                            player.playSound(fushou1, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                            var10001 = String.valueOf(ChatColor.GOLD);
                            fushou1.sendMessage(var10001 + player.getName() + "加入了你的团队！");
                        }
                    }

                    return true;
                }
            }
        }
    }

    public boolean reject(Player player, Command command, String label, String[] args) {
        String var10001;
        if (TeamManager.getTeamByPlayer(player) != null) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "哥，你已经有团队了！");
            this.invitations.remove(player);
            return true;
        } else if (!this.invitations.containsKey(player)) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你没有收到任何团队邀请!");
            return true;
        } else {
            Team team = (Team)this.invitations.get(player);
            if (!TeamManager.hasTeam(team.getName())) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队已经不存在了！");
                this.invitations.remove(player);
                return true;
            } else {
                this.invitations.remove(player);
                var10001 = MyUtil.msgColor(team.color);
                player.sendMessage("你拒绝了来自" + var10001 + team.getName() + String.valueOf(ChatColor.GOLD) + "的团队邀请！");
                Player fushou1;
                if (team.hasPresident()) {
                    fushou1 = Bukkit.getPlayer(team.getPresident());
                    if (fushou1 != null) {
                        var10001 = String.valueOf(ChatColor.GOLD);
                        fushou1.sendMessage(var10001 + player.getName() + "加入了你的团队！");
                    }
                }

                if (team.hasVicePresident()) {
                    fushou1 = Bukkit.getPlayer(team.getVicePresident());
                    if (fushou1 != null) {
                        var10001 = String.valueOf(ChatColor.GOLD);
                        fushou1.sendMessage(var10001 + player.getName() + "加入了你的团队！");
                    }
                }

                return true;
            }
        }
    }

    public boolean enforcerename(Player player, Command command, String label, String[] args) {
        if (!player.hasPermission("teamplugin.op")) {
            player.sendMessage(String.valueOf(ChatColor.DARK_RED) + "你没有权限执行这个命令！");
            return true;
        } else if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team rename <新名称>");
            return true;
        } else {
            String teamName1 = args[1];
            String var10001;
            if (!TeamManager.hasTeam(teamName1)) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队不存在！");
                return true;
            } else {
                Team team = TeamManager.getTeam(teamName1);
                String newName = args[2];
                if (!newName.matches("[a-zA-Z0-9_一-龥]+")) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "团队名称只能包含字母、数字、下划线和中文！");
                    return true;
                } else {
                    int length = 0;
                    char[] var9 = newName.toCharArray();
                    int var10 = var9.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                        char c = var9[var11];
                        if (c >= 19968 && c <= '龥') {
                            length += 2;
                        } else {
                            ++length;
                        }
                    }

                    if (length > 12) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "团队名称不得超过六个汉字（12个字符）！");
                        return true;
                    } else if (TeamManager.hasTeam(newName)) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "这个团队名称已经被注册过了！");
                        return true;
                    } else {
                        String oldName = team.getName();
                        TeamManager.rename(team, newName);
                        var10001 = MyUtil.msgColor(team.color);
                        player.sendMessage("你成功将你的团队名从" + var10001 + oldName + String.valueOf(ChatColor.RESET) + "更改为" + MyUtil.msgColor(team.color) + newName + String.valueOf(ChatColor.RESET) + "!");
                        Iterator var14 = TeamManager.getMembers(team).iterator();

                        while(var14.hasNext()) {
                            MCPlayer mcPlayer = (MCPlayer)var14.next();
                            Player p = Bukkit.getPlayer(mcPlayer.uuid);
                            if (p != null) {
                                var10001 = String.valueOf(ChatColor.GOLD);
                                p.sendMessage(var10001 + "你们的团队名已经被" + player.getName() + "更改为" + newName + "!");
                            }
                        }

                        return true;
                    }
                }
            }
        }
    }

    public boolean quit(Player player, Command command, String label, String[] args) {
        Team team = TeamManager.getTeamByPlayer(player);
        String var10001;
        if (team == null) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！大哥你没有团队咋退出啊！？");
            return true;
        } else if (team.isPresident(player)) {
            var10001 = String.valueOf(ChatColor.DARK_RED);
            player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "哼！想逃？如果你想解散团队，请使用/team del <名称>命令！");
            return true;
        } else {
            if (team.isVicePresident(player)) {
                TeamManager.unsetVicePresident(team);
            }

            TeamManager.kickMember(player.getUniqueId());
            var10001 = String.valueOf(ChatColor.GOLD);
            player.sendMessage(var10001 + "你成功退出了团队" + MyUtil.msgColor(team.color) + team.getName() + String.valueOf(ChatColor.GOLD) + "！");
            Iterator var6 = TeamManager.getMembers(team).iterator();

            while(var6.hasNext()) {
                MCPlayer mcPlayer = (MCPlayer)var6.next();
                Player p = Bukkit.getPlayer(mcPlayer.uuid);
                if (p != null) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    p.sendMessage(var10001 + "很遗憾" + player.getName() + "退出了你们的团队！");
                }
            }

            Player fushou1;
            if (team.hasPresident()) {
                fushou1 = Bukkit.getPlayer(team.getPresident());
                if (fushou1 != null) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    fushou1.sendMessage(var10001 + "很遗憾" + player.getName() + "退出了你们的团队！");
                }
            }

            if (team.hasVicePresident()) {
                fushou1 = Bukkit.getPlayer(team.getVicePresident());
                if (fushou1 != null) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    fushou1.sendMessage(var10001 + "很遗憾" + player.getName() + "退出了你们的团队！");
                }
            }

            return true;
        }
    }

    public boolean qq(Player player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(String.valueOf(ChatColor.YELLOW) + "用法: /team qq <qq号>");
            return true;
        } else {
            Team team = TeamManager.getTeamByPlayer(player);
            String var10001;
            if (team == null) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "你不在一个团队中！");
                return true;
            } else if (!team.isPresident(player) && !team.isVicePresident(player)) {
                var10001 = String.valueOf(ChatColor.DARK_RED);
                player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "只有队长和副手才能设置团队qq！");
                return true;
            } else {
                int teamSize = TeamManager.getSize(team);
                if (teamSize < 5 && !ObjectPool.pluginConfig.debug) {
                    var10001 = String.valueOf(ChatColor.DARK_RED);
                    player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "团队规模小于5人设置QQ！");
                    return true;
                } else {
                    String qq = args[1];
                    Pattern qqPattern = Pattern.compile("^[0-9]{5,11}$");
                    Matcher matcher = qqPattern.matcher(qq);
                    if (!matcher.find()) {
                        var10001 = String.valueOf(ChatColor.DARK_RED);
                        player.sendMessage(var10001 + "错误：" + String.valueOf(ChatColor.GOLD) + "qq格式不正确！");
                        return true;
                    } else {
                        TeamManager.setQQ(team, qq);
                        var10001 = MyUtil.msgColor(team.color);
                        player.sendMessage("你成功将你的团队的qq设置为" + var10001 + qq + String.valueOf(ChatColor.RESET) + "!");
                        return true;
                    }
                }
            }
        }
    }

    public boolean reload(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("只有控制台才能使用该命令");
            return true;
        } else {
            PluginConfig.reload();
            String var10001;
            if (ObjectPool.plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                (new TeampluginExpansion(ObjectPool.plugin)).register();
                ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
                var10001 = String.valueOf(ChatColor.DARK_AQUA);
                var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.GREEN) + "PlaceholderAPI 准备就绪");
            }

            var10001 = String.valueOf(ChatColor.DARK_AQUA);
            sender.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.GREEN) + "配置更新成功");
            return true;
        }
    }
}
