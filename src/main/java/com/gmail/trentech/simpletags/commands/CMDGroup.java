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

import com.gmail.trentech.simpletags.tags.GroupTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDGroup implements CommandExecutor {

	public CMDGroup() {
		new Help("group", "group", "View and edit group tags", false)
			.setPermission("simpletags.cmd.tag.group")
			.setUsage("/tag group <group> <tag>\n /t g <group> <tag>")
			.setExample("/tag group admin\n /tag group admin &e[BOSS]\n /tag group admin reset")
			.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String name = args.<String> getOne("group").get();

		Optional<GroupTag> optionalGroupTag = GroupTag.get(name);

		if (!args.hasAny("tag")) {
			if (optionalGroupTag.isPresent()) {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalGroupTag.get().getTag()));
			} else {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RED, "NONE"));
			}

			src.sendMessage(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag group <group> <tag>"));

			return CommandResult.success();
		}
		String tag = args.<String> getOne("tag").get();

		if (tag.equalsIgnoreCase("reset")) {
			if (optionalGroupTag.isPresent()) {
				optionalGroupTag.get().setTag(null);
			}
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag reset"));

			return CommandResult.success();
		}

		if (optionalGroupTag.isPresent()) {
			GroupTag groupTag = optionalGroupTag.get();
			groupTag.setTag(tag);
		} else {
			GroupTag.create(name, tag);
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextColors.RESET, TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
