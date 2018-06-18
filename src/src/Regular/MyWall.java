package Regular;

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
 * Servlet implementation class Wall
 */
@WebServlet("/MyWall")
public class MyWall extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyWall() {
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
        if(session.getAttribute("personId") == null)
        {
        	request.getRequestDispatcher("index.jsp").include(request, response);
        }
        else
        {
            int wallId = (int) session.getAttribute("wallId");
            session.setAttribute("last_wall_id", wallId);
            JSONObject obj = DbHandler.getWall(wallId);
		    try {
				request.setAttribute("name", obj.getString("Name"));
				request.setAttribute("myname", obj.getString("Name"));
				request.setAttribute("group_description", obj.getString("group_description"));
				request.setAttribute("group_photo", obj.getString("group_photo"));
				request.setAttribute("profile_pic", obj.getString("group_photo"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in doGet of myWall");
				e.printStackTrace();
			}
			request.getRequestDispatcher("Wall.jsp").forward(request, response);
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
