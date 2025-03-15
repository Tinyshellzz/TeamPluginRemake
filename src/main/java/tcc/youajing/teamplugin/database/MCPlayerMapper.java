package tcc.youajing.teamplugin.database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import tcc.youajing.teamplugin.entities.LoginTime;
import tcc.youajing.teamplugin.entities.MCPlayer;
import tcc.youajing.teamplugin.utils.MyUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static tcc.youajing.teamplugin.ObjectPool.gson;
import static tcc.youajing.teamplugin.ObjectPool.pluginConfig;

public class MCPlayerMapper {
    public MCPlayerMapper() {

        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS mc_players (" +
                    "name Varchar(48)," +
                    "uuid Char(36)," +
                    "team Varchar(48)," +
                    "login_time Varchar(256)," +
                    "owner Char(36)," +
                    "active Bool," +
                    "KEY (name)," +
                    "UNIQUE KEY (uuid)," +
                    "KEY (team)," +
                    "KEY (login_time)," +
                    "KEY (owner)," +
                    "KEY (active)" +
                    ") ENGINE=InnoDB CHARACTER SET=utf8;");
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MCPlayerMapper.MCPlayerMapper:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public MCPlayer get_user_by_uuid(String mc_uuid) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        MCPlayer player = null;
        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM mc_players WHERE uuid=?");
            stmt.setString(1, mc_uuid);
            rs = stmt.executeQuery();
            if(rs.next()) {
                player =  new MCPlayer(rs.getString(1), UUID.fromString(rs.getString(2)), rs.getString(3), gson.fromJson(rs.getString(4), LoginTime.class), rs.getString(5) == null ? null : UUID.fromString(rs.getString(5)), rs.getBoolean(6));
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MCPlayerMapper.get_user_by_uuid:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }

        return player;
    }

    public List<MCPlayer> get_users_by_team(String teamName) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        List<MCPlayer> players = new ArrayList<MCPlayer>();
        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM mc_players WHERE team=?");
            stmt.setString(1, teamName);
            rs = stmt.executeQuery();
            while(rs.next()) {
                MCPlayer player =  new MCPlayer(rs.getString(1), UUID.fromString(rs.getString(2)), rs.getString(3), gson.fromJson(rs.getString(4), LoginTime.class), rs.getString(5) == null ? null : UUID.fromString(rs.getString(5)), rs.getBoolean(6));
                players.add(player);
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MCPlayerMapper.get_user_by_uuid:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }

        return players;
    }

    public MCPlayer get_user_by_name(String name) {
        name = name.toLowerCase();

        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        MCPlayer player = null;
        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM mc_players WHERE name=?");
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if(rs.next()) {
                player =  new MCPlayer(rs.getString(1), UUID.fromString(rs.getString(2)), rs.getString(3), gson.fromJson(rs.getString(4), LoginTime.class), rs.getString(5) == null ? null : UUID.fromString(rs.getString(5)), rs.getBoolean(6));
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MCPlayerMapper.get_user_by_name:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }

        return player;
    }

    public MCPlayer get_user_by_uuid(UUID uuid) {
        return get_user_by_uuid(uuid.toString());
    }

    public MCPlayer get(Player player) {
        return get_user_by_uuid(player.getUniqueId());
    }

    public boolean exists(Player player) {
        return exists(player.getUniqueId());
    }

    public boolean exists(UUID uuid) {
        return get_user_by_uuid(uuid) != null;
    }

    public void insert(MCPlayer player) {
        // uuid已存在
        if (exists(player.uuid)) {
            // update
            return;
        }

        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("INSERT INTO mc_players VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setString(1, player.name);
            stmt.setString(2, player.uuid.toString());
            stmt.setString(3, player.team == null ? null : player.team);
            stmt.setString(4, gson.toJson(player.login_time));
            stmt.setString(5, player.owner == null ? null : player.owner.toString());
            stmt.setBoolean(6, player.active);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MCPlayerMapper.insert:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void update_team_by_uuid(String uuid, String team) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE mc_players SET team=? WHERE uuid=?");
            stmt.setString(1, team);
            stmt.setString(2, uuid);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "TeamMapper.update_team_by_uuid:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void update_team_by_team(String team, String newTeam) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE mc_players SET team=? WHERE team=?");
            stmt.setString(1, newTeam);
            stmt.setString(2, team);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "TeamMapper.update_team_by_uuid:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }


    public void delete_team_by_teamName(String team) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE mc_players SET team=? WHERE team=?");
            stmt.setString(1, null);
            stmt.setString(2, team);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "TeamMapper.delete_team_by_teamName:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void update_team_by_uuid(UUID uuid, String team) {
        update_team_by_uuid(uuid.toString(), team);
    }

    public Integer get_team_size(String team) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        Integer size = null;
        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM mc_players WHERE team=?");
            stmt.setString(1, team);
            rs = stmt.executeQuery();
            if(rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MCPlayerMapper.get_user_by_uuid:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }

        return size;
    }

    public void update_player(Player player) {
        MCPlayer mcPlayer = get_user_by_uuid(player.getUniqueId());
        int playTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;     // 单位为秒

        if(mcPlayer == null) {
            insert(new MCPlayer(player.getName(), player.getUniqueId(), null, new LoginTime(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, playTime), null, false));
            return;
        }

        if(mcPlayer.login_time.day_14 == -1) {
            mcPlayer.login_time.day_14 = playTime;
        }

        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE mc_players SET name = ?, login_time=? WHERE uuid=?");
            stmt.setString(1, player.getName().toLowerCase());
            stmt.setString(2, gson.toJson(mcPlayer.login_time));
            stmt.setString(3, player.getUniqueId().toString());
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "TeamMapper.update_player:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void update_login_time_by_uuid(UUID uuid, LoginTime login_time, Boolean active) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE mc_players SET login_time=?, active=? WHERE uuid=?");
            stmt.setString(1, gson.toJson(login_time));
            stmt.setBoolean(2, active);
            stmt.setString(3, uuid.toString());
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "TeamMapper.update_login_time_by_uuid:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void update_players() {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        Integer size = null;
        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM mc_players WHERE login_time!=?");
            stmt.setString(1, "{\"day_1\":-1,\"day_2\":-1,\"day_3\":-1,\"day_4\":-1,\"day_5\":-1,\"day_6\":-1,\"day_7\":-1,\"day_8\":-1,\"day_9\":-1,\"day_10\":-1,\"day_11\":-1,\"day_12\":-1,\"day_13\":-1,\"day_14\":-1}");
            rs = stmt.executeQuery();
            while(rs.next()) {
                MCPlayer mcPlayer =  new MCPlayer(rs.getString(1), UUID.fromString(rs.getString(2)), rs.getString(3), gson.fromJson(rs.getString(4), LoginTime.class), rs.getString(5) == null ? null : UUID.fromString(rs.getString(5)), rs.getBoolean(6));
                mcPlayer.login_time.day_1 = mcPlayer.login_time.day_2;
                mcPlayer.login_time.day_2 = mcPlayer.login_time.day_3;
                mcPlayer.login_time.day_3 = mcPlayer.login_time.day_4;
                mcPlayer.login_time.day_4 = mcPlayer.login_time.day_5;
                mcPlayer.login_time.day_5 = mcPlayer.login_time.day_6;
                mcPlayer.login_time.day_6 = mcPlayer.login_time.day_7;
                mcPlayer.login_time.day_7 = mcPlayer.login_time.day_8;
                mcPlayer.login_time.day_8 = mcPlayer.login_time.day_9;
                mcPlayer.login_time.day_9 = mcPlayer.login_time.day_10;
                mcPlayer.login_time.day_10 = mcPlayer.login_time.day_11;
                mcPlayer.login_time.day_11 = mcPlayer.login_time.day_12;
                mcPlayer.login_time.day_12 = mcPlayer.login_time.day_13;
                mcPlayer.login_time.day_13 = mcPlayer.login_time.day_14;
                mcPlayer.login_time.day_14 = -1;

                Player player = Bukkit.getPlayer(mcPlayer.uuid);
                if(player != null) {        // 如果玩家在线上
                    mcPlayer.login_time.day_14 = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
                }

                boolean active = false;
                int last_play_time = -1;
                if(mcPlayer.login_time.day_1 != -1) {
                    last_play_time = mcPlayer.login_time.day_1;
                } else if(mcPlayer.login_time.day_2 != -1) {
                    last_play_time = mcPlayer.login_time.day_2;
                } else if(mcPlayer.login_time.day_3 != -1) {
                    last_play_time = mcPlayer.login_time.day_3;
                } else if(mcPlayer.login_time.day_4 != -1) {
                    last_play_time = mcPlayer.login_time.day_4;
                } else if(mcPlayer.login_time.day_5 != -1) {
                    last_play_time = mcPlayer.login_time.day_5;
                } else if(mcPlayer.login_time.day_6 != -1) {
                    last_play_time = mcPlayer.login_time.day_6;
                } else if (mcPlayer.login_time.day_7 != -1) {
                    last_play_time = mcPlayer.login_time.day_7;
                } else if (mcPlayer.login_time.day_8 != -1) {
                    last_play_time = mcPlayer.login_time.day_8;
                } else if (mcPlayer.login_time.day_9 != -1) {
                    last_play_time = mcPlayer.login_time.day_9;
                } else if (mcPlayer.login_time.day_10 != -1) {
                    last_play_time = mcPlayer.login_time.day_10;
                } else if (mcPlayer.login_time.day_11 != -1) {
                    last_play_time = mcPlayer.login_time.day_11;
                } else if (mcPlayer.login_time.day_12 != -1) {
                    last_play_time = mcPlayer.login_time.day_12;
                } else if (mcPlayer.login_time.day_13 != -1) {
                    last_play_time = mcPlayer.login_time.day_13;
                } else if (mcPlayer.login_time.day_14 != -1) {
                    last_play_time = mcPlayer.login_time.day_14;
                }

                if(last_play_time != -1) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(mcPlayer.uuid);
                    active = offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20 - last_play_time > pluginConfig.active_playtime;
                }

                this.update_login_time_by_uuid(mcPlayer.uuid, mcPlayer.login_time, active);
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MCPlayerMapper.update_players:" + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }
}
