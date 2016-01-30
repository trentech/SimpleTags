package com.gmail.trentech.simpletags.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.spongepowered.api.service.sql.SqlService;

import com.gmail.trentech.simpletags.Main;

public abstract class SQLUtils {

    protected static SqlService sql;

    protected static DataSource getDataSource() throws SQLException {
	    if (sql == null) {
	        sql = Main.getGame().getServiceManager().provide(SqlService.class).get();
	    }
	    
        return sql.getDataSource("jdbc:h2:./config/simpletags/tags");
	}

	public static void createTables() {
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Players (Name TEXT, Tag TEXT)");
			statement.executeUpdate();
			
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Worlds (Name TEXT, Tag TEXT)");
			statement.executeUpdate();
			
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Groups (Name TEXT, Tag TEXT)");
			statement.executeUpdate();
			
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Channels (Name TEXT, Tag TEXT)");
			statement.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}