package com.gmail.trentech.simpletags.commands;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.pjc.help.Help;
import com.gmail.trentech.simpletags.tags.PlayerTag;

public class CMDPlayer implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("tag player").get();
		
		if (args.hasAny("help")) {		
			help.execute(src);
			return CommandResult.empty();
		}
		
		Player player = args.<Player> getOne("player").get();

		if (src instanceof Player && !((Player) src).equals(player) && !src.hasPermission("simpletags.cmd.tag.player.others")) {
			throw new CommandException(Text.of(TextColors.RED, "You only have permission to tag yourself!"), false);
		}

		Optional<PlayerTag> optionalPlayerTag = PlayerTag.get(player);

		if (!args.hasAny("tag")) {
			if (optionalPlayerTag.isPresent()) {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalPlayerTag.get().getTag()));
			} else {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", PlayerTag.getDefault(player)));
			}

			src.sendMessage(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag player <player> <tag>"));

			return CommandResult.success();
		}
		String tag = args.<String> getOne("tag").get();

		if (tag.equalsIgnoreCase("reset")) {
			if (optionalPlayerTag.isPresent()) {
				optionalPlayerTag.get().setTag(null);
			}
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag reset"));

			return CommandResult.success();
		}

		if (optionalPlayerTag.isPresent()) {
			PlayerTag playerTag = optionalPlayerTag.get();
			playerTag.setTag(tag);
		} else {
			PlayerTag.create(player, tag);
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextColors.RESET, TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
