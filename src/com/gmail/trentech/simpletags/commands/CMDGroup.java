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
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.tags.GroupTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDGroup implements CommandExecutor {

	public CMDGroup(){
		Help help = new Help("group", "group", " View and edit group tags");
		help.setSyntax(" /tag group <group> <tag>\n /t g <group> <tag>");
		help.setExample(" /tag group admin\n /tag group admin &e[BOSS]\n /tag group admin reset");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/tag group <group> <tag>"));
			return CommandResult.empty();
		}
		String name = args.<String>getOne("name").get();

		PermissionService permissionService = Main.getGame().getServiceManager().provide(PermissionService.class).get();
		
		boolean groupExist = false;
		
		for(Subject subject : permissionService.getGroupSubjects().getAllSubjects()){
			String group = subject.getIdentifier();

			if(name.equalsIgnoreCase(group)){
				groupExist = true;
				break;
			}		
		}
		
    	if(!groupExist){
			src.sendMessage(Text.of(TextColors.DARK_RED, "Group does not exist!"));
			return CommandResult.empty();
    	}

    	Optional<GroupTag> optionalGroupTag = GroupTag.get(name);
    	
		if(!args.hasAny("tag")) {
			PaginationBuilder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.AQUA, "Group")).build());
			
			List<Text> list = new ArrayList<>();

			if(optionalGroupTag.isPresent()){
				list.add(Text.of(TextColors.AQUA, "Current Tag: ", optionalGroupTag.get().getTag()));	
			}else{
				list.add(Text.of(TextColors.AQUA, "Current Tag: ", TextColors.RED, "NONE"));
			}
			
			list.add(Text.of(TextColors.AQUA, "Update Tag: ", TextColors.GREEN, "/tag group <group> <tag>"));
			
			pages.contents(list);
			pages.sendTo(src);
			
			return CommandResult.success();
		}
		String tag = args.<String>getOne("tag").get();
    	
		if(tag.equalsIgnoreCase("reset")){
			if(optionalGroupTag.isPresent()){
				optionalGroupTag.get().delete();
			}
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag reset"));
			
			return CommandResult.success();
		}
		
		if(optionalGroupTag.isPresent()){
			optionalGroupTag.get().setTag(tag);
		}else{
			new GroupTag(name, tag);
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextSerializers.FORMATTING_CODE.deserialize(tag)));
		
		return CommandResult.success();
	}

}
