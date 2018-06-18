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
function reloadPage(){
	location.reload();
}
</script>
<script>
function addChatter(chatbox_id){
	$("#addChatterForm").submit(function(e){
	e.preventDefault();
	$.ajax({
		type: "POST",
		url: "Chatter",
		dataType : 'html',
		data : $("#addChatterForm").serialize(),
		complete : function(data){
			output="<form id=\"addChatterForm\" method=\"post\">"
				+"<input type=\"text\" class=\"search-input\" name=\"personId\" id=\"personId\" onclick=\"getPeopleList()\" Placeholder=\"Search for people \"/>"
				+"<input type=\"hidden\" name=\"personIdHidden\" id=\"personIdHidden\"/>"
				+"<input type=\"hidden\" name=\"chatboxIdHidden\" id=\"chatboxIdHidden\" value=\""+chatbox_id+"\"/>"
				+"<input type=\"submit\" class=\"search-btn\" value=\"Select\" onclick=\"addChatter("+chatbox_id+")\">"
				+"</form><br><br>"
				+"<button type=\"button\" onclick=\"reloadPage()\">Done</button>";
				$("#chatIn").html(output);
		}
	});
	});
}
</script>
<script>
function getPeopleList(){

	$.ajax({
	      url : "PeopleList",
	      dataType : 'html',
	      success : function(data) {
	    	  var json_obj = $.parseJSON(data);
	    	  var availableTags=[];
	    	  for (i=0; i < json_obj.length; i++)
	    		{
	    		  var person = json_obj[i];
	    		  availableTags.push({
	    			  "label" : person.first_name+" "+person.last_name+"    "+person.other_details,
	    		  	  "value" : person.first_name,
	    		  	  "id" : person.person_id
	    		  });
	    		}
	    	  $( "#personId" ).autocomplete({
	    	      source: availableTags,
	    	      minLength: 1,
	    	      select: function(event, ui){
	    	    	  $("#personIdHidden").val(ui.item.id);
	    	      }
	    	    });
	      },
			error: function(){
				alert("Error in ajax call for getting peopleList");
			}
	  });
}
</script>
<script>
function createChatBox(){
	$.ajax({
		type: "GET",
		url : "ChatBox",
		success: function(data)
		{
			output="<form id=\"addChatterForm\" method=\"post\">"
			+"<input type=\"text\" class=\"search-input\" name=\"personId\" id=\"personId\" onclick=\"getPeopleList()\" Placeholder=\"Search for people \"/>"
			+"<input type=\"hidden\" name=\"personIdHidden\" id=\"personIdHidden\"/>"
			+"<input type=\"hidden\" name=\"chatboxIdHidden\" id=\"chatboxIdHidden\" value=\""+data+"\"/>"
			+"<input type=\"submit\" class=\"search-btn\" value=\"Select\" onclick=\"addChatter("+data+")\">"
			+"</form><br><br>"
			+"<button type=\"button\" onclick=\"reloadPage()\">Done</button>";
			$("#chatIn").html(output);
		},
		error : function()
		{
			alert("Error in creating new chatbox");
		}
	});
}
</script>
<script>

function postChat(chatbox_id) {
	$("#chatBox_"+chatbox_id).submit(function(e){
		$.ajax({
			type: "POST",
			url: "PostChat",
			data: $("#chatBox_"+chatbox_id).serialize(),
			success: function(data)
			{
				$("#viewChatBoxBtn_"+chatbox_id).click();
				$("#chatBox_"+post_id)[0].reset();
			},
			error: function(){
				alert("Error in ajax call for posting chat on chatbox_id "+chatbox_id );
			}
		});
		e.preventDefault();
	});
}
</script>
<script>
function getChat(chatbox_id) {
	$.ajax({
		url: "Chat",
		dataType : 'html',
		type: "POST",
		data: {
			chatbox_id: chatbox_id
		},
		success: function(result){
			var json_obj = $.parseJSON(result);
	    	  var output= "";
	    	  output+="<br><form id=\"chatBox_"+chatbox_id+"\" method=\"post\">"
	    	  +"<input type=\"hidden\" name=\"chatbox_id\" value=\""+chatbox_id+"\"/>"
	    	  +"<textarea name=\"Text1\" cols=\"80\" rows=\"2\"></textarea>"
	    	  +"<input type=\"submit\" value=\"Send message\"onclick=\"postChat("+chatbox_id+")\"/>"
	    	  +"</form><br>";
	    	  for (i=0; i < json_obj.length; i++)
	    		{
	    		  var chat = json_obj[i];
	    		  output+= "<div style=\" border:1px solid black; padding-left: 10px; padding-right: 10px;border-radius: 15px;\"><div>"
		    			+"<div align=\"right\" style=\"float: left;\">"
			    		+"<br><form method=\"post\" action=\"Wall\" class=\"inline\">"
						  +"<input type=\"hidden\" name=\"wallIdHidden\" value=\""+chat.group_or_wall_id+"\">"
							+"<button type=\"submit\" name=\"submit_param\" value=\"submit_value\" class=\"link-button\">"
							+chat.first_name+" "+chat.last_name
							+"</button>"
							+"</form>"
			    		+"</div>"
			    		+"<div align=\"right\" style=\"float: center;\">"
			    		+"<p style=\"float: right\">Posted on: "+chat.time1+"</p>"
			    		+"</div>"
			    		+"</div>"
			    		+"<br>"
			    		+"<br>"
			    		+"<p>"+chat.message_text+"</p>"
			    		+"</div>"
			    		+"<br>";
	    		}
	    	  $("#chatIn").html(output);
		},
		error: function(){
			alert("Error in ajax call for getting comments for post_id "+post_id );
		}
	});
}
</script>
<script>
  $.ajax({
      url : "ChatList",
      dataType : 'html',
      success : function(data) {
    	  var json_obj = $.parseJSON(data);
    	  var output= "";
    	  for (i=0; i < json_obj.length; i++)
    		{
    		  var chatbox = json_obj[i];
    		  output+="<div id=\"chatBoxOut\"><div id=\"chatBox\">";
			  output+="<button class=\"chatboxBtn\" id=\"viewChatBoxBtn_"+chatbox.chatbox_id+"\" type=\"button\" onclick=\"getChat("+chatbox.chatbox_id+")\">"+chatbox.name+"</button>";
    		  output+="</div></div>";
    		}
    	  $("#chatboxArray").append(output);
      },
		error: function(){
			alert("Error in ajax call for getting newsfeed");
		}
  });
  </script>
<title>Messages</title>
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
#chatBoxOut{
	margin-top: 10px;
	margin-left: 10px;
	margin-right: 10px;
	background-color: #fafafa;
	border: 2px solid #ffffff;
	border-radius: 25px;
}
#chatBox{
	border: 2px solid #aaaaaa;
	padding-left: 10px;
	padding-right: 10px;
	border-radius: 25px;
}
#chatOut{
	margin-top: 10px;
	margin-left: 10px;
	margin-right: 10px;
	background-color: #fafafa;
	border: 2px solid #ffffff;
	border-radius: 25px;
}
#chatIn{
	border: 2px solid #aaaaaa;
	padding-left: 10px;
	padding-right: 10px;
	border-radius: 25px;
}
#leftside{
	background-color:  #e3e3e3;
	width: 190px;
	height: 1066px;
	position: absolute;
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
    right: 8px;
    float: right;
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
.text {
    margin: 16px 5px;
    float: left;
    color: #333
    }
.image {
    background: #FFD119
    }

.video {
    background: #AD855C
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
.chatboxBtn {
    background-color: #e7e7e7;
    border: none;
    border-radius : 8px;
    color: blue;
    padding-left:15px;
    padding-right:15px;
    padding-top:15px;
    padding-bottom: 15px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    font-weight: bold;
    margin: 4px 2px;
    cursor: pointer;
}
</style>
<body>
<div id="top">
<div id="menu">
<div id="logo">
<img src="http://${serverIp}/DBProjectImages/logo.png"></div>
<div id="logo">
<p><a href="Home">Home</a></p>
</div>
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
<button class="chatboxBtn" type="button" onclick="createChatBox()">Start new conversation</button>
	<div id="chatboxArray">
</div>
</div>
<div id="middleside">
<div id="chatOut">
<div id="chatIn">
</div>
</div>
</div>
</div>
</body>
</html>