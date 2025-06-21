

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class CheckLogin
 */
@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String url = "jdbc:mariadb://localhost:3306/forumdb";
    private String user = "root";	
    private String pwd = "";    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Cookie[] cookies = request.getCookies();
		boolean logged = false;
		String cookieUID = null;
		if(cookies != null) {
			for(Cookie cookie: cookies) {
				if("UID".equals(cookie.getName())) {
					cookieUID = cookie.getValue();
					break;
				}
			}
		}
		if(cookieUID !=null) {
			try {
				
				try {
					Class.forName("org.mariadb.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				Connection connection = DriverManager.getConnection(url,user,pwd);
				String sql = "SELECT id FROM users WHERE id=?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1,Integer.parseInt(cookieUID));
				ResultSet idSet = statement.executeQuery();
				
				if(idSet.next()) {
					logged = true;
				}
				
				idSet.close();
				statement.close();
				
				if(!connection.isClosed()) {
					connection.close();
				}
				
				
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		JsonObject jsonResponse = new JsonObject();
		response.setContentType("application/json");
		if(!logged) {
			jsonResponse.addProperty("logged", logged);
			response.setStatus(HttpServletResponse.SC_OK);
		    response.getWriter().write(jsonResponse.toString());
		}else {
			jsonResponse.addProperty("logged", logged);
			response.setStatus(HttpServletResponse.SC_OK);
		    response.getWriter().write(jsonResponse.toString());
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
