package com.example.heightlimit;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class HeightLimitPlugin extends JavaPlugin {

    private int maxBuildHeight;
    private boolean creativeOnly;
    private boolean showWarning;
    private String warningMessage;

    @Override
    public void onEnable() {
        // config.yml を保存（なければデフォルトを生成）
        saveDefaultConfig();
        loadConfig();

        // リスナー登録
        getServer().getPluginManager().registerEvents(new HeightLimitListener(this), this);

        getLogger().info("HeightLimitPlugin が有効になりました！最大高度: " + maxBuildHeight);
    }

    @Override
    public void onDisable() {
        getLogger().info("HeightLimitPlugin が無効になりました。");
    }

    public void loadConfig() {
        reloadConfig();
        maxBuildHeight = getConfig().getInt("max-build-height", 1000);
        creativeOnly   = getConfig().getBoolean("creative-only", false);
        showWarning    = getConfig().getBoolean("show-warning-message", true);
        warningMessage = getConfig().getString("warning-message",
                "§c高度 %height% はサーバーの制限（" + maxBuildHeight + "）を超えています！");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("heightlimit")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("heightlimit.bypass")) {
                    sender.sendMessage(ChatColor.RED + "権限がありません。");
                    return true;
                }
                loadConfig();
                sender.sendMessage(ChatColor.GREEN + "設定をリロードしました！最大高度: " + maxBuildHeight);
                return true;
            }

            String info = ChatColor.GOLD + "=== HeightLimitPlugin ===" + "\n" +
                    ChatColor.YELLOW + "最大建築高度: " + ChatColor.WHITE + maxBuildHeight + "\n" +
                    ChatColor.YELLOW + "クリエイティブ専用: " + ChatColor.WHITE + (creativeOnly ? "はい" : "いいえ");

            if (sender instanceof Player player) {
                int currentY = player.getLocation().getBlockY();
                info += "\n" + ChatColor.YELLOW + "現在の高度: " + ChatColor.WHITE + currentY;
            }

            sender.sendMessage(info);
            return true;
        }
        return false;
    }

    // ── Getter ──────────────────────────────────────────
    public int getMaxBuildHeight()  { return maxBuildHeight; }
    public boolean isCreativeOnly() { return creativeOnly; }
    public boolean isShowWarning()  { return showWarning; }
    public String getWarningMessage() { return warningMessage; }
}
