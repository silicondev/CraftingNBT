package io.github.silicondev.craftingnbt.outputs;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.silicondev.siliconmccli.ICLIOutput;
import io.github.silicondev.siliconmccli.Result;

public class RemoveNbt implements ICLIOutput {

	private FileConfiguration _config;
	private JavaPlugin _parent;
	
	public RemoveNbt(FileConfiguration config, JavaPlugin parent) {
		_config = config;
		_parent = parent;
	}
	
	public Result Run(CommandSender sender, List<String> args) {
		if (args.isEmpty()) {
			sender.sendMessage("[CraftingNBT] Id argument required.");
			return new Result(true, false, true);
		} else {
			String id = args.get(0);
			
			ConfigurationSection section = _config.getConfigurationSection("nbt");
			
			if (section.contains(id)) {
				_config.set("nbt." + id, null);
				
				_parent.saveConfig();
				
				sender.sendMessage("[CraftingNBT] Record " + id + " removed.");
				return new Result(true, true);
			} else {
				sender.sendMessage("[CraftingNBT] Record " + id + " does not exist.");
				return new Result(true, false, true);
			}
		}
	}
}
