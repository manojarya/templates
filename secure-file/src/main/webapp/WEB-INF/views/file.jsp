<%@ page contentType="text/html; charset = UTF-8" %>
<%@ taglib prefix = "form" uri = "http://www.springframework.org/tags/form"%>
<html>
   <head>
      <title>Secure file upload</title>
   </head>
   <body>
      <form:form method = "POST" action="upload" enctype = "multipart/form-data">
         <table>
	        <tr>
	            <td>User Id</td>
	            <td><input type="text" name="user" /></td>
	        </tr>
	        <tr>
	            <td>Select a file to upload</td>
	            <td><input type="file" name="file" /></td>
	        </tr>
	        <tr>
	            <td><input type="submit" value="Upload" /></td>
	        </tr>
	    </table>
      </form:form>
   </body>
</html>