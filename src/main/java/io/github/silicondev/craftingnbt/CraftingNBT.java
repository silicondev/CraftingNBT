package io.github.silicondev.craftingnbt;

import io.github.silicondev.craftingnbt.outputs.*;
import io.github.silicondev.siliconmccli.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class CraftingNBT extends JavaPlugin {
	
	public static String PluginName = "CraftingNBT";
	public static String Version = "Alpha 0.0.1";
	
	static List<CLICommand> Commands = new ArrayList<CLICommand>();
	static List<ICLIOutput> Outputs = new ArrayList<ICLIOutput>(Arrays.asList(
		new AddNbt(),
		new RemoveNbt()
	));
	
	@Override
	public void onEnable() {
		getLogger().info("Enabled " + PluginName + " successfully!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Disabled " + PluginName + " successfully!");
	}
}
