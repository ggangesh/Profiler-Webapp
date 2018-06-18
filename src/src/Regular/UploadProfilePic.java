package Regular;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import DBHandler.DbHandler;

/**
 * Servlet implementation class UploadProfilePic
 */
@WebServlet("/UploadProfilePic")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class UploadProfilePic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadProfilePic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String applicationPath = request.getServletContext().getRealPath("");
		System.out.println(applicationPath);
		String uploadFilePath = "/var/www/html/DBProjectImages";
        File fileSaveDir = new File(uploadFilePath);
		System.out.println("Upload File Directory="+fileSaveDir.getAbsolutePath());
		int personId = (int) request.getSession().getAttribute("personId");
		int wallId = (int) request.getSession().getAttribute("wallId");
		String fileName = null;
		String fileName1 = null;
		String extension = null;
		fileName1 = "profilePic_"+personId;
		
		//Get all the parts from request and write it to the file on server
        for (Part part : request.getParts()) {
        	fileName = getFileName(part);
        	if(fileName.equals(""))
        		break;
        	extension = fileName.substring(fileName.lastIndexOf("."));
            part.write(uploadFilePath + File.separator + fileName1 + extension);
        }
		DbHandler.updateProfilePic(personId, fileName1+extension);
		DbHandler.updateWallPic(wallId, fileName1+extension);
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    HttpSession session1=request.getSession();
        session1.invalidate();
        request.setAttribute("error", "Successfully registered");
        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
	}

	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
        System.out.println("content-disposition header= "+contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("file")) {
                return token.substring(token.indexOf("=") + 2, token.length()-1);
            }
        }
        return "";
	}

}
