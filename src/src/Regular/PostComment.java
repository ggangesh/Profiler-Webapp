package Regular;

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
 * Servlet implementation class PostComment
 */
@WebServlet("/PostComment")
public class PostComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostComment() {
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
		String text1=request.getParameter("Text1");
		HttpSession session=request.getSession(false);
        if(session.getAttribute("personId") == null)
        {
        	request.getRequestDispatcher("index.jsp").include(request, response);
        }
        else
        {
            int person_id = (int) session.getAttribute("personId");
            int post_id = Integer.parseInt(request.getParameter("post_id"));
            int newCommentId = DbHandler.getNewCommentId();
            Timestamp time1 = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            DbHandler.createNewComment(person_id, post_id,  text1, time1 , newCommentId);
        }
	}

}
