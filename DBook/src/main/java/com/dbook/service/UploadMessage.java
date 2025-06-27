package com.dbook.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class UploadMessage
 */
@WebServlet("/UploadMessage")
public class UploadMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String url = "jdbc:mariadb://localhost:3306/forumdb";
    private static String user = "root";	
    private static String pwd = "";    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		BufferedReader reader = request.getReader();
		StringBuilder strBuilder = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null) {
			strBuilder.append(line);
		}
		
		response.setContentType("application/json");
		JsonObject jsonObject = JsonParser.parseString(strBuilder.toString()).getAsJsonObject();
		
		int topicId = jsonObject.get("topicId").getAsInt();
		String message = jsonObject.get("message").getAsString();
		int UID = Integer.parseInt(jsonObject.get("UID").getAsString());
		
		if(message == null || message.isEmpty() || message.isBlank()) {
			JsonObject jsonObj = new JsonObject();
			jsonObj.addProperty("success", false);
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(jsonObj.toString());
			return;
		}
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		try {
			
			JsonObject jsonObj = new JsonObject();
			
			Connection connection = DriverManager.getConnection(url,user,pwd);
			String sql = "INSERT INTO messages VALUES (null,?,?,?,null)";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, topicId);
			preparedStatement.setInt(2, UID);
			preparedStatement.setString(3, message);
			
			int affectedRows = preparedStatement.executeUpdate();
			
			if(affectedRows > 0) {
				sql = "SELECT COUNT(*) FROM MESSAGES WHERE TOPIC_ID = ?";
				preparedStatement = connection.prepareStatement(sql);
				
				preparedStatement.setInt(1, topicId);
				
				ResultSet resultSet = preparedStatement.executeQuery();
				int messagesUploaded = 0;
				while(resultSet.next()) {
					messagesUploaded = resultSet.getInt(1);
				}
				
				resultSet.close();
				if(!connection.isClosed()) {
					connection.close();
				}
				
				response.setStatus(HttpServletResponse.SC_OK);
				jsonObj.addProperty("success", true);
				jsonObj.addProperty("messages", messagesUploaded);
			}else {
				response.setStatus(HttpServletResponse.SC_OK);
				jsonObj.addProperty("success", false);
				jsonObj.addProperty("messages", 0);
			}
			
			response.getWriter().write(jsonObj.toString());
			
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
	}

}
