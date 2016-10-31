package com.gmail.trentech.simpletags.commands;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.tags.SingleTag;

public class CMDServer implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Optional<SingleTag> optionalTag = SingleTag.get(Main.getPlugin().getId(), "console");

		if (!args.hasAny("tag")) {
			if (optionalTag.isPresent()) {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalTag.get().getTag()));
			} else {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RED, "NONE"));
			}
			
			src.sendMessage(Text.of(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag server <tag>")));

			return CommandResult.success();
		}
		String tag = args.<String> getOne("tag").get();
		
		if (tag.equalsIgnoreCase("reset")) {
			if (optionalTag.isPresent()) {
				optionalTag.get().setTag(null);
			}
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag reset"));

			return CommandResult.success();
		}
		
		SingleTag.create(Main.getPlugin().getId(), "console", tag);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextColors.RESET, TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
