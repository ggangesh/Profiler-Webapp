package Requests;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;

import DBHandler.DbHandler;

/**
 * Servlet implementation class Requests
 */
@WebServlet("/Requests")
public class Requests extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Requests() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");  
        HttpSession session=request.getSession(false);
        if(session != null)
        {
        	if(session.getAttribute("personId") != null){
	        	int person_id = (int) session.getAttribute("personId");
	        	String first_name = DbHandler.getFirstName(person_id);
			    request.setAttribute("first_name", first_name);
			    JSONArray requests = DbHandler.getRequests(person_id);
			    request.setAttribute("requests", requests);
			    System.out.println("requests: "+requests);
			    try {
					request.setAttribute("profile_pic", DbHandler.getWall((int) session.getAttribute("wallId")).getString("group_photo"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("Requests.jsp").forward(request, response);
        	}
        	else{
            	request.getRequestDispatcher("index.jsp").include(request, response);
        	}
        }
        else
        {
        	request.getRequestDispatcher("index.jsp").include(request, response);
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
