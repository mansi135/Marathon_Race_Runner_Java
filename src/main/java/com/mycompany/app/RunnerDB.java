package com.mycompany.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Mansi
 *
 * This class reads the runners' information from derby database.
 */
public class RunnerDB implements RaceConfigDAO {
	/**
	 * Connects to Derby database
	 */
	private Connection connect() throws SQLException {
		Connection connection = null;
		System.out.println("Connecting to database...");
		String dbDirectory = "Resources";
		System.setProperty("derby.system.home", dbDirectory);
		
		String url = "jdbc:derby:FinalDB";
        String user = "";
        String password = "";
        connection = DriverManager.getConnection(url, user, password);
        return connection;
	}

	/**
	 * Provides the list of Runners from database
	 */
    @Override
	public RaceConfig getRaceConfig() throws IOException{
		try {
			Connection connection = connect();
			
			String query = "SELECT Name, RunnersSpeed, RestPercentage "
							+ "FROM Runners";
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
            RaceConfig raceConfig = new RaceConfig();
			while(rs.next()) {
				String name = rs.getString("Name");
                int speed = rs.getInt("RunnersSpeed");
                int restPercent = rs.getInt("RestPercentage");
                
                raceConfig.add(name, speed, restPercent);
			}
			rs.close();
            ps.close();
            connection.close();          
            return raceConfig;
		}
		catch(SQLException sqle){
            throw new IOException("Error while getting runners from database", sqle);
		}
	}
}
