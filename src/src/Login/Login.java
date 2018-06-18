package Login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import DBHandler.DbHandler;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		JSONObject jsonobject = DbHandler.authenticate(username, password);
		boolean status;
		try {
			status = jsonobject.getBoolean("status");
			if(status){
				HttpSession session=request.getSession(true);
				response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
		        session.setAttribute("personId", jsonobject.getInt("personId"));
		        session.setAttribute("wallId", jsonobject.getInt("wallId"));
		        response.sendRedirect("Home");
			}
			else{
				String error = jsonobject.getString("message");
				response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    request.setAttribute("error", error);
			    HttpSession session1=request.getSession(false);
		        session1.invalidate();
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in doPost of Login");
			e.printStackTrace();
		}
	}

}
