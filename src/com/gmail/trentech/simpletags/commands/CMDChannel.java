package com.gmail.trentech.simpletags.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.tags.ChannelTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDChannel implements CommandExecutor {

	public CMDChannel(){
		Help help = new Help("channel", "channel", " View and edit channel tags");
		help.setSyntax(" /tag channel <channel> <tag>\n /t c <channel> <tag>");
		help.setExample(" /tag channel 1\n /tag channel 1 &e[CH1]");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/tag channel <channel> <tag>"));
			return CommandResult.empty();
		}
		String name = args.<String>getOne("name").get();
		
		Optional<ChannelTag> optionalChannelTag = ChannelTag.get(name);
		
    	if(!optionalChannelTag.isPresent()){
			src.sendMessage(Text.of(TextColors.DARK_RED, "Channel does not exist!"));
			return CommandResult.empty();
    	}
    	ChannelTag channelTag = optionalChannelTag.get();

		if(!args.hasAny("tag")) {
			Builder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.AQUA, "Group")).build());
			
			List<Text> list = new ArrayList<>();

			list.add(Text.of(TextColors.AQUA, "Current Tag: ", channelTag.getTag()));
			list.add(Text.of(TextColors.AQUA, "Update Tag: ", TextColors.GREEN, "/tag channel <channel> <tag>"));
			
			pages.contents(list);
			pages.sendTo(src);
			
			return CommandResult.success();
		}
		String tag = args.<String>getOne("tag").get();

		channelTag.setTag(tag);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextSerializers.FORMATTING_CODE.deserialize(tag)));
		
		return CommandResult.success();
	}

}
