//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import tcc.youajing.teamplugin.ObjectPool;
import tcc.youajing.teamplugin.entities.MCPlayer;
import tcc.youajing.teamplugin.entities.MyLocation;
import tcc.youajing.teamplugin.entities.Team;

public class TeamManager {
    public static Map<UUID, Team> teams = new HashMap();

    public TeamManager() {
    }

    public static void createTeam(String teamName, Player president) {
        Team team = new Team(teamName, president.getUniqueId());
        ObjectPool.mcPlayerMapper.update_team_by_uuid(president.getUniqueId(), teamName);
        ObjectPool.teamMapper.insert(team);
        teams.put(president.getUniqueId(), team);
    }

    public static int getSize(String teamName) {
        return ObjectPool.mcPlayerMapper.get_team_size(teamName);
    }

    public static int getSize(Team team) {
        return getSize(team.getName());
    }

    public static void setColor(String teamName, String color) {
        ObjectPool.teamMapper.update_color_by_name(teamName, color);
    }

    public static void setColor(Team team, String color) {
        team.color = color;
        setColor(team.name, color);
    }

    public static void setAbbr(String teamName, String abbr) {
        ObjectPool.teamMapper.update_abbr_by_name(teamName, abbr);
        Iterator var2 = teams.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<UUID, Team> entry = (Map.Entry)var2.next();
            Team team = (Team)entry.getValue();
            if (team.name.equals(teamName)) {
                team.abbr = abbr;
            }
        }

    }

    public static void setAbbr(Team team, String abbr) {
        team.abbr = abbr;
        setAbbr(team.name, abbr);
    }

    public static boolean hasTeam(String teamName) {
        return ObjectPool.teamMapper.exists(teamName);
    }

    public static Team getTeam(String teamName) {
        return ObjectPool.teamMapper.get_team_by_name(teamName);
    }

    public static Team getTeamByPlayer(Player player) {
        MCPlayer userByUuid = ObjectPool.mcPlayerMapper.get_user_by_uuid(player.getUniqueId());
        if (userByUuid.team == null) {
            teams.put(player.getUniqueId(), null);
            return null;
        } else {
            Team team = ObjectPool.teamMapper.get_team_by_name(userByUuid.team);
            teams.put(player.getUniqueId(), team);
            return team;
        }
    }

    public static Team getTeamByPlayer2(Player player) {
        if (teams.containsKey(player.getUniqueId())) {
            return (Team)teams.get(player.getUniqueId());
        } else {
            MCPlayer userByUuid = ObjectPool.mcPlayerMapper.get_user_by_uuid(player.getUniqueId());
            if (userByUuid.team == null) {
                teams.put(player.getUniqueId(), null);
                return null;
            } else {
                Team team = ObjectPool.teamMapper.get_team_by_name(userByUuid.team);
                teams.put(player.getUniqueId(), team);
                return team;
            }
        }
    }

    public static void deleteTeam(String teamName) {
        ObjectPool.mcPlayerMapper.delete_team_by_teamName(teamName);
        ObjectPool.teamMapper.delete_team_by_name(teamName);
        Iterator var1 = teams.entrySet().iterator();

        while(var1.hasNext()) {
            Map.Entry<UUID, Team> entry = (Map.Entry)var1.next();
            Team team = (Team)entry.getValue();
            if (team.name.equals(teamName)) {
                teams.remove(entry.getKey());
            }
        }

    }

    public static void setHome(String teamName, Location home) {
        ObjectPool.teamMapper.update_home_by_name(teamName, new MyLocation(home));
    }

    public static void setHome(Team team, Location home) {
        team.home = home;
        setHome(team.name, team.home);
    }

    public static boolean isMember(String teamName, UUID player) {
        MCPlayer userByUuid = ObjectPool.mcPlayerMapper.get_user_by_uuid(player);
        return userByUuid.team != null && Objects.equals(userByUuid.team, teamName);
    }

    public static boolean isMember(Team team, Player player) {
        return isMember(team.name, player.getUniqueId());
    }

    public static void kickMember(UUID uuid) {
        ObjectPool.mcPlayerMapper.update_team_by_uuid(uuid, (String)null);
        teams.remove(uuid);
    }

    public static void addMember(Team team, UUID uuid) {
        ObjectPool.mcPlayerMapper.update_team_by_uuid(uuid, team.name);
        teams.put(uuid, team);
    }

    public static void setVicePresident(Team team, UUID uuid) {
        ObjectPool.teamMapper.update_VP_by_name(team.name, uuid);
    }

    public static void unsetVicePresident(Team team) {
        ObjectPool.teamMapper.update_VP_by_name(team.name, (UUID)null);
    }

    public static void rename(Team team, String newName) {
        ObjectPool.teamMapper.update_name_by_name(team.name, newName);
        ObjectPool.mcPlayerMapper.update_team_by_team(team.name, newName);
        Iterator var2 = teams.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<UUID, Team> entry = (Map.Entry)var2.next();
            Team team2 = (Team)entry.getValue();
            if (Objects.equals(team2.name, team.name)) {
                team2.name = newName;
                teams.put((UUID)entry.getKey(), team2);
            }
        }

    }

    public static List<MCPlayer> getMembers(Team team) {
        return ObjectPool.mcPlayerMapper.get_users_by_team(team.name);
    }

    public static List<Team> getTeams() {
        return ObjectPool.teamMapper.get_teams();
    }

    public static void setQQ(String teamName, String qq) {
        ObjectPool.teamMapper.update_qq_by_name(teamName, qq);
    }

    public static void setQQ(Team team, String qq) {
        setQQ(team.name, qq);
    }

    public static void setVisit(Team team, Location visit) {
        ObjectPool.teamMapper.update_visit_by_name(team.name, new MyLocation(visit));
    }
}
