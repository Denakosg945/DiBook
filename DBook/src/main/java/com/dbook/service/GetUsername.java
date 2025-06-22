package com.dbook.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUsername {
	

    

	
	public static String getUsernameByUID(String uid) throws SQLException {
	    String url = "jdbc:mariadb://localhost:3306/forumdb";
	    String user = "root";	
	    String pwd = ""; 
		String username = null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Connection connection = DriverManager.getConnection(url,user,pwd);
		String sql = "SELECT UNAME FROM users WHERE ID =?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1,Integer.parseInt(uid));
		
		ResultSet usernameSet = statement.executeQuery();
		
		if(usernameSet.next()) {
			username = usernameSet.getString("uname");
		}
		
	    statement.close();
	    
	    if(!connection.isClosed()) {
	        connection.close();
	    }
		
		return username;
	}
}
