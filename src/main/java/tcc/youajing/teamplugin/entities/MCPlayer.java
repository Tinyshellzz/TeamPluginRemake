package tcc.youajing.teamplugin.entities;

import org.bukkit.Bukkit;

import java.util.UUID;

public class MCPlayer {
    public String name;
    public UUID uuid;
    public String team;
    public LoginTime login_time;      // 上个星期的在线时间，以分钟为单位
    public UUID owner;          // 是否是小号, null则是大号
    public Boolean active;      // 改号在最近两个星期是否活跃

    public MCPlayer(String name, UUID uuid, String team, LoginTime login_time, UUID owner, Boolean active) {
        this.name = name.toLowerCase();
        this.team = team;
        this.uuid = uuid;
        this.login_time = login_time;
        this.owner = owner;
        this.active = active;
    }

    public boolean isActive() {
        return active;   // 上星期在线时间达到x分钟，则算活跃玩家
    }

    public boolean isOnline() {
        return Bukkit.getServer().getPlayer(name) != null;
    }

    public boolean isSmurf() {
        return owner == null;
    }

    public boolean inTeam(String team) {
        return team.equals(this.team);
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTeam() {
        return team;
    }

    public LoginTime getLogin_time() {
        return login_time;
    }

    public UUID getOwner() {
        return owner;
    }
}
