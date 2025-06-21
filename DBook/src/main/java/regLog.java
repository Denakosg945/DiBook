

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//JDBC
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class regLog
 */
@WebServlet("/regLog")
@MultipartConfig
public class regLog extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String url = "jdbc:mariadb://localhost:3306/forumdb";
    private String user = "root";	
    private String pwd = "";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public regLog() {
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
		String password = request.getParameter("pwd");
		
		response.setContentType("application/json");
		
		if(username == null || password == null || password.isEmpty() || username.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"Error\": \"Username or password is empty\"}");
			return;
		}
		//Verify again if the name exists
		URL url = new URL("http://localhost:8082/DBook/CheckUserServlet");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);
		
		String jsonInput = "{\"username\": \"" + username + "\"}";
		
		OutputStream os = connection.getOutputStream();
		byte[] input = jsonInput.getBytes("utf-8");
		os.write(input,0,input.length);
			
		StringBuilder responseText = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseText.append(responseLine.trim());
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to check user \"}");
            return;
        } finally {
            connection.disconnect();
        }
		
		boolean userExists = false;
		String json = responseText.toString();
		if(json.contains("\"exists\":true")) {
			userExists = true;
		}
	

		
		JsonObject jsonResponse = new JsonObject();
		
		if(!userExists) {
			boolean created = createAccount(username,password);
			jsonResponse.addProperty("status", "created");
		    jsonResponse.addProperty("success", created);
			if(created) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType("application/json");
				response.getWriter().write(jsonResponse.toString());
			}else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.setContentType("application/json");
				response.getWriter().write(jsonResponse.toString());
			}
		}else {
			int logged = logIn(username,password);

		    boolean success = logged > 0;
		    jsonResponse.addProperty("success", success);
		    if (success) {
			    jsonResponse.addProperty("status", "logged");

		        jsonResponse.addProperty("userId", logged);
		        response.setStatus(HttpServletResponse.SC_OK);
		    } else {
		        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			    jsonResponse.addProperty("status", "error");
		        jsonResponse.addProperty("userId", logged);
		        jsonResponse.addProperty("error", "Login failed");
		    }
		    response.setStatus(HttpServletResponse.SC_OK);
		    response.setContentType("application/json");
		    response.getWriter().write(jsonResponse.toString());
		}
		
	}
	
	private boolean createAccount(String username, String password) {
		String hashedPassword = Hashing.getHash256(password);
		try {
			
			try {
				Class.forName("org.mariadb.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			Connection connection = DriverManager.getConnection(url,user,pwd);
			String sql = "INSERT INTO users(UNAME,UPASSHASH) VALUES(?,?)";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, hashedPassword);
			
			int rowsAffected = statement.executeUpdate();
			connection.commit();
			
			
			
			if(!connection.isClosed()) {
				connection.close();
			}
			
			return rowsAffected > 0;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private int logIn(String username,String password) {
		String hashedPassword = Hashing.getHash256(password);
		int id = -1;
		try {
			
			try {
				Class.forName("org.mariadb.jdbc.Driver");
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			Connection connection = DriverManager.getConnection(url,user,pwd);
			String sql = "SELECT id FROM users WHERE uname = ? AND upasshash = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, hashedPassword);
			
			// Execute the query
			ResultSet resultSet = statement.executeQuery();

			// Check if a result was found
			
			if (resultSet.next()) {
			    id = resultSet.getInt("id");
			}

			resultSet.close();
			statement.close();
			
			if(!connection.isClosed()) {
				connection.close();
			}
			

		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
		
	}

}
