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
import org.spongepowered.api.world.storage.WorldProperties;

import com.gmail.trentech.simpletags.tags.WorldTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDWorld implements CommandExecutor {

	public CMDWorld() {
		Help help = new Help("world", "world", " View and edit world tags");
		help.setPermission("simpletags.cmd.tag.world");
		help.setSyntax(" /tag world <world> <tag>\n /t w <world> <tag>");
		help.setExample(" /tag world DIM-1\n /tag world DIM-1 &4[NETHER]\n /tag world @w &6[MyWorld]\n /tag world world reset");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		WorldProperties properties = args.<WorldProperties> getOne("world").get();

		Optional<WorldTag> optionalWorldTag = WorldTag.get(properties);

		if (!args.hasAny("tag")) {
			if (optionalWorldTag.isPresent()) {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalWorldTag.get().getTag()));
			} else {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RED, "NONE"));
			}

			src.sendMessage(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag world <world> <tag>"));

			return CommandResult.success();
		}
		String tag = args.<String> getOne("tag").get();

		if (tag.equalsIgnoreCase("reset")) {
			if (optionalWorldTag.isPresent()) {
				optionalWorldTag.get().setTag(null);
			}
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag reset"));

			return CommandResult.success();
		}

		if (optionalWorldTag.isPresent()) {
			WorldTag worldTag = optionalWorldTag.get();
			worldTag.setTag(tag);
		} else {
			WorldTag.create(properties, tag);
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextColors.RESET, TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
