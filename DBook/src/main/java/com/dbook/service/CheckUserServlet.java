// Servlet API
package com.dbook.service;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// JSON parsing with Gson
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// JDBC
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// IO
import java.io.BufferedReader;
import java.io.IOException;


/**
 * Servlet implementation class CheckUserServlet
 */
@WebServlet("/CheckUserServlet")
public class CheckUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
    private String url = "jdbc:mariadb://localhost:3306/forumdb";
    private String user = "root";	
    private String pwd = "";
    
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckUserServlet() {
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
		
		BufferedReader reader = request.getReader();
		StringBuilder strBuilder = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null){
			strBuilder.append(line);
		}
		
		String requestBody = strBuilder.toString();
		
		JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
		String username = jsonObject.get("username").getAsString();
		
		ResultSet result =null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		try {
			
			
			Connection connection = DriverManager.getConnection(url,user,pwd);
			
			String sql = "SELECT UNAME FROM USERS WHERE UNAME = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,username);
			
			result = preparedStatement.executeQuery();
			
			JsonObject obj = new JsonObject();
			obj.addProperty("exists", result.next());
			String jsonResponse = obj.toString();
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(jsonResponse);
			
			if(!connection.isClosed()) {
				connection.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		
	}

}
