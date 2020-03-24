package io.github.silicondev.craftingnbt.outputs;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.silicondev.siliconmccli.ICLIOutput;
import io.github.silicondev.siliconmccli.Result;

public class ToggleDebug implements ICLIOutput {

	private FileConfiguration _config;
	private JavaPlugin _parent;
	
	public ToggleDebug(FileConfiguration config, JavaPlugin parent) {
		_config = config;
		_parent = parent;
	}
	
	public Result Run(CommandSender sender, List<String> args) {
		boolean debug = _config.getBoolean("debug");
		if (debug) {
			debug = false;
			sender.sendMessage("[CraftingNBT] Debug Mode disabled.");
		} else {
			debug = true;
			sender.sendMessage("[CraftingNBT] Debug Mode enabled.");
		}
		
		_config.set("debug", debug);
		_parent.saveConfig();
		return new Result(true, true);
	}
	
}
