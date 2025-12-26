package com.bentahsin.bentheventbridge;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class BenthEventBridge extends JavaPlugin {

    private static BenthEventBridge instance;
    private BenthRegistry internalRegistry;

    @Override
    public void onEnable() {
        instance = this;

        long startTime = System.nanoTime();
        printBanner();

        this.internalRegistry = new BenthRegistry(this);

        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;

        getLogger().info(String.format("§a✔ BenthEventBridge başlatıldı. §8[§e%.3fms§8]", durationMs));
    }

    @Override
    public void onDisable() {
        instance = null;
        getLogger().info("BenthEventBridge motoru durduruldu.");
    }

    public static BenthEventBridge getInstance() {
        return instance;
    }

    public BenthRegistry getRegistry() {
        return internalRegistry;
    }

    private void printBanner() {
        getLogger().info("§b   ___           _   _      ___               _   ___       _    _            ");
        getLogger().info("§b  / __| ___ _ _ | |_| |_   | __|_ _____ _ _ _| |_| _ )_ _(_)__| | __ _ ___  ");
        getLogger().info("§b | __|/ -_) ' \\|  _| ' \\  | _|\\ V / -_) ' \\  _| _ \\ '_| / _` |/ _` / -_) ");
        getLogger().info("§b |___/\\___|_||_|\\__|_||_| |___|\\_/\\___|_||_\\__|___/_| |_\\__,_|\\__, \\___| ");
        getLogger().info("§b                                                              |___/        ");
        getLogger().info("§e  >> Version: " + getDescription().getVersion() + " | Native-Speed Event System");
    }
}