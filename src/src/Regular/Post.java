package Regular;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import DBHandler.DbHandler;

/**
 * Servlet implementation class Post
 */
@WebServlet("/Post")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB

public class Post extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Post() {
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
	    PrintWriter out=response.getWriter();
		String value=request.getParameter("Addpost");
		if(value != null){
			int postWallId = Integer.parseInt(request.getParameter("postWallId"));
			String text1 = request.getParameter("Text1");
			String applicationPath = request.getServletContext().getRealPath("");
			System.out.println(applicationPath);
			String uploadFilePath = "/var/www/html/DBProjectImages";
	        File fileSaveDir = new File(uploadFilePath);
			System.out.println("Upload File Directory="+fileSaveDir.getAbsolutePath());
			int personId = (int) request.getSession().getAttribute("personId");
			String fileName = null;
			String fileNameImage = "";
			String fileNameVideo = "";
			String extension = null;
			String extensionImage = "";
			String extensionVideo = "";
			String image = "";
			String video = "";
			int newPostId = DbHandler.getNewPostId();
			fileNameImage = "postPic_"+newPostId;
			fileNameVideo = "postVideo_"+newPostId;
			//Get all the parts from request and write it to the file on server
	        for (Part part : request.getParts()) {
	        	if(part.getName().equalsIgnoreCase("postWallId") || part.getName().equalsIgnoreCase("Text1") || part.getName().equalsIgnoreCase("Link1")|| part.getName().equalsIgnoreCase("privacy"))
	        		continue;
	        	fileName = getFileName(part);
	        	System.out.println("File uploaded is "+fileName);
	        	if(fileName.equals(""))
	        		break;
	        	extension = fileName.substring(fileName.lastIndexOf("."));
	            if(extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".gif")){
	            	extensionImage = extension;
		        	part.write(uploadFilePath + File.separator + fileNameImage + extensionImage);
	            }
	            else{
	            	extensionVideo = extension;
		        	part.write(uploadFilePath + File.separator + fileNameVideo + extensionVideo);
	            }
	        }
	        if(extensionImage.equals(""))
	        	image ="";
	        else
	        	image = fileNameImage+extensionImage;
	        if(extensionVideo.equals(""))
	        	video = "";
	        else
	        	video = fileNameVideo+extensionVideo;
			String link = request.getParameter("Link1");
			String privacy = request.getParameter("Privacy");
			Timestamp time1 = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
			DbHandler.createNewPost(personId, postWallId,  text1, image, video, link, privacy, time1 , newPostId);
		    out.println("Posted sucessfully");
		    System.out.println("Posted sucessfully");
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.sendRedirect("Home");
		    //request.getRequestDispatcher("Home").forward(request, response);
		}
		out.close();
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
