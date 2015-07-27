package com.plotsquared.bukkit.util.bukkit;

import com.intellectualcrafters.plot.object.OfflinePlotPlayer;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.intellectualcrafters.plot.util.EconHandler;
import com.plotsquared.bukkit.object.BukkitOfflinePlayer;
import com.plotsquared.bukkit.object.BukkitPlayer;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class BukkitEconHandler extends EconHandler {
    
    private Economy econ;
    private Permission perms;
    
    public boolean init() {
        if (econ == null || perms == null) {
            setupPermissions();
            setupEconomy();
        }
        return econ != null && perms != null;
    }
    
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            perms = permissionProvider.getProvider();
        }
        return (perms != null);
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }
        return (econ != null);
    }

    @Override
    public double getMoney(PlotPlayer player) {
        double bal = super.getMoney(player);
        if (bal == Double.NaN) {
            return econ.getBalance(player.getName());
        }
        return bal;
    }

    @Override
    public void withdrawMoney(PlotPlayer player, double amount) {
        econ.withdrawPlayer(player.getName(), amount);
    }

    @Override
    public void depositMoney(PlotPlayer player, double amount) {
        econ.depositPlayer(player.getName(), amount);
    }

    @Override
    public void depositMoney(OfflinePlotPlayer player, double amount) {
        econ.depositPlayer(((BukkitOfflinePlayer) player).player, amount);
    }

    @Override
    public void setPermission(PlotPlayer player, String perm, boolean value) {
        if (value) {
            perms.playerAdd(((BukkitPlayer) player).player, perm);
        }
        else {
            perms.playerRemove(((BukkitPlayer) player).player, perm);
        }
    }
}
