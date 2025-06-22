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
 * Servlet implementation class AddTopic
 */
@WebServlet("/AddTopic")
public class AddTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String url = "jdbc:mariadb://localhost:3306/forumdb";
    private String user = "root";	
    private String pwd = "";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTopic() {
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
		while((line = reader.readLine()) != null){
			strBuilder.append(line);
		}
		
		response.setContentType("application/json");
		String requestBody = strBuilder.toString();
		JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
		
		//get topic name from json
		String topicName = jsonObject.get("topicName").getAsString();
		//get topic description from json string
		String topicDescription = jsonObject.get("topicDescription").getAsString();
		
		if(topicName == null || topicName.isEmpty() || topicName.isBlank()) {
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
			Connection connection = DriverManager.getConnection(url,user,pwd);
			
			String sql = "INSERT INTO topics VALUES (null, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1,topicName);
			preparedStatement.setString(2,topicDescription);
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			JsonObject jsonObj = new JsonObject();
			response.setStatus(HttpServletResponse.SC_OK);
			if(rowsAffected > 0) {
				jsonObj.addProperty("success", true);
			}else {
				jsonObj.addProperty("success", false);
			}
			response.getWriter().write(jsonObj.toString());
			
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
	}

}
