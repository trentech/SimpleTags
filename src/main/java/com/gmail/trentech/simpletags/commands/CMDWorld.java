package com.gmail.trentech.simpletags.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.spongepowered.api.world.World;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.tags.WorldTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDWorld implements CommandExecutor {

	public CMDWorld() {
		Help help = new Help("world", "world", " View and edit world tags");
		help.setSyntax(" /tag world <world> <tag>\n /t w <world> <tag>");
		help.setExample(" /tag world DIM-1\n /tag world DIM-1 &4[NETHER]\n /tag world @w &6[MyWorld]\n /tag world world reset");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/tag world <world> <tag>"));
			return CommandResult.empty();
		}
		String name = args.<String> getOne("name").get();

		if (src instanceof Player && name.equalsIgnoreCase("@w")) {
			name = ((Player) src).getWorld().getName();
		}

		Optional<World> optionalWorld = Main.getGame().getServer().getWorld(name);

		if (!optionalWorld.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "World does not exist!"));
			return CommandResult.empty();
		}
		World world = optionalWorld.get();

		Optional<WorldTag> optionalWorldTag = WorldTag.get(world);

		if (!args.hasAny("tag")) {
			List<Text> list = new ArrayList<>();

			if (optionalWorldTag.isPresent()) {
				list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalWorldTag.get().getTag()));
			} else {
				list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RED, "NONE"));
			}

			list.add(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag world <world> <tag>"));

			if (src instanceof Player) {
				Builder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

				pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "World")).build());

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
			WorldTag.create(world, tag);
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
