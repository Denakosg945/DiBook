package com.dbook.service;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * Servlet implementation class ForgotPassword
 */
@WebServlet("/ForgotPassword")
@MultipartConfig
public class ForgotPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String url = "jdbc:mariadb://localhost:3306/forumdb";
    private String user = "root";	
    private String pwd = "";    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForgotPassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("uname");
		String password = request.getParameter("newPwd");
		String oldPassword = request.getParameter("pwd");
		
		System.out.println(username + password + oldPassword);
		boolean success = false;
		
		try {
			success = resetPassword(username,oldPassword,password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		JsonObject jsonResponse = new JsonObject();
		response.setContentType("application/json");
		jsonResponse.addProperty("success", success);
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(jsonResponse.toString());

	}

	private boolean resetPassword(String username, String oldPassword, String password) throws SQLException {
		String hashedOldPassword = Hashing.getHash256(oldPassword);
		String hashedPassword = Hashing.getHash256(password);
		
		
		try {
			try {
				Class.forName("org.mariadb.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			Connection connection = DriverManager.getConnection(url,user,pwd);
			String sql = "UPDATE users SET upasshash = ? WHERE id IN (SELECT id FROM users WHERE uname = ? AND upasshash = ?)";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1,hashedPassword);
			statement.setString(2, username);
			statement.setString(3, hashedOldPassword);
			
			int rowsAffected = statement.executeUpdate();
			connection.commit();
			
			statement.close();
			
			if(!connection.isClosed()) {
				connection.close();
			}
			
			return rowsAffected > 0;
			
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
	}

}
