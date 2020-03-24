package io.github.silicondev.craftingnbt.listeners;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
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
	public void onCraftItem(CraftItemEvent event) {
		ItemStack item = event.getRecipe().getResult();
		Material type = item.getType();
		if (!type.isBlock())
			return;
		
		boolean debug = _config.getBoolean("debug");
		
		_runCommand("scoreboard objectives add tempStore dummy");
		
		Player player = (Player)event.getWhoClicked();
		
		_runCommand("scoreboard objectives add tempStore dummy");
		if (debug) player.sendMessage("[CraftingNBT/debug] Added tempStore Objective.");
		
		Location loc = player.getLocation();
		Block existingBlock = loc.getBlock();
		BlockData existingData = existingBlock.getBlockData();
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
			String nbt = subSection.getString("nbt").trim();
			
			if (!tag.startsWith("#"))
				tag = "#" + tag;
			
			Location playerLoc = player.getLocation();
			_runCommand("/execute store success score " + player.getName() + " store if block " + String.valueOf(playerLoc.getBlockX()) + " " + String.valueOf(playerLoc.getBlockY()) + " " + String.valueOf(playerLoc.getBlockZ()) + " " + tag);
			
			Scoreboard scoreboard = _parent.getServer().getScoreboardManager().getMainScoreboard();
			Objective objective = scoreboard.getObjective("tempStore");
			int score = objective.getScore(player.getName()).getScore();
			
			if (score == 1) {
				if (debug) player.sendMessage("[CraftingNBT/debug] Found matching node and modifying item.");
				
				NBTItem nbtItem = new NBTItem(item);
				nbtItem.mergeCompound(new NBTContainer(nbt));
				item = nbtItem.getItem();
			}
		}
		
		loc.getBlock().setType(existingBlock.getType());
		loc.getBlock().setBlockData(existingData);
		if (debug) player.sendMessage("[CraftingNBT/debug] Reset block.");
		
		_runCommand("scoreboard objectives remove tempStore");
		if (debug) player.sendMessage("[CraftingNBT/debug] Removed tempStore Objective.");
	}
	
	private void _runCommand(String command) {
		_parent.getServer().dispatchCommand(_parent.getServer().getConsoleSender(), command);
	}
}
