//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.placeholder;

import java.util.Iterator;
import java.util.List;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tcc.youajing.teamplugin.ObjectPool;
import tcc.youajing.teamplugin.TeamPlugin;
import tcc.youajing.teamplugin.entities.Team;
import tcc.youajing.teamplugin.services.TeamManager;
import tcc.youajing.teamplugin.utils.MyUtil;

public class TeampluginExpansion extends PlaceholderExpansion {
    private final TeamPlugin plugin;

    public TeampluginExpansion(TeamPlugin plugin) {
        this.plugin = plugin;
    }

    public @NotNull String getIdentifier() {
        return "teamPlugin";
    }

    public @NotNull String getAuthor() {
        return this.plugin.getDescription().getAuthors().toString();
    }

    public @NotNull String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return null;
        } else {
            Team team = TeamManager.getTeamByPlayer2(player);
            switch (identifier) {
                case "name":
                    if (team == null) {
                        return "<#FFFFFF>";
                    }

                    return team.getName();
                case "leader":
                    if (team == null) {
                        return "队伍不存在";
                    }

                    return TeamManager.get_name_by_uuid(team.getPresident());
                case "color":
                    if (team == null) {
                        return "<#FFFFFF>";
                    } else {
                        if (team.getColor() == null) {
                            return "<#FFFFFF>";
                        }

                        return "<" + team.getColor() + ">";
                    }
                case "color2":
                    if (team == null) {
                        return "<#FFFFFF>";
                    } else {
                        if (team.getColor() == null) {
                            return "<#FFFFFF>";
                        }

                        return MyUtil.msgColor(team.getColor());
                    }
                case "playtime":
                    return String.valueOf(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000);
                case "elytra":
                    return String.valueOf(player.getStatistic(Statistic.AVIATE_ONE_CM) / 100000);
                case "name4tab":
                    if (team == null) {
                        return "<#FFFFFF>";
                    }

                    String var10000 = team.getColor();
                    return "&3&l&o·<" + var10000 + ">" + team.getName();
                case "name4chat":
                    if (team == null) {
                        return "<#FFFFFF>";
                    }

                    return "『" + team.getName() + "』";
                case "abbr":
                    if (team == null) {
                        return "<#FFFFFF>";
                    } else {
                        if (team.getAbbr() == null) {
                            return "<#FFFFFF>";
                        }

                        return "『" + team.getAbbr() + "』";
                    }
                default:
                    List<Team> teams = ObjectPool.teamMapper.get_teams();
                    Iterator var7 = teams.iterator();

                    Team t;
                    do {
                        if (!var7.hasNext()) {
                            return null;
                        }

                        t = (Team)var7.next();
                        if (identifier.equalsIgnoreCase(t.getName() + "_leaders")) {
                            StringBuilder msg = new StringBuilder("队长: " + TeamManager.get_name_by_uuid(t.getPresident()));
                            if (t.getVicePresident() != null) {
                                msg.append("副队: ").append(TeamManager.get_name_by_uuid(t.getVicePresident()));
                            }

                            return msg.toString();
                        }
                    } while(!identifier.equalsIgnoreCase(t.getName() + "_leader"));

                    return TeamManager.get_name_by_uuid(t.getPresident());
            }
        }
    }
}
