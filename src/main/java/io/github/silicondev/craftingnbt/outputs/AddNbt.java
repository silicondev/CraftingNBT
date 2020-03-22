package io.github.silicondev.craftingnbt.outputs;

import java.util.List;

import org.bukkit.command.CommandSender;

import io.github.silicondev.siliconmccli.ICLIOutput;

public class AddNbt implements ICLIOutput {

	public boolean Run(CommandSender sender, List<String> args) {
		// TODO Auto-generated method stub
		sender.sendMessage("AddNbt");
		return true;
	}

	public boolean Run(CommandSender sender) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
