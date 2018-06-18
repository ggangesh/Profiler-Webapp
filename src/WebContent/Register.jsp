<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <html>
<head>
<title>Register</title>
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
<h1><strong>Welcome.</strong> Please login.</h1>
<fieldset>
<p>${error}</p>
<form action="RegisterConfirm" method="post">
<table>
<tr><td>First name</td> <td><input type="text" name="firstName" maxlength="15" onBlur="if(this.value=='')this.value=''" onFocus="if(this.value=='')this.value='' "></td></tr>
<tr><td>Last name</td> <td><input type="text" name="lastName" maxlength="15" onBlur="if(this.value=='')this.value=''" onFocus="if(this.value=='')this.value='' "></td></tr>
<tr><td>Date Of Birth(yyyy-mm-dd)</td> <td><input type="date" name="dateOfBirth" onBlur="if(this.value=='')this.value=''" onFocus="if(this.value=='')this.value='' "></td></tr>
<tr><td>Gender</td> <td>
<input type="text" list="genders" name="gender" nBlur="if(this.value=='')this.value=''" onFocus="if(this.value=='')this.value='' "
>
  <datalist id="genders">
    <option value="Male">
    <option value="Female">
    <option value="Other">
  </datalist>
</input>
</td></tr>
<tr><td>Mobile no.</td> <td><input type="number" name="mobileno" min="1000000000" max="9999999999" onBlur="if(this.value=='')this.value=''" onFocus="if(this.value=='')this.value='' "></td><td></td></tr>
<tr><td>Email</td> <td><input type="text" name="email" maxlength="30" value=""  onBlur="if(this.value=='')this.value=''" onFocus="if(this.value=='')this.value='' "></td><td></td></tr>
<tr><td>Address</td> <td><input type="text" name="address" maxlength="50" value=""  onBlur="if(this.value=='')this.value=''" onFocus="if(this.value=='')this.value='' "></td><td></td></tr>
<tr><td>Other Details</td> <td><input type="text" name="other" maxlength="50" value=""  onBlur="if(this.value=='')this.value=''" onFocus="if(this.value=='')this.value='' "></td><td></td></tr>
<tr><td>Username</td> <td><input type="text" name="username" maxlength="15" value=""  onBlur="if(this.value=='')this.value=''" onFocus="if(this.value=='Username')this.value='' "></td><td></td></tr>
<tr><td>Password</td> <td><input type="password" name="password" maxlength="15" onBlur="if(this.value=='')this.value='Password'" onFocus="if(this.value=='Password')this.value='' "></td></tr>
<tr><td>Re-enter Password</td><td><input type="password" name="confirm_password" maxlength="15" onBlur="if(this.value=='')this.value='Confirm Password'" onFocus="if(this.value=='Confirm Password')this.value='' "></td></tr>
<tr><td><input type="submit" name="RegisterButton" value ="Register"></td> <td><input type="submit" name="CancelButton"value ="Cancel"></td></tr>

</table>

</form>
</fieldset>
</div> <!-- end register -->
</body>
</html>
