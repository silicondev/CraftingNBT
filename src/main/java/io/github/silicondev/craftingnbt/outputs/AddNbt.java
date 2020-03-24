package io.github.silicondev.craftingnbt.outputs;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.silicondev.siliconmccli.ICLIOutput;
import io.github.silicondev.siliconmccli.Result;

public class AddNbt implements ICLIOutput {

	private FileConfiguration _config;
	private JavaPlugin _parent;
	
	public AddNbt(FileConfiguration config, JavaPlugin parent) {
		_config = config;
		_parent = parent;
	}
	
	public Result Run(CommandSender sender, List<String> args) {
		
		if (args.size() < 3) {
			sender.sendMessage("[CraftingNBT] Not enough arguments!");
			return new Result(false, true, true);
		} else {
			String id = args.get(0);
			String sectionId = "nbt." + id;
			String tag = args.get(1);
			String nbt = args.get(2);
			
			_config.createSection(sectionId);
			_config.set(sectionId + ".tag", tag);
			_config.set(sectionId + ".data", nbt);
			
			_parent.saveConfig();
			
			sender.sendMessage("[CraftingNBT] " + id + " record added.");
			return new Result(true, true);
		}
	}
}
