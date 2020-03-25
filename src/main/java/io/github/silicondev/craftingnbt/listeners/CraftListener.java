package io.github.silicondev.craftingnbt.listeners;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;

public class CraftListener implements Listener {
	
	private JavaPlugin _parent;
	private FileConfiguration _config;
	
	public CraftListener(JavaPlugin parent, FileConfiguration config) {
		_parent = parent;
		_config = config;
	}
	
	@EventHandler
	public void onCraftItem(PrepareItemCraftEvent event) {
		if (event.getRecipe() == null)
			return;
		
		ItemStack item = event.getRecipe().getResult();
		Material type = item.getType();
		if (!type.isBlock())
			return;
		
		boolean debug = _config.getBoolean("debug");
		
		_runCommand("scoreboard objectives add tempStore dummy");
		
		Player player = (Player)event.getInventory().getHolder();
		
		Scoreboard scoreboard = _parent.getServer().getScoreboardManager().getMainScoreboard();
		Objective objective = scoreboard.getObjective("tempStore");
		if (debug) player.sendMessage("[CraftingNBT/debug] Added tempStore Objective.");
		
		Location loc = new Location(player.getWorld(), 0, 0, 0);
		loc.getBlock().setType(type);
		if (debug) player.sendMessage("[CraftingNBT/debug] Set block to one that's being crafted.");
		
		ConfigurationSection section = _config.getConfigurationSection("nbt");
		Map<String, Object> nodes = section.getValues(false);
		
		if (debug) player.sendMessage("[CraftingNBT/debug] Running through nbt nodes.");
		for (Map.Entry<String, Object> entry : nodes.entrySet()) {
			if (entry.getValue() instanceof ConfigurationSection) {}
			else 
				continue;
			
			ConfigurationSection subSection = (ConfigurationSection)entry.getValue();
			
			String tag = subSection.getString("tag").trim();
			String nbt = subSection.getString("data").trim();
			
			if (debug) player.sendMessage("[CraftingNBT/debug] Checking " + tag + ".");
			
			if (!tag.startsWith("#"))
				tag = "#" + tag;
			
			_runCommand("execute store success score " + player.getName() + " tempStore if block 0 0 0 " + tag);
			
			int score = objective.getScore(player.getName()).getScore();
			
			if (score == 1) {
				if (debug) player.sendMessage("[CraftingNBT/debug] Found matching node and adding " + nbt + ".");
				
				NBTItem nbtItem = new NBTItem(item);
				nbtItem.mergeCompound(new NBTContainer(nbt));
				ItemStack newItem = nbtItem.getItem();
				
				if (debug && newItem == item) player.sendMessage("[WARN][CraftingNBT/debug] Both new and old items are identical!!");
				
				ItemStack tmp = new ItemStack(Material.STONE);
				ItemStack result = new ItemStack(item.getType());
				result.setAmount(item.getAmount());
				result.setItemMeta(newItem.getItemMeta());
				event.getInventory().setResult(result);
			}
		}
		
		loc.getBlock().setType(Material.BEDROCK);
		if (debug) player.sendMessage("[CraftingNBT/debug] Reset block.");
		
		objective.unregister();
		if (debug) player.sendMessage("[CraftingNBT/debug] Removed tempStore Objective.");
	}
	
	private void _runCommand(String command) {
		_parent.getServer().dispatchCommand(_parent.getServer().getConsoleSender(), command);
	}
}
