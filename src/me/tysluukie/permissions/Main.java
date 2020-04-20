package me.tysluukie.permissions;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;
    public HashMap<UUID, PermissionAttachment> playerPermissions = new HashMap<>();

    public void onEnable() {
        plugin = this;
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
        playerPermissions.clear();
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        setupPermissions(player);
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerPermissions.remove(player.getUniqueId());
    }

    public void setupPermissions(Player player) {
        PermissionAttachment attachment = player.addAttachment(this);
        this.playerPermissions.put(player.getUniqueId(), attachment);
        permissionsSetter(player.getUniqueId());
    }

    private void permissionsSetter(UUID uuid) {
        PermissionAttachment attachment = this.playerPermissions.get(uuid);
        for (String groups : this.getConfig().getConfigurationSection("Groups").getKeys(false)) {
            for (String permissions : this.getConfig().getStringList("Groups." + groups + ".permissions")) {
                System.out.print(permissions);
                attachment.setPermission(permissions, true);
            }
        }
    }


}