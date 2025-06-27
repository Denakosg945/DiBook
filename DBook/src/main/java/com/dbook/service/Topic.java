package com.dbook.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.http.HttpServletResponse;

public class Topic {
	private int id;
	private String topicName;
	private String description;
	
    private static String url = "jdbc:mariadb://localhost:3306/forumdb";
    private static String user = "root";	
    private static String pwd = "";
    
    public Topic(int id,String topicName,String description) {
    	this.id = id;
    	this.topicName = topicName;
    	this.description = description;
    }
	
	public static List<Topic> getAllTopics() throws SQLException {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Connection connection = DriverManager.getConnection(url,user,pwd);
		
		String sql = "SELECT ID,NAME,DESCRIPTION FROM topics";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		List<Topic> topics = new ArrayList<>();
		
		while(resultSet.next()) {
			Topic topic = new Topic(
					resultSet.getInt("ID"),
					resultSet.getString("NAME"),
					resultSet.getString("DESCRIPTION")
			);
			
			topics.add(topic);
		}
		
		if(!connection.isClosed()) {
			connection.close();
		}
		resultSet.close();
		
		return topics;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.topicName;
	}
	
	public String getDescription() {
		return this.description;
	}
}
