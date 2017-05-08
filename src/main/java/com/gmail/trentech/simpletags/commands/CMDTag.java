package com.gmail.trentech.simpletags.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import com.gmail.trentech.pjc.help.Help;

public class CMDTag implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help.executeList(src, Help.get("tag").get().getChildren());
		
		return CommandResult.success();
		
//		List<Text> list = new ArrayList<>();
//
//		for (Entry<CommandSpec, String[]> entry : CommandManager.hash.entrySet()) {
//			String[] aliases = entry.getValue();
//
//			if (src.hasPermission("simpletags.cmd.tag." + aliases[0])) {
//				list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simpletags:tag " + aliases[0])).append(Text.of(" /tag " + aliases[0])).build());
//			}
//		}
//
//		if (src instanceof Player) {
//			Builder pages = PaginationList.builder();
//
//			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Command List")).build());
//
//			pages.contents(list);
//
//			pages.sendTo(src);
//		} else {
//			for (Text text : list) {
//				src.sendMessage(text);
//			}
//		}
//
//		return CommandResult.success();
	}
}
