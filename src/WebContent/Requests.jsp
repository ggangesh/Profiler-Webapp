<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="http://<% String serverIp = request.getLocalAddr(); pageContext.setAttribute("serverIp", serverIp);%>${serverIp}/DBProjectImages/logo.png" />
<script src="${pageContext.request.contextPath}/jquery-1.12.4.js"></script>

<script>
function acceptRequest(person_id){
	$.ajax({
		type: "POST",
		url: "AcceptRequest",
		dataType : 'html',
		data : {
			person_id : person_id
		},
		success : function(data){
			location.reload();
		}
	});
}
</script>
<script>
  $.ajax({
      url : "RequestList",
      dataType : 'html',
      success : function(data) {
    	  var json_obj = $.parseJSON(data);
    	  var output= "";
    	  for (i=0; i < json_obj.length; i++)
    		{
    		  var request = json_obj[i];
    		  output+="<div style=\" border:1px solid black; padding-left: 10px; padding-right: 10px;border-radius: 15px;\"><div>"
	    			+"<div align=\"right\" style=\"float: left;\">"
		    		+"<br>"
		    		+"</div>"
		    		+"</div>"
		    		+"<br>"
		    		+"<p>"+request.first_name+" "+request.last_name+"</p>"
		    		+"<button id=\"Request_btn_"+request.person_id+"\" onclick=\"acceptRequest("+request.person_id+")\">Accept Request</button>"
		    		+"</div>"
		    		+"<br>";
    		}
    	  $("#requestArray").append(output);
      },
		error: function(){
			alert("Error in ajax call for getting requests");
		}
  });
  </script>
<title>Requests</title>
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
#requestOut{
	margin-top: 10px;
	margin-left: 10px;
	margin-right: 10px;
	background-color: #fafafa;
	border: 2px solid #ffffff;
	border-radius: 25px;
}
#request{
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
</div>
<div id="middleside">
<div id="requestArray">
</div>
</div>
</div>
</body>
</html>