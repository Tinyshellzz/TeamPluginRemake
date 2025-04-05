//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigWrapper {
    private final File file;
    private final String path;
    private final Plugin plugin;
    private YamlConfiguration config;

    public ConfigWrapper(@NotNull Plugin plugin, @NotNull String path) {
        this.path = path;
        File dataFolder = plugin.getDataFolder();
        this.file = new File(dataFolder, path);
        this.plugin = plugin;
        this.saveDefaultConfig();
    }

    public ConfigWrapper(@NotNull File file) {
        this.file = file;
        this.path = file.getPath();
        if (!this.file.exists()) {
            try {
                this.file.getParentFile().mkdirs();
                this.file.createNewFile();
            } catch (IOException var3) {
                IOException e = var3;
                throw new RuntimeException(e);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        this.plugin = null;
    }

    public void saveDefaultConfig() {
        if (!this.file.exists()) {
            try {
                this.plugin.saveResource(this.path, false);
            } catch (NullPointerException | IllegalArgumentException var4) {
                try {
                    this.file.getParentFile().mkdirs();
                    this.file.createNewFile();
                } catch (IOException var3) {
                    IOException e = var3;
                    throw new RuntimeException(e);
                }
            }
        }

        this.reloadConfig();
    }

    public @NotNull YamlConfiguration getConfig() {
        if (this.config == null) {
            this.reloadConfig();
        }

        return this.config;
    }

    public void setConfig(YamlConfiguration config) {
        this.config = config;
    }

    public boolean contains(String key) {
        return this.config.contains(key);
    }

    public void set(@NotNull String key, @Nullable Object object) {
        this.config.set(key, object);
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public synchronized void saveConfig() {
        try {
            String s = this.config.saveToString();
            String s1 = s.replaceAll(" *?!!.*?\n", "\n");
            String content = s1.replaceAll("\n.*?: null\n", "\n");
            Writer writer = new BufferedWriter(new FileWriter(this.file));
            ((Writer)writer).write(content);
            ((Writer)writer).flush();
            ((Writer)writer).close();
        } catch (IOException var5) {
            IOException var2 = var5;
            var2.printStackTrace();
        }

    }

    public @NotNull String getPath() {
        return this.path;
    }

    public @NotNull File getFile() {
        return this.file;
    }
}
