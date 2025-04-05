//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.entities;

import java.util.UUID;
import org.bukkit.Bukkit;
import tcc.youajing.teamplugin.ObjectPool;

public class MCPlayer {
    public String name;
    public UUID uuid;
    public String team;
    public LoginTime login_time;
    public UUID owner;
    public Boolean active;

    public MCPlayer(String name, UUID uuid, String team, LoginTime login_time, UUID owner, Boolean active) {
        this.name = name.toLowerCase();
        this.team = team;
        this.uuid = uuid;
        this.login_time = login_time;
        this.owner = owner;
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isOnline() {
        return Bukkit.getServer().getPlayer(this.name) != null;
    }

    public boolean isSmurf() {
        return this.owner == null;
    }

    public boolean inTeam(Team team) {
        return team.name.equals(this.team);
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return ObjectPool.gson.toJson(this);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getTeam() {
        return this.team;
    }

    public LoginTime getLogin_time() {
        return this.login_time;
    }

    public UUID getOwner() {
        return this.owner;
    }
}
