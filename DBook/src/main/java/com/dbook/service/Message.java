package com.dbook.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

public class Message{
	private String message;
	private String username;
	private String date;
    private static String url = "jdbc:mariadb://localhost:3306/forumdb";
    private static String user = "root";	
    private static String pwd = "";  
	
	
	public Message(int usrId, String message,String date) throws SQLException {
		this.username = resolveUsername(usrId);
		this.message = message;
		this.date = date;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getUsername() {
		return username;
	}


	//Searches the database and resolves the username
	private String resolveUsername(int usrId) throws SQLException {
		String username = null;
		
		Connection connection = DriverManager.getConnection(url,user,pwd);
		String sql = "SELECT UNAME FROM users WHERE ID=?";
		
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1,usrId);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if(resultSet.next()) {
			username = resultSet.getString("UNAME");
		}
		
		resultSet.close();
		if(!connection.isClosed()) {
			connection.close();
		}
		
		return username;
	}
	
	public static List<Message> getAllMessages(int topicId) throws SQLException{
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Connection connection = DriverManager.getConnection(url,user,pwd);
		String sql = "SELECT USER_ID,MSG,DATE_SENT FROM messages WHERE TOPIC_ID=?";
		
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, topicId);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		List<Message> messages = new ArrayList<>();
		
		while(resultSet.next()) {
			Message message = new Message(
					resultSet.getInt("USER_ID"),
					resultSet.getString("MSG"),
					resultSet.getString("DATE_SENT")
			);	
			
			messages.add(message);
		}
		
		resultSet.close();
		
		if(!connection.isClosed()) {
			connection.close();
		}
		
		
		return messages;
	}
}
