//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.tasks;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class RunTask {
    public RunTask() {
    }

    public static void run() {
        ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
        String var10001 = String.valueOf(ChatColor.DARK_AQUA);
        var10000.sendMessage(var10001 + "[Team]" + String.valueOf(ChatColor.GREEN) + "RunTask");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime nextRun = now.withHour(0).withMinute(0).withSecond(0);
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1L);
        }

        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new UpdateLoginTime(), initialDelay, TimeUnit.DAYS.toSeconds(1L), TimeUnit.SECONDS);
    }
}
