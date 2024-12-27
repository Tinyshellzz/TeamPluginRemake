package tcc.youajing.teamplugin.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class MyLocation {
    public String world;
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;

    public MyLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
    }
}
