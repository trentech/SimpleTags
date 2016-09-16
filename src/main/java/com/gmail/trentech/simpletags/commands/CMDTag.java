package com.gmail.trentech.simpletags.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simpletags.utils.Help;

public class CMDTag implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		List<Text> list = new ArrayList<>();

		list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command to execute "))).onClick(TextActions.runCommand("/simpletags:tag help")).append(Text.of(" /tag help")).build());
		
		for (Entry<String, Help> entry : Help.all().entrySet()) {
			String id = entry.getKey();
			String command = entry.getValue().getCommand();
			
			Optional<String> optionalPermission = entry.getValue().getPermission();
			
			if(optionalPermission.isPresent()) {
				if (src.hasPermission(optionalPermission.get())) {
					list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information "))).onClick(TextActions.executeCallback(Help.getHelp(id))).append(Text.of(" /tag " + command)).build());
				}
			} else {
				list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information "))).onClick(TextActions.executeCallback(Help.getHelp(id))).append(Text.of(" /tag " + command)).build());
			}
		}

		if (src instanceof Player) {
			Builder pages = PaginationList.builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Command List")).build());

			pages.contents(list);

			pages.sendTo(src);
		} else {
			for (Text text : list) {
				src.sendMessage(text);
			}
		}

		return CommandResult.success();
	}
}
