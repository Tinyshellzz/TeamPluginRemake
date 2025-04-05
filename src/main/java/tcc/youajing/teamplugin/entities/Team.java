//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.entities;

import java.util.Objects;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Team {
    public String name;
    public UUID president;
    public UUID vice_president;
    public String qq;
    public Location home;
    public String color = "<#FFFFFF>";
    public String abbr;
    public Location visit;

    public Team(String name, UUID president, UUID vice_president, String qq, Location home, String color, String abbr, Location visit) {
        this.name = name;
        this.president = president;
        this.vice_president = vice_president;
        this.qq = qq;
        this.home = home;
        this.color = color;
        this.abbr = abbr;
        this.visit = visit;
    }

    public Team(String name, UUID president, UUID vice_president, String qq, MyLocation home, String color, String abbr, MyLocation visit) {
        this.name = name;
        this.president = president;
        this.vice_president = vice_president;
        this.qq = qq;
        this.home = home == null ? null : home.toLocation();
        this.color = color;
        this.abbr = abbr;
        this.visit = visit == null ? null : visit.toLocation();
    }

    public Team(String name, UUID president) {
        this.name = name;
        this.president = president;
        this.vice_president = null;
        this.home = null;
        this.color = "#FFFFFF";
        this.abbr = null;
    }

    public boolean isPresident(Player player) {
        return Objects.equals(player.getUniqueId(), this.president);
    }

    public boolean hasPresident() {
        return this.president != null;
    }

    public boolean isVicePresident(Player player) {
        return Objects.equals(player.getUniqueId(), this.vice_president);
    }

    public boolean hasVicePresident() {
        return this.vice_president != null;
    }

    public String getName() {
        return this.name;
    }

    public UUID getPresident() {
        return this.president;
    }

    public UUID getVicePresident() {
        return this.vice_president;
    }

    public Location getHome() {
        return this.home;
    }

    public String getColor() {
        return this.color;
    }

    public String getAbbr() {
        return this.abbr;
    }
}
