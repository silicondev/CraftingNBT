package io.github.silicondev.craftingnbt;

import io.github.silicondev.craftingnbt.outputs.*;
import io.github.silicondev.siliconmccli.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftingNBT extends JavaPlugin {
	
	public static String PluginName = "CraftingNBT";
	public static String Version = "Alpha 0.0.2";
	
	static List<CLICommand> Commands = new ArrayList<CLICommand>();
	static Dictionary<String, ICLIOutput> Outputs = new Hashtable<String, ICLIOutput>();
	//static List<ICLIOutput> Outputs = new ArrayList<ICLIOutput>(Arrays.asList(
	//	new TestCommand(),
	//	new AddNbt(),
	//	new RemoveNbt()
	//));
	
	static Handler CommandHandler;
	static FileConfiguration Config;
	
	@Override
	public void onEnable() {
		
		try {
			this.saveDefaultConfig();
			
			Config = this.getConfig();
			
			Outputs.put("test", new TestCommand());
			Outputs.put("addnbt", new AddNbt(Config, this));
			Outputs.put("removenbt", new RemoveNbt(Config, this));
			
			Commands.add(new CLICommand("cnbt", "main", new TestCommand(), new ArrayList<CLICommand>(Arrays.asList(
				new CLICommand("test", "test", Outputs.get("test")),
				new CLICommand("add", "addnbt", Outputs.get("addnbt")),
				new CLICommand("remove", "removenbt", Outputs.get("removenbt"))
			))));
			
			CommandHandler = new Handler(PluginName, Commands, false);
			this.getCommand("cnbt").setExecutor(CommandHandler);
			
			Map<String, Object> ConfigValues = new HashMap<String, Object>();
			
			// Do something with the config values.
			
			getLogger().info("Enabled " + PluginName + " successfully!");
			
			
		} catch (Exception e) {
			getLogger().severe("Error on initialization!");
		}
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Disabled " + PluginName + " successfully!");
	}
}
