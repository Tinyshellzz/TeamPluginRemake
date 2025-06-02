//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import tcc.youajing.teamplugin.ObjectPool;
import tcc.youajing.teamplugin.entities.MyLocation;
import tcc.youajing.teamplugin.entities.Team;

public class TeamMapper {
    public TeamMapper() {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS teams (name Varchar(48),president Char(36),vice_president Char(36),qq Char(12),home Varchar(128),color Char(7),abbr Char(16),visit Varchar(128),UNIQUE KEY (name),KEY (president),KEY (vice_president),KEY (qq),KEY (abbr),KEY (visit)) ENGINE=InnoDB CHARACTER SET=utf8;");
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "TeamMapper.TeamMapper:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException var12) {
            }

        }
    }

    public Team get_team_by_name(String name) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        Team team = null;

        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM teams WHERE name=?");
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if (rs.next()) {
                team = new Team(rs.getString(1), UUID.fromString(rs.getString(2)), rs.getString(3) == null ? null : UUID.fromString(rs.getString(3)), rs.getString(4), rs.getString(5) == null ? null : (MyLocation)ObjectPool.gson.fromJson(rs.getString(5), MyLocation.class), rs.getString(6), rs.getString(7), rs.getString(8) == null ? null : (MyLocation)ObjectPool.gson.fromJson(rs.getString(8), MyLocation.class));
            }
        } catch (SQLException var15) {
            SQLException e = var15;
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "TeamMapper.get_team_by_name:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (rs != null) {
                    rs.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var14) {
            }

        }

        return team;
    }

    public List<Team> get_teams() {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        List<Team> teams = new ArrayList();

        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT * FROM teams");
            rs = stmt.executeQuery();

            while(rs.next()) {
                Team team = new Team(rs.getString(1), UUID.fromString(rs.getString(2)), rs.getString(3) == null ? null : UUID.fromString(rs.getString(3)), rs.getString(4), rs.getString(5) == null ? null : (MyLocation)ObjectPool.gson.fromJson(rs.getString(5), MyLocation.class), rs.getString(6), rs.getString(7), rs.getString(8) == null ? null : (MyLocation)ObjectPool.gson.fromJson(rs.getString(8), MyLocation.class));
                teams.add(team);
            }
        } catch (SQLException var14) {
            SQLException e = var14;
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "TeamMapper.get_team_by_name:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (rs != null) {
                    rs.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var13) {
            }

        }

        return teams;
    }

    public boolean exists(String name) {
        return this.get_team_by_name(name) != null;
    }

    public void insert(Team team) {
        if (!this.exists(team.name)) {
            PreparedStatement stmt = null;
            Connection conn = null;
            ResultSet rs = null;

            try {
                conn = MysqlConfig.connect();
                stmt = conn.prepareStatement("INSERT INTO teams VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                stmt.setString(1, team.name);
                stmt.setString(2, team.president.toString());
                stmt.setString(3, team.vice_president == null ? null : team.vice_president.toString());
                stmt.setString(4, team.qq);
                stmt.setString(5, team.home == null ? null : ObjectPool.gson.toJson(team.home));
                stmt.setString(6, team.color);
                stmt.setString(7, team.abbr);
                stmt.setString(8, team.visit == null ? null : ObjectPool.gson.toJson(team.visit));
                stmt.executeUpdate();
                conn.commit();
            } catch (SQLException var14) {
                SQLException e = var14;
                ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
                String var10001 = String.valueOf(ChatColor.RED);
                var10000.sendMessage(var10001 + "TeamMapper.insert_team:" + e.getMessage());
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }

                    if (rs != null) {
                        ((ResultSet)rs).close();
                    }

                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException var13) {
                }

            }

        }
    }

    public void update_color_by_name(String name, String color) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE teams SET color=? WHERE name=?");
            stmt.setString(1, color);
            stmt.setString(2, name);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException var15) {
            SQLException e = var15;
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "TeamMapper.update_color_by_name:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (rs != null) {
                    ((ResultSet)rs).close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var14) {
            }

        }

    }

    public void delete_team_by_name(String name) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("DELETE FROM teams WHERE name=?");
            stmt.setString(1, name);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException var14) {
            SQLException e = var14;
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "TeamMapper.delete_team_by_name:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (rs != null) {
                    ((ResultSet)rs).close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var13) {
            }

        }

    }

    public void update_abbr_by_name(String name, String abbr) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE teams SET abbr=? WHERE name=?");
            stmt.setString(1, abbr);
            stmt.setString(2, name);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException var15) {
            SQLException e = var15;
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "TeamMapper.update_abbr_by_name:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (rs != null) {
                    ((ResultSet)rs).close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var14) {
            }

        }

    }

    public void update_home_by_name(String name, MyLocation home) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE teams SET home=? WHERE name=?");
            stmt.setString(1, ObjectPool.gson.toJson(home));
            stmt.setString(2, name);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException var15) {
            SQLException e = var15;
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "TeamMapper.update_home_by_name:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (rs != null) {
                    ((ResultSet)rs).close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var14) {
            }

        }

    }

    public void update_qq_by_name(String name, String qq) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE teams SET qq=? WHERE name=?");
            stmt.setString(1, qq);
            stmt.setString(2, name);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException var15) {
            SQLException e = var15;
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "TeamMapper.update_qq_by_name:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (rs != null) {
                    ((ResultSet)rs).close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var14) {
            }

        }

    }

    public void update_VP_by_name(String name, UUID vicePresident) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE teams SET vice_president=? WHERE name=?");
            stmt.setString(1, vicePresident.toString());
            stmt.setString(2, name);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException var15) {
            SQLException e = var15;
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "TeamMapper.update_VP_by_name:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (rs != null) {
                    ((ResultSet)rs).close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var14) {
            }

        }

    }

    public void update_name_by_name(String name, String newName) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE teams SET name=? WHERE name=?");
            stmt.setString(1, newName);
            stmt.setString(2, name);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException var15) {
            SQLException e = var15;
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "TeamMapper.update_name_by_name:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (rs != null) {
                    ((ResultSet)rs).close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var14) {
            }

        }

    }

    public void update_visit_by_name(String name, MyLocation visit) {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = MysqlConfig.connect();
            stmt = conn.prepareStatement("UPDATE teams SET visit=? WHERE name=?");
            stmt.setString(1, ObjectPool.gson.toJson(visit));
            stmt.setString(2, name);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException var15) {
            SQLException e = var15;
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "TeamMapper.update_visit_by_name:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (rs != null) {
                    ((ResultSet)rs).close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var14) {
            }

        }

    }

    public List<String> get_visit_teamName_list() {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        List<String> teamNames = new ArrayList();

        try {
            conn = MysqlConfig.connect();
            conn.commit();
            stmt = conn.prepareStatement("SELECT name FROM teams WHERE visit IS NOT NULL");
            rs = stmt.executeQuery();

            while(rs.next()) {
                teamNames.add(rs.getString(1));
            }
        } catch (SQLException var14) {
            SQLException e = var14;
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "TeamMapper.get_visit_teamName_list:" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (rs != null) {
                    rs.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var13) {
            }

        }

        return teamNames;
    }
}
