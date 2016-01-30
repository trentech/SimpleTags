package com.gmail.trentech.simpletags.tags;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.utils.SQLUtils;

public class ConsoleTag extends SQLUtils {

	private final String name = "CONSOLE";
	private Text tag;

	private ConsoleTag(String tag){
		this.tag = TextSerializers.FORMATTING_CODE.deserialize(tag);
	}
	
	public ConsoleTag(){
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
	
	public static Optional<ConsoleTag> get(){
		Optional<ConsoleTag> optionalConsoleTag = Optional.empty();

		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("SELECT * FROM Players");
		    
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				if(result.getString("Name").equalsIgnoreCase("CONSOLE")){
					optionalConsoleTag = Optional.of(new ConsoleTag(result.getString("Tag")));		
					break;
				}			
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return optionalConsoleTag;
	}
	
	private void save(){
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("INSERT into Players (Name, Tag) VALUES (?, ?)");	
			
		    statement.setString(1, this.name);
		    statement.setString(2, "&6[CONSOLE]");

			statement.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void update(){
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("UPDATE Players SET Tag = ? WHERE Name = ?");	
			
		    statement.setString(1, TextSerializers.FORMATTING_CODE.serialize(this.tag));
		    statement.setString(2, this.name);

			statement.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
