package com.gmail.trentech.simpletags.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.gmail.trentech.simpletags.tags.GroupTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDGroup implements CommandExecutor {

	public CMDGroup(){
		Help help = new Help("group", "group", " View and edit group tags");
		help.setSyntax(" /tag group <group> <tag>\n /t g <group> <tag>");
		help.setExample(" /tag group admin\n /tag group admin &e[BOSS]");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/tag group <group> <tag>"));
			return CommandResult.empty();
		}
		String name = args.<String>getOne("name").get();
		
		Optional<GroupTag> optionalGroupTag = GroupTag.get(name);
		
    	if(!optionalGroupTag.isPresent()){
			src.sendMessage(Text.of(TextColors.DARK_RED, "Group does not exist!"));
			return CommandResult.empty();
    	}
    	GroupTag groupTag = optionalGroupTag.get();

		if(!args.hasAny("tag")) {
			PaginationBuilder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.AQUA, "Group")).build());
			
			List<Text> list = new ArrayList<>();

			list.add(Text.of(TextColors.AQUA, "Current Tag: ", groupTag.getTag()));
			list.add(Text.of(TextColors.AQUA, "Update Tag: ", TextColors.GREEN, "/tag group <group> <tag>"));
			
			pages.contents(list);
			pages.sendTo(src);
			
			return CommandResult.empty();
		}
		String tag = args.<String>getOne("tag").get();
    	
		groupTag.setTag(tag);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag Changed!"));
		
		return CommandResult.success();
	}

}
