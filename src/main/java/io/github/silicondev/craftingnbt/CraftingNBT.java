package io.github.silicondev.craftingnbt;

import io.github.silicondev.craftingnbt.outputs.*;
import io.github.silicondev.siliconmccli.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class CraftingNBT extends JavaPlugin {
	
	public static String PluginName = "CraftingNBT";
	public static String Version = "Alpha 0.0.2";
	
	static List<CLICommand> Commands = new ArrayList<CLICommand>();
	//static List<ICLIOutput> Outputs = new ArrayList<ICLIOutput>(Arrays.asList(
	//	new TestCommand(),
	//	new AddNbt(),
	//	new RemoveNbt()
	//));
	
	static Handler CommandHandler;
	
	@Override
	public void onEnable() {
		
		try {
			
			
			Commands.add(new CLICommand("cnbt", "main", new TestCommand(), new ArrayList<CLICommand>(Arrays.asList(
				new CLICommand("test", "test", new TestCommand()),
				new CLICommand("add", "addnbt", new AddNbt())
			))));
			
			CommandHandler = new Handler(Commands, false);
			this.getCommand("cnbt").setExecutor(CommandHandler);
			
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
