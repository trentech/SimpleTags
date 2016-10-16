package com.gmail.trentech.simpletags.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import com.gmail.trentech.simpletags.utils.Help;

public class CMDHelp implements CommandExecutor {

	public CMDHelp() {
		new Help("tag help", "help", "Get help with all commands in SimpleTags", false)
			.setPermission("stackban.cmd.sban")
			.setUsage("/tag help <rawCommand>")
			.setExample("/tag help tag create")
			.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = args.<Help> getOne("command").get();
		help.execute(src);
		
		return CommandResult.success();
	}
}
