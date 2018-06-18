package Login;

import java.io.IOException;
import java.sql.Timestamp;

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
 * Servlet implementation class RegisterConfirm
 */
@WebServlet("/RegisterConfirm")
public class RegisterConfirm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterConfirm() {
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
		
		String value=request.getParameter("RegisterButton");
		if(value != null)
		{
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String dateOfBirthS = request.getParameter("dateOfBirth");
			//System.out.println(dateOfBirthS);
			dateOfBirthS = dateOfBirthS + " 00:00:00.0";
			//System.out.println(dateOfBirthS);
			Timestamp dateOfBirth = Timestamp.valueOf(dateOfBirthS);
			String gender = request.getParameter("gender");
			String mobileno = request.getParameter("mobileno");
			String email = request.getParameter("email");
			String address = request.getParameter("address");
			String other = request.getParameter("other");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String confirmPassword = request.getParameter("confirm_password");
			/*checking username*/
			JSONObject jsonobject = DbHandler.checkUsername(username);
			boolean status;
			try {
				status = jsonobject.getBoolean("status");
				if(status){
					if(!password.equals(confirmPassword)){
						response.setContentType("application/json");
					    response.setCharacterEncoding("UTF-8");
					    request.setAttribute("error", "ERROR : Passwords don't match");
					    HttpSession session1=request.getSession();
				        session1.invalidate();
						request.getRequestDispatcher("Register.jsp").forward(request, response);
					}else{
						JSONObject ids = DbHandler.registerUser(firstName, lastName, dateOfBirth, gender, mobileno, email, address, other, username, password);
						response.setContentType("application/json");
					    response.setCharacterEncoding("UTF-8");
					    HttpSession session1=request.getSession();
				        session1.setAttribute("personId", ids.getInt("person_id")); 
				        session1.setAttribute("wallId", ids.get("wall_id"));
				        request.getRequestDispatcher("UploadProfilePic.jsp").forward(request, response);
					}
				}
				else{
					response.setContentType("application/json");
				    response.setCharacterEncoding("UTF-8");
				    request.setAttribute("error", "ERROR : Username already exists");
				    HttpSession session1=request.getSession();
			        session1.invalidate();
					request.getRequestDispatcher("Register.jsp").forward(request, response);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in doPost of RegisterConfirm");
				e.printStackTrace();
			}

		}
		else
		{
			value= request.getParameter("CancelButton");
			if(value.equals("Cancel"))
			{
				response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    request.getRequestDispatcher("index.jsp").forward(request, response);
			    
			}
			else
			{
				
			}
		}
	}

}
