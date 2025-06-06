//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tcc.youajing.teamplugin;

import com.google.gson.Gson;
import java.util.HashMap;
import tcc.youajing.teamplugin.config.PluginConfig;
import tcc.youajing.teamplugin.database.HomeBanListMapper;
import tcc.youajing.teamplugin.database.MCPlayerMapper;
import tcc.youajing.teamplugin.database.TeamMapper;
import tcc.youajing.teamplugin.database.VisitBanListMapper;
import tcc.youajing.teamplugin.services.TeamService;
import tcc.youajing.teamplugin.services.TeamVisitService;

public class ObjectPool {
    public static TeamPlugin plugin;
    public static PluginConfig pluginConfig;
    public static Gson gson = new Gson();
    public static MCPlayerMapper mcPlayerMapper;
    public static VisitBanListMapper visitBanListMapper;
    public static HomeBanListMapper homeBanListMapper;
    public static TeamMapper teamMapper;
    public static TeamService teamService;
    public static TeamVisitService teamVisitService;
    public static HashMap<String, String> colors = new HashMap();

    public ObjectPool() {
    }

    static {
        colors.put("正白", "#FFFFFF");
        colors.put("正黑", "#000000");
        colors.put("正红", "#FF0000");
        colors.put("正蓝", "#0000FF");
        colors.put("正黄", "#FFFF00");
        colors.put("青", "#00FFFF");
        colors.put("紫红", "#FF00FF");
        colors.put("灰", "#808080");
        colors.put("橙", "#FFA500");
        colors.put("棕", "#A52A2A");
        colors.put("粉红", "#FFC0CB");
        colors.put("浅蓝", "#ADD8E6");
        colors.put("浅绿", "#90EE90");
        colors.put("深蓝", "#00008B");
        colors.put("暗绿", "#006400");
        colors.put("暗紫", "#800080");
        colors.put("暗红", "#8B0000");
        colors.put("土黄", "#FFD700");
        colors.put("三色堇紫", "#7400a1");
        colors.put("中岩蓝", "#7b68ee");
        colors.put("中缘松石", "#48d1cc");
        colors.put("亮天蓝", "#87cefa");
        colors.put("亚麻色", "#faf0e6");
        colors.put("酒红", "#470024");
        colors.put("亮青", "#e0ffff");
        colors.put("皇家蓝", "#4169e1");
        colors.put("品红", "#f400a1");
        colors.put("卡其色", "#996b1f");
        colors.put("古铜", "#b87333");
        colors.put("孔雀蓝", "#00808c");
        colors.put("小麦色", "#f5deb3");
        colors.put("黄绿", "#66ff00");
        colors.put("暗橙", "#ff8c00");
        colors.put("暗海绿", "#8fbc8f");
        colors.put("梅紅", "#dda0dd");
        colors.put("水蓝", "#66ffe6");
        colors.put("淡紫丁香", "#e6cfe6");
        colors.put("热带橙", "#ff8033");
        colors.put("灰绿", "#98fb98");
        colors.put("番茄红", "#ff6347");
        colors.put("萨克斯蓝", "#4798b3");
        colors.put("银色", "#c0c0c0");
    }
}
