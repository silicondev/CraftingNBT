package io.github.silicondev.craftingnbt.listeners;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
		boolean debug = _config.getBoolean("debug");
		
		if (event.getRecipe() == null)
			return;
		ItemStack item = event.getRecipe().getResult();
		
		Material type = item.getType();
		if (!type.isBlock())
			return;
		
		Player player = (Player)event.getInventory().getHolder();

		event.getInventory().setResult(_modifyItem(player, player.getWorld(), item, debug));
	}
	
	@EventHandler
	public void onSmeltItem(FurnaceSmeltEvent event) {
		boolean debug = _config.getBoolean("debug");
		
		if (event.getResult() == null)
			return;
		ItemStack item = event.getResult();
		
		Material type = item.getType();
		if (!type.isBlock())
			return;
		
		
		event.setResult(_modifyItem(_parent.getServer().getConsoleSender(), event.getBlock().getWorld(), item, debug));
	}
	
	private ItemStack _modifyItem(CommandSender sender, World world, ItemStack item, boolean debug) {
		
		ItemStack result = item;
		
		Scoreboard scoreboard = _parent.getServer().getScoreboardManager().getMainScoreboard();
		if (scoreboard.getObjective("tempStore") == null)
			scoreboard.registerNewObjective("tempStore", "dummy", "tempStore");
		Objective objective = scoreboard.getObjective("tempStore");
		
		if (debug) sender.sendMessage("[CraftingNBT/debug] Added tempStore Objective.");
		
		Location loc = new Location(world, 0, 0, 0);
		loc.getBlock().setType(item.getType());
		if (debug) sender.sendMessage("[CraftingNBT/debug] Set block to one that's being crafted.");
		
		Map<String, Object> nodes = _config.getConfigurationSection("nbt").getValues(false);
		
		if (debug) sender.sendMessage("[CraftingNBT/debug] Running through nbt nodes.");
		for (Map.Entry<String, Object> entry : nodes.entrySet()) {
			if (entry.getValue() instanceof ConfigurationSection) {}
			else 
				continue;
			
			ConfigurationSection subSection = (ConfigurationSection)entry.getValue();
			
			String tag = subSection.getString("tag").trim();
			String nbt = subSection.getString("data").trim();
			
			if (debug) sender.sendMessage("[CraftingNBT/debug] Checking " + tag + ".");
			
			if (!tag.startsWith("#"))
				tag = "#" + tag;
			
			String execute = "execute store success score " + sender.getName() + " tempStore if block 0 0 0 " + tag;
			_parent.getServer().dispatchCommand(sender, execute);
			
			int score = objective.getScore(sender.getName()).getScore();
			
			if (score == 1) {
				if (debug) sender.sendMessage("[CraftingNBT/debug] Found matching node and adding " + nbt + ".");
				
				NBTItem nbtItem = new NBTItem(item);
				nbtItem.mergeCompound(new NBTContainer(nbt));
				ItemMeta newItemMeta = nbtItem.getItem().getItemMeta();
				
				result = new ItemStack(item.getType());
				result.setAmount(item.getAmount());
				result.setItemMeta(newItemMeta);
			}
		}
		
		loc.getBlock().setType(Material.BEDROCK);
		if (debug) sender.sendMessage("[CraftingNBT/debug] Reset block.");
		
		objective.unregister();
		if (debug) sender.sendMessage("[CraftingNBT/debug] Removed tempStore Objective.");
		
		return result;
	}
}
