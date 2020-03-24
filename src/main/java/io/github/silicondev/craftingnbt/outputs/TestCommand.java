package io.github.silicondev.craftingnbt.outputs;

import java.util.List;

import org.bukkit.command.CommandSender;

import io.github.silicondev.siliconmccli.ICLIOutput;
import io.github.silicondev.siliconmccli.Result;

public class TestCommand implements ICLIOutput {

	public Result Run(CommandSender sender, List<String> args) {
		
		if (args.size() > 0) {
			String argString = "";
			for (String str : args) {
				argString += str + " ";
			}
			sender.sendMessage("Test succeeded with args: " + argString);
		} else
			sender.sendMessage("Test Succeeded with no args.");
		return new Result(true, true);
	}
}
