package com.gmail.trentech.simpletags.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDReload implements CommandExecutor {

	public CMDReload(){
		Help help = new Help("reload", "reload", " Run this after making changes in your permissions plugin");
		help.setSyntax(" /tag reload");
		help.setExample(" /tag reload");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Main.initGroups();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Reloaded"));
		
		return CommandResult.success();
	}

}
