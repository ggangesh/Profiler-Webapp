package Messages;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DBHandler.DbHandler;

/**
 * Servlet implementation class Chatter
 */
@WebServlet("/Chatter")
public class Chatter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Chatter() {
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
            int chatbox_id = Integer.parseInt(request.getParameter("chatboxIdHidden"));
            int person_id_chatter = Integer.parseInt(request.getParameter("personIdHidden"));
            DbHandler.createNewChatter(chatbox_id, person_id_chatter);
            System.out.println("Created new chatter: chatbox_id: "+chatbox_id+" and person_id: "+person_id_chatter);
        }
	}

}
