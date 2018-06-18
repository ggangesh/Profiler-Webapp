package DBHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DbHandler {
	// connection strings
	private static String connString = DbHandlerParameters.connString;
	private static String userName = DbHandlerParameters.userName;
	private static String passWord = DbHandlerParameters.passWord;
	
	public static void follow(int person_id, int wall_id){
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query1 = "insert into request values(?, ?);";
			PreparedStatement preparedStmt1 = conn.prepareStatement(query1);
			preparedStmt1.setInt(1, person_id);
			preparedStmt1.setInt(2, wall_id);
			preparedStmt1.executeUpdate();
			preparedStmt1.close();
			conn.close();
		}
		catch(Exception e){
			System.out.println("Error in follow");
			System.out.println(e);
		}
		return;	
	}
	
	public static void unfollow(int person_id, int wall_id){
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "delete from follower where person_id=? and group_or_wall_id=?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, person_id);
			preparedStmt.setInt(2, wall_id);
			preparedStmt.executeUpdate();
			preparedStmt.close();
			conn.close();
		}
		catch(Exception e){
			System.out.println("Error in unfollow");
			System.out.println(e);
		}
		return;	
	}
	
	public static boolean isFollowed(int wall_id, int person_id){
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select count(*) from Follower where person_id=? and group_or_wall_id=?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, person_id);
			preparedStmt.setInt(2, wall_id);
			ResultSet result =  preparedStmt.executeQuery();
			if(result.next()){
				int follow = result.getInt(1);
				preparedStmt.close();
				conn.close();
				if(follow == 1)
					return true;
				else
					return false;
			}
			else{
				preparedStmt.close();
				conn.close();
				return false;
			}
		} 
		catch(Exception e){
			System.out.println("Error in isFollow");
			System.out.println(e);
			return false;
		}
	}
	
	public static JSONObject authenticate(String username, String password){		
		JSONObject obj = new JSONObject();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select Person_id, first_name from Person where Username=? and Password=?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, username);
			preparedStmt.setString(2, password);
			ResultSet result =  preparedStmt.executeQuery();
			int personId = -1;
			String first_name="";
			if(result.next()){
				personId = result.getInt("person_id");
				first_name = result.getString("first_name");
				obj.put("status",true);				
				obj.put("personId", personId);
				obj.put("first_name", first_name);
				String queryWallId = "select Admin.Group_or_Wall_id from Admin, Group_or_Wall where Admin.Group_or_Wall_id = Group_or_Wall.Group_or_Wall_id and Type=1 and Person_id = ?";
				PreparedStatement preparedStmtWallId = conn.prepareStatement(queryWallId);
				preparedStmtWallId.setInt(1, personId);
				ResultSet resultWallId =  preparedStmtWallId.executeQuery();
				if(resultWallId.next()){
					obj.put("wallId", resultWallId.getInt("Group_or_Wall_id"));
				}
				else{
					System.out.println("No wall exists for this user");
				}
				
			}
			else{
				obj.put("status",false);
				obj.put("message", "Authentication Failed");	
			}
			preparedStmt.close();
			conn.close();
		} 
		catch(Exception e){
			System.out.println("Error in authenticate");
			System.out.println(e);
		}
		return obj;
	}
	
	public static JSONArray getNewsFeed(int person_id){
		
		JSONArray jsonObj = new JSONArray();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select post.post_id, post.text1, post.image, post.video, post.link, post.privacy, post.time1, person.first_name, person.last_name, group_or_wall.group_or_wall_id "
					+ "from (follower natural join posted_on natural join post), (posted_by natural join (person natural join admin natural join group_or_wall)) "
					+ "where follower.person_id=? and posted_by.post_id=post.post_id and group_or_wall.type=1 "
					+ "order by post.time1 desc "
					+ "limit 50";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, person_id);
			ResultSet result =  preparedStmt.executeQuery();
			jsonObj = ResultSetConverter(result);
			preparedStmt.close();
			conn.close();
			 
		} catch(Exception e){
			System.out.println(e);
		}
		
		return jsonObj;
	}
	
public static JSONArray getTimeLine(int wall_id, int person_id, int my_wall_id){
		
		JSONArray jsonObj = new JSONArray();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query1 = "Select count(*) " 
					+ "from follower " 
					+ "where follower.group_or_wall_id=? and follower.person_id=?";
			PreparedStatement preparedStmt1 = conn.prepareStatement(query1);
			preparedStmt1.setInt(1, wall_id);
			preparedStmt1.setInt(2, person_id);
			ResultSet result1 =  preparedStmt1.executeQuery();
			result1.next();
			int privacy = result1.getInt(1);
			String query="";
			if(wall_id == my_wall_id)
			{
				query = "select post.post_id, post.text1, post.image, post.video, post.link, post.privacy, post.time1, person.first_name, person.last_name, group_or_wall.group_or_wall_id "
						+ "from (posted_on natural join post), (posted_by natural join (person natural join admin natural join group_or_wall)) "
						+ "where posted_on.group_or_wall_id=? and posted_by.post_id=post.post_id and group_or_wall.type=1 "
						+ "order by post.time1 desc "
						+ "limit 50";
			}
			else if(privacy == 0){
			query = "select post.post_id, post.text1, post.image, post.video, post.link, post.privacy, post.time1, person.first_name, person.last_name, group_or_wall.group_or_wall_id "
					+ "from (posted_on natural join post), (posted_by natural join (person natural join admin natural join group_or_wall)) "
					+ "where posted_on.group_or_wall_id=? and posted_by.post_id=post.post_id and group_or_wall.type=1 and post.privacy = 0 "
					+ "order by post.time1 desc "
					+ "limit 50";
			}
			else{
			query = "select post.post_id, post.text1, post.image, post.video, post.link, post.privacy, post.time1, person.first_name, person.last_name, group_or_wall.group_or_wall_id "
					+ "from (posted_on natural join post), (posted_by natural join (person natural join admin natural join group_or_wall)) "
					+ "where posted_on.group_or_wall_id=? and posted_by.post_id=post.post_id and group_or_wall.type=1 and post.privacy !=2 "
					+ "order by post.time1 desc "
					+ "limit 50";
			}
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, wall_id);
			ResultSet result =  preparedStmt.executeQuery();
			jsonObj = ResultSetConverter(result);
			preparedStmt.close();
			conn.close();
			 
		} catch(Exception e){
			System.out.println(e);
		}
		
		return jsonObj;
	}
	
	public static JSONArray getComments(int post_id){
		
		JSONArray jsonObj = new JSONArray();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select comment_on_by.person_id, comment_on_by.comment_id, comment.text1, comment.time1, person.first_name, person.last_name, person.profile_picture, group_or_wall.group_or_wall_id "
					+ "from (comment_on_by natural join comment) natural join ((person natural join admin) natural join group_or_wall) "
					+ "where group_or_wall.type=1 and post_id=? "
					+ "order by comment.time1 desc "
					+ "limit 10 ";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, post_id);
			ResultSet result =  preparedStmt.executeQuery();
			jsonObj = ResultSetConverter(result);
			preparedStmt.close();
			conn.close();
			 
		} catch(Exception e){
			System.out.println(e);
		}
		
		return jsonObj;
	}
	
	public static JSONArray getChats(int chatbox_id){
		
		JSONArray jsonObj = new JSONArray();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select message.person_id, message.chatbox_id, message.message_text, message.time1, person.first_name, person.last_name, person.profile_picture, group_or_wall.group_or_wall_id "
					+ "from message natural join ((person natural join admin) natural join group_or_wall) "
					+ "where group_or_wall.type=1 and chatbox_id=? "
					+ "order by message.time1 desc "
					+ "limit 20 ";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, chatbox_id);
			ResultSet result =  preparedStmt.executeQuery();
			jsonObj = ResultSetConverter(result);
			preparedStmt.close();
			conn.close();
			 
		} catch(Exception e){
			System.out.println(e);
		}
		
		return jsonObj;
	}
	
	public static JSONArray getWalls(){
		
		JSONArray jsonObj = new JSONArray();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select * from group_or_wall";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			ResultSet result =  preparedStmt.executeQuery();
			jsonObj = ResultSetConverter(result);
			preparedStmt.close();
			conn.close();
			 
		} catch(Exception e){
			System.out.println(e);
		}
		
		return jsonObj;
	}
	
	public static JSONArray getRequests(int wall_id){
		
		JSONArray jsonObj = new JSONArray();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select person_id, person.first_name, person.last_name from request natural join person where group_or_wall_id=?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, wall_id);
			ResultSet result =  preparedStmt.executeQuery();
			jsonObj = ResultSetConverter(result);
			preparedStmt.close();
			conn.close();
			 
		} catch(Exception e){
			System.out.println(e);
		}
		
		return jsonObj;
	}
	
public static void acceptRequest(int wall_id, int person_id){
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "insert into follower values(?, ?);";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, person_id);
			preparedStmt.setInt(2, wall_id);
			preparedStmt.executeUpdate();
			preparedStmt.close();
			String query1 = "delete from request where person_id=? and group_or_wall_id=?;";
			PreparedStatement preparedStmt1 = conn.prepareStatement(query1);
			preparedStmt1.setInt(1, person_id);
			preparedStmt1.setInt(2, wall_id);
			preparedStmt1.executeUpdate();
			preparedStmt1.close();
			conn.close();
			 
		} catch(Exception e){
			System.out.println(e);
		}
		
		return;
	}
	
	public static JSONArray getPeople(){
		
		JSONArray jsonObj = new JSONArray();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select person_id, first_name, last_name, other_details from person";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			ResultSet result =  preparedStmt.executeQuery();
			jsonObj = ResultSetConverter(result);
			preparedStmt.close();
			conn.close();
			 
		} catch(Exception e){
			System.out.println(e);
		}
		
		return jsonObj;
	}
	
	public static JSONArray getChatBoxes(int person_id){
		
		JSONArray jsonObj = new JSONArray();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select name, chatbox_id from chatbox natural join chatters where chatters.person_id =? order by chatbox_id desc";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, person_id);
			ResultSet result =  preparedStmt.executeQuery();
			jsonObj = ResultSetConverter(result);
			preparedStmt.close();
			conn.close();
			 
		} catch(Exception e){
			System.out.println(e);
		}
		
		return jsonObj;
	}
	
	public static int getNewPostId(){
		int post_id = -1;
		try{
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			
			String query = "select ID from ids where table1='Post';";
			PreparedStatement preparedStmtId = conn.prepareStatement(query);
			ResultSet result =  preparedStmtId.executeQuery();
			result.next();
			post_id = result.getInt("ID");
			preparedStmtId.close();
			conn.close();
		}
		catch(Exception e){
			System.out.println("Error in gtNewPostId");
			System.out.println(e);
		}
		return post_id;
	}
	
	public static int getNewCommentId(){
		int post_id = -1;
		try{
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			
			String query = "select ID from ids where table1='Comment';";
			PreparedStatement preparedStmtId = conn.prepareStatement(query);
			ResultSet result =  preparedStmtId.executeQuery();
			result.next();
			post_id = result.getInt("ID");
			preparedStmtId.close();
			conn.close();
		}
		catch(Exception e){
			System.out.println("Error in getNewCommentId");
			System.out.println(e);
		}
		return post_id;
	}

	public static int getNewChatBoxId(){
		int post_id = -1;
		try{
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			
			String query = "select ID from ids where table1='Chatbox';";
			PreparedStatement preparedStmtId = conn.prepareStatement(query);
			ResultSet result =  preparedStmtId.executeQuery();
			result.next();
			post_id = result.getInt("ID");
			preparedStmtId.close();
			conn.close();
		}
		catch(Exception e){
			System.out.println("Error in getNewChatboxId");
			System.out.println(e);
		}
		return post_id;
	}

	
	public static void createNewPost(int person_id, int group_or_wall_id, String text1, String image, String video, String link, String privacy, Timestamp time1, int post_id){
		try{
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			
			String queryCreatePost = "insert into Post values(?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement preparedStmtCreatePost = conn.prepareStatement(queryCreatePost);
			preparedStmtCreatePost.setInt(1, post_id);
			preparedStmtCreatePost.setString(2, text1);
			preparedStmtCreatePost.setString(3, image);
			preparedStmtCreatePost.setString(4, video);
			preparedStmtCreatePost.setString(5, link);
			preparedStmtCreatePost.setObject(6, privacy, java.sql.Types.NUMERIC);
			preparedStmtCreatePost.setTimestamp(7, time1);
			preparedStmtCreatePost.executeUpdate();
			preparedStmtCreatePost.close();
			String queryPostedOn = "insert into Posted_on values(?, ?);";
			PreparedStatement preparedStmtPostedOn = conn.prepareStatement(queryPostedOn);
			preparedStmtPostedOn.setInt(1, post_id);
			preparedStmtPostedOn.setInt(2, group_or_wall_id);
			preparedStmtPostedOn.executeUpdate();
			preparedStmtPostedOn.close();
			String queryPostedBy = "insert into Posted_By values(?, ?);";
			PreparedStatement preparedStmtPostedBy = conn.prepareStatement(queryPostedBy);
			preparedStmtPostedBy.setInt(1, post_id);
			preparedStmtPostedBy.setInt(2, person_id);
			preparedStmtPostedBy.executeUpdate();
			preparedStmtPostedBy.close();
			String queryUpdate = "update ids set id=(id +1) where table1='Post';";
			PreparedStatement preparedStmtUpdate = conn.prepareStatement(queryUpdate);
			preparedStmtUpdate.executeUpdate();
			preparedStmtUpdate.close();
			conn.close();
		}
		catch(Exception e){
			System.out.println("Error in createNewPost");
			System.out.println(e);
		}
		return;
	}
	
	public static void createNewComment(int person_id, int post_id, String text1, Timestamp time1, int comment_id){
		try{
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			
			String queryCreatePost = "insert into Comment values(?, ?, ?);";
			PreparedStatement preparedStmtCreatePost = conn.prepareStatement(queryCreatePost);
			preparedStmtCreatePost.setInt(1, comment_id);
			preparedStmtCreatePost.setString(2, text1);
			preparedStmtCreatePost.setTimestamp(3, time1);
			preparedStmtCreatePost.executeUpdate();
			preparedStmtCreatePost.close();
			String queryPostedOn = "insert into Comment_on_by values(?, ?, ?);";
			PreparedStatement preparedStmtPostedOn = conn.prepareStatement(queryPostedOn);
			preparedStmtPostedOn.setInt(1, person_id);
			preparedStmtPostedOn.setInt(2, comment_id);
			preparedStmtPostedOn.setInt(3, post_id);
			preparedStmtPostedOn.executeUpdate();
			preparedStmtPostedOn.close();
			String query = "select ID from ids where table1='Notification';";
			PreparedStatement preparedStmtId = conn.prepareStatement(query);
			ResultSet result =  preparedStmtId.executeQuery();
			result.next();
			int notification_id = result.getInt("ID");
			preparedStmtId.close();
			String queryNotification = "insert into Notification values(?, ?, ?, ?);";
			PreparedStatement preparedStmtNotification = conn.prepareStatement(queryNotification);
			preparedStmtNotification.setInt(1, notification_id);
			preparedStmtNotification.setString(2, getFirstName(person_id)+" commented \""+text1.substring(0, Math.min(10, text1.length()))+"\" on your post");
			preparedStmtNotification.setObject(3, 1, java.sql.Types.NUMERIC);
			preparedStmtNotification.setTimestamp(4, time1);
			preparedStmtNotification.executeUpdate();
			preparedStmtNotification.close();
			String queryUpdate = "update ids set id=(id +1) where table1='Comment' or table1='Notification';";
			PreparedStatement preparedStmtUpdate = conn.prepareStatement(queryUpdate);
			preparedStmtUpdate.executeUpdate();
			preparedStmtUpdate.close();
			String queryNotificationFor = "insert into Notification_for values(?, (select person_id from posted_by where post_id=?));";
			PreparedStatement preparedStmtNotificationFor = conn.prepareStatement(queryNotificationFor);
			preparedStmtNotificationFor.setInt(1, notification_id);
			preparedStmtNotificationFor.setInt(2, post_id);
			preparedStmtNotificationFor.executeUpdate();
			conn.close();
		}
		catch(Exception e){
			System.out.println("Error in createNewComment");
			System.out.println(e);
		}
		return;
	}
	
	public static void createNewChat(int person_id, int chatbox_id, String text1, Timestamp time1){
		try{
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			
			String queryCreatePost = "insert into Message values(?, ?, ?, ?);";
			PreparedStatement preparedStmtCreatePost = conn.prepareStatement(queryCreatePost);
			preparedStmtCreatePost.setInt(1, chatbox_id);
			preparedStmtCreatePost.setInt(2, person_id);
			preparedStmtCreatePost.setTimestamp(3, time1);
			preparedStmtCreatePost.setString(4, text1);
			preparedStmtCreatePost.executeUpdate();
			preparedStmtCreatePost.close();
			conn.close();
		}
		catch(Exception e){
			System.out.println("Error in createNewComment");
			System.out.println(e);
		}
		return;
	}
	
	public static void createNewChatter(int chatbox_id, int person_id){
		try{
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			
			String queryCreatePost = "insert into Chatters values(?, ?);";
			PreparedStatement preparedStmtCreatePost = conn.prepareStatement(queryCreatePost);
			preparedStmtCreatePost.setInt(1, chatbox_id);
			preparedStmtCreatePost.setInt(2, person_id);
			preparedStmtCreatePost.executeUpdate();
			preparedStmtCreatePost.close();
			String queryUpdate = "update chatbox set name=concat(name , ' "+getFirstName(person_id)+"') where chatbox_id=?;";
			PreparedStatement preparedStmtUpdate = conn.prepareStatement(queryUpdate);
			preparedStmtUpdate.setInt(1, chatbox_id);
			preparedStmtUpdate.executeUpdate();
			preparedStmtUpdate.close();
			conn.close();
		}
		catch(Exception e){
			System.out.println("Error in createNewChatter");
			System.out.println(e);
		}
		return;
	}
	
	public static void createNewChatBox(int person_id, Timestamp time1, int chatbox_id){
		try{
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			
			String queryCreatePost = "insert into chatbox values(?, ?);";
			PreparedStatement preparedStmtCreatePost = conn.prepareStatement(queryCreatePost);
			preparedStmtCreatePost.setInt(1, chatbox_id);
			preparedStmtCreatePost.setString(2, getFirstName(person_id));
			preparedStmtCreatePost.executeUpdate();
			preparedStmtCreatePost.close();
			String queryPostedOn = "insert into Chatters values(?, ?);";
			PreparedStatement preparedStmtPostedOn = conn.prepareStatement(queryPostedOn);
			preparedStmtPostedOn.setInt(1, chatbox_id);
			preparedStmtPostedOn.setInt(2, person_id);
			preparedStmtPostedOn.executeUpdate();
			preparedStmtPostedOn.close();
			String queryUpdate = "update ids set id=(id +1) where table1='Chatbox';";
			PreparedStatement preparedStmtUpdate = conn.prepareStatement(queryUpdate);
			preparedStmtUpdate.executeUpdate();
			preparedStmtUpdate.close();
			
			conn.close();
		}
		catch(Exception e){
			System.out.println("Error in createNewComment");
			System.out.println(e);
		}
		return;
	}
	
	public static JSONObject getWall(int wallId){		
		JSONObject obj = new JSONObject();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select * from Group_or_Wall where group_or_wall_id=?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, wallId);
			ResultSet result =  preparedStmt.executeQuery();
			if(result.next()){
				obj.put("Name", result.getString("Name"));
				obj.put("group_description", result.getString("group_description"));
				obj.put("group_photo", result.getString("group_photo"));
			}
			else{
				System.out.println("Given person_id doesn't exist");
			}
			preparedStmt.close();
			conn.close();
		} 
		catch(Exception e){
			System.out.println("Error in authenticate");
			System.out.println(e);
		}
		return obj;
	}
	
	public static String getFirstName(int personId){		
		String first_name="";
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select first_name from Person where person_id=?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, personId);
			ResultSet result =  preparedStmt.executeQuery();
			if(result.next()){
				first_name = result.getString("first_name");
				return first_name;
			}
			else{
				System.out.println("Given person_id doesn't exist");
			}
			preparedStmt.close();
			conn.close();
		} 
		catch(Exception e){
			System.out.println("Error in authenticate");
			System.out.println(e);
		}
		return first_name;
	}
	
	public static JSONObject checkUsername(String username){		
		JSONObject obj = new JSONObject();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "select count(*) from Person where Username=?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, username);
			ResultSet result =  preparedStmt.executeQuery();
			result.next();
			boolean ans = !(result.getInt(1) > 0); 
			preparedStmt.close();
			conn.close();
			if(ans==true){
				obj.put("status",true);
				
			}
			else{						
					obj.put("status",false);					
			}			
		} 
		catch(Exception e){
			System.out.println("Error in checkUsername");
			System.out.println(e);
		}
		return obj;
	}
	
	public static JSONObject registerUser(String firstName, String  lastName, Timestamp dateOfBirth, String gender, String mobileno, String email,  String address, String other, String username, String password){		
		JSONObject obj = new JSONObject();
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "insert  into person values ((select ID from ids where Table1='Person'), ?, ?, ?, ?, ?, ?, ?, ?, 'logo.png', ?, ?);";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, firstName);
			preparedStmt.setString(2, lastName);
			preparedStmt.setTimestamp(3, dateOfBirth);
			if(gender.equalsIgnoreCase("male")){
				preparedStmt.setObject(4, 0, java.sql.Types.NUMERIC);				
			}
			else{
				if(gender.equalsIgnoreCase("female")){
					preparedStmt.setObject(4, 1, java.sql.Types.NUMERIC);				
				}
				else{
					preparedStmt.setObject(4, 2, java.sql.Types.NUMERIC);
				}
			}
			preparedStmt.setObject(5, mobileno, java.sql.Types.NUMERIC);
			preparedStmt.setString(6, email);
			preparedStmt.setString(7, username);
			preparedStmt.setString(8, password);
			preparedStmt.setString(9, address);
			preparedStmt.setString(10, other);
			preparedStmt.executeUpdate();
			preparedStmt.close();
			String personId = "select ID from ids where table1='Person';";
			PreparedStatement preparedStmtId = conn.prepareStatement(personId);
			preparedStmtId.executeQuery();
			ResultSet result =  preparedStmtId.executeQuery();
			result.next();
			int person_id = result.getInt("ID");
			obj.put("person_id", person_id);
			preparedStmtId.close();
			
			String queryWall = "insert into group_or_wall values((select ID from ids where Table1='Group_or_Wall'), ?, ?, ?,'logo.png');";
			PreparedStatement preparedStmtWall = conn.prepareStatement(queryWall);
			preparedStmtWall.setString(1,firstName + " " + lastName );
			preparedStmtWall.setObject(2, 1, java.sql.Types.NUMERIC);// For PErson the Type is 1
			preparedStmtWall.setString(3, other);
			preparedStmtWall.executeUpdate();
			preparedStmtWall.close();
			
			String wallId = "select ID from ids where table1='Group_or_Wall';";
			PreparedStatement preparedStmtwallId = conn.prepareStatement(wallId);
			preparedStmtwallId.executeQuery();
			ResultSet resultwall =  preparedStmtwallId.executeQuery();
			resultwall.next();
			int wall_id = resultwall.getInt("ID");
			obj.put("wall_id", wall_id);
			preparedStmtId.close();
			
			String admin = "insert into Admin values(?, ?);";
			PreparedStatement preparedStmtAdmin = conn.prepareStatement(admin);
			preparedStmtAdmin.setInt(1, person_id);
			preparedStmtAdmin.setInt(2, wall_id);
			preparedStmtAdmin.executeUpdate();
			preparedStmtAdmin.close();
			
			String follower = "insert into Follower values(?, ?);";
			PreparedStatement preparedStmtFollower = conn.prepareStatement(follower);
			preparedStmtFollower.setInt(1, person_id);
			preparedStmtFollower.setInt(2, wall_id);
			preparedStmtFollower.executeUpdate();
			preparedStmtFollower.close();
			
			String queryUpdate = "update ids set id=(id +1) where table1='Person' or table1 = 'Group_or_Wall';";
			PreparedStatement preparedStmtUpdate = conn.prepareStatement(queryUpdate);
			preparedStmtUpdate.executeUpdate();
			preparedStmtUpdate.close();
			
			
			conn.close();
		} 
		catch(Exception e){
			System.out.println("Error in registerUser");
			System.out.println(e);
		}
		return obj;
	}
	
	public static void updateProfilePic(int personId, String filename){
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "update person set profile_picture = ? where person_id = ?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, filename);
			preparedStmt.setInt(2, personId);
			preparedStmt.executeUpdate();
			preparedStmt.close();
			
		} 
		catch(Exception e){
			System.out.println("Error in updateProfilePic");
			System.out.println(e);
		}
		return ;
	}
	

	public static void updateWallPic(int group_or_wall_id, String filename){
		try{
			// Create the connection
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			String query = "update Group_or_Wall set Group_photo = ? where Group_or_wall_id = ?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, filename);
			preparedStmt.setInt(2, group_or_wall_id);
			preparedStmt.executeUpdate();
			preparedStmt.close();
			conn.close();
		} 
		catch(Exception e){
			System.out.println("Error in updateWallPic");
			System.out.println(e);
		}
		return ;
	}
	
	private static JSONArray ResultSetConverter(ResultSet rs) throws SQLException, JSONException {
		
		// TODO Auto-generated method stub
		JSONArray json = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    while(rs.next()) {
	        int numColumns = rsmd.getColumnCount();
	        JSONObject obj = new JSONObject();

	        for (int i=1; i<numColumns+1; i++) {
	          String column_name = rsmd.getColumnName(i);

	          if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
	           obj.put(column_name, rs.getArray(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
	           obj.put(column_name, rs.getBoolean(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
	           obj.put(column_name, rs.getBlob(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
	           obj.put(column_name, rs.getDouble(column_name)); 
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
	           obj.put(column_name, rs.getFloat(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
	           obj.put(column_name, rs.getNString(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
	           obj.put(column_name, rs.getString(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
	           obj.put(column_name, rs.getDate(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
	          obj.put(column_name, rs.getTimestamp(column_name));   
	          }
	          else{
	           obj.put(column_name, rs.getObject(column_name));
	          }
	        }

	        json.put(obj);
	      }
	    return json;
	}
	
}