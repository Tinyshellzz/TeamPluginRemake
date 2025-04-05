//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.tasks;

import org.bukkit.Bukkit;
import tcc.youajing.teamplugin.ObjectPool;

public class UpdateLoginTime implements Runnable {
    public UpdateLoginTime() {
    }

    public void run() {
        Bukkit.getConsoleSender().sendMessage("TeamPlugin: Updating login time");
        ObjectPool.mcPlayerMapper.update_players();
    }
}
