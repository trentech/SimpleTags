package com.gmail.trentech.simpletags.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDTag implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		PaginationBuilder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

		pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.AQUA, "Commands")).build());
		
		List<Text> list = new ArrayList<>();
		
		if(src.hasPermission("simpletags.cmd.tag.group")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("group"))).append(Text.of(" /tag group")).build());
		}
		if(src.hasPermission("simpletags.cmd.tag.player")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("player"))).append(Text.of(" /tag player")).build());
		}
		if(src.hasPermission("simpletags.cmd.tag.reload")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("reload"))).append(Text.of(" /tag reload")).build());
		}
		if(src.hasPermission("simpletags.cmd.tag.server")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("server"))).append(Text.of(" /tag server")).build());
		}
		if(src.hasPermission("simpletags.cmd.tag.world")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("world"))).append(Text.of(" /tag world")).build());
		}
		
		Optional<PluginContainer> plugin = Main.getGame().getPluginManager().getPlugin("SimpleChat");
		
		if(plugin.isPresent()){
			if(src.hasPermission("simpletags.cmd.tag.broadcast")) {
				list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
						.onClick(TextActions.executeCallback(Help.getHelp("broadcast"))).append(Text.of(" /tag broadcast")).build());
			}
			if(src.hasPermission("simpletags.cmd.tag.channel")) {
				list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
						.onClick(TextActions.executeCallback(Help.getHelp("channel"))).append(Text.of(" /tag channel")).build());
			}
		}

		pages.contents(list);
		pages.sendTo(src);
		
		return CommandResult.success();
	}
}


