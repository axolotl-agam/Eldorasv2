package com.eldoras.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class EldorasCore extends JavaPlugin implements Listener {
    private final Map<UUID, PlayerProgress> progress = new HashMap<>();
    private File playerFolder;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        playerFolder = new File(getDataFolder(), "players");
        if (!playerFolder.exists() && !playerFolder.mkdirs()) {
            getLogger().severe("Could not create player data folder.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("EldorasCore enabled. Level and EXP module is active.");
    }

    @Override
    public void onDisable() {
        for (UUID uuid : progress.keySet().toArray(new UUID[0])) saveProgress(uuid);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerProgress data = loadProgress(player.getUniqueId());
        progress.put(player.getUniqueId(), data);
        player.sendMessage(ChatColor.GOLD + "Eldoras " + ChatColor.GRAY + "Seviyen: "
                + ChatColor.YELLOW + data.level + ChatColor.GRAY + " | EXP: "
                + ChatColor.GREEN + data.exp + "/" + expRequired(data.level));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        saveProgress(event.getPlayer().getUniqueId());
        progress.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;
        int reward = getConfig().getInt("experience.mob-default", 10);
        Entity mob = event.getEntity();
        String key = "experience.mob-values." + mob.getType().name();
        reward = getConfig().getInt(key, reward);
        if (reward > 0) addExperience(killer, reward);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("level")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Bu komut oyuncular içindir.");
                return true;
            }
            PlayerProgress data = getProgress(player);
            player.sendMessage(ChatColor.GOLD + "Seviye " + data.level + ChatColor.GRAY
                    + " | EXP: " + ChatColor.GREEN + data.exp + "/" + expRequired(data.level));
            return true;
        }
        if (!command.getName().equalsIgnoreCase("eldoras")) return false;
        if (!sender.hasPermission("eldoras.admin")) {
            sender.sendMessage(ChatColor.RED + "Bu komutu kullanma yetkin yok.");
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("giveexp")) {
            Player target = Bukkit.getPlayerExact(args[1]);
            sender.sendMessage(target == null ? ChatColor.RED + "Oyuncu çevrimiçi değil."
                    : ChatColor.YELLOW + "Kullanım: /eldoras giveexp <oyuncu> <miktar>");
            return true;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("giveexp")) {
            Player target = Bukkit.getPlayerExact(args[1]);
            long amount;
            try { amount = Long.parseLong(args[2]); }
            catch (NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + "Miktar sayı olmalı."); return true;
            }
            if (target == null || amount <= 0) {
                sender.sendMessage(ChatColor.RED + "Çevrimiçi oyuncu ve pozitif miktar gerekli."); return true;
            }
            addExperience(target, amount);
            sender.sendMessage(ChatColor.GREEN + target.getName() + " oyuncusuna " + amount + " EXP verildi.");
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "EldorasCore v" + getPluginMeta().getVersion() + " aktif.");
        sender.sendMessage(ChatColor.GRAY + "Seviye sınırı: " + getMaxLevel());
        sender.sendMessage(ChatColor.GRAY + "Komutlar: /level, /eldoras giveexp <oyuncu> <miktar>");
        return true;
    }

    private PlayerProgress getProgress(Player player) {
        return progress.computeIfAbsent(player.getUniqueId(), this::loadProgress);
    }

    private int getMaxLevel() { return Math.max(1, getConfig().getInt("settings.max-level", 120)); }

    private long expRequired(int level) {
        double base = getConfig().getDouble("experience.base-required", 100.0);
        double growth = getConfig().getDouble("experience.level-growth", 1.18);
        return Math.max(1L, (long) Math.floor(base * Math.pow(growth, Math.max(0, level - 1))));
    }

    private void addExperience(Player player, long amount) {
        PlayerProgress data = getProgress(player);
        if (data.level >= getMaxLevel()) return;
        data.exp = Math.min(Long.MAX_VALUE - amount, data.exp) + amount;
        while (data.level < getMaxLevel() && data.exp >= expRequired(data.level)) {
            data.exp -= expRequired(data.level);
            data.level++;
            int points = getConfig().getInt("settings.skill-points-per-level", 1);
            data.skillPoints += Math.max(0, points);
            player.sendMessage(ChatColor.GOLD + "SEVİYE ATLADIN! Yeni seviyen: " + data.level
                    + ChatColor.YELLOW + " (+" + points + " yetenek puanı)");
        }
        if (data.level >= getMaxLevel()) data.exp = 0;
        saveProgress(player.getUniqueId());
    }

    private PlayerProgress loadProgress(UUID uuid) {
        File file = new File(playerFolder, uuid + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        PlayerProgress data = new PlayerProgress();
        data.level = Math.min(getMaxLevel(), Math.max(1, yaml.getInt("level", 1)));
        data.exp = Math.max(0L, yaml.getLong("exp", 0L));
        data.skillPoints = Math.max(0, yaml.getInt("skill-points", 0));
        while (data.level < getMaxLevel() && data.exp >= expRequired(data.level)) {
            data.exp -= expRequired(data.level);
            data.level++;
            data.skillPoints += Math.max(0, getConfig().getInt("settings.skill-points-per-level", 1));
        }
        return data;
    }

    private void saveProgress(UUID uuid) {
        PlayerProgress data = progress.get(uuid);
        if (data == null || playerFolder == null) return;
        File file = new File(playerFolder, uuid + ".yml");
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("level", data.level);
        yaml.set("exp", data.exp);
        yaml.set("skill-points", data.skillPoints);
        try { yaml.save(file); }
        catch (IOException ex) { getLogger().severe("Could not save player " + uuid + ": " + ex.getMessage()); }
    }

    private static final class PlayerProgress {
        private int level = 1;
        private long exp = 0;
        private int skillPoints = 0;
    }
}
