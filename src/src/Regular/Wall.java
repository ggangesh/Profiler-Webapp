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
@WebServlet("/Wall")
public class Wall extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Wall() {
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
        	System.out.println("WallId received: "+request.getParameter("wallIdHidden"));
            int wallId =  Integer.parseInt(request.getParameter("wallIdHidden"));
            session.setAttribute("last_wall_id", wallId);
            JSONObject obj = DbHandler.getWall(wallId);
            JSONObject myobj = DbHandler.getWall((int)session.getAttribute("wallId"));
            boolean follow = DbHandler.isFollowed(wallId, (int) session.getAttribute("personId"));
		    try {
		    	request.setAttribute("follow", follow);
		    	request.setAttribute("WallId", wallId);
				request.setAttribute("name", obj.getString("Name"));
				request.setAttribute("myname", myobj.getString("Name"));
				request.setAttribute("group_description", obj.getString("group_description"));
				request.setAttribute("group_photo", obj.getString("group_photo"));
				request.setAttribute("profile_pic", myobj.getString("group_photo"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in doGet of myWall");
				e.printStackTrace();
			}
			request.getRequestDispatcher("Wall.jsp").forward(request, response);
        }
	}

}
