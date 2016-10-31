package com.gmail.trentech.simpletags;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageEvent.MessageFormatter;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.events.ChangeTagEvent;
import com.gmail.trentech.simpletags.tags.GroupTag;
import com.gmail.trentech.simpletags.tags.PlayerTag;
import com.gmail.trentech.simpletags.tags.SingleTag;
import com.gmail.trentech.simpletags.tags.Tag;
import com.gmail.trentech.simpletags.tags.WorldTag;

public class EventListener {

	@Listener(order = Order.LATE)
	public void onMessageChannelEventChat(MessageChannelEvent.Chat event, @First Player player) {
		Builder playerTag = Text.builder().onHover(TextActions.showText(Text.of(player.getName())));

		Optional<PlayerTag> optionalPlayerTag = PlayerTag.get(player);

		if (!optionalPlayerTag.isPresent()) {
			playerTag.append(PlayerTag.getDefault(player));
		} else {
			playerTag.append(optionalPlayerTag.get().getTag());
		}

		Text worldTag = Text.EMPTY;

		Optional<WorldTag> optionalWorldTag = WorldTag.get(player.getWorld().getProperties());

		if (optionalWorldTag.isPresent()) {
			worldTag = optionalWorldTag.get().getTag();
		}

		Builder groupTagBuilder = Text.builder();

		for (Entry<Set<Context>, List<Subject>> parent : player.getSubjectData().getAllParents().entrySet()) {
			for (Subject subject : parent.getValue()) {
				String group = subject.getIdentifier();

				if (group.equalsIgnoreCase("op_0") || group.equalsIgnoreCase("op_1") || group.equalsIgnoreCase("op_2") || group.equalsIgnoreCase("op_3") || group.equalsIgnoreCase("op_4")) {
					group = "op";
				}

				Optional<GroupTag> optionalGroupTag = GroupTag.get(group);

				if (optionalGroupTag.isPresent()) {
					groupTagBuilder.append(optionalGroupTag.get().getTag());
				}
			}
		}

		MessageFormatter formatter = event.getFormatter();

		String oldStr = TextSerializers.FORMATTING_CODE.serialize(formatter.getHeader().toText());
		
		String name = oldStr.substring(oldStr.indexOf("<"), oldStr.lastIndexOf(">") + 1);
		
		Text old = TextSerializers.FORMATTING_CODE.deserialize(oldStr.replace(name, ""));

		formatter.setHeader(TextTemplate.of(worldTag, groupTagBuilder.build(), playerTag.build(), old, TextColors.RESET));
	}

	@Listener
	public void onSendCommandEvent(SendCommandEvent event, @First CommandSource src) {
		String command = event.getCommand();
		if (!command.equalsIgnoreCase("say")) {
			return;
		}

		Text message = TextSerializers.FORMATTING_CODE.deserialize(event.getArguments());
		
		if(src instanceof Player) {
			Player player = (Player) src;
			
			Optional<PlayerTag> optionalPlayerTag = PlayerTag.get(player);

			if (optionalPlayerTag.isPresent()) {
				Sponge.getServer().getBroadcastChannel().send(Text.of(optionalPlayerTag.get().getTag(), " ", message));
			} else {
				Sponge.getServer().getBroadcastChannel().send(Text.of(PlayerTag.getDefault(player), " ", message));
			}
		} else {
			Optional<SingleTag> optionalTag = SingleTag.get(Main.getPlugin().getId(), "console");
			
			if(optionalTag.isPresent()) {
				Sponge.getServer().getBroadcastChannel().send(Text.of(optionalTag.get().getTag(), " ", message));
			} else {
				Sponge.getServer().getBroadcastChannel().send(Text.of(message));
			}
		}

		event.setCancelled(true);
	}

	@Listener
	public void onChangeTagEventUpdate(ChangeTagEvent.Update event) {
		Tag tag = event.getTag();

		if (tag instanceof GroupTag) {
			GroupTag.cache.put(tag.getName(), (GroupTag) tag);
		} else if (tag instanceof WorldTag) {
			WorldTag.cache.put(tag.getName(), (WorldTag) tag);
		} else if (tag instanceof PlayerTag) {
			PlayerTag.cache.put(tag.getName(), (PlayerTag) tag);
		} else if (tag instanceof SingleTag) {
			SingleTag.cache.put(tag.getName(), (SingleTag) tag);
		}
	}

	@Listener
	public void onChangeTagEventDelete(ChangeTagEvent.Delete event) {
		Tag tag = event.getTag();

		if (tag instanceof GroupTag) {
			GroupTag.cache.remove(tag.getName());
		} else if (tag instanceof WorldTag) {
			WorldTag.cache.remove(tag.getName());
		} else if (tag instanceof PlayerTag) {
			PlayerTag.cache.remove(tag.getName());
		} else if (tag instanceof SingleTag) {
			SingleTag.cache.remove(tag.getName());
		}
	}
}
