<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Upload Profile Picture</title>
<style type="text/css">
body {
background-color: #f4f4f4;
color: #5a5656;
font-family: 'Open Sans', Arial, Helvetica, sans-serif;
font-size: 16px;
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

/* ---------- LOGIN ---------- */
#login {
margin: 50px auto;
width: 50%;
}
fieldset form  input[type="text"], input[type="password"] ,input[type="date"] ,input[type="number"]  {
background-color: #e5e5e5;
border: none;
border-radius: 3px;
-moz-border-radius: 3px;
-webkit-border-radius: 3px;
color: #5a5656;
font-family: 'Open Sans', Arial, Helvetica, sans-serif;
font-size: 14px;
height: 50px;
outline: none;
padding: 0px 10px;
width: 280px;
-webkit-appearance:none;
}
fieldset form  input[type="submit"], input[type="file"]{
background-color: #008dde;
border: none;
border-radius: 3px;
-moz-border-radius: 3px;
-webkit-border-radius: 3px;
color: #f4f4f4;
cursor: pointer;
font-family: 'Open Sans', Arial, Helvetica, sans-serif;
height: 50px;
text-transform: uppercase;
width: 300px;
-webkit-appearance:none;
}

fieldset form  a {
color: #5a5656;
font-size: 10px;
}
fieldset  form  a:hover { text-decoration: underline; }
</style>
</head>
<body>
<div id="login">
<fieldset>
<p>${error}</p>
<form action="UploadProfilePic" method="post" enctype="multipart/form-data">
<table>
	<tr><td><span>Upload Profile Picture</span></td><td><input type="file" name="file" /></td></tr>
    <tr><td><input type="submit"  name="UploadButton" value = "Upload" /></td><td><input type="submit" name="CancelButton" value ="Cancel" /></td></tr>
</table>
</form>
</fieldset>
</div> <!-- end register -->
</body>
</html>