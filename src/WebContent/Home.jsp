<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="http://<% String serverIp = request.getLocalAddr(); pageContext.setAttribute("serverIp", serverIp);%>${serverIp}/DBProjectImages/logo.png" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/jquery-ui.css">
<script src="${pageContext.request.contextPath}/jquery-1.12.4.js"></script>
<script src="${pageContext.request.contextPath}/jquery-ui.js"></script>
<script>
function getComments(post_id) {
	$.ajax({
		url: "Comments",
		dataType : 'html',
		type: "POST",
		data: {
			post_id: post_id
		},
		success: function(result){
			var json_obj = $.parseJSON(result);
	    	  var output= "";
	    	  for (i=0; i < json_obj.length; i++)
	    		{
	    		  var comment = json_obj[i];
	    		  output+= "<div style=\" border:1px solid black; padding-left: 10px; padding-right: 10px;border-radius: 15px;\"><div>"
		    			+"<div align=\"right\" style=\"float: left;\">"
			    		+"<br><form method=\"post\" action=\"Wall\" class=\"inline\">"
						  +"<input type=\"hidden\" name=\"wallIdHidden\" value=\""+comment.group_or_wall_id+"\">"
							+"<button type=\"submit\" name=\"submit_param\" value=\"submit_value\" class=\"link-button\">"
							+comment.first_name+" "+comment.last_name
							+"</button>"
							+"</form>"
			    		+"</div>"
			    		+"<div align=\"right\" style=\"float: center;\">"
			    		+"<p style=\"float: right\">Posted on: "+comment.time1+"</p>"
			    		+"</div>"
			    		+"</div>"
			    		+"<br>"
			    		+"<br>"
			    		+"<p>"+comment.text1+"</p>"
			    		+"</div>"
			    		+"<br>";
	    		}
	    	  $("#post_"+post_id).html(output);
		},
		error: function(){
			alert("Error in ajax call for getting comments for post_id "+post_id );
		}
	});
}
</script>
<script>

function postComment(post_id) {
	$("#postComment_"+post_id).submit(function(e){
		$.ajax({
			type: "POST",
			url: "PostComment",
			data: $("#postComment_"+post_id).serialize(),
			success: function(data)
			{
				$("#viewCommentBtn_"+post_id).click();
				$("#postComment_"+post_id)[0].reset();
			},
			error: function(){
				alert("Error in ajax call for posting comments on post_id "+post_id );
			}
		});
		e.preventDefault();
	});
}
</script>
<script>
  $.ajax({
      url : "NewsFeed",
      dataType : 'html',
      success : function(data) {
    	  var json_obj = $.parseJSON(data);
    	  var output= "";
    	  for (i=0; i < json_obj.length; i++)
    		{
    		  var post = json_obj[i];
    		  var privacyString = post.privacy;
    		  if(post.privacy == 0){
    			  privacyString = "public";
    		  }
    		  if(post.privacy == 1){
    			  privacyString = "followers";
    		  }
    		  if(post.privacy == 2){
    			  privacyString = "private";
    		  }
    		  output+="<div id=\"postout\"><div id=\"post\">";
    		  output+="<div>"
		    			+"<div align=\"right\" style=\"float: left;\">"
		    			+"<br><form method=\"post\" action=\"Wall\" class=\"inline\">"
						  +"<input type=\"hidden\" name=\"wallIdHidden\" value=\""+post.group_or_wall_id+"\">"
							+"<button type=\"submit\" name=\"submit_param\" value=\"submit_value\" class=\"link-button\">"
							+post.first_name+" "+post.last_name
							+"</button>"
							+"</form>"
			    		+"</div>"
			    		+"<div align=\"right\" style=\"float: center;\">"
			    		+"<p style=\"float: right\">Posted on: "+post.time1+"</p>"
			    		+"</div>"
			    		+"<div align=\"right\" style=\"float: right;\">"
			    		+"<p>Privacy: "+privacyString+" &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp</p>"
			    		+"</div>"
			    		+"</div>"
			    		+"<br>"
			    		+"<br>"
			    		+"<p>"+post.text1+"</p>";
			  if(post.link != ""){
			  var link1 = post.link;
			  var linkHttp = link1.startsWith("http");
			  if(linkHttp){
			  output+="<p><a target=\"_blank\" href=\""+post.link+"\">"+post.link+"</a></p>";
			  }
			  else{
			  output+="<p><a target=\"_blank\" href=\"http://"+post.link+"\">"+post.link+"</a></p>";
			  }
			  }
			  if(post.image != ""){
			  output+="<img src=\"http://${serverIp}/DBProjectImages/"+post.image+"\" alt=\"Wall photo of ${myname}\" width=\"800px\" align=\"middle\" style=\"border-radius: 0px;\">"
			    		+"<br>"
			    		+"<br>";
			  }
			  if(post.video != ""){
			  output+="<video width=\"800px\" align=\"middle\" style=\"border-radius: 0px;\" controls>"
			    		+"  <source src=\"http://${serverIp}/DBProjectImages/"+post.video+"\" type=\"video/mp4\" >"
			    		+"Your browser does not support the video tag.<a target=\"_blank\" href=\"http://${serverIp}/DBProjectImages/postVideo_0.mp4\" type=\"video/mp4\">click here</a>"
			    		+"</video> ";
			  }
			  output+="<button id=\"viewCommentBtn_"+post.post_id+"\" type=\"button\" onclick=\"getComments("+post.post_id+")\">View comments</button>";
			  output+="<br><br><form id=\"postComment_"+post.post_id+"\" method=\"post\"><input type=\"hidden\" name=\"post_id\" value=\""+post.post_id+"\"/><textarea name=\"Text1\" cols=\"80\" rows=\"2\"></textarea><input type=\"submit\" value=\"Post Comment\"onclick=\"postComment("+post.post_id+")\"/></form><br>";
			  output+="<div id=\"post_"+post.post_id+"\">";
    		  output+="</div></div></div>";
    		}
    	  $("#postArray").append(output);
      },
		error: function(){
			alert("Error in ajax call for getting newsfeed");
		}
  });
  </script>
  
  <script>
  $.ajax({
      url : "SearchList",
      dataType : 'html',
      error : function() {
          alert("Error Occured");
      },
      success : function(data) {
    	  var json_obj = $.parseJSON(data);
    	  var availableTags=[];
    	  for (i=0; i < json_obj.length; i++)
    		{
    		  var post = json_obj[i];
    		  availableTags.push({
    			  "label" : post.name+"    "+post.type+"    "+post.group_description,
    		  	  "value" : post.name,
    		  	  "id" : post.group_or_wall_id
    		  });
    		}
    	  
    	  $( "#wallId" ).autocomplete({
    	      source: availableTags,
    	      minLength: 1,
    	      select: function(event, ui){
    	    	  $("#wallIdHidden").val(ui.item.id);
    	      }
    	    });
      },
		error: function(){
			alert("Error in ajax call for getting searchList");
		}
  });
  </script>
  
<title>Home</title>
</head>
<style type="text/css">
body {
background-color: #f4f4f4;
color: #5a5656;
font-family: 'Open Sans', Arial, Helvetica, sans-serif;
font-size: 14px;
line-height: 1.5em;
}
a { text-decoration: none; }
h1 { font-size: 1em; }
h1, p {
margin-bottom: 10px;
}
strong {
font-weight: bold;
}
.uppercase { text-transform: uppercase; }
#top{
	width: 100%;
	height: 45px;
	top: 0px;
	left: 0px;
	position: fixed;
	background-color: #2E7742;
	font-family: tahoma;
	font-size: 1.2em;

}
#postout{
	margin-top: 10px;
	margin-left: 10px;
	margin-right: 10px;
	background-color: #fafafa;
	border: 2px solid #ffffff;
	border-radius: 25px;
}
#post{
	border: 2px solid #aaaaaa;
	padding-left: 10px;
	padding-right: 10px;
	border-radius: 25px;
}
#postformout{
	margin-top: 10px;
	margin-left: 10px;
	margin-right: 10px;
	background-color: #fafafa;
	border: 2px solid #ffffff;
	border-radius: 25px;
	margin-bottom:30px;
}
#postform{
	border: 2px solid #aaaaaa;
	padding-left: 10px;
	padding-right: 10px;
	border-radius: 25px;	
	
}

#leftside{
	background-color:  #e3e3e3;
	width: 190px;
	height: 1066px;
	position: fixed;
	background-image: url(http://${serverIp}/DBProjectImages/canvas1.png);
    background-repeat: repeat-y-x;
}

#middleside{
	width: 900px;
	left: 15%;
	background-color:  #e3e3e3;
	margin-left: 200px;
	float: left;
	background-image: url(http://${serverIp}/DBProjectImages/canvas1.png);
    background-repeat: repeat-y-x;
}
#container{
	width: 100%px;
	height: 1066px;
	top: 45px;
	margin-top: 45px;
}
#links{
	width: 180px;
	margin-left: 3px;
	border: 2px solid #ffffff;
	border-radius: 25px;
}
#linksContent{
	font-family: tahoma;
	font-size: 1em;
	background-color: #ffffff;
	padding-left: 5px;
	padding-right: 5px;
	border: 2px solid #bbbbbb;
	border-radius: 25px;
}
#name{
width: 20%;
color: #fff;
font-size: 15px;
top: 10px;
left: 65%;
position: fixed;
font-size: 1.2em;
}
#name a{
color: #fff;
font-size: 15px;
top: 10px;
left: 70%;
position: fixed;
}
#logout{
width: 15%;
height: 30px;
color: #fff;
font-size: 15px;
top: 10px;
left: 85%;
position: fixed;
}
#logout a{
color: #fff;
font-size: 15px;
top: 10px;
left: 85%;
position: fixed;
}
#logo{
width: 20%;
height: 30px;
color: #fff;
font-size: 15px;
top: 10px;
left: 5%;
position: fixed;
}
#logo a { 
color: #fff;
font-size: 15px;
top: 10px;
left: 10%;
position: fixed;
 }
.search{
width: 45%;
height: 30px;
color: #fff;
font-size: 15px;
top: 5px;
left: 15%;
position: relative;
}
.search a{
color: #fff;
font-size: 15px;
top: 5px;
left: 20%;
position: relative;
}
.search-input {
    padding: 2px 0 2px 8px;
    border: 1px solid #384C7F;
    height: 25px;
    -moz-border-radius: 4px;
    -webkit-border-radius: 4px;
    border-radius: 4px;
    width: 500px;
    margin: 0 auto;
    display: block
    }

.search-btn {
    position: absolute;
    right: 8px;
    top: 3px;
    width: 60px;
    height: 30px;
    font-size: 14px;
    color: #434343
    }

.searchsubmit {
    cursor: pointer;
    width: 18px;
    height: 18px;
    position: absolute;
    right: 0;
    top: 0;
    text-shadow: none;
    border: none;
    -moz-box-shadow: none;
    -webkit-box-shadow: none;
    box-shadow: none;
    background: rgba(0, 0, 0, 0)
    }
.content-list {
    width: 100%;
    background: #FFF;
    float: left;
    border: 1px solid #E1E1E1;
    border-color: rgba(0, 0, 0, 0.2);
    border-left: 0;
    border-right: 0;
    border-top: 0;
    -moz-box-shadow: 0 3px 5px 0 rgba(0, 0, 0, 0.25);
    -webkit-box-shadow: 0 3px 5px 0 rgba(0, 0, 0, 0.25);
    box-shadow: 0 3px 5px 0 rgba(0, 0, 0, 0.25);
    margin-bottom: 8px;
    display: none
    }

.active {
    display: block
    }

.drop-list {
    list-style-type: none;
    margin: 0;
    padding: 0;
    background: white
    }

.drop-list li {
    border: none;
    padding: 0;
    color: white
    }

.drop-list li a {
    width: 100%;
    display: block;
    float: left
    }

.drop-list li a:hover {
    background: #ECECEC
    }

.item {
    display: block;
    margin: 0 auto;
    padding: 0;
    width: 590px
    }

.icon {
    float: left;
    height: 38px;
    margin: 4px 0 4px 0;
    width: 48px;
    text-align: center;
    color: #FFF;
    padding: 10px 0 0 0
    }

.icon span {
    font-size: 25px
    }

.text {
    margin: 16px 5px;
    float: left;
    color: #333
    }

.people {
    background: #69F
    }

.image {
    background: #FFD119
    }

.video {
    background: #AD855C
    }

.place {
    background: #B85C8A
    }

.music {
    background: #A3A3FF
    }
ul.tab {
    list-style-type: none;
    margin: 0;
    padding: 0;
    overflow: hidden;
    border: 1px solid #ccc;
    background-color: #f1f1f1;
    border-radius:10px;
}

/* Float the list items side by side */
ul.tab li {float: left;
border-radius: 20px;}

/* Style the links inside the list items */
ul.tab li a {
    display: inline-block;
    color: black;
    text-align: center;
    padding: 14px 16px;
    text-decoration: none;
    transition: 0.3s;
    font-size: 17px;
}

/* Change background color of links on hover */
ul.tab li a:hover {
    background-color: #ddd;
}

/* Create an active/current tablink class */
ul.tab li a:focus, .active {
    background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
    display: none;
    padding: 6px 12px;
    border: 1px solid #ccc;
    border-top: none;
    border-radius: 20px;
}

.tab2 input[type="submit"], input[type="file"]{
background-color: #008dde;
border: none;
border-radius: 10px;
-moz-border-radius: 10px;
-webkit-border-radius: 10px;
color: #f4f4f4;
cursor: pointer;
font-family: 'Open Sans', Arial, Helvetica, sans-serif;
height: 30px;
text-transform: uppercase;
width: 150px;
-webkit-appearance:none;
}



#tab2div {
	float: right;
}

#postform input[type="file"]{
background-color: #008dde;
border: none;
border-radius: 10px;
-moz-border-radius: 10px;
-webkit-border-radius: 10px;
color: #f4f4f4;
cursor: pointer;
font-family: 'Open Sans', Arial, Helvetica, sans-serif;
height: 30px;
text-transform: uppercase;
width: 300px;
-webkit-appearance:none;

}
.inline {
  display: inline;
}
 
.link-button {
  background: none;
  border: none;
  color: blue;
  text-decoration: underline;
  cursor: pointer;
  font-size: 1em;
  font-family: serif;
}
.link-button:focus {
  outline: none;
}
.link-button:active {
  color:red;
}

</style>
<script>
function openAddPostTab(evt, cityName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.className += " active";
}
</script>
<body onload="openAddPostTab(event, 'Text1')">
<div id="top">
<div id="menu">
<div id="logo">
<img src="http://${serverIp}/DBProjectImages/logo.png"></div>
<div id="logo">
<p><a href="Home">Home</a></p>
</div>

<form name="search" class="search" id="search" action="Wall" method="post">
<div>
<div class="search">
<input type="text" class="search-input" name="wallId" id="wallId" Placeholder="Search for people or groups"/>
</div>
<input type="hidden" name="wallIdHidden" id="wallIdHidden"/>
<div class="search-btn">
<input type="submit" class="search-btn" value="Select" onclick="ajaxFunction();">
</div>
</div>
</form>


<div id="name">
<a href="MyWall">
<img src="http://${serverIp}/DBProjectImages/${profile_pic}" alt="Wall photo of ${first_name}" height="30" align="middle" style="border-radius: 10px;"> ${first_name}</a>
</div>
<div id="logout">
<p><a href="Logout">Logout</a></p>
</div>
</div>
</div>
<div id="container">
<div id="leftside">
	<div id="links">
		<div id="linksContent"><p><a href="Messages">Messages</a></p></div>
	</div>
	<div id="links">
		<div id="linksContent"><p><a href="Requests">Requests</a></p></div>
	</div>
</div>

<div id="middleside">
<!-- ------------------------------------------------------------------------------------------------------------------------------ -->
<div id="postformout">
	<div id="postform">
		<ul class="tab">
		  <li><a href="javascript:void(0)" class="tablinks" onclick="openAddPostTab(event, 'Text1')">Text</a></li>
		  <li><a href="javascript:void(0)" class="tablinks" onclick="openAddPostTab(event, 'Image')">Image</a></li>
		  <li><a href="javascript:void(0)" class="tablinks" onclick="openAddPostTab(event, 'Video')">Video</a></li>
		  <li><a href="javascript:void(0)" class="tablinks" onclick="openAddPostTab(event, 'Link')">Link</a></li>
		  
		</ul>
		<form name = "addPost" id = "addPost" action = "Post" method = "post" enctype="multipart/form-data">
		<input type="hidden" name="postWallId" value="<%=request.getSession().getAttribute("wallId")%>" />  
		<div id="Text1" class="tabcontent">
		  <h3>Text</h3>
		   
		   <textarea name="Text1" cols="80" rows="5"></textarea>
		</div>
		
		<div id="Image" class="tabcontent">
		  <h3>Image</h3>
		  <input type="file" name="Image" />
		 
		</div>
		
		<div id="Video" class="tabcontent">
		  <h3>Video</h3>
		  <input type="file" name="Video" />
		  
		</div>
		<div id="Link" class="tabcontent">
		  <h3>Link</h3>
		  <textarea name="Link1" cols="80" rows="5"></textarea>
		 
		</div>
		
		<div id="tab2div">
		<table class="tab2">
		<tr>
		<td>Privacy: </td>
			<td><select name="Privacy">
			    <option value="0">Public</option>
			    <option value="1">Followers</option>
			    <option value="2">Private</option>
			</select> </td>
		  <td><input type="submit" name="Addpost" value="Post"></td>
		</tr>
		</table>
		<br>
		<br>
		<br>
		</div>
		</form>
		
	</div>
</div>

<!-- ------------------------------------------------------------------------------------------------------------------------------ -->


<div id="postArray">
</div>
</div>
</div>
</body>
</html>