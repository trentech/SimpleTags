package com.gmail.trentech.simpletags.tags;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.events.ChangeTagEvent;
import com.gmail.trentech.simpletags.utils.SQLUtils;

public class Tag extends SQLUtils {

	private String name;
	private final String type;
	private Text tag;

	protected Tag(String name, Class<? extends Tag> type, String tag) {
		this.name = name;
		this.type = type.getSimpleName();
		this.tag = TextSerializers.FORMATTING_CODE.deserialize(tag);

		if (!exists()) {
			create();
		} else {
			update();
		}
	}

	protected Tag(Tag tag) {
		this.name = tag.getName();
		this.type = tag.getType();
		this.tag = tag.getTag();
	}

	public String getName() {
		return name;
	}

	public Text getTag() {
		return tag;
	}

	public void setTag(String tag) {
		if (tag == null) {
			delete();
		} else {
			this.tag = TextSerializers.FORMATTING_CODE.deserialize(tag);

			update();
		}
	}

	private String getType() {
		return type;
	}

	private void create() {
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("INSERT into " + getType() + " (Name, Tag) VALUES (?, ?)");

			statement.setString(1, getName());
			statement.setString(2, TextSerializers.FORMATTING_CODE.serialize(getTag()));

			statement.executeUpdate();

			connection.close();

			Sponge.getEventManager().post(new ChangeTagEvent.Update(this));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void update() {
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("UPDATE " + getType() + " SET Tag = ? WHERE Name = ?");

			statement.setString(1, TextSerializers.FORMATTING_CODE.serialize(getTag()));
			statement.setString(2, getName());

			statement.executeUpdate();

			connection.close();

			Sponge.getEventManager().post(new ChangeTagEvent.Update(this));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void delete() {
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("DELETE from " + getType() + " WHERE Name = ?");

			statement.setString(1, getName());
			statement.executeUpdate();

			connection.close();

			Sponge.getEventManager().post(new ChangeTagEvent.Delete(this));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean exists() {
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + getType());

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				if (result.getString("Name").equals(getName())) {
					connection.close();
					return true;
				}
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	protected static Optional<Tag> get(Class<? extends Tag> clazz, String name) {
		String type = clazz.getSimpleName();

		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + type);

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String uuid = result.getString("Name");
				if (uuid.equals(name)) {
					connection.close();
					return Optional.of(new Tag(uuid, clazz, result.getString("Tag")));
				}
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Optional.empty();
	}

	protected static List<Tag> getAll(Class<? extends Tag> clazz) {
		String type = clazz.getSimpleName();

		List<Tag> list = new ArrayList<>();

		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + type);

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String name = result.getString("Name");
				String tag = result.getString("Tag");

				list.add(new Tag(name, clazz, tag));
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
}
