package Requests;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;

import DBHandler.DbHandler;

/**
 * Servlet implementation class AcceptRequest
 */
@WebServlet("/AcceptRequest")
public class AcceptRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AcceptRequest() {
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
		response.setContentType("text/html");  
        HttpSession session=request.getSession(false);
        if(session.getAttribute("personId") == null)
        {
        	request.getRequestDispatcher("index.jsp").include(request, response);
        }
        else
        {
            int wall_id = (int) session.getAttribute("wallId");
		    DbHandler.acceptRequest(wall_id, Integer.parseInt(request.getParameter("person_id")));
		    System.out.println("Request accepted");
        }
	}

}
