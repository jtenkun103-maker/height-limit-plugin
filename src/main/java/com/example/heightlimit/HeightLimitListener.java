package com.example.heightlimit;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;

public class HeightLimitListener implements Listener {

    private final HeightLimitPlugin plugin;

    public HeightLimitListener(HeightLimitPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * ブロック設置イベント：高度 > maxBuildHeight のとき設置をキャンセル
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        // bypass 権限を持つプレイヤーは制限なし
        if (player.hasPermission("heightlimit.bypass")) return;

        // creative-only モードの場合、クリエイティブ以外はバニラ挙動に任せる
        if (plugin.isCreativeOnly() && player.getGameMode() != GameMode.CREATIVE) return;

        int blockY = event.getBlock().getLocation().getBlockY();

        if (blockY >= plugin.getMaxBuildHeight()) {
            event.setCancelled(true);
            if (plugin.isShowWarning()) {
                String msg = plugin.getWarningMessage()
                        .replace("%height%", String.valueOf(blockY));
                player.sendMessage(msg);
            }
        }
    }

    /**
     * プレイヤー移動イベント：高度 > maxBuildHeight に達したら
     * 少し下に押し戻す（落下ではなく足元でブロックを踏んだとき）
     *
     * ※ Minecraft のワールド上限自体は変えられないため、
     *   Y=320 より上は通常ブロックが存在できません。
     *   このイベントは「建築上限を下げる」用途（例: max=200）に
     *   使うことを想定した拡張ポイントです。
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        // 高度チェックのみ（XZ移動はスキップしてパフォーマンスを節約）
        if (event.getFrom().getBlockY() == event.getTo().getBlockY()) return;

        Player player = event.getPlayer();
        if (player.hasPermission("heightlimit.bypass")) return;

        int currentY = event.getTo().getBlockY();

        // maxBuildHeight を大幅に超えた場合の警告（5ブロック余裕を持たせる）
        if (plugin.isShowWarning() && currentY > plugin.getMaxBuildHeight() + 5) {
            if (System.currentTimeMillis() % 3000 < 50) { // 3秒に1回だけ送信
                String msg = plugin.getWarningMessage()
                        .replace("%height%", String.valueOf(currentY));
                player.sendMessage(msg);
            }
        }
    }
}
