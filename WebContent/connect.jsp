<%@ page contentType="text/html;charset=UTF-8" 
  import="java.sql.*, javax.naming.*, javax.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>データベースに接続</title>
</head>
<body>
<%
Context context = new InitialContext();
DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/Jsp10");
Connection db = ds.getConnection();
out.println("データベースに接続できました。");
db.close();
%>
</body>
</html>
