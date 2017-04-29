package net.TheDgtl.Stargate;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Stargate - A portal plugin for Bukkit
 * Copyright (C) 2011, 2012 Steven "Drakia" Scott <Contact@TheDgtl.net>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class iConomyHandler {
	public static boolean useiConomy = false;
	public static Vault vault = null;
	public static Economy economy = null;
	
	public static int useCost = 0;
	public static int createCost = 0;
	public static int destroyCost = 0;
	public static boolean toOwner = false;
	public static boolean chargeFreeDestination = true;
	public static boolean freeGatesGreen = false;
	
	public static double getBalance(String player) {
		if (!useiConomy) return 0;
		if (economy != null) {
			return economy.getBalance(player);
		}
		return 0;
	}
	
	public static boolean chargePlayer(String player, String target, double amount) {
		if (!useiConomy) return true;
		if (economy != null) {
			if (player.equals(target)) return true;
			
			if (!economy.has(player, amount)) return false;
			economy.withdrawPlayer(player, amount);
			
			if (target != null) {
				economy.depositPlayer(target, amount);
			}
			return true;
		}
		return true;
	}
	
	public static boolean useiConomy() {
		if (!useiConomy) return false;
		if (economy != null) return true;
		return false;
	}
	
	public static String format(int amt) {
		if (economy != null) {
			return economy.format(amt);
		}
		return "";
	}
	
	public static boolean setupeConomy(PluginManager pm) {
		if (!useiConomy) return false;
		// Check for Vault
		Plugin p = pm.getPlugin("Vault");
		if (p != null)
			return setupVault(p);	
		return false;
	}
	
	public static boolean setupVault(Plugin p) {
		if (!useiConomy) return false;
		if (p == null || !p.isEnabled()) return false;
		if (!p.getDescription().getName().equals("Vault")) return false;
		
		RegisteredServiceProvider<Economy> economyProvider = Stargate.server.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
        	vault = (Vault)p;
            economy = economyProvider.getProvider();
        }

        return (economy != null);
	}
	
	public static boolean checkLost(Plugin p) {
		if (p.equals(vault)) {
			economy = null;
			vault = null;
			return true;
		}
		return false;
	}
}
