<%@ page contentType="text/html; charset = UTF-8" %>
<%@ taglib prefix = "form" uri = "http://www.springframework.org/tags/form"%>
<html>
   <head>
      <title>Secure file download</title>
   </head>
   <body>
      <form:form method = "POST" action="download">
         <table>
	        <tr>
	            <td>User Id</td>
	            <td><input type="text" name="user" /></td>
	        </tr>
	        <tr>
	            <td>Password</td>
	            <td><input type="password" name="secret" /></td>
	        </tr>
	        <tr>
	            <td>File Name</td>
	            <td><input type="text" name="file" /></td>
	        </tr>
	        <tr>
	            <td><input type="submit" value="Download" /></td>
	        </tr>
	    </table>
      </form:form>
   </body>
</html>