package tcc.youajing.teamplugin.services;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import tcc.youajing.teamplugin.database.MCPlayerMapper;
import tcc.youajing.teamplugin.database.TeamMapper;
import tcc.youajing.teamplugin.entities.MCPlayer;
import tcc.youajing.teamplugin.entities.MyLocation;
import tcc.youajing.teamplugin.entities.Team;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static tcc.youajing.teamplugin.ObjectPool.mcPlayerMapper;
import static tcc.youajing.teamplugin.ObjectPool.teamMapper;

public class TeamManager {
    public static void createTeam(String teamName, Player president) {
        Team team = new Team(teamName, president.getUniqueId());

        mcPlayerMapper.update_team_by_uuid(president.getUniqueId(), teamName);
        teamMapper.insert(team);
    }

    public static int getSize(String teamName) {
        return mcPlayerMapper.get_team_size(teamName);
    }

    public static int getSize(Team team) {
        return getSize(team.getName());
    }

    public static void setColor(String teamName, String color) {
        teamMapper.update_color_by_name(teamName, color);
    }

    public static void setColor(Team team, String color) {
        team.color = color;
        setColor(team.name, color);
    }

    public static void setAbbr(String teamName, String abbr) {
        teamMapper.update_abbr_by_name(teamName, abbr);
    }

    public static void setAbbr(Team team, String abbr) {
        team.abbr = abbr;
        setAbbr(team.name, abbr);
    }

    public static boolean hasTeam(String teamName) {
        return teamMapper.exists(teamName);
    }

    public static Team getTeam(String teamName) {
        return teamMapper.get_team_by_name(teamName);
    }

    public static Team getTeamByPlayer(Player player) {
        MCPlayer userByUuid = mcPlayerMapper.get_user_by_uuid(player.getUniqueId());
        if (userByUuid.team == null) {
            return null;    // 玩家还没有加入团队
        }

        return teamMapper.get_team_by_name(userByUuid.team);
    }

    public static void deleteTeam(String teamName) {
        mcPlayerMapper.delete_team_by_teamName(teamName);
        teamMapper.delete_team_by_name(teamName);
    }

    public static void setHome(String teamName, Location home) {
        teamMapper.update_home_by_name(teamName, new MyLocation(home));
    }

    public static void setHome(Team team, Location home) {
        team.home = home;
        setHome(team.name, team.home);
    }

    public static boolean isMember(String teamName, UUID player) {
        MCPlayer userByUuid = mcPlayerMapper.get_user_by_uuid(player);
        return userByUuid.team != null && Objects.equals(userByUuid.team, teamName);
    }

    public static boolean isMember(Team team, Player player) {
        return isMember(team.name, player.getUniqueId());
    }

    public static void kickMember(UUID uuid) {
        mcPlayerMapper.update_team_by_uuid(uuid, null);
    }

    public static void addMember(Team team, UUID uuid) {
        mcPlayerMapper.update_team_by_uuid(uuid, team.name);
    }

    public static void setVicePresident(Team team, UUID uuid) {
        teamMapper.update_VP_by_name(team.name, uuid);
    }

    public static void unsetVicePresident(Team team) {
        teamMapper.update_VP_by_name(team.name, null);
    }

    public static void rename(Team team, String newName) {
        teamMapper.update_name_by_name(team.name, newName);
        mcPlayerMapper.update_team_by_team(team.name, newName);
    }

    public static List<MCPlayer> getMembers(Team team) {
        return mcPlayerMapper.get_users_by_team(team.name);
    }

    public static List<Team> getTeams() {
        return teamMapper.get_teams();
    }

    public static void setQQ(String teamName, String qq) {
        teamMapper.update_qq_by_name(teamName, qq);
    }

    public static void setQQ(Team team, String qq) {setQQ(team.name, qq);}
}
