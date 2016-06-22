package com.gmail.trentech.simpletags.commands;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.tags.PlayerTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDServer implements CommandExecutor {

	public CMDServer() {
		Help help = new Help("server", "server", " View and edit server tag");
		help.setSyntax(" /tag server <tag>\n /t s <tag>");
		help.setExample(" /tag server\n /tag server &7[CONSOLE]");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		PlayerTag consoleTag = PlayerTag.getConsole().get();

		if (!args.hasAny("tag")) {
			List<Text> list = new ArrayList<>();

			list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, consoleTag.getTag()));
			list.add(Text.of(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag server <tag>")));

			if (src instanceof Player) {
				Builder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

				pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Server")).build());

				pages.contents(list);

				pages.sendTo(src);
			} else {
				for (Text text : list) {
					src.sendMessage(text);
				}
			}

			return CommandResult.success();
		}
		String tag = args.<String> getOne("tag").get();

		consoleTag.setTag(tag);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
