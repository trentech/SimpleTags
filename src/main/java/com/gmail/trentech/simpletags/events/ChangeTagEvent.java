package com.gmail.trentech.simpletags.events;

import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

import com.gmail.trentech.simpletags.tags.Tag;

public class ChangeTagEvent extends AbstractEvent {

	private Tag tag;

	public ChangeTagEvent(Tag tag) {
		this.tag = tag;
	}

	public Tag getTag() {
		return tag;
	}

	@Override
	public Cause getCause() {
		return null;
	}

	public static class Delete extends ChangeTagEvent {

		public Delete(Tag tag) {
			super(tag);
		}
	}

	public static class Update extends ChangeTagEvent {

		public Update(Tag tag) {
			super(tag);
		}
	}
}
