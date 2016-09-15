package com.gmail.trentech.simpletags.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import com.gmail.trentech.simpletags.utils.Help;

public class CMDHelp implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = args.<Help> getOne("command").get();
		help.execute(src);
		
		return CommandResult.success();
	}
}
