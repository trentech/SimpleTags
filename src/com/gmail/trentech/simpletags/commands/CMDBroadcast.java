package com.gmail.trentech.simpletags.commands;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.tags.BroadcastTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDBroadcast implements CommandExecutor {

	public CMDBroadcast(){
		Help help = new Help("broadcast", "broadcast", " View and edit broadcast tag");
		help.setSyntax(" /tag broadcast <tag>\n /t s <tag>");
		help.setExample(" /tag broadcast\n /tag broadcast &7[CONSOLE]");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		BroadcastTag broadcastTag = BroadcastTag.get().get();
		
		if(!args.hasAny("tag")) {
			PaginationBuilder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.AQUA, "Server")).build());
			
			List<Text> list = new ArrayList<>();

			list.add(Text.of(TextColors.AQUA, "Current Tag: ", broadcastTag.getTag()));
			list.add(Text.of(Text.of(TextColors.AQUA, "Update Tag: ", TextColors.GREEN, "/tag broadcast <tag>")));
			
			pages.contents(list);
			pages.sendTo(src);
			
			return CommandResult.empty();
		}
		String tag = args.<String>getOne("tag").get();
    	
		broadcastTag.setTag(tag);
		
		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag Changed!"));
		return CommandResult.success();
	}

}
