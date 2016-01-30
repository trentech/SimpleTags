package com.gmail.trentech.simpletags.tags;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.utils.SQLUtils;

public class ChannelTag extends SQLUtils {

	private final String name;
	private Text tag;
	
	private ChannelTag(String group, String tag, boolean dummy){
		this.name = group;
		this.tag = TextSerializers.FORMATTING_CODE.deserialize(tag);
	}
	
	public ChannelTag(String group, String tag){
		this.name = group;
		this.tag = TextSerializers.FORMATTING_CODE.deserialize(tag);
		save();
	}
	
	public String getName(){
		return name;
	}
	
	public Text getTag(){
		return tag;
	}
	
	public void setTag(String tag){
		this.tag = TextSerializers.FORMATTING_CODE.deserialize(tag);
		update();
	}
	
	public static Optional<ChannelTag> get(String name){
		Optional<ChannelTag> optionalGroupTag = Optional.empty();

		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("SELECT * FROM Channels");
		    
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				if(result.getString("Name").equalsIgnoreCase(name)){
					optionalGroupTag = Optional.of(new ChannelTag(name, result.getString("Tag"), true));		
					break;
				}			
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return optionalGroupTag;
	}
	
	public static List<ChannelTag> all(){
		List<ChannelTag> list = new ArrayList<>();

		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("SELECT * FROM Channels");
		    
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				String name = result.getString("Name");
				String tag = result.getString("Tag");

		    	list.add(new ChannelTag(name, tag, true));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	private void save(){
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("INSERT into Channels (Name, Tag) VALUES (?, ?)");	
			
		    statement.setString(1, this.name);
		    statement.setString(2, TextSerializers.FORMATTING_CODE.serialize(this.tag));

			statement.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void update(){
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("UPDATE Worlds SET Channels = ? WHERE Name = ?");	
			
		    statement.setString(1, TextSerializers.FORMATTING_CODE.serialize(this.tag));
		    statement.setString(2, this.name);

			statement.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(){
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("DELETE from Channels WHERE Name = ?");
		    
			statement.setString(1, this.name);
			statement.executeUpdate();
			
			connection.close();
			
			for(Task task : Main.getGame().getScheduler().getScheduledTasks()){
				if(task.getName().contains(this.name)){
					task.cancel();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
