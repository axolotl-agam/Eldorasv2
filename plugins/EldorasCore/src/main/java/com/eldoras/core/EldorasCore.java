package com.eldoras.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class EldorasCore extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("EldorasCore enabled. MMORPG modules are being built incrementally.");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("eldoras")) return false;
        if (!sender.hasPermission("eldoras.admin")) {
            sender.sendMessage("Bu komutu kullanma yetkin yok.");
            return true;
        }
        sender.sendMessage("EldorasCore v" + getPluginMeta().getVersion() + " aktif.");
        sender.sendMessage("Seviye sınırı: " + getConfig().getInt("settings.max-level", 120));
        return true;
    }
}
